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

import javax.xml.transform.stream.StreamResult;

import org.estat.xsdgen.sdmx20.XsdGenerator;
import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.estat.xsdgen.sdmx20.XsdGeneratorSdmx;

/**
 * A default implementation for a XSD generator, which delegates the tasks to {@link XsdGeneratorSdmx20}
 * helper classes.
 */
class XsdGeneratorImpl implements XsdGenerator {

    /**
     * The delegate instance used for generating XML schema definitions for SDMX 2.0 standard.
     */
    private XsdGeneratorSdmx sdmxInstance = new XsdGeneratorSdmx();

    /**
     * Constructs a new <code>XsdGeneratorImpl</code> instance.
     */
    public XsdGeneratorImpl() {
    }

    /**
     * Generates a XML schema definition for <strong>SDMX v2.0</strong> standard.
     *
     * @param source the source document with data structure definition
     * @param type   the type of resulted XSD
     * @param result the stream result where the result is being streamed
     *
     * @return the resulted XML schema document
     */
    public void generate(InputStream source, XsdType type, StreamResult result) {
        try {
            sdmxInstance.generate(source, type, result);
        } catch (Exception e) {
        	System.out.println("message:" + e.getMessage());
        	e.printStackTrace();
            if (!(e instanceof XsdGeneratorException)) {
                throw new XsdGeneratorException("SDMX 2.0 XSD generation failed", e);
            } else {
                throw (RuntimeException) e;
            }
        }
    }
}
