/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.w3c.dom.Document;

/**
 * Results page.
 */
public class ResultsPagePanel extends AbstractWizardPage {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7186256316239816968L;

	/**
     * The field used for SDMX version.
     */
    private JTextField sdmxVersionField = null;

    /**
     * The field used for DSD id.
     */
    private JTextField idField = null;

    /**
     * The field used for agency.
     */
    private JTextField agencyField = null;

    /**
     * The field used for DSD version.
     */
    private JTextField versionField = null;

    /**
     * The save DSD button.
     */
    private JButton saveDSDButton = null;

    /**
     * The combo-box element with supported XSD formats.
     */
    private JComboBox xsdTypeComboBox = null;

    /**
     * The file chooser used for saving the DSDs and XSDs.
     */
    private JFileChooser saveFileChooser = null;
    
    /**
     * The classpath used for the SDMX conversion
     */
    private String classpath = null;

    /**
     * Constructs a new <code>ResultsPagePanel</code> instance.
     */
    public ResultsPagePanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(this.createDsdInfoPanel());
        contentPanel.add(this.createXsdPanel());

        contentPanel.setMaximumSize(contentPanel.getPreferredSize());
        setPageContent(contentPanel, true);

        this.saveFileChooser = new JFileChooser();
    }

    /**
     * Creates the panel with DSD information.
     *
     * @return the panel
     */
    private JPanel createDsdInfoPanel() {
        ResourceBundle messages = Globals.getMessages();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorderWithPadding(messages.getString("results.dsd.title")));

        this.sdmxVersionField = this.createReadOnlyField(20);
        this.idField = this.createReadOnlyField(20);
        this.agencyField = this.createReadOnlyField(20);
        this.versionField = this.createReadOnlyField(20);

        this.addDSDComponent(panel, new JLabel(messages.getString("results.dsd.sdmx.version")), 0, 0, 0.25);
        this.addDSDComponent(panel, this.sdmxVersionField, 1, 0, 0.75);
        this.addDSDComponent(panel, new JLabel(messages.getString("results.dsd.id")), 0, 1, 0.25);
        this.addDSDComponent(panel, this.idField, 1, 1, 0.75);
        this.addDSDComponent(panel, new JLabel(messages.getString("results.dsd.agency")), 0, 2, 0.25);
        this.addDSDComponent(panel, this.agencyField, 1, 2, 0.75);
        this.addDSDComponent(panel, new JLabel(messages.getString("results.dsd.version")), 0, 3, 0.25);
        this.addDSDComponent(panel, this.versionField, 1, 3, 0.75);

        GridBagConstraints c = new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 1, 1, 1), 0, 0);

        this.saveDSDButton = Utils.createButton("button.save.dsd.dots", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveDSDButtonClicked();
            }
        });

        panel.add(this.saveDSDButton, c);
        return panel;
    }

    /**
     * Creates a read-only <code>JTextField</code> instance.
     *
     * @param columns the number of columns
     *
     * @return the instance
     */
    private JTextField createReadOnlyField(int columns) {
        JTextField result = new JTextField(columns);
        result.setEditable(false);
        return result;
    }

    /**
     * Helper for {@link #createDsdInfoPanel} method.
     *
     * @param panel     the panel
     * @param component the component
     * @param x         the column
     * @param y         the row
     * @param weightx   horizontal weight
     */
    private void addDSDComponent(JPanel panel, JComponent component,
            int x, int y, double weightx) {
        GridBagConstraints c = new GridBagConstraints(x, y, 1, 1, weightx, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0);
        panel.add(component, c);
    }

    /**
     * Creates the <em>Xsd</em> options panel.
     *
     * @return the panel
     */
    private JPanel createXsdPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorderWithPadding(Globals.getMessages().getString("results.xsd.title")));

        this.xsdTypeComboBox = new JComboBox();
        panel.add(this.xsdTypeComboBox, BorderLayout.WEST);

        panel.add(Box.createRigidArea(new Dimension(5, 1)), BorderLayout.CENTER);

        panel.add(Utils.createButton("button.generate.dots", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGenerateXSDButtonClicked();
            }
        }), BorderLayout.EAST);

        return panel;
    }

    /**
     * Called when the user clicks the <em>Save DSD</em> button.
     */
    private void onSaveDSDButtonClicked() {
        ResourceBundle messages = Globals.getMessages();

        this.saveFileChooser.resetChoosableFileFilters();
        this.saveFileChooser.setFileFilter(new FileNameExtensionFilter(messages.getString("filechooser.dsd.type"),
                Constants.EXTENSION_XML));

        SdmxHelper helper = SdmxHelper.getInstance(Globals.getDsdInfo().getSdmxVersion());
        this.saveFileChooser.setSelectedFile(new File(this.saveFileChooser.getCurrentDirectory(),
                helper.getDsdRecommendedName(Globals.getDsdInfo())));

        if (this.saveFileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = this.saveFileChooser.getSelectedFile();
        if (StringUtils.trimToNull(FilenameUtils.getExtension(file.getPath())) == null) {
            file = new File(file.getPath() + '.' + Constants.EXTENSION_XML);
        }

        try {
        	Document dsd = Utils.readXML(Globals.getCurrentDSD());
            Utils.writeXML(file, dsd);
        } catch (Exception e) {
            handleError("results.dsd.save.error", e);
        }

        JOptionPane.showMessageDialog(this, messages.getString("results.dsd.save.success"));
    }

    /**
     * Called when the user clicks the <em>Generate XSD</em> button.
     */
    private void onGenerateXSDButtonClicked() {
        ResourceBundle messages = Globals.getMessages();
        String[] command = null;
        String appline = messages.getString("app.name"), errormsg = null;
        StringBuffer bfolder = null;
        ProcessResult result = null;
        boolean found = false, dev = false;

        this.saveFileChooser.resetChoosableFileFilters();
        this.saveFileChooser.setFileFilter(new FileNameExtensionFilter(messages.getString("filechooser.xsd.type"),
                Constants.EXTENSION_XSD));

        SdmxVersion sdmxVersion = Globals.getDsdInfo().getSdmxVersion();
        SdmxHelper helper = SdmxHelper.getInstance(sdmxVersion);

        SdmxHelper.XsdOption option = (SdmxHelper.XsdOption) this.xsdTypeComboBox.getSelectedItem();
        this.saveFileChooser.setSelectedFile(new File(this.saveFileChooser.getCurrentDirectory(),
                helper.getXsdRecommendedName(Globals.getDsdInfo(), option)));

        if (this.saveFileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = this.saveFileChooser.getSelectedFile();
        if (StringUtils.trimToNull(FilenameUtils.getExtension(file.getPath())) == null) {
        	file = new File(file.getPath() + '.' + Constants.EXTENSION_XSD);
        }

        try {
        	String version = 
        			new StringBuffer(sdmxVersion.toString()).replace(1, 2, "_").toString();
       		bfolder = new StringBuffer(
       			messages.getString("jars.sdmx_" + version + ".dependencies"));
    		
    		found = checkJarDependencies(bfolder);
    		if (!found) {
        		bfolder = new StringBuffer(
        			messages.getString("jars.sdmx_" + version + ".dev.dependencies"));
    			
        		found = checkJarDependencies(bfolder);
        		
        		if (!found) {
        			errormsg = "No jar dependencies folder found !!!";
        			throw new Exception(errormsg);
        		}
        		
        		dev = true;
    		}
    		
    		String stroption = option.toString().toUpperCase();
    		System.out.println("Option selected : " + stroption);
    		
    		if (stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_0.option.compact").toUpperCase()) || 
    			stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_1.option.flat").toUpperCase())) {
    			command = new String[8];
    		} else if (stroption.startsWith(messages.getString("results.sdmx_2_1.option.cross").substring(0,3).toUpperCase())) {
    			command = new String[10];
    		} else command = new String[9];

            command[0] = Constants.JAVA_RUNLINE;
            command[1] = "-classpath";
            command[2] = classpath;
            command[3] = appline;
            command[4] = "-i";
            command[5] = Globals.getCurrentDSD().getAbsolutePath();
            command[6] = "-o";
            command[7] = file.getAbsolutePath();
            
    		if (!stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_0.option.compact").toUpperCase()) &&
    			!stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_1.option.flat").toUpperCase())) {
    			if (stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_0.option.cross_sectional").toUpperCase())) {
        			command[8] = "-x"; // SDMX 2.0 Cross Sectional
    			} else if (stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_0.option.utility").toUpperCase())) {
        			command[8] = "-u"; // SDMX 2.0 Utility
    			} else if (stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_1.option.timeseries").toUpperCase())) {
        			command[8] = "-t"; // SDMX 2.1 TimeSeries
    			} else if (stroption.startsWith(messages.getString("results.sdmx_2_1.option.cross").substring(0,3).toUpperCase())) {
        			command[8] = "-s"; // SDMX 2.1 XS
        			command[9] = stroption.substring(3).trim().toUpperCase();
    			} else if (stroption.equalsIgnoreCase(messages.getString("results.sdmx_2_1.option.explicit").toUpperCase())) {
        			command[8] = "-e"; // SDMX 2.1 Explicit
    			}
    		}
            
            if (dev) {
    			String cmdline = "";
    			
        		for (String cmd : command)
           			cmdline += cmd + " ";
        		
                System.out.println("Command line : " + cmdline);
            }

        	result = ProcessExecutor.execute(command);
    	
            if (result.getReturnCode() != 0) {
            	errormsg = result.getErrorBuffer();
            	int enderrormsg = errormsg.indexOf(messages.getString("cli.error.help"));
            	
            	if (enderrormsg > -1) {
                	errormsg = "Error during the execution of the command line\n" + 
                    		errormsg.substring(0, enderrormsg).trim();
            	} else {
                	errormsg = "Error during the execution of the command line\n" + 
                    		errormsg.trim();
            	}
            	throw new Exception(errormsg);
            }
    	
            System.out.println(result.getBuffer());
        } catch (Exception e) {
            handleError("results.xsd.generate.error", e);
            return;
        }

        JOptionPane.showMessageDialog(this, messages.getString("results.xsd.save.success"));
    }

    /**
     * Called whenever the page is made visible.
     *
     * @param forceUpdate if <code>true</code> then the page should update all its components
     */
    public void onPageShow(boolean forceUpdate) {
        if (!forceUpdate) {
            return;
        }

        try {
        	Document dsd = Utils.readXML(Globals.getCurrentDSD());
            this.xsdTypeComboBox.setModel(new DefaultComboBoxModel(
                    SdmxHelper.getInstance(Globals.getDsdInfo().getSdmxVersion()).getXsdGenOptions(dsd
                            ).toArray()));
        } catch (Exception e) {
            throw new XsdGeneratorException(e);
        }

        DsdInfo info = Globals.getDsdInfo();

        this.idField.setText(info.getId());
        this.agencyField.setText(info.getAgency());
        this.versionField.setText(info.getVersion());
        this.sdmxVersionField.setText(info.getSdmxVersion().toString());

        this.saveDSDButton.setVisible(!Globals.isLocalDsd());

        getPageContentPanel().setMaximumSize(getPageContentPanel().getPreferredSize());
    }
    
    /**
     * Check the libraries folder for the jar dependencies
     * 
     * @param bfolder     The folders to search if there are some jar depend
     * @return <code>true</code> if the jar dependencies are found, <code>false</code> otherwise
     */
    public boolean checkJarDependencies(StringBuffer bfolder) {
        File[] listFiles = null;
        String initfolder = "{0}", fsep = "{1}", semicolon = ";", checkfolder = null;
        int i = -1, j = 0;
    	boolean found = false;
    	
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
		
        classpath = "\"";

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
				classpath += checkfolder + ";";
			} else {
    			for (j = 0; j < listFiles.length; j++) {
    				found = true;
    				classpath += listFiles[j].getAbsolutePath() + ";";
    			}
			}
			
			j = i + 1;
    		i = bfolder.indexOf(semicolon, j);
		}

		classpath = classpath.substring(0, classpath.length()-1) + "\"";
        
		return found;
    }

    /**
     * Specifies if for this wizard page a <em>Previous</em> button should be present.
     *
     * @return <code>true</code> if the <em>Previous</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHavePreviousButton() {
        return true;
    }

    /**
     * Specifies if for this wizard page a <em>Restart</em> button should be present.
     *
     * @return <code>true</code> if the <em>Restart</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveRestartButton() {
        return true;
    }

    /**
     * Getter for the expected type of previous page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedPreviousPageType() {
        return Globals.isLocalDsd() ? LocalDSDPagePanel.class : RegistryDSDPagePanel.class;
    }

    /**
     * Getter for the expected type of next page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedNextPageType() {
        return StartPagePanel.class;
    }
}
