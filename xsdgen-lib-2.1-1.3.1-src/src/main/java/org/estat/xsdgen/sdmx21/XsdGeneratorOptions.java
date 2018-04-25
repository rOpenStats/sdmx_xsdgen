/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import java.io.Serializable;

/**
 * The options required for generating XML schema definitions for <strong>SDMX 2.1</strong> standard.
 *
 * @since 1.1.0
 */
public class XsdGeneratorOptions implements Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5560969776026461770L;

	/**
     * Specifies if the resulted XSD is for time series messages.
     *
     * <p>If <code>true</code> then {@link #dimensionAtObservation} and {@link #explicitMeasures} are ignored.</p>
     */
    private boolean timeSeries = false;

    /**
     * Specifies the dimension at the observation level.
     *
     * <p>If is <code>AllDimensions</code> then the resulted XSD will be for flat messages.</p>
     *
     * <p>This is ignored if {@link #timeSeries} is <code>true</code> because only the time dimension can be at
     * observation level.</p>
     *
     * <p>Note that it can be set to <code>TIME_PERIOD</code> if {@link #timeSeries} is <code>false</code>, this being
     * valid according to SDMX 2.1 documentation, the resulted XSD being be for structure specific data messages with
     * the time dimension at observation level.</p>
     */
    private String dimensionAtObservation = null;

    /**
     * Indicates whether explicit measures are used in the cross sectional format.
     *
     * <p>This is used only if the DSD defines a measure dimension and if the measure dimension is at observation level
     * (set explicitly using {@link #dimensionAtObservation} or if the resulted XSD is for flat messages). It is ignored
     * for time series XSDs.</p>
     */
    private boolean explicitMeasures = false;

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
     * An optional option specifying the prefix to be used for SDMX 2.1 schemas imported into resulted XSD. This can be
     * used for specifying a relative path for example (and in this case should end with <code>/</code> character).</p>
     */
    private String importedSchemasPrefix = null;

    /**
     * Constructs a new <code>XsdGeneratorParams</code> instance.
     */
    public XsdGeneratorOptions() {
    }

    /**
     * Creates an instance which can be used for generating XML schema files for time series messages.
     *
     * @return the instance
     */
    public static XsdGeneratorOptions getOptionsForTimeSeriesMessages() {
        XsdGeneratorOptions o = new XsdGeneratorOptions();
        o.setTimeSeries(true);
        return o;
    }

    /**
     * Creates an instance which can be used for generating XML schema files for flat messages.
     *
     * @return the instance
     */
    public static XsdGeneratorOptions getOptionsForFlatMessages() {
        XsdGeneratorOptions o = new XsdGeneratorOptions();
        o.setDimensionAtObservation("AllDimensions");
        return o;
    }

    /**
     * Creates an instance which can be used for generating XML schema files for cross sectional messages.
     *
     * @param obsDim   the dimension at observation level
     * @param explicit specifies if explicit measures are used in the cross sectional format (the value for {@link
     *                 #explicitMeasures} option}
     *
     * @return the instance
     */
    public static XsdGeneratorOptions getOptionsForCrossMessages(String obsDim, boolean explicit) {
        XsdGeneratorOptions o = new XsdGeneratorOptions();
        o.setDimensionAtObservation(obsDim);
        o.setExplicitMeasures(explicit);
        return o;
    }

    /**
     * Getter for {@link #timeSeries} property.
     *
     * @return the value
     */
    public boolean isTimeSeries() {
        return this.timeSeries;
    }

    /**
     * Setter for {@link #timeSeries} property.
     *
     * @param value the value
     */
    public void setTimeSeries(boolean value) {
        this.timeSeries = value;
    }

    /**
     * Getter for {@link #dimensionAtObservation} property.
     *
     * @return the value
     */
    public String getDimensionAtObservation() {
        return this.dimensionAtObservation;
    }

    /**
     * Setter for {@link #dimensionAtObservation} property.
     *
     * @param value the value
     */
    public void setDimensionAtObservation(String value) {
        this.dimensionAtObservation = value;
    }

    /**
     * Getter for {@link #explicitMeasures} property.
     *
     * @return the value
     */
    public boolean isExplicitMeasures() {
        return this.explicitMeasures;
    }

    /**
     * Setter for {@link #isExplicitMeasures} property.
     *
     * @param value the value
     */
    public void setExplicitMeasures(boolean value) {
        this.explicitMeasures = value;
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

    /**
     * Getter for {@link #importedSchemasPrefix} property.
     *
     * @return the value
     */
    public String getImportedSchemasPrefix() {
        return this.importedSchemasPrefix;
    }

    /**
     * Setter for {@link #importedSchemasPrefix} property.
     *
     * @param value the value
     */
    public void setImportedSchemasPrefix(String value) {
        this.importedSchemasPrefix = value;
    }
}
