/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Command line options.
 */
//@SuppressWarnings( {"FieldCanBeLocal"})
@Parameters(resourceBundle = "org.estat.xsdgen.app.messages")
public class CommandLineOptions {

    /**
     * If <code>true</code> then the application will display this help and then exists.
     */
    @Parameter(names = {"-h", "--help"}, descriptionKey = "cli.option.help")
    private boolean showUsage = false;

    /**
     * The path to a input DSD file.
     */
    @Parameter(names = {"-i", "--input"}, required = true, descriptionKey = "cli.option.input")
    private File inputFile = null;

    /**
     * The path to the output XSD file.
     */
    @Parameter(names = {"-o", "--output"}, required = true, descriptionKey = "cli.option.output")
    private File outputFile = null;

    /**
     * Specifies that the generated XSD must be for Cross Sectional format.
     */
    @Parameter(names = {"-x", "--cross"}, descriptionKey = "cli.option.cross")
    private boolean crossFormat = false;

    /**
     * Specifies that the generated XSD must be for Utility format.
     */
    @Parameter(names = {"-u", "--utility"}, descriptionKey = "cli.option.utility")
    private boolean utilityFormat = false;

    /**
     * Specifies that the generated XSD must be for time series messages (SDMX 2.0).
     */
    @Parameter(names = {"-t", "--ts", "--timeseries"}, descriptionKey = "cli.option.timeseries")
    private boolean timeSeries = false;

    /**
     * Specifies that the generated XSD must be for XS messages with specified dimension at observation level.
     */
    @Parameter(names = {"-s", "--xs"}, descriptionKey = "cli.option.xs")
    private String crossDimension = null;

    /**
     * Specifies that the generated XSD must be for messages for which explicit measures are used in the cross sectional
     * format.
     */
    @Parameter(names = {"-e", "--explicit"}, descriptionKey = "cli.option.explicit")
    private boolean explicit = false;

    /**
     * Specifies if the destination file can be overwritten. This is hidden because it should not be used.
     */
    @Parameter(names = {"-w", "--overwrite"}, hidden = true)
    private boolean overwrite = false;
    
    /**
     * Specifies the development environnement
     */
    @Parameter(names = {"-dev", "--development"}, hidden = true)
    private boolean development = false;

    /**
     * The additional/unknown arguments.
     */
    @Parameter(hidden = true)
    private List<String> additionalArguments = null;

    /**
     * Constructs a new <code>CommandLineOptions</code> instance.
     */
    public CommandLineOptions() {
    }

    /**
     * Performs additional validation, being called right after command line is parsed.
     */
    public void validateOptions() {
        ResourceBundle messages = Globals.getMessages();

        if ((this.additionalArguments != null) && !this.additionalArguments.isEmpty()) {
            throw new ParameterException(MessageFormat.format(messages.getString("cli.error.unknown.arguments"),
                    this.additionalArguments.get(0)));
        }

        if ((this.outputFile != null) && this.outputFile.exists()) {
            if (!this.overwrite) {
                throw new ParameterException(messages.getString("cli.error.output.exists"));
            }
        }
    }

    /**
     * Validates the options for specified SDMX version, this being called after the DSD is loaded and version
     * detected.
     *
     * @param version the SDMX version
     */
    public void validateOptions(SdmxVersion version) {
        ResourceBundle messages = Globals.getMessages();

        if (version == SdmxVersion.SDMX_2_0) {
            if (this.crossFormat && this.utilityFormat) {
                throw new ParameterException(messages.getString("cli.error.multiple.output.formats"));
            }
        }
    }

    /**
     * Getter for {@link #showUsage} property.
     *
     * @return the value
     */
    public boolean getShowUsage() {
        return this.showUsage;
    }

    /**
     * Getter for {@link #inputFile} property.
     *
     * @return the value
     */
    public File getInputFile() {
        return this.inputFile;
    }

    /**
     * Getter for {@link #outputFile} property.
     *
     * @return the value
     */
    public File getOutputFile() {
        return this.outputFile;
    }

    /**
     * Getter for {@link #crossFormat} property.
     *
     * @return the value
     */
    public boolean isCrossFormat() {
        return this.crossFormat;
    }

    /**
     * Getter for {@link #utilityFormat} property.
     *
     * @return the value
     */
    public boolean isUtilityFormat() {
        return this.utilityFormat;
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
     * Getter for {@link #crossDimension} property.
     *
     * @return the value
     */
    public String getCrossDimension() {
        return this.crossDimension;
    }

    /**
     * Getter for {@link #explicit} property.
     *
     * @return the value
     */
    public boolean isExplicit() {
        return this.explicit;
    }
    
    /**
     * Getter for {@link #development} property.
     *
     * @return the value
     */
    public boolean isDevelopment() {
        return this.development;
    }
}
