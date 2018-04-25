/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Global variables.
 */
public final class Globals {

    /**
     * The main resource bundle.
     */
    private static ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_BASE_NAME);

    /**
     * Current registry configuration.
     */
    private static RegistryConfig registryConfig = null;

    /**
     * Current DSD document.
     */
    private static File dsd = null;

    /**
     * The data about key family (for SDMX v2.0) or data structure (for SDMX v2.1).
     */
    private static DsdInfo dsdInfo = null;

    /**
     * Specifies if the current DSD is loaded from local file system or is retrieved from SDMX registry.
     */
    private static boolean localDsd = false;

    /**
     * Private constructor which prevents instantiation.
     */
    private Globals() {
    }

    /**
     * Getter for the main resource bundle.
     *
     * @return the value
     */
    public static ResourceBundle getMessages() {
        return Globals.messages;
    }

    /**
     * Getter for current registry configuration.
     *
     * @return the value
     */
    public static RegistryConfig getCurrentRegistryConfig() {
        return Globals.registryConfig;
    }

    /**
     * Setter for current registry configuration.
     *
     * @param value the value
     */
    public static void setCurrentRegistryConfig(RegistryConfig value) {
        Globals.registryConfig = value;
    }

    /**
     * Getter for current DSD document.
     *
     * @return the value
     */
    public static File getCurrentDSD() {
        return Globals.dsd;
    }

    /**
     * Sets the current DSD document.
     *
     * @param value   the document
     * @param info    basic data about DSD document
     * @param isLocal specifies if the document is loaded from local file system or received from SDMX Registry
     */
    public static void setCurrentDSD(File value, DsdInfo info, boolean isLocal) {
        Globals.dsd = value;
        Globals.dsdInfo = info;
        Globals.localDsd = isLocal;
    }

    /**
     * Getter for the data about current key family (for SDMX v2.0) or about data structure (for SDMX v2.1).
     *
     * @return the value
     */
    public static DsdInfo getDsdInfo() {
        return Globals.dsdInfo;
    }

    /**
     * @return <code>true</code> if current DSD is loaded from local file system
     */
    public static boolean isLocalDsd() {
        return Globals.localDsd;
    }
}
