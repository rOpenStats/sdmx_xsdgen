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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Application's main frame.
 */
public class MainFrame extends JFrame {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6149506199480801445L;

	/**
     * The pages panel.
     *
     * <p>Note that this uses a <code>CardLayout</code> instance as a layout manager.</p>
     */
    private JPanel pagesPanel = null;

    /**
     * The "start" navigation button, being visible only on first page.
     */
    private JButton startButton = null;

    /**
     * The "previous" navigation button, being visible on all pages, except the first one.
     */
    private JButton prevButton = null;

    /**
     * The "next" navigation button, being visible on all pages, except the first and last one.
     */
    private JButton nextButton = null;

    /**
     * The "restart" navigation button, being visible only on last page.
     */
    private JButton restartButton = null;

    /**
     * Constructs a new <code>MainFrame</code> instance.
     */
    public MainFrame() {
        super(Globals.getMessages().getString("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(Constants.MAIN_FRAME_SIZE);
        setPreferredSize(Constants.MAIN_FRAME_SIZE);

        setJMenuBar(this.createMenuBar());
        this.setupFrameContent();

        this.changeCurrentPage(StartPagePanel.class, true);
    }

    /**
     * Creates frame's menu bar.
     *
     * @return the menu bar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(this.createFileMenu());
        menuBar.add(this.createHelpMenu());

        return menuBar;
    }

    /**
     * Creates the <em>File</em> menu.
     *
     * @return the menu
     */
    private JMenu createFileMenu() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.MENU_BUNDLE_BASE_NAME);
        JMenu menu = new JMenu(bundle.getString("menu.file"));

        JMenuItem exitItem = new JMenuItem(bundle.getString("menu.file.exit"));
        menu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.dispose();
            }
        });

        return menu;
    }

    /**
     * creates the <em>Help</em> menu.
     *
     * @return the menu
     */
    private JMenu createHelpMenu() {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.MENU_BUNDLE_BASE_NAME);
        JMenu menu = new JMenu(bundle.getString("menu.help"));

        JMenuItem aboutItem = new JMenuItem(bundle.getString("menu.help.about"));
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResourceBundle messages = Globals.getMessages();
                String message = MessageFormat.format("{0} - v{1}\n{2}",
                        messages.getString("app.title"), messages.getString("app.version"),
                        messages.getString("app.copyright"));
                JOptionPane.showMessageDialog(MainFrame.this, message, messages.getString("app.title"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(aboutItem);

        return menu;
    }

    /**
     * Setups frame's content.
     *
     * <p>This creates the main elements (navigation buttons, wizard's pages), setups the layout etc...</p>
     */
    private void setupFrameContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.pagesPanel = this.createPagesPanel();
        this.createNavigationButtons();
        JPanel navigationButtonsPanel = this.createNavigationButtonsPanel();

        contentPanel.add(this.pagesPanel, BorderLayout.CENTER);
        contentPanel.add(navigationButtonsPanel, BorderLayout.SOUTH);

        add(contentPanel);
    }

    /**
     * Creates the pages panel.
     *
     * @return a <code>JPanel</code> instance which contains all pages
     */
    private JPanel createPagesPanel() {
        JPanel panel = new JPanel();

        CardLayout layout = new CardLayout();
        panel.setLayout(layout);

        panel.add(new StartPagePanel(), StartPagePanel.class.getName());
        panel.add(new SelectDSDLocationPagePanel(), SelectDSDLocationPagePanel.class.getName());
        panel.add(new LocalDSDPagePanel(), LocalDSDPagePanel.class.getName());
        panel.add(new RegistryConfigPagePanel(), RegistryConfigPagePanel.class.getName());
        panel.add(new RegistryDSDPagePanel(), RegistryDSDPagePanel.class.getName());
        panel.add(new ResultsPagePanel(), ResultsPagePanel.class.getName());

        return panel;
    }

    /**
     * Creates the standard navigation buttons.
     */
    private void createNavigationButtons() {
        this.startButton = Utils.createButton("button.start", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onStartButtonClicked();
            }
        });

        this.restartButton = Utils.createButton("button.restart", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRestartButtonClicked();
            }
        });

        this.prevButton = Utils.createButton("button.previous", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPrevButtonClicked();
            }
        });

        this.nextButton = Utils.createButton("button.next", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNextButtonClicked();
            }
        });
    }

    /**
     * Creates the panel with wizard navigation buttons.
     *
     * @return the navigation buttons panel
     */
    protected JPanel createNavigationButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());

        JButton[] navButtons = new JButton[] {this.startButton, this.prevButton, this.nextButton, this.restartButton};

        for (JButton button : navButtons) {
            panel.add(panel.getComponentCount() > 0 ? Box.createHorizontalStrut(3) : Box.createHorizontalGlue());
            button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            panel.add(button);
        }

        panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
        return panel;
    }

    /**
     * Setups the visibility of navigation buttons.
     */
    private void setupNavigationButtonsVisibility() {
        Map<JButton, Boolean> visibility = new HashMap<JButton, Boolean>();
        AbstractWizardPage page = this.getCurrentPage();

        visibility.put(this.startButton, page.shouldHaveStartButton());
        visibility.put(this.prevButton, page.shouldHavePreviousButton());
        visibility.put(this.nextButton, page.shouldHaveNextButton());
        visibility.put(this.restartButton, page.shouldHaveRestartButton());

        //TODO: to setup the visibility for fillers too, or to change the layout used by navigation buttons panel
        for (JButton button : visibility.keySet()) {
            button.setVisible(visibility.get(button));
        }
    }

    /**
     * Getter for a page.
     *
     * @param type the type of the page to retrieve
     *
     * @return the <code>AbstractWizardPage</code> instance for specified type
     */
    private AbstractWizardPage getPage(Class<?> type) {
        if (AbstractWizardPage.class.isAssignableFrom(type)) {
            for (Component c : this.pagesPanel.getComponents()) {
                if (c.getClass().equals(type)) {
                    return (AbstractWizardPage) c;
                }
            }
        }

        throw new IllegalArgumentException("Unable to find page with type: " + type);
    }

    /**
     * Getter for current page.
     *
     * @return the active/current page
     */
    private AbstractWizardPage getCurrentPage() {
        for (Component c : this.pagesPanel.getComponents()) {
            if ((c instanceof AbstractWizardPage) && c.isVisible()) {
                return (AbstractWizardPage) c;
            }
        }

        throw new IllegalStateException("No active page!");
    }

    /**
     * Sets the current page.
     *
     * @param type the type of the page to be set as current one
     * @param forceUpdate specifies if the new page should update all its components
     */
    private void changeCurrentPage(Class<?> type, boolean forceUpdate) {
        AbstractWizardPage nextPage = this.getPage(type);
        ((CardLayout) this.pagesPanel.getLayout()).show(this.pagesPanel, nextPage.getClass().getName());
        this.setupNavigationButtonsVisibility();
        nextPage.onPageShow(forceUpdate);
    }

    /**
     * Called when the user clicks the <em>Start</em> navigation button.
     */
    private void onStartButtonClicked() {
        this.onNextButtonClicked();
    }

    /**
     * Called when the user clicks the <em>Previous</em> navigation button.
     */
    private void onPrevButtonClicked() {
        AbstractWizardPage currentPage = this.getCurrentPage();
        Class<?> prevType = currentPage.getExpectedPreviousPageType();

        if (prevType != null) {
            currentPage.onPageHide(false);
            this.changeCurrentPage(prevType, false);
        }
    }

    /**
     * Called when the user clicks the <em>Next</em> navigation button.
     */
    private void onNextButtonClicked() {
        AbstractWizardPage currentPage = this.getCurrentPage();

        if (currentPage.onPageHide(true)) {
            Class<?> nextType = currentPage.getExpectedNextPageType();

            if (nextType != null) {
                this.changeCurrentPage(nextType, true);
            }
        }
    }

    /**
     * Called when the user clicks the <em>Restart</em> navigation button.
     */
    private void onRestartButtonClicked() {
        AbstractWizardPage currentPage = this.getCurrentPage();
        if (currentPage.onPageHide(true)) {
            this.changeCurrentPage(StartPagePanel.class,
                    true);
        }
    }
}
