/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import java.awt.*;
import java.io.File;

/**
 * Defines various constants used by GUI classes.
 */
public final class Constants {

    /**
     * Private constructor which prevents instantiation.
     */
    private Constants() {
    }

    /**
     * The configuration file.
     */
    public static final String CONFIGURATION_FILE = "xsdgen.properties";

    /**
     * Standard XML file extension.
     */
    public static final String EXTENSION_XML = "xml";

    /**
     * Standard XSD file extension.
     */
    public static final String EXTENSION_XSD = "xsd";

    /**
     * The mime type for XML files.
     */
    public static final String XML_MIME_TYPE = "text/xml";

    /**
     * The base name of main resource bundle.
     */
    public static final String MESSAGES_BUNDLE_BASE_NAME = "org.estat.xsdgen.app.messages";

    /**
     * The base name of the resource bundle with messages related to menus.
     */
    public static final String MENU_BUNDLE_BASE_NAME = "org.estat.xsdgen.app.menu";

    /**
     * The size of the main frame.
     */
    public static final Dimension MAIN_FRAME_SIZE = new Dimension(500, 325);

    /**
     * SDMX Registry service name.
     */
    public static final String REGISTRY_WS_SERVICE_NAME = "SdmxRegistryService";

    /**
     * SDMX Registry namespace.
     */
    public static final String REGISTRY_WS_NAMESPACE = "http://www.aglis-sa.com/sdmxrr";

    /**
     * SDMX Registry target service endpoint.
     */
    public static final String REGISTRY_WS_PORT = "SdmxRegistryPort";

    /**
     * SDMX Registry operation.
     */
    public static final String REGISTRY_WS_OPERATION = "processRi";

    /**
     * The name of the SOAP header element with authentication data.
     */
    public static final String SDMX_AUTH_HEADER_NAME = "securityHeader";

    /**
     * The NS for the SOAP authentication header element.
     */
    public static final String SDMX_AUTH_HEADER_NAMESPACE =
            "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/SDMXSecurityMessage";

    /**
     * The namespace prefix used for SOAP header element with authentication data.
     */
    public static final String SDMX_AUTH_HEADER_PREFIX = "sec";

    /**
     * Name of the element which contains the domain used for authentication..
     */
    public static final String SDMX_AUTH_DOMAIN_ELM_NAME = "domain";

    /**
     * Name of the element which contains the username used for authentication.
     */
    public static final String SDMX_AUTH_USERNAME_ELM_NAME = "username";

    /**
     * Name of the element which contains the password used for authentication.
     */
    public static final String SDMX_AUTH_PASSWORD_ELM_NAME = "password";
    
    /**
     * Name of the java RunLine
     */
    public static final String JAVA_RUNLINE = "java";
    
    /**
     * Name of Root directory
     */
    public static final String ROOTDIR = new File(".").getAbsolutePath();
}
