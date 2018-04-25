/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

/**
 * All supported SDMX versions.
 */
public enum SdmxVersion {

    /**
     * SDMX v2.0
     */
    SDMX_2_0 {
        /**
         * Returns the SDMX version as a <code>String</code> object.
         *
         * @return the version
         */
        @Override
        public String toString() {
            return "2.0";
        }
    },

    /**
     * SDMX v2.1
     */
    SDMX_2_1 {
        /**
         * Returns the SDMX version as a <code>String</code> object.
         *
         * @return the version
         */
        @Override
        public String toString() {
            return "2.1";
        }
    };
}
