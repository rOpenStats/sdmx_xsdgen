/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import javax.xml.transform.URIResolver;

import org.w3c.dom.Document;

/**
 * A default implementation for a XSD generator, which delegates the tasks to {@link XsdGeneratorSdmx20} and {@link
 * XsdGeneratorSdmx} helper classes.
 */
class XsdGeneratorImpl implements XsdGenerator {

    /**
     * The delegate instance used for generating XML schema definitions for SDMX 2.1 standard.
     */
    private XsdGeneratorSdmx sdmxInstance = new XsdGeneratorSdmx();

    /**
     * Constructs a new <code>XsdGeneratorImpl</code> instance.
     */
    public XsdGeneratorImpl() {
    }

    /**
     * Generates a XML schema definition for <strong>SDMX v2.1</strong> standard.
     *
     * @param source   the source document with data structure definition
     * @param options  the options
     * @param resolver used for retrieving any other documents referenced from source document; may be <code>null</code>
     *                 if the source document contains the definitions for all required objects (the dsd, the concept
     *                 schemes, the codelists etc...)
     * @param result the stream result where the result is being streamed
     *
     * @return the resulted XML schema document
     */
    public Document generate(Document source, XsdGeneratorOptions options, URIResolver resolver) {
        try {
        	return sdmxInstance.generate(source, options, resolver);
        } catch (Exception e) {
            if (!(e instanceof XsdGeneratorException)) {
                throw new XsdGeneratorException("SDMX 2.1 XSD generation failed", e);
            } else {
                throw (RuntimeException) e;
            }
        }
    }
}
