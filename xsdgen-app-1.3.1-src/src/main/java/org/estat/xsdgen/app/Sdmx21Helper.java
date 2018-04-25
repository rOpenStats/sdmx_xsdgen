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
import org.estat.xsdgen.app.SdmxHelper.XsdOption;
import org.estat.xsdgen.sdmx21.XsdGeneratorException;
import org.estat.xsdgen.sdmx21.XsdGeneratorFactory;
import org.estat.xsdgen.sdmx21.XsdGeneratorOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Support class for SDMX 2.1 standard.
 */
public class Sdmx21Helper extends SdmxHelper implements Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5860509452491081321L;

	/**
     * The one and only one instance.
     */
    private static Sdmx21Helper instance = new Sdmx21Helper();

    /**
     * The factory used for creating {@link Transformer} instances.
     */
    //private TransformerFactory transformerFactory = new org.apache.xalan.processor.TransformerFactoryImpl();
    //private TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
    private TransformerFactory transformerFactory = null;

    /**
     * The ID of the dimension to be used for generating flat messages.
     */
    private static final String FLAT_DIMENSION = "AllDimensions";

    /**
     * Constructs a new <code>SdmxSupport21</code> instance.
     */
    public Sdmx21Helper() {
        super(SdmxVersion.SDMX_2_1, new SdmxNamespaceContext("sdmx-ns-21.properties"),
                "/message:Structure/message:Structures/structure:DataStructures" +
                        "/structure:DataStructure");
    }

    /**
     * Getter for {@link #instance}.
     *
     * @return the value
     */
    protected static SdmxHelper getInstance() {
        return Sdmx21Helper.instance;
    }

    /**
     * Resolves any external references from specified document and creates a new one which contains the data about all
     * required items (concepts, codelists etc...).
     *
     * @param dsd      the DSD document
     * @param basePath the path used for resolving relative references
     *
     * @return the resulted document, which can be the source one if there are no external references
     *
     * @throws Exception if fails
     */
    public Document resolveExternalReferences(Document dsd, File basePath)
            throws Exception {
        InputStream is = this.getClass().getResourceAsStream("resolve-external-references.xsl");

        if (is == null) {
            throw new XsdGeneratorException("Failed to get from classpath the XSLT stylesheet used for resolving " +
                    "external references");
        }

        try {
            Transformer transformer = this.transformerFactory.newTransformer(new StreamSource(is));
            // Maybe is better to receive the URIResolver as an argument because this should not decide
            // how errors are reported...
            transformer.setURIResolver(new RefURIResolver(basePath.toURI(), true));
            DOMResult result = new DOMResult();
            transformer.transform(new DOMSource(dsd), result);
            return (Document) result.getNode();
        } finally {
            is.close();
        }
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
    public List<XsdOption> getXsdGenOptions(Document dsd) throws Exception {
        XPath xPath = createXPath();

        NodeList nodeList = (NodeList) xPath.evaluate(
                "/message:Structure/message:Structures/structure:DataStructures" +
                        "/structure:DataStructure/structure:DataStructureComponents/structure:DimensionList" +
                        "/structure:MeasureDimension", dsd, XPathConstants.NODESET);

        String measureDimId = null;

        if (nodeList.getLength() == 1) {
            measureDimId = this.getComponentId(nodeList.item(0));
        }

        NodeList dimList = (NodeList) xPath.evaluate(
                "/message:Structure/message:Structures/structure:DataStructures/structure:DataStructure" +
                        "/structure:DataStructureComponents/structure:DimensionList" +
                        "/structure:*", dsd, XPathConstants.NODESET);

        List<XsdOption> options = new ArrayList<XsdOption>();

        options.add(new XsdOptionImpl());
        options.add(new XsdOptionImpl(Sdmx21Helper.FLAT_DIMENSION, false));

        for (int i = 0; i < dimList.getLength(); i++) {
            String id = this.getComponentId(dimList.item(i));

            options.add(new XsdOptionImpl(id, false));
            if ((measureDimId != null) && measureDimId.equals(id)) {
                options.add(new XsdOptionImpl(id, true));
            }
        }

        return options;
    }

    /**
     * Getter for the recommended name to be used for a XSD file.
     *
     * @param info   the info about the source DSD document
     * @param option the option used for generating the XSD document
     *
     * @return the name
     */
    public String getXsdRecommendedName(DsdInfo info, XsdOption option) {
        XsdOptionImpl opt = (XsdOptionImpl) option;
        String dimAtObs = opt.getDimensionAtObservation();

        StringBuffer result = new StringBuffer();
        result.append(info.getBaseFileName()).append('_');

        if (dimAtObs != null) {
            result.append(dimAtObs.equals(Sdmx21Helper.FLAT_DIMENSION) ? "FLAT" : "XS_" + dimAtObs);

            if (opt.isExplicitMeasures()) {
                result.append("_EXPLICIT");
            }
        } else {
            result.append("TS");
        }

        result.append('.').append(Constants.EXTENSION_XSD);
        return result.toString().toUpperCase();
    }

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
    @Override
    public void generateXsd(InputStream dsd, XsdOption option, StreamResult result) throws Exception {
    }
    
	@Override
	public Document generateXsd(Document dsd, XsdOption option) throws Exception {
        return XsdGeneratorFactory.getInstance().newGeneratorInstance().generate(dsd,
                ((XsdOptionImpl) option).getGeneratorOptions(), null);
    }

    /**
     * Gets the ID for a DSD component.
     *
     * @param node the DSD component node element
     *
     * @return component's ID
     *
     * @throws Exception if fails
     */
    private String getComponentId(Node node)
            throws Exception {
        Element elm = (Element) node;

        String id = StringUtils.trimToNull(elm.getAttribute("id"));

        if (id != null) {
            return id; // cheap
        }

        XPath xpath = createXPath();

        String refId = StringUtils.trimToNull((String) xpath.evaluate("structure:ConceptIdentity/Ref/@id",
                elm, XPathConstants.STRING));

        if (refId != null) {
            return refId; // also this is cheap
        }

        String urn = StringUtils.trimToNull((String) xpath.evaluate("structure:ConceptIdentity/URN",
                elm, XPathConstants.STRING));

        if (urn == null) {
            throw new XsdGeneratorException("Failed to get the concept for a component, both Ref and URN elements " +
                    "are missing");
        }

        id = StringUtils.trimToNull((String) xpath.evaluate("//structure:Concept[@urn='"
                + urn + "']/@id", elm.getOwnerDocument(), XPathConstants.STRING));

        if (id == null) {
            throw new XsdGeneratorException("Failed to get the concept identified using urn '"
                    + urn + "'");
        }

        return id;
    }

    /**
     * The {@link XsdOption} implementation for SDMX 2.0 version.
     */
    class XsdOptionImpl implements XsdOption, Serializable {

        /**
         * Serial Version UID
		 */
		private static final long serialVersionUID = 8249591134432063692L;

		/**
         * The dimension at observation level.
         *
         * <p>This must be <code>null</code> for time series messages, {@link Sdmx21Helper#FLAT_DIMENSION} for flat
         * messages or the ID of the dimension for XS messages.</p>
         */
        private String dimensionAtObservation = null;

        /**
         * This is valid only if the XSD is for XS messages and indicates if explicit measures are used in the cross
         * sectional format.
         */
        private boolean explicitMeasures = false;

        /**
         * Constructs a new <code>XsdOptionImpl</code> instance for time series XSDs.
         */
        public XsdOptionImpl() {
        }

        /**
         * Constructs a new <code>XsdOptionImpl</code> instance for specified dimension at observation level.
         *
         * @param dimAtObs the value for {@link #dimensionAtObservation} property
         * @param explicit value for {@link #explicitMeasures} property
         */
        public XsdOptionImpl(String dimAtObs, boolean explicit) {
            this.dimensionAtObservation = dimAtObs;
            this.explicitMeasures = explicit;
        }

        /**
         * Getter for {@link #dimensionAtObservation} property.
         *
         * @return the value
         */
        public String getDimensionAtObservation() {
            return this.dimensionAtObservation;
        }

        /**
         * Getter for {@link #explicitMeasures} property.
         *
         * @return the value
         */
        public boolean isExplicitMeasures() {
            return this.explicitMeasures;
        }

        /**
         * Returns a string representation of the object.
         *
         * @return the result
         */
        public String toString() {
            if (this.dimensionAtObservation != null) {
                String result;

                if (Sdmx21Helper.FLAT_DIMENSION.equals(this.dimensionAtObservation)) {
                    result = Globals.getMessages().getString("results.sdmx_2_1.option.flat");
                } else {
                    result = MessageFormat.format(Globals.getMessages().getString("results.sdmx_2_1.option.cross"),
                            this.dimensionAtObservation);
                }

                if (this.explicitMeasures) {
                    result += " " + Globals.getMessages().getString("results.sdmx_2_1.option.explicit");
                }

                return result;
            }

            return Globals.getMessages().getString("results.sdmx_2_1.option.timeseries");
        }

        /**
         * Getter for the XSD Generator options instance.
         *
         * @return the value
         */
        public XsdGeneratorOptions getGeneratorOptions() {
            if (this.dimensionAtObservation != null) {
                return XsdGeneratorOptions.getOptionsForCrossMessages(this.dimensionAtObservation,
                        this.explicitMeasures);
            } else {
                return XsdGeneratorOptions.getOptionsForTimeSeriesMessages();
            }
        }
    }
}
