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
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

/**
 * This is used for resolving the external references from documents used in XSL transformations.
 *
 * <p>Supports only local file system.</p>.
 */
public class RefURIResolver implements URIResolver {

    /**
     * The URI used for resolving the resources for which no explicit base URI is specified.
     */
    private URI baseURI = null;

    /**
     * Specifies if the file not found errors are reported to user using a Swing dialog.
     */
    private boolean reportErrors = false;

    /**
     * Constructs a new <code>RefURIResolver</code> instance.
     *
     * @param uri    the default base URI
     * @param repErr if <code>true</code> then the file not found errors are reported to user using a dialog
     */
    public RefURIResolver(URI uri, boolean repErr) {
        this.baseURI = uri;
        this.reportErrors = repErr;
    }

    /**
     * Called by the XSLT processor when it encounters an xsl:include, xsl:import, or document() function.
     *
     * @param href an href attribute, which may be relative or absolute
     * @param base the base URI against which the first argument will be made absolute if the absolute URI is required
     *
     * @return a <code>Source</code> object, or null if the href cannot be resolved and the processor should try to
     *         resolve the URI itself
     *
     * @throws TransformerException if an error occurs when trying to resolve the URI
     */
    public Source resolve(String href, String base) throws TransformerException {
        File baseFile = null;

        try {
            baseFile = (base != null) && (base.length() > 0) ? new File(new URI(base)).getParentFile()
                    : new File(this.baseURI);
        } catch (URISyntaxException e) {
            throw new TransformerException(e);
        }

        StreamSource ss = null;
        File resourceFile = new File(baseFile, href);

        try {
            ss = new StreamSource(new FileInputStream(resourceFile));
        } catch (FileNotFoundException e) {
            if (this.reportErrors) {
                this.reportNotFoundError(resourceFile, href);
            }
            throw new TransformerException(e);
        }

        ss.setPublicId(href);
        ss.setSystemId(resourceFile.toURI().toString());

        return ss;
    }

    /**
     * Reports a file not found error.
     *
     * @param resourceFile the <code>File</code> object created for requested resource
     * @param href         the href of requested resource
     *
     * @throws TransformerException if fails
     */
    private void reportNotFoundError(final File resourceFile, final String href) throws TransformerException {
        Runnable runnable = new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null,
                        MessageFormat.format(Globals.getMessages().getString("local.dsd.reference.error"),
                                resourceFile.toString(),
                                href),
                        Globals.getMessages().getString("error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (Exception e) {
                throw new TransformerException(e);
            }
        }
    }
}
