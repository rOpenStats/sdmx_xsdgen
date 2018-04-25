/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.w3c.dom.Document;

import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Main class.
 */
public class XSDGeneratorApp {

    /**
     * Main method.
     *
     * <p>The user must specify 3 arguments, in any order: the path to DSD file, the format for XSD file and the path
     * where to save the result.</p>
     *
     * @param args command line arguments
     *
     * @throws Exception if fails
     */
    public static void main(String[] args) throws Exception {
        ResourceBundle messages = Globals.getMessages();

        CommandLineOptions options = new CommandLineOptions();
        JCommander commander = new JCommander(options);

        ParameterException paramError = null;

        try {
            commander.parse(args);
            options.validateOptions();

        	XSDGeneratorApp.generate(options);
        } catch (ParameterException e) {
            paramError = e;
        }

        if (paramError != null) {
            System.err.println(MessageFormat.format(messages.getString("cli.error.command.line"),
                    paramError.getMessage()));
            System.err.println(messages.getString("cli.error.help"));
            System.err.println();
            System.exit(2);
        } else {
            System.exit(0);
        }
    }

    /**
     * Generates the XSD file for specified set of options.
     *
     * @param options the options, passed as command line arguments
     *
     * @throws Exception if fails
     */
    private static void generate(CommandLineOptions options) throws Exception {
        ResourceBundle messages = Globals.getMessages();

        System.out.println(MessageFormat.format(messages.getString("cli.progress.load.dsd"), options.getInputFile()));
        Document dsd = Utils.readXML(options.getInputFile());

        SdmxVersion sdmxVersion = SdmxHelper.getSdmxVersion(dsd);

        if (sdmxVersion == null) {
            throw new org.estat.xsdgen.sdmx21.XsdGeneratorException("Failed to detect the SDMX version");
        }

        SdmxHelper helper = SdmxHelper.getInstance(sdmxVersion);

        System.out.println(MessageFormat.format(messages.getString("cli.progress.info.dsd"),
                helper.getDsdInfo(dsd), sdmxVersion));

    	InputStream is = new FileInputStream(options.getInputFile());
    	FileOutputStream result = new FileOutputStream(options.getOutputFile());

    	if (sdmxVersion.equals(SdmxVersion.SDMX_2_0)) {
    		org.estat.xsdgen.sdmx20.XsdGenerator generator = 
    				org.estat.xsdgen.sdmx20.XsdGeneratorFactory.getInstance().newGeneratorInstance();
            
    		org.estat.xsdgen.sdmx20.XsdGenerator.XsdType xsdType = options.isCrossFormat() ? 
    				org.estat.xsdgen.sdmx20.XsdGenerator.XsdType.CROSS_SECTIONAL
                    : options.isUtilityFormat() ? org.estat.xsdgen.sdmx20.XsdGenerator.XsdType.UTILITY
                    : org.estat.xsdgen.sdmx20.XsdGenerator.XsdType.COMPACT;

            System.out.println(MessageFormat.format(messages.getString("cli.progress.generate.xsd"), xsdType));
            generator.generate(is, xsdType, new StreamResult(result));
        } else {
        	org.estat.xsdgen.sdmx21.XsdGenerator generator = org.estat.xsdgen.sdmx21.XsdGeneratorFactory.getInstance().newGeneratorInstance();
        	org.estat.xsdgen.sdmx21.XsdGeneratorOptions o = org.estat.xsdgen.sdmx21.XsdGeneratorOptions.getOptionsForFlatMessages();

            if (options.isTimeSeries()) {
                System.out.println(messages.getString("cli.progress.generate.timeseries"));
                o.setTimeSeries(true);
            } else if (options.getCrossDimension() != null) {
                o.setDimensionAtObservation(options.getCrossDimension());
                String key = "cli.progress.generate.xs";
                if (options.isExplicit()) {
                    o.setExplicitMeasures(true);
                    key += ".explicit";
                }
                System.out.println(MessageFormat.format(messages.getString(key), options.getCrossDimension()));
            } else {
                System.out.println(messages.getString("cli.progress.generate.flat"));
            }

            File parent = options.getInputFile().getParentFile();
            if (parent == null) {
                parent = new File(".");
            }

            Document xsd = generator.generate(dsd, o, new RefURIResolver(parent.toURI(), false));
            Utils.writeXML(options.getOutputFile(), xsd);
        }

        System.out.println(MessageFormat.format(messages.getString("cli.progress.write.xsd"), options.getOutputFile())
        		+ "\n" + messages.getString("cli.progress.done"));
    }
}
