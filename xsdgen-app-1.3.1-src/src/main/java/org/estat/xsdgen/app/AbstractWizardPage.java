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
import javax.swing.border.Border;
import java.awt.*;

/**
 * Base class for all wizard page, implemented as <code>JPanel</code> instances.
 *
 * <p>Note that even if theoretically <code>AbstractWizardPage</code> instances should handle only the local business
 * logic, they also provide some metadata information for parent container/controller, to know which is should be the
 * previous or next page, which navigation buttons should be enabled etc...</p>
 */
public abstract class AbstractWizardPage extends JPanel {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4075060227527540924L;

	/**
     * The panel set with {@link #setPageContent} helper method. This is <code>null</code> if the wizard page doesn't
     * use the helper method...
     */
    protected JPanel pageContentPanel = null;

    /**
     * Constructs a new <code>AbstractWizardPage</code> instance.
     */
    public AbstractWizardPage() {
    }

    /**
     * Helper method for derived classes, which should use this for setting page's content.
     *
     * @param contentPanel the panel with page's elements
     * @param center       if <code>true</code> then the elements will be centered inside page, both horizontally and
     *                     vertically; note that this does not work as expected if the <code>contentPanel</code> has a
     *                     layout manager which uses as much space as available, except if a correct maximum size is set
     *                     (<code>contentPanel.setMaximumSize(contentPanel.getPreferredSize())</code> sometimes helps)
     */
    protected void setPageContent(JPanel contentPanel, boolean center) {
        this.pageContentPanel = contentPanel;

        setLayout(new BorderLayout());

        if (center) {
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
            wrapperPanel.add(Box.createVerticalGlue());
            contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            wrapperPanel.add(contentPanel);
            wrapperPanel.add(Box.createVerticalGlue());

            contentPanel = wrapperPanel;
        }

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Getter for {@link #pageContentPanel} property.
     *
     * @return the value
     */
    protected JPanel getPageContentPanel() {
        return this.pageContentPanel;
    }

    /**
     * Specifies if for this wizard page a <em>Start</em> button should be present.
     *
     * @return <code>true</code> if the <em>Start</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveStartButton() {
        return false;
    }

    /**
     * Specifies if for this wizard page a <em>Previous</em> button should be present.
     *
     * @return <code>true</code> if the <em>Previous</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHavePreviousButton() {
        return false;
    }

    /**
     * Specifies if for this wizard page a <em>Next</em> button should be present.
     *
     * @return <code>true</code> if the <em>Next</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveNextButton() {
        return false;
    }

    /**
     * Specifies if for this wizard page a <em>Restart</em> button should be present.
     *
     * @return <code>true</code> if the <em>Restart</em> button should be visible, <code>false</code> otherwise
     */
    public boolean shouldHaveRestartButton() {
        return false;
    }

    /**
     * Getter for the expected type of previous page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedPreviousPageType() {
        return null;
    }

    /**
     * Getter for the expected type of next page.
     *
     * @return the type as a <code>Class</code>, <code>null</code> if there's none
     */
    public Class<?> getExpectedNextPageType() {
        return null;
    }

    /**
     * Called whenever the page is made visible.
     *
     * @param forceUpdate if <code>true</code> then the page should update all its components
     */
    public void onPageShow(boolean forceUpdate) {
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
        return true;
    }

    /**
     * Helper method for derived classes, which creates a title border with padding.
     *
     * @param title the title
     *
     * @return the border
     */
    protected Border createTitledBorderWithPadding(String title) {
        return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Called whenever an error occurs.
     *
     * @param errorMsgKey the key of the message to be displayed to user
     * @param e           the exception associated with the error
     */
    protected void handleError(String errorMsgKey, Exception e) {
        String errortag = "{0}";
        StringBuffer berrormsg = new StringBuffer(Globals.getMessages().getString(errorMsgKey));
        int idxerrortag = berrormsg.indexOf(errortag);
        
        if (idxerrortag > -1) {
            berrormsg = berrormsg.replace(idxerrortag, idxerrortag + errortag.length(),
            		"\n" + e.getMessage());
        }
        JOptionPane.showMessageDialog(this, berrormsg.toString(), 
        		Globals.getMessages().getString("error"), JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
