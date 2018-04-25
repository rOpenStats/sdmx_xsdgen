/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

import java.io.Serializable;

import org.estat.xsdgen.sdmx20.XsdGeneratorOptions;

/**
 * The options required for generating XML schema definitions for <strong>SDMX 2.0</strong> standard.
 *
 * @since 1.1.0
 */
public class XsdGeneratorOptions implements Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6710802660732088524L;

    /**
     * Optional option specifying the ID of the DSD for which to generate the XSD.
     *
     * <p>This is not mandatory because usually the source file contains only one data structure definition. Should be
     * used together with {@link #structureAgencyId} and {@link #structureVersion}, but is not mandatory.</p>
     */
    private String structureId = null;

    /**
     * Optional option specifying the ID of data structure's maintenance agency.
     */
    private String structureAgencyId = null;

    /**
     * Optional option specifying the version of DSD.
     */
    private String structureVersion = null;

    /**
     * Optional option specifying the namespace to be used for resulted XSD.
     *
     * <p>If this is empty or <code>null</code> then a default one will be generated using a standard convention.</p>
     */
    private String namespace = null;

    /**
     * Constructs a new <code>XsdGeneratorParams</code> instance.
     */
    public XsdGeneratorOptions() {
    }

    /**
     * Getter for {@link #structureId} property.
     *
     * @return the value
     */
    public String getStructureId() {
        return this.structureId;
    }

    /**
     * Setter for {@link #structureId} property.
     *
     * @param value the value
     */
    public void setStructureId(String value) {
        this.structureId = value;
    }

    /**
     * Getter for {@link #structureAgencyId} property.
     *
     * @return the value
     */
    public String getStructureAgencyId() {
        return this.structureAgencyId;
    }

    /**
     * Setter for {@link #structureAgencyId} property.
     *
     * @param value the value
     */
    public void setStructureAgencyId(String value) {
        this.structureAgencyId = value;
    }

    /**
     * Getter for {@link #structureVersion} property.
     *
     * @return the value
     */
    public String getStructureVersion() {
        return this.structureVersion;
    }

    /**
     * Setter for {@link #structureVersion} property.
     *
     * @param value the value
     */
    public void setStructureVersion(String value) {
        this.structureVersion = value;
    }

    /**
     * Getter for {@link #namespace} property.
     *
     * @return the value
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Setter for {@link #namespace} property.
     *
     * @param value the value
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }
}
