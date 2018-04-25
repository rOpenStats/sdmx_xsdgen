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
 * A bean with some basic properties about a key family (for SDMX v2.0) or with the basic properties about a structure
 * definition (for SDMX v2.1).
 */
public class DsdInfo implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The SDMX version.
     */
    private SdmxVersion sdmxVersion = null;

    /**
     * The agency.
     */
    private String agency = null;

    /**
     * The id.
     */
    private String id = null;

    /**
     * The version.
     */
    private String version = null;

    /**
     * Constructs a new <code>DsdInfo</code> instance.
     */
    public DsdInfo() {
    }

    /**
     * Returns a string representation of the object.
     *
     * @return the result
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.id).append(" - ").append(this.agency);
        if (this.version != null) {
            result.append(" - ").append(this.version);
        }
        return result.toString();
    }

    /**
     * Returns a base name to be used as start point for the file names.
     *
     * @return the value
     */
    public String getBaseFileName() {
        return (this.id + "_" + this.agency + (this.version != null ? "_" + this.version.replace('.', '_') : ""))
                .toUpperCase();
    }

    /**
     * Getter for {@link #sdmxVersion} property.
     *
     * @param value the value
     */
    public void setSdmxVersion(SdmxVersion value) {
        this.sdmxVersion = value;
    }

    /**
     * Setter for {@link #sdmxVersion} property.
     *
     * @return the value
     */
    public SdmxVersion getSdmxVersion() {
        return this.sdmxVersion;
    }

    /**
     * Getter for {@link #agency} property.
     *
     * @return the value
     */
    public String getAgency() {
        return this.agency;
    }

    /**
     * Setter for {@link #agency} property.
     *
     * @param value the value
     */
    public void setAgency(String value) {
        this.agency = value;
    }

    /**
     * Getter for {@link #id} property.
     *
     * @return the value
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for {@link #id} property.
     *
     * @param value the value
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Getter for {@link #version} property.
     *
     * @return the value
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Setter for {@link #version} property.
     *
     * @param value the value
     */
    public void setVersion(String value) {
        this.version = value;
    }
}
