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
import javax.swing.border.EmptyBorder;
import java.util.ResourceBundle;

/**
 * Start page.
 */
public class StartPagePanel extends AbstractWizardPage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new <code>StartPagePanel</code> instance.
     */
    public StartPagePanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        ResourceBundle messages = Globals.getMessages();
        String[] keys = new String[] {"start.text.1", "start.text.2", "start.text.3"};

        for (int i = 0; i < keys.length; i++) {
            JLabel label = new JLabel(Utils.textToHTML(messages.getString(keys[i]), true, true));
            label.setBorder(new EmptyBorder(3, 3, 3, 3));
            contentPanel.add(label);
        }

        setPageContent(contentPanel, false);
    }

    /**
     * Specifies if for this wizard page a <em>Start</em> button should be present.
     *
     * @return <code>true</code> if the <em>Start</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveStartButton() {
        return true;
    }

    /**
     * Getter for the expected type of next page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedNextPageType() {
        return SelectDSDLocationPagePanel.class;
    }
}
