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

/**
 * Defines the methods which must be implemented by a XSD generator implementation.
 *
 * <p>Note that is not always possible to use the resulted XSD documents directly for creating a
 * <code></code>javax.xml.validation.Schema</code> using <code>DOMSource</code> for specifying the XSD source/sources,
 * the error thrown being related to some missing namespaces. This has nothing to do with XSD Generator implementation,
 * being probably a bug in some Java versions (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6626853, this may
 * have something to do with this problem). Instead convert the XSD document to a string and then use
 * <code>StreamSource</code> for specifying schema's source(s) XSD(s).</p>
 */
public interface XsdGenerator {

    /**
     * Supported formats for SDMX 2.0 XSD files.
     */
    public enum XsdType {
        /**
         * XSD for compact formats.
         */
        COMPACT,

        /**
         * XSD for cross-sectional formats.
         */
        CROSS_SECTIONAL,

        /**
         * XSD for utility formats.
         */
        UTILITY
    }

    /**
     * Generates a XML schema definition for <strong>SDMX v2.0</strong> standard.
     *
     * @param source the source document with data structure definition
     * @param type   the type of resulted XSD
     * @param result the stream result where the result is being streamed
     *
     * @return nothing
     */
    void generate(InputStream source, XsdType type, StreamResult result);
}
