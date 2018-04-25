/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.apache.axis.AxisProperties;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A basic client for a SDMX Registry instance.
 *
 * Note that currently this supports only SDMX 2.0 standard, because there's no public SDMX Registry implementation for
 * newer version. When support for SDMX 2.1 will be added probably a lot of refactoring will be required...
 */
public class RegistryClient {

    /**
     * The registry configuration to use. If <code>null</code> then the current configured value will be used.
     */
    private RegistryConfig registryConfig = null;

    /**
     * Constructs a new <code>RegistryClient</code> instance.
     */
    public RegistryClient() {
    }

    /**
     * Constructs a new <code>RegistryClient</code> instance.
     *
     * @param rc the registry configuration to use
     */
    public RegistryClient(RegistryConfig rc) {
        this.registryConfig = rc;
    }

    /**
     * Retrieves from SDMX registry a list with all available key families.
     *
     * @return a list with available key families
     *
     * @throws Exception if fails
     */
    public List<DsdInfo> getAvailableKeyFamilies() throws Exception {
        Document response = this.query(this.createQueryDocument("query-get-all-key-families.xml"));

        XPathFactory factory = XPathFactory.newInstance();
        XPath path = factory.newXPath();

        NodeList nodes = (NodeList) path.evaluate("//*[local-name() = 'KeyFamily']", response, XPathConstants.NODESET);

        List<DsdInfo> result = new ArrayList<DsdInfo>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);

            DsdInfo info = new DsdInfo();
            info.setSdmxVersion(SdmxVersion.SDMX_2_0);
            info.setAgency(e.getAttribute("agencyID"));
            info.setId(e.getAttribute("id"));
            info.setVersion(e.getAttribute("version"));
            result.add(info);
        }

        return result;
    }

    /**
     * Retrieves the DSD for a key family.
     *
     * @param info the data about the key family to retrieve
     *
     * @return a document with DSD content
     *
     * @throws Exception if fails
     */
    public Document getKeyFamily(DsdInfo info) throws Exception {
        Document response = this.query(this.createQueryDocument("query-get-key-family.xml",
                info.getAgency(), info.getId(), info.getVersion()));

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        Element header = (Element) xpath.compile("//*[local-name() = 'Header']")
                .evaluate(response, XPathConstants.NODE);
        Element codelists = (Element) xpath.compile("//*[local-name() = 'CodeLists']")
                .evaluate(response, XPathConstants.NODE);
        Element concepts = (Element) xpath.compile("//*[local-name() = 'Concepts']")
                .evaluate(response, XPathConstants.NODE);
        Element keyFamilies = (Element) xpath.compile("//*[local-name() = 'KeyFamilies']")
                .evaluate(response, XPathConstants.NODE);

        Document document = this.createQueryDocument("template.xml");
        Element root = document.getDocumentElement();

        root.appendChild(this.importElement(document, header, true));
        root.appendChild(this.importElement(document, codelists, false));
        root.appendChild(this.importElement(document, concepts, false));
        root.appendChild(this.importElement(document, keyFamilies, false));

        return document;
    }

    /**
     * Imports an element from a document to another.
     *
     * <p>Note that this removes element's prefix and namespace.</p>
     *
     * @param document                the destination document, where the element will be appended
     * @param elm                     the element to import
     * @param removeChildrenNamespace if <code>true</code> then the prefix and namespace of all child nodes will also be
     *                                removed
     *
     * @return the imported element
     */
    private Element importElement(Document document, Element elm, boolean removeChildrenNamespace) {
        Element result = document.createElementNS(
                document.getDocumentElement().getNamespaceURI(),
                elm.getLocalName());
        result.setNodeValue(elm.getNodeValue());

        NamedNodeMap attributes = elm.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            result.setAttribute(attr.getName(), attr.getValue());
        }

        NodeList children = elm.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (removeChildrenNamespace && (children.item(i) instanceof Element)) {
                result.appendChild(this.importElement(document, (Element) children.item(i), true));
            } else {
                result.appendChild(document.importNode(children.item(i), true));
            }
        }

        return result;
    }

    /**
     * Creates a query document.
     *
     * @param name the name of the file which contains the query
     *
     * @return a <code>Document</code> instance
     *
     * @throws Exception if fails
     */
    private Document createQueryDocument(String name) throws Exception {
        return Utils.readXMLResource(name);
    }

    /**
     * Getter for a string resource.
     *
     * @param name the name of the file which contains the query
     * @param args the arguments used for formatting the query
     *
     * @return a <code>Document</code> instance
     *
     * @throws Exception if fails
     */
    private Document createQueryDocument(String name, Object... args) throws Exception {
        InputStreamReader reader = null;
        String content = null;

        try {
            reader = new InputStreamReader(getClass().getResourceAsStream(name));
            StringWriter writer = new StringWriter();
            IOUtils.copy(reader, writer);
            content = writer.toString();
        } finally {
            IOUtils.closeQuietly(reader);
        }

        String query = MessageFormat.format(content, args);
        ByteArrayInputStream bais = new ByteArrayInputStream(query.getBytes("UTF-8"));
        return Utils.readXML(bais);
    }

    /**
     * Invokes the SDMX Registry instance.
     *
     * @param message the query message to send to registry
     *
     * @return the response, <code>null</code> if there was none
     *
     * @throws Exception if fails
     */
    private Document query(Document message) throws Exception {
        RegistryConfig cfg = this.registryConfig != null ? this.registryConfig
                : Globals.getCurrentRegistryConfig();

        Service service = new Service(cfg.getURL(),
                new QName(Constants.REGISTRY_WS_NAMESPACE, Constants.REGISTRY_WS_SERVICE_NAME));

        Call call = (Call) service.createCall(new QName(Constants.REGISTRY_WS_NAMESPACE, Constants.REGISTRY_WS_PORT));

        SOAPEnvelope envelope = new SOAPEnvelope();
        envelope.addBodyElement(new SOAPBodyElement(message.getDocumentElement()));
        envelope.addHeader(this.createAuthenticationHeaderElement(cfg.getDomain(), cfg.getUsername(),
                cfg.getPassword()));

        this.setupProxy(cfg);
        Vector<?> result = call.invoke(envelope).getBodyElements();

        return result.size() == 0 ? null : ((SOAPBodyElement) result.get(0)).getAsDocument();
    }

    /**
     * Creates authentication header.
     *
     * @param domain   the domain
     * @param username the username
     * @param password the password
     *
     * @return the SOAP header element to be used when invoking the registry web service
     *
     * @throws Exception if fails
     */
    private SOAPHeaderElement createAuthenticationHeaderElement(String domain, String username, String password)
            throws Exception {
        SOAPHeaderElement elm =
                new SOAPHeaderElement(Constants.SDMX_AUTH_HEADER_NAMESPACE,
                        Constants.SDMX_AUTH_HEADER_NAME);

        elm.setPrefix(Constants.SDMX_AUTH_HEADER_PREFIX);
        elm.setMustUnderstand(false);

        SOAPElement e;

        e = elm.addChildElement(Constants.SDMX_AUTH_USERNAME_ELM_NAME);
        e.addTextNode(StringUtils.defaultString(username));

        e = elm.addChildElement(Constants.SDMX_AUTH_PASSWORD_ELM_NAME);
        e.addTextNode(StringUtils.defaultString(password));

        e = elm.addChildElement(Constants.SDMX_AUTH_DOMAIN_ELM_NAME);
        e.addTextNode(StringUtils.defaultString(domain));

        return elm;
    }

    /**
     * Setups the proxy.
     *
     * <p>Note that this currently only supports HTTP protocol. Support for HTTPs should be added in future.</p>
     *
     * @param cfg the registry configuration
     */
    private void setupProxy(RegistryConfig cfg) {
        String axisProxyUser = "";
        String axisProxyPassword = "";

        if (cfg.getProxyHost() != null) {
            System.setProperty("http.proxyHost", cfg.getProxyHost());
            System.setProperty("http.proxyPort", "" + cfg.getProxyPort());
            if ((cfg.getProxyUsername() != null) && (cfg.getProxyPassword() != null)) {
                axisProxyUser = cfg.getProxyUsername();
                axisProxyPassword = cfg.getProxyPassword();
            }
        } else {
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
        }

        AxisProperties.setProperty("http.proxyUser", axisProxyUser);
        AxisProperties.setProperty("http.proxyPassword", axisProxyPassword);
    }
}
