/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Base class for a SDMX helper class.
 *
 * <p>The idea is to avoid having a lot of IFs in other places just to do something for a SDMX version and something
 * else for another SDMX version, at least as long as the operations are very similar. All other classes should use this
 * one for working with DSDs, without being interested too much about the SDMX version.</p>
 */
abstract public class SdmxHelper {

    /**
     * The XPath factory.
     */
    private static XPathFactory xPathFactory = XPathFactory.newInstance();

    /**
     * The SDMX version supported by this instance.
     */
    private SdmxVersion sdmxVersion = null;

    /**
     * The namespace context instance with support for SDMX namespaces and their standard prefixes.
     */
    private NamespaceContext namespaceContext = null;

    /**
     * The XPath expression which retrieves the node with DSD details.
     */
    private String dsdInfoXPathExpr = null;

    /**
     * Constructs a new <code>SdmxSupport</code> instance.
     *
     * @param version       the value for {@link #sdmxVersion} property
     * @param nsCtx         the value for {@link #namespaceContext} property
     * @param infoXPathExpr the value for {@link #dsdInfoXPathExpr} property
     */
    protected SdmxHelper(SdmxVersion version, NamespaceContext nsCtx, String infoXPathExpr) {
        this.sdmxVersion = version;
        this.namespaceContext = nsCtx;
        this.dsdInfoXPathExpr = infoXPathExpr;
    }

    /**
     * Getter for the helper instance.
     *
     * @param version the SDMX version for which to get the instance
     *
     * @return the helper instance
     */
    public static SdmxHelper getInstance(SdmxVersion version) {
        switch (version) {
            case SDMX_2_0:
                return Sdmx20Helper.getInstance();
            case SDMX_2_1:
                return Sdmx21Helper.getInstance();
            default:
                throw new IllegalArgumentException("Unsupported SDMX version: " + version);
        }
    }

    /**
     * Getter for the SDMX version used by specified DSD document.
     *
     * @param dsd the DSD document
     *
     * @return the SDMX version, <code>null</code> if unknown or unsupported
     */
    public static SdmxVersion getSdmxVersion(Document dsd) {
        String namespace = dsd.getDocumentElement().getNamespaceURI();

        for (SdmxVersion version : SdmxVersion.values()) {
            if (namespace.equals(SdmxHelper.getInstance(version).getNamespaceContext().getNamespaceURI(
                    XMLConstants.DEFAULT_NS_PREFIX))) {
                return version;
            }
        }

        return null;
    }

    /**
     * Getter for {@link #sdmxVersion} property.
     *
     * @return the value
     */
    protected SdmxVersion getSdmxVersion() {
        return this.sdmxVersion;
    }

    /**
     * Getter for {@link #namespaceContext} property.
     *
     * @return the value
     */
    protected NamespaceContext getNamespaceContext() {
        return this.namespaceContext;
    }

    /**
     * Creates a XPath instance, which has support for SDMX namespaces and standard prefixes.
     *
     * @return the instance
     */
    protected XPath createXPath() {
        XPath xpath = SdmxHelper.xPathFactory.newXPath();
        xpath.setNamespaceContext(this.getNamespaceContext());
        return xpath;
    }

    /**
     * Resolves any external references from specified document and creates a new one with all the data about all
     * required items (concepts, codelists etc...).
     *
     * @param dsd      the DSD document
     * @param basePath the path used for resolving relative references; this should be the folder from where the DSD
     *                 document was loaded
     *
     * @return the resulted document, which can be the source one if there are no external references
     *
     * @throws Exception if fails
     */
    public Document resolveExternalReferences(Document dsd, File basePath) throws Exception {
        return dsd;
    }

    /**
     * Evaluates an XPath expression used for retrieving the data about a DSD document.
     *
     * @param dsd the DSD document
     *
     * @return the resulted <code>DsdInfo</code> instance
     *
     * @throws Exception if fails
     */
    public DsdInfo getDsdInfo(Document dsd) throws Exception {
        XPath xpath = this.createXPath();

        NodeList nodeList = (NodeList) xpath.compile(this.dsdInfoXPathExpr)
                .evaluate(dsd, XPathConstants.NODESET);

        if (nodeList.getLength() != 1) {
            throw new org.estat.xsdgen.sdmx20.XsdGeneratorException(
            	"Invalid DSD file, contains " + nodeList.getLength() + " definitions, expected only 1");
        }

        Element elm = (Element) nodeList.item(0);

        DsdInfo info = new DsdInfo();

        info.setSdmxVersion(this.getSdmxVersion());
        info.setAgency(StringUtils.trimToNull(elm.getAttribute("agencyID")));
        info.setId(StringUtils.trimToNull(elm.getAttribute("id")));
        info.setVersion(StringUtils.trimToNull(elm.getAttribute("version")));

        if ((info.getAgency() == null) || (info.getId() == null)) {
            throw new org.estat.xsdgen.sdmx21.XsdGeneratorException(
            	"Failed to get all mandatory properties: " + info);
        }

        return info;
    }

    /**
     * Getter for the recommended name to be used for a DSD file.
     *
     * @param info the info about the DSD document
     *
     * @return the name
     */
    public String getDsdRecommendedName(DsdInfo info) {
        return info.getBaseFileName() + "." + Constants.EXTENSION_XML;
    }

    /**
     * Getter for a list of options which can be displayed to user for selecting the type of the XSD to generate.
     *
     * @param dsd the DSD document for which to get the options list
     *
     * @return the list with options
     *
     * @throws Exception if fails
     */
    public abstract List<XsdOption> getXsdGenOptions(Document dsd) throws Exception;

    /**
     * Getter for the recommended name to be used for a XSD file.
     *
     * @param info   the info about the source DSD document
     * @param option the option used for generating the XSD document
     *
     * @return the name
     */
    public abstract String getXsdRecommendedName(DsdInfo info, XsdOption option);

    /**
     * Generates a XSD using <code>XSD Generator</code> library.
     *
     * @param dsd    the DSD document for which the XSD must be generated
     * @param option the option which specifies the type of the XSD to generate
     *
     * @return the resulted XSD document
     *
     * @throws Exception if fails
     */
    public abstract void generateXsd(InputStream dsd, XsdOption option, StreamResult result) throws Exception;

    /**
     * Generates a XSD using <code>XSD Generator</code> library.
     *
     * @param dsd    the DSD document for which the XSD must be generated
     * @param option the option which specifies the type of the XSD to generate
     *
     * @return the resulted XSD document
     *
     * @throws Exception if fails
     */
    public abstract Document generateXsd(Document dsd, XsdOption option) throws Exception;

    /**
     * An option which specifies the type of the XSD to generate.
     */
    public interface XsdOption {
    }
}
