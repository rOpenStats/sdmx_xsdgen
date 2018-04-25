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

import javax.swing.*;

import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Main class.
 */
public class XSDGeneratorRun {

    /**
     * Main method.
     *
     * @param args command line arguments
     *
     * @throws Exception if fails
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            XSDGeneratorRun.cli(args);
        } else {
            XSDGeneratorRun.gui();
        }
    }

    /**
     * Main method for GUI mode.
     */
    private static void gui() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    JFrame frame = new MainFrame();

                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    // Cannot use a resource bundle here, because the exception can be
                    // a MissingResourceException instance...
                    JOptionPane.showMessageDialog(null, e, "XSD Generator", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Main method for CLI mode.
     *
     * <p>The user must specify 3 arguments, in any order: the path to DSD file, the format for XSD file and the path
     * where to save the result.</p>
     *
     * @param args command line arguments
     *
     * @throws Exception if fails
     */
    private static void cli(String[] args) throws Exception {
        ResourceBundle messages = Globals.getMessages();
        String[] command = null, arguments = null;
        String appline = messages.getString("app.name"), errormsg = null, 
        		initfolder = "{0}", fsep = "{1}", semicolon = ";", checkfolder = null,
        		cpfiles = null;
        StringBuffer bfolder = null;
        StringBuilder sb = null;
        File[] listFiles = null;
        Document dsd = null;
        SdmxVersion sdmxVersion = null;
        ProcessResult result = null;
        int i = 0, j = 0;

        System.out.println(messages.getString("app.title") + " v" + messages.getString("app.version"));
        System.out.println(messages.getString("app.copyright"));
        System.out.println();
        
        arguments = new String[args.length];
        sb = new StringBuilder();

        for (String argument : args) {
        	if (argument.startsWith("-")) {
        		if (sb != null && sb.length() > 0) {
        			arguments[i] = sb.toString().trim();
        			i++;
            		sb = new StringBuilder();
        		}
        		
        		arguments[i] = argument; 
        		i++;
        	} else {
            	sb.append(argument + " ");
        	}
        }
        
		if (sb != null && sb.length() > 0) {
			arguments[i] = sb.toString().trim();
			i++;
		}

        ArrayList<String> argslist = new ArrayList<String>();
        for (String argument : arguments) {
            if (argument != null && argument.length() > 0)
                argslist.add(argument);
        }

        arguments = argslist.toArray(new String[argslist.size()]);
        
        CommandLineOptions options = new CommandLineOptions();
        JCommander commander = new JCommander(options);

        ParameterException paramError = null;

        try {
            commander.parse(arguments);
            options.validateOptions();

            if (!options.getShowUsage()) {
                dsd = Utils.readXML(options.getInputFile());

                sdmxVersion = SdmxHelper.getSdmxVersion(dsd);

                if (sdmxVersion == null) {
                    throw new org.estat.xsdgen.sdmx21.XsdGeneratorException(
                    		"Failed to detect the SDMX version");
                } else {
                	String version = 
                			new StringBuffer(sdmxVersion.toString()).replace(1, 2, "_").toString();
                	if (options.isDevelopment()) {
                		bfolder = new StringBuffer(
                				messages.getString("jars.sdmx_" + version + ".dev.dependencies"));
                	} else {
                		bfolder = new StringBuffer(
                				messages.getString("jars.sdmx_" + version + ".dependencies"));
                	}
            		
            		i = bfolder.indexOf(initfolder);
            		while (i > -1) {
                		bfolder = bfolder.replace(i, i + initfolder.length(), 
                				Constants.ROOTDIR.substring(0, Constants.ROOTDIR.length()-2));
                		i = bfolder.indexOf(initfolder);
            		}
            		
            		i = bfolder.indexOf(fsep);
            		while (i > -1) {
                		bfolder = bfolder.replace(i, i + fsep.length(), File.separator);
                		i = bfolder.indexOf(fsep);
            		}
            		
            		cpfiles = "\"";

                    i = bfolder.indexOf(semicolon);
                    j = 0;
            		while (i > -1) {
            			checkfolder = bfolder.substring(j, i);
            			listFiles = new File(checkfolder).listFiles(
            					new FilenameFilter() {
            						public boolean accept(File dir, String name) {
            							return name.endsWith(".jar");
            						}
            					});
        		
            			if (listFiles == null || listFiles.length == 0) {
            				cpfiles += checkfolder + ";";
            			} else {
                			for (j = 0; j < listFiles.length; j++) {
                				cpfiles += listFiles[j].getAbsolutePath() + ";";
                			}
            			}
            			
            			j = i + 1;
                		i = bfolder.indexOf(semicolon, j);
            		}
                    cpfiles = cpfiles.substring(0, cpfiles.length()-1) + "\"";
                    
                    command = new String[arguments.length + 4];
            	}
            	
                command[0] = Constants.JAVA_RUNLINE;
                command[1] = "-classpath";
                command[2] = cpfiles;
                command[3] = appline;
                System.arraycopy(arguments, 0, command, 4, arguments.length);
                
        		if (options.isDevelopment()) {
        			String cmdline = "";

            		for (String cmd : command)
               			cmdline += cmd + " ";
            		
            		System.out.println("Command line : " + cmdline.trim());
        		}

            	result = ProcessExecutor.execute(command);
            	
            	if (result.getReturnCode() != 0) {
            		errormsg = "Error during the execution of the command line\n" + result.getErrorBuffer();
            		throw new ParameterException(errormsg);
            	}
            	
        		System.out.println(result.getBuffer());
            }
        } catch (ParameterException e) {
            paramError = e;
        }

        if (options.getShowUsage()) {
            commander.setProgramName("java -classpath <path to class file> args");
            commander.usage();
        } else if (paramError != null) {
            System.out.println(MessageFormat.format(messages.getString("cli.error.command.line"),
                    paramError.getMessage()));
            System.out.println(messages.getString("cli.error.help"));
            System.out.println();
        }
    }
}
