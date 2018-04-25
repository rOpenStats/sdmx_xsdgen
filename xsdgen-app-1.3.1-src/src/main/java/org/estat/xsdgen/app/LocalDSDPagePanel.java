/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Local DSD page.
 */
public class LocalDSDPagePanel extends AbstractWizardPage {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1078186651843341340L;

	/**
     * The text field which contains the path to currently selected DSD file.
     */
    private JTextField dsdFileField = null;

    /**
     * The file chooser to be used for selecting the DSD file.
     */
    private JFileChooser fileChooser = null;

    /**
     * Constructs a new <code>LocalDSDPagePanel</code> instance.
     */
    public LocalDSDPagePanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        ResourceBundle messages = Globals.getMessages();
        contentPanel.setBorder(createTitledBorderWithPadding(messages.getString("local.dsd.title")));

        this.dsdFileField = new JTextField(50);
        this.dsdFileField.setEditable(false);
        this.dsdFileField.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.dsdFileField.setMaximumSize(this.dsdFileField.getPreferredSize());
        contentPanel.add(this.dsdFileField);

        contentPanel.add(Box.createRigidArea(new Dimension(1, 5)));

        JButton browseButton = Utils.createButton("button.browse.dots", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowseButtonClicked();
            }
        });
        browseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(browseButton);

        setPageContent(contentPanel, true);

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(messages.getString("filechooser.dsd.type"),
                Constants.EXTENSION_XML));

        Configuration cfg = Configuration.getInstance();

        if (cfg.getLastDSDPath() != null) {
            this.fileChooser.setCurrentDirectory(cfg.getLastDSDPath());
        }
    }

    /**
     * Called when the user clicks the <em>Browse</em> button.
     */
    private void onBrowseButtonClicked() {
        if (this.fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = this.fileChooser.getSelectedFile();

            this.dsdFileField.setText(selectedFile.getPath());
            Configuration.getInstance().setLastDSDPath(selectedFile.getParentFile());
        }
    }

    /**
     * Called whenever the page is made visible.
     *
     * @param forceUpdate if <code>true</code> then the page should update all its components
     */
    public void onPageShow(boolean forceUpdate) {
        Globals.setCurrentDSD(null, null, false);
    }

    /**
     * Called before the page will be made invisible.
     *
     * @param validate if <code>true</code> then page's data must be validated (this being used when the user clicks the
     *                 <em>Next</em> button)
     *
     * @return <code>true</code> to allow page change, <code>false</code> otherwise
     */
    public boolean onPageHide(boolean validate) {
        if (validate) {
            ResourceBundle messages = Globals.getMessages();
            File selectedFile = this.fileChooser.getSelectedFile();

            if ((selectedFile == null) || !selectedFile.isFile()) {
                JOptionPane.showMessageDialog(this, messages.getString("local.dsd.empty"), messages.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Document dsd = Utils.readXML(selectedFile);
                SdmxVersion sdmxVersion = SdmxHelper.getSdmxVersion(dsd);
                if (sdmxVersion == null) {
                    throw new XsdGeneratorException("Unsupported SDMX version");
                }
                SdmxHelper helper = SdmxHelper.getInstance(sdmxVersion);
                DsdInfo dsdInfo = helper.getDsdInfo(dsd);
                Globals.setCurrentDSD(selectedFile, dsdInfo, true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        messages.getString("local.dsd.load.error"), messages.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
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
     * Specifies if for this wizard page a <em>Next</em> button should be present.
     *
     * @return <code>true</code> if the <em>Next</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveNextButton() {
        return true;
    }

    /**
     * Getter for the expected type of previous page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedPreviousPageType() {
        return SelectDSDLocationPagePanel.class;
    }

    /**
     * Getter for the expected type of next page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedNextPageType() {
        return ResultsPagePanel.class;
    }
}
