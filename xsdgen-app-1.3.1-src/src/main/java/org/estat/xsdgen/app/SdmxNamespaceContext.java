/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.estat.xsdgen.sdmx20.XsdGeneratorException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A namespace context implementation which supports standard SDMX namespaces.
 */
public class SdmxNamespaceContext implements NamespaceContext {

    /**
     * The supported namespaces, keys being prefixes, while values being namespace URIs.
     */
    private Properties namespaces = new Properties();

    /**
     * Constructs a new <code>SdmxNamespaceContext</code> instance.
     *
     * @param nsFile the properties file from where to load the namespaces and their standard prefixes
     */
    public SdmxNamespaceContext(String nsFile) {
        InputStream is = getClass().getResourceAsStream(nsFile);

        try {
            this.namespaces.load(is);
        } catch (IOException e) {
            throw new XsdGeneratorException("Failed to read the standard SDMX namespaces from "
                    + nsFile + " configuration file", e);
        } finally {
            IOUtils.closeQuietly(is);
        }

        // The convention is to use the "xmlns" key for default namespace...
        String defaultNamespace = this.namespaces.getProperty(XMLConstants.XMLNS_ATTRIBUTE);

        if (defaultNamespace != null) {
            this.namespaces.put(XMLConstants.DEFAULT_NS_PREFIX, defaultNamespace);
        } else {
            throw new XsdGeneratorException("Default namespace missing");
        }

        // Adds XML standard namespaces.

        this.namespaces.setProperty(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        this.namespaces.setProperty(XMLConstants.XMLNS_ATTRIBUTE,
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    }

    /**
     * Getter for the namespace URI bound to a prefix.
     *
     * @param prefix the prefix
     *
     * @return the URI
     */
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null");
        }

        String value = this.namespaces.getProperty(prefix);
        return value != null ? value : XMLConstants.NULL_NS_URI;
    }

    /**
     * Getter for the prefix bound to a namespace URI.
     *
     * @param uri the namespace URI for which to get the prefix
     *
     * @return the prefix
     */
    public String getPrefix(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("The namespace URI cannot be empty");
        }

        if (uri.equals(this.namespaces.get(XMLConstants.DEFAULT_NS_PREFIX))) {
            return XMLConstants.DEFAULT_NS_PREFIX;
        }

        Enumeration<?> keys = this.namespaces.propertyNames();

        while (keys.hasMoreElements()) {
            String key = "" + keys.nextElement();
            if (StringUtils.equals(this.namespaces.getProperty(key), uri)) {
                return key;
            }
        }

        return null;
    }

    /**
     * Getter for all prefixes bound to a namespace URI.
     *
     * @param uri the namespace URI for which to get the prefixes
     *
     * @return a iterator
     */
    public Iterator<?> getPrefixes(String uri) {
        List<String> prefixes = new ArrayList<String>();
        Enumeration<?> keys = this.namespaces.propertyNames();

        while (keys.hasMoreElements()) {
            String key = "" + keys.nextElement();
            if (StringUtils.equals(this.namespaces.getProperty(key), uri)) {
                prefixes.add(key);
            }
        }

        return prefixes.iterator();
    }
}
