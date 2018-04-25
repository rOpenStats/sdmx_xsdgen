/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.URIResolver;

import org.w3c.dom.Document;

/**
 * Generates XML schema files for a DSD using XSL transformations. This supports only the SDMX 2.1 standard.
 */
class XsdGeneratorSdmx extends XsdGeneratorDelegateBase {

    /**
     * The XSLT stylesheet used for generating SDMX 2.1 XSDs.
     */
    private static final String XSLT_STYLESHEET = "StructureToXSD.xsl";

    // Stylesheet parameters.
    private static final String PARAM_IS_TIME_SERIES = "Param_IsTimeSeries";
    private static final String PARAM_DIMENSION_AT_OBSERVATION = "Param_DimensionAtObservation";
    private static final String PARAM_EXPLICIT_MEASURES = "Param_ExplicitMeasures";
    private static final String PARAM_STRUCTURE_ID = "Param_StructureId";
    private static final String PARAM_STRUCTURE_AGENCYID = "Param_StructureAgencyID";
    private static final String PARAM_STRUCTURE_VERSION = "Param_StructureVersion";
    private static final String PARAM_NAMESPACE = "Param_Namespace";
    private static final String PARAM_IMPORTED_SCHEMA_PREFIX = "Param_ImportedSchemaPrefix";

    /**
     * Constructs a new <code>XsdGeneratorSdmx21</code> instance.
     */
    public XsdGeneratorSdmx() {
    }

    /**
     * Generates a XML schema definition.
     *
     * @param source   the source document with data structure definition
     * @param options  the options
     * @param resolver used for retrieving any other documents referenced from source document; may be <code>null</code>
     *                 if the source document contains the definitions for all required objects (the dsd, the concept
     *                 schemes, the codelists etc...)
     *
     * @return the resulted XML schema document
     *
     * @throws Exception if fails
     */
    public Document generate(Document source, XsdGeneratorOptions options, URIResolver resolver) throws Exception {
        return super.transform(XsdGeneratorSdmx.XSLT_STYLESHEET, source, this.createParametersMap(options), resolver);
    }

    /**
     * Creates the map with stylesheet's parameters.
     *
     * @param options the options bean
     *
     * @return the parameters map
     */
    private Map<String, Object> createParametersMap(XsdGeneratorOptions options) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(XsdGeneratorSdmx.PARAM_IS_TIME_SERIES, options.isTimeSeries());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_DIMENSION_AT_OBSERVATION,
                options.getDimensionAtObservation());
        params.put(XsdGeneratorSdmx.PARAM_EXPLICIT_MEASURES, options.isExplicitMeasures());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_STRUCTURE_ID, options.getStructureId());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_STRUCTURE_AGENCYID,
                options.getStructureAgencyId());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_STRUCTURE_VERSION, options.getStructureVersion());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_NAMESPACE, options.getNamespace());
        this.addOptionalStringParam(params, XsdGeneratorSdmx.PARAM_IMPORTED_SCHEMA_PREFIX,
                options.getImportedSchemasPrefix());

        return params;
    }

    /**
     * Adds an optional parameter to the parameters map.
     *
     * @param params the parameters map
     * @param name   the name of the parameter
     * @param value  the value
     */
    private void addOptionalStringParam(Map<String, Object> params, String name, String value) {
        String v = value == null ? null : value.trim();
        if ((v != null) && (v.length() > 0)) {
            params.put(name, v);
        }
    }
}
