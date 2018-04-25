/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * SDMX Registry configuration page.
 */
public class RegistryConfigPagePanel extends AbstractWizardPage {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The combo-box with configuration names.
     */
    private JComboBox namesComboBox = null;

    /**
     * The button used for deleting a configuration.
     */
    private JButton deleteButton = null;

    /**
     * The text field for domain.
     */
    private JTextField domainField = null;

    /**
     * The text field for username.
     */
    private JTextField usernameField = null;

    /**
     * The text field for password.
     */
    private JTextField passwordField = null;

    /**
     * The text field for URL.
     */
    private JTextField urlField = null;

    /**
     * The text field for proxy host.
     */
    private JTextField proxyHostField = null;

    /**
     * The text field for proxy port.
     */
    private JTextField proxyPortField = null;

    /**
     * The text field for proxy username.
     */
    private JTextField proxyUsernameField = null;

    /**
     * The text field for proxy password.
     */
    private JTextField proxyPasswordField = null;

    /**
     * Constructs a new <code>RegistryConfigPagePanel</code>.
     */
    public RegistryConfigPagePanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.setBorder(createTitledBorderWithPadding(Globals.getMessages().getString("registry.config.title")));

        JPanel operationsPanel = this.createOperationsPanel();
        operationsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        contentPanel.add(operationsPanel);

        contentPanel.add(Box.createVerticalStrut(5));

        this.createFields();
        contentPanel.add(this.createFieldsPanel());

        contentPanel.setMaximumSize(contentPanel.getPreferredSize());
        setPageContent(contentPanel, true);
    }

    /**
     * Creates the operation elements panel.
     *
     * @return the panel
     */
    private JPanel createOperationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(this.createNamesComboBox());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(Utils.createButton("button.save", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveButtonClicked();
            }
        }));
        panel.add(Box.createHorizontalStrut(5));
        this.deleteButton = Utils.createButton("button.delete", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDeleteButtonClicked();
            }
        });
        panel.add(this.deleteButton);

        return panel;
    }

    /**
     * Creates the combo-box element with configuration names.
     *
     * @return the combo-box element
     */
    private JComboBox createNamesComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");
        for (RegistryConfig cfg : Configuration.getInstance().getRegistryConfigs()) {
            model.addElement(cfg.getName());
        }
        this.namesComboBox = new JComboBox(model);
        this.namesComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCurrentConfigChanged();
            }
        });
        return this.namesComboBox;
    }

    /**
     * Creates the text fields used for editing a configuration.
     */
    private void createFields() {
        this.domainField = new JTextField(25);
        this.usernameField = new JTextField(25);
        this.passwordField = new JPasswordField(25);
        this.urlField = new JTextField(30);
        this.proxyHostField = new JTextField(20);
        this.proxyPortField = new JTextField(5);
        this.proxyUsernameField = new JTextField(25);
        this.proxyPasswordField = new JPasswordField(25);
    }

    /**
     * Creates the fields panel.
     *
     * @return the panel
     */
    private JPanel createFieldsPanel() {
        ResourceBundle messages = Globals.getMessages();

        JPanel panel = new JPanel(new GridBagLayout());

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.domain")), 0, 0, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.domainField, 1, 0, 0.75,
                GridBagConstraints.NONE);

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.username")), 0, 1, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.usernameField, 1, 1, 0.75,
                GridBagConstraints.NONE);

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.password")), 0, 2, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.passwordField, 1, 2, 0.75,
                GridBagConstraints.NONE);

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.url")), 0, 3, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.urlField, 1, 3, 0.75,
                GridBagConstraints.BOTH);

        JPanel proxyPanel = new JPanel();
        proxyPanel.setLayout(new BoxLayout(proxyPanel, BoxLayout.X_AXIS));
        proxyPanel.add(this.proxyHostField);
        proxyPanel.add(Box.createHorizontalStrut(5));
        proxyPanel.add(this.proxyPortField);
        proxyPanel.add(Box.createHorizontalGlue());

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.proxy")), 0, 4, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, proxyPanel, 1, 4, 0.75,
                GridBagConstraints.NONE);

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.proxy.username")), 0, 5, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.proxyUsernameField, 1, 5, 0.75,
                GridBagConstraints.NONE);

        this.addFieldsComponent(panel,
                new JLabel(messages.getString("registry.config.proxy.password")), 0, 6, 0.25,
                GridBagConstraints.NONE);
        this.addFieldsComponent(panel, this.proxyPasswordField, 1, 6, 0.75,
                GridBagConstraints.NONE);

        return panel;
    }

    /**
     * Helper for {@link #createFieldsPanel} method.
     *
     * @param panel     the panel
     * @param component the component
     * @param x         the column
     * @param y         the row
     * @param weightx   horizontal weight
     * @param fill      fill attribute
     */
    private void addFieldsComponent(JPanel panel, JComponent component,
            int x, int y, double weightx, int fill) {
        GridBagConstraints c = new GridBagConstraints(x, y, 1, 1, weightx, 1.0, GridBagConstraints.WEST,
                fill, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(component, c);
    }

    /**
     * Called when the user changes the current SDMX Registry configuration.
     */
    private void onCurrentConfigChanged() {
        String name = "" + this.namesComboBox.getModel().getSelectedItem();
        RegistryConfig cfg = name.length() > 0 ? Configuration.getInstance().findRegistryConfig(name)
                : new RegistryConfig();

        this.domainField.setText(StringUtils.trimToEmpty(cfg.getDomain()));
        this.usernameField.setText(StringUtils.trimToEmpty(cfg.getUsername()));
        this.passwordField.setText(StringUtils.trimToEmpty(cfg.getPassword()));
        this.urlField.setText(cfg.getURL() == null ? "" : "" + cfg.getURL());

        this.proxyHostField.setText(StringUtils.trimToEmpty(cfg.getProxyHost()));
        this.proxyPortField.setText("" + cfg.getProxyPort());
        this.proxyUsernameField.setText(StringUtils.trimToEmpty(cfg.getProxyUsername()));
        this.proxyPasswordField.setText(StringUtils.trimToEmpty(cfg.getProxyPassword()));

        this.deleteButton.setEnabled(name.length() > 0);
    }

    /**
     * Called when the user clicks the <em>Save</em> button.
     */
    private void onSaveButtonClicked() {
        Object result = createRegistryConfig();
        ResourceBundle messages = Globals.getMessages();

        if (result instanceof String) {
            JOptionPane.showMessageDialog(this, messages.getString((String) result), messages.getString("error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        RegistryConfig cfg = (RegistryConfig) result;
        String name = (String) this.namesComboBox.getSelectedItem();
        boolean isNew = name.isEmpty();

        if (isNew) {
            do {
                name = JOptionPane.showInputDialog(this, messages.getString("registry.config.save.label"));

                if (name == null) {
                    return;
                }

                name = StringUtils.trimToNull(name);
                String errorKey = null;

                if (name != null) {
                    if (Configuration.getInstance().findRegistryConfig(name) != null) {
                        errorKey = "registry.config.save.error.duplicate.name";
                        name = null;
                    }
                } else {
                    errorKey = "registry.config.save.error.empty.name";
                }

                if (errorKey != null) {
                    JOptionPane.showMessageDialog(this, messages.getString(errorKey), messages.getString("error"),
                            JOptionPane.ERROR_MESSAGE);
                }
            } while (name == null);
        }

        cfg.setName(name);

        Configuration.getInstance().saveRegistryConfig((RegistryConfig) result);
        JOptionPane.showMessageDialog(this,
                messages.getString("registry.config.save.success"));

        if (isNew) {
            ((DefaultComboBoxModel) this.namesComboBox.getModel()).addElement(name);
            this.namesComboBox.setSelectedItem(name);
        }
    }

    /**
     * Creates a registry configuration from data present into text fields.
     *
     * @return a <code>RegistryConfig</code> instance if succeeds, the key of the error if fails
     */
    private Object createRegistryConfig() {
        RegistryConfig cfg = new RegistryConfig();

        cfg.setDomain(StringUtils.trimToNull(this.domainField.getText()));
        if (cfg.getDomain() == null) {
            return "registry.config.invalid.domain";
        }

        cfg.setUsername(StringUtils.trimToNull(this.usernameField.getText()));
        if (cfg.getUsername() == null) {
            return "registry.config.invalid.username";
        }

        cfg.setPassword(StringUtils.trimToNull(this.passwordField.getText()));
        if (cfg.getPassword() == null) {
            return "registry.config.invalid.password";
        }

        try {
            cfg.setURL(new URL(StringUtils.trimToEmpty(this.urlField.getText())));
        } catch (MalformedURLException e) {
            return "registry.config.invalid.url";
        }

        cfg.setProxyHost(StringUtils.trimToNull(this.proxyHostField.getText()));

        try {
            cfg.setProxyPort(Integer.parseInt(this.proxyPortField.getText()));
        } catch (IllegalArgumentException e) {
            return "registry.config.invalid.proxy.port";
        }

        cfg.setProxyUsername(StringUtils.trimToNull(this.proxyUsernameField.getText()));
        cfg.setProxyPassword(
                StringUtils.trimToNull(this.proxyPasswordField.getText()));

        return cfg;
    }

    /**
     * Called when the user clicks the <em>Delete</em> button.
     */
    private void onDeleteButtonClicked() {
        String name = (String) this.namesComboBox.getSelectedItem();
        ResourceBundle messages = Globals.getMessages();

        if (name.length() > 0) {
            if (JOptionPane.showConfirmDialog(this,
                    MessageFormat.format(messages.getString("registry.config.delete.confirm"), name),
                    null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Configuration.getInstance().deleteRegistryConfig(name);
                ((DefaultComboBoxModel) this.namesComboBox.getModel()).removeElement(name);

                JOptionPane.showMessageDialog(this, MessageFormat.format(
                        messages.getString("registry.config.delete.success"), name));
            }
        }
    }

    /**
     * Called whenever the page is made visible.
     *
     * @param forceUpdate if <code>true</code> then the page should update all its components
     */
    public void onPageShow(boolean forceUpdate) {
        Globals.setCurrentRegistryConfig(null);
        this.onCurrentConfigChanged();
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
            Object result = this.createRegistryConfig();

            if (result instanceof String) {
                ResourceBundle messages = Globals.getMessages();
                JOptionPane.showMessageDialog(this, messages.getString((String) result), messages.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Globals.setCurrentRegistryConfig((RegistryConfig) result);
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
        return RegistryDSDPagePanel.class;
    }
}
