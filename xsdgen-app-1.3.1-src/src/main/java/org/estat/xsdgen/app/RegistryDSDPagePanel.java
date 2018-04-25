/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import javax.swing.*;

import org.w3c.dom.Document;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Registry DSD page.
 */
public class RegistryDSDPagePanel extends AbstractWizardPage {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6948313834321448431L;

	/**
     * The SDMX Registry client.
     */
    private RegistryClient registryClient = new RegistryClient();

    /**
     * The list with available key families, retrieved from SDMX Registry.
     */
    private List<DsdInfo> keyFamilies = null;

    /**
     * The combo-box with available key families.
     */
    private JComboBox kfComboBox = null;

    /**
     * Constructs a new <code>RegistryDSDPagePanel</code> instance.
     */
    public RegistryDSDPagePanel() {
        this.kfComboBox = new JComboBox();
    }

    /**
     * Setups panel's content when the list of available key families is successfully retrieved from SDMX Registry.
     */
    private void setupStandardContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.setBorder(createTitledBorderWithPadding(
                Globals.getMessages().getString("registry.select.keyfamily.title")));

        contentPanel.add(this.kfComboBox);

        DefaultComboBoxModel model = new DefaultComboBoxModel(this.keyFamilies.toArray());
        this.kfComboBox.setModel(model);

        contentPanel.setMaximumSize(contentPanel.getPreferredSize());
        setPageContent(contentPanel, true);
    }

    /**
     * Setups panel's content when an error is encountered.
     */
    private void setupErrorContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        String errorKey = this.keyFamilies == null ? "registry.query.keyfamilies.error"
                : "registry.query.keyfamlies.empty";
        contentPanel.add(new JLabel(Utils.textToHTML(Globals.getMessages().getString(errorKey), false, false),
                JLabel.CENTER));
        setPageContent(contentPanel, true);
    }

    /**
     * Called whenever the page is made visible.
     *
     * @param forceUpdate if <code>true</code> then the page should update all its components
     */
    public void onPageShow(boolean forceUpdate) {
        Globals.setCurrentDSD(null, null, false);

        if (forceUpdate) {
            removeAll();
            this.keyFamilies = null;

            try {
                this.keyFamilies = this.registryClient.getAvailableKeyFamilies();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((this.keyFamilies != null) && !this.keyFamilies.isEmpty()) {
                this.setupStandardContent();
            } else {
                this.setupErrorContent();
            }
        }
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
            if ((this.keyFamilies != null) && !this.keyFamilies.isEmpty()) {
                try {
                    DsdInfo kf = (DsdInfo) this.kfComboBox.getSelectedItem();
                    Document dsd = this.registryClient.getKeyFamily(kf);
                    File file = File.createTempFile(UUID.randomUUID().toString(), "xml");
                    Utils.writeXML(file, dsd);
                    Globals.setCurrentDSD(file, kf, false);
                } catch (Exception e) {
                    ResourceBundle messages = Globals.getMessages();
                    JOptionPane.showMessageDialog(this, messages.getString("registry.query.dsd.error"),
                            messages.getString("error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
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
        return RegistryConfigPagePanel.class;
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
