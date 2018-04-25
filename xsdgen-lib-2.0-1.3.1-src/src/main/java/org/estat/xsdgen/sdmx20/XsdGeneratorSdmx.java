/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;

import org.estat.xsdgen.sdmx20.XsdGenerator;
import org.estat.xsdgen.sdmx20.XsdGeneratorDelegateBase;
import org.estat.xsdgen.sdmx20.XsdGeneratorSdmx;

/**
 * Generates XML schema files for a DSD using XSL transformations. This supports only the SDMX 2.0 standard.
 */
class XsdGeneratorSdmx extends XsdGeneratorDelegateBase {

    /**
     * The default prefix to be used for the namespaces of resulted XSD files.
     */
    private static final String DEFAULT_NS_PREFIX = "urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=";

    /**
     * The name of the XSLT parameter used for specifying the namespace.
     */
    private static final String PARAM_NAMESPACE = "Namespace";

    /**
     * A XSLT stylesheet for each output type.
     */
    private Map<XsdGenerator.XsdType, String> stylesheets = new HashMap<XsdGenerator.XsdType, String>();

    /**
     * Constructs a new <code>XsdGeneratorSdmx20</code> instance
     */
    public XsdGeneratorSdmx() {
        this.stylesheets.put(XsdGenerator.XsdType.COMPACT, "StructureToCompact.xsl");
        this.stylesheets.put(XsdGenerator.XsdType.CROSS_SECTIONAL, "StructureToCrossSection.xsl");
        this.stylesheets.put(XsdGenerator.XsdType.UTILITY, "StructureToUtility.xsl");
    }

    /**
     * Generates a XML schema definition.
     *
     * @param source the source document with data structure definition
     * @param type   the type of resulted XSD
     *
     * @return the resulted XML schema document
     *
     * @throws Exception if fails
     */
    public void generate(InputStream source, XsdGenerator.XsdType type, StreamResult result) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(XsdGeneratorSdmx.PARAM_NAMESPACE, this.getDefaultNamespace(source, type));
        super.transform(this.stylesheets.get(type), source, params, null, result);
    }

    /**
     * Getter for the default namespace.
     *
     * @param dsd  the DSD document
     * @param type the type of the XSD which will result after transformation
     *
     * @return the namespace
     *
     * @throws Exception if fails
     */
    private String getDefaultNamespace(InputStream dsd, XsdGenerator.XsdType type) throws Exception {
//      XPathFactory factory = XPathFactory.newInstance();
//      XPath xpath = factory.newXPath();
//
//      // TODO: to add support for SDMX 2.0 namespaces...
//      
//      Element kfElm = (Element) xpath.compile(
//              "/*[local-name() = 'Structure']/*[local-name() = 'KeyFamilies']/*[local-name() = 'KeyFamily']")
//              .evaluate(dsd, XPathConstants.NODE);

      String suffix = type == XsdGenerator.XsdType.COMPACT
              ? "compact" : (type == XsdGenerator.XsdType.CROSS_SECTIONAL ? "cross" : "utility");

//      return XsdGeneratorSdmx20.DEFAULT_NS_PREFIX + kfElm.getAttribute("agencyID") + ":"
//              + kfElm.getAttribute("id") + ":" + suffix;
      return XsdGeneratorSdmx.DEFAULT_NS_PREFIX + "ESTAT" + ":"
      + "FOO_KF" + ":" + suffix;
  }
}
