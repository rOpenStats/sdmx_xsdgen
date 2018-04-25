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
import java.util.ResourceBundle;

/**
 * Implementation of the page which allows the user to select the location of the DSD.
 */
public class SelectDSDLocationPagePanel extends AbstractWizardPage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The radio button for file system.
     */
    private JRadioButton fileSystemButton = null;

    /**
     * Constructs a new <code>SelectDSDLocationPagePanel</code> instance.
     */
    public SelectDSDLocationPagePanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        ResourceBundle messages = Globals.getMessages();
        contentPanel.setBorder(createTitledBorderWithPadding(messages.getString("dsd.location.title")));

        this.fileSystemButton = new JRadioButton(messages.getString("dsd.location.filesystem"), true);
        JRadioButton registryButton = new JRadioButton(messages.getString("dsd.location.registry"));

        ButtonGroup group = new ButtonGroup();
        group.add(this.fileSystemButton);
        group.add(registryButton);

        contentPanel.add(this.fileSystemButton);
        contentPanel.add(registryButton);

        setPageContent(contentPanel, true);
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
    @Override
    public boolean shouldHaveNextButton() {
        return true;
    }

    /**
     * Getter for the expected type of previous page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    @Override
    public Class<?> getExpectedPreviousPageType() {
        return StartPagePanel.class;
    }

    /**
     * Getter for the expected type of next page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    @Override
    public Class<?> getExpectedNextPageType() {
        return isFileSystemSelected() ? LocalDSDPagePanel.class : RegistryConfigPagePanel.class;
    }

    /**
     * @return <code>true</code> if if the local file system is selected as source for DSD
     */
    public boolean isFileSystemSelected() {
        return this.fileSystemButton.isSelected();
    }
}
