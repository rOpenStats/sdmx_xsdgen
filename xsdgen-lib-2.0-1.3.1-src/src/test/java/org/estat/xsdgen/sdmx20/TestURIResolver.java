/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.junit.Ignore;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A simple resource resolver to be used for tests.
 */
@Ignore
public class TestURIResolver implements URIResolver, LSResourceResolver {

    /**
     * The base URI, used for resolving the resources for which no explicit base URI is specified.
     */
    private URI baseURI = null;

    /**
     * The factory used for creating <code>LSInput</code> instances.
     */
    private DOMImplementationLS domLS = null;

    /**
     * Constructs a new <code>TestURIResolver</code> instance.
     *
     * @param uri the default base URI
     */
    public TestURIResolver(URI uri) {
        this.baseURI = uri;
    }

    /**
     * Constructs a new <code>TestURIResolver</code> instance, to be used as a resource resolver for a
     * <code>SchemaFactory</code> instance.
     *
     * @param uri the default base URI
     * @param ls  the factory for <code>LSInput</code> instances
     */
    public TestURIResolver(URI uri, DOMImplementationLS ls) {
        this(uri);
        this.domLS = ls;
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
        URI resourceURI = this.getResourceURI(href, base);
        StreamSource ss = new StreamSource(this.getResourceStream(resourceURI, href, base));

        ss.setPublicId(href);
        ss.setSystemId(resourceURI.toString());

        return ss;
    }

    /**
     * Called to resolve an external resource.
     *
     * @param type         the type of the resource being resolved
     * @param namespaceURI the namespace of the resource being resolved
     * @param publicId     the public identifier of the external entity being referenced
     * @param systemId     the system identifier
     * @param baseURI      the absolute base URI of the resource being parsed
     *
     * @return a <code>LSInput</code> object describing the new input source, or <code>null</code> to request that the
     *         parser open a regular URI connection to the resource
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        URI resourceURI = this.getResourceURI(systemId, baseURI);

        LSInput input = this.domLS.createLSInput();

        input.setPublicId(publicId);
        input.setSystemId(systemId);
        input.setBaseURI(resourceURI.toString());
        input.setByteStream(this.getResourceStream(resourceURI, systemId, baseURI));

        return input;
    }

    /**
     * Creates the URI for a requested resource.
     *
     * @param name the name of the requested resource
     * @param base the base URI (if any)
     *
     * @return the resulted URI
     */
    private URI getResourceURI(String name, String base) {
        try {
            File baseFile = (base != null) && (base.length() > 0) ? new File(new URI(base)).getParentFile()
                    : new File(this.baseURI);
            return new File(baseFile, name).toURI();
        } catch (Exception e) {
            throw new XsdGeneratorException(e);
        }
    }

    /**
     * Gets the stream for a resource.
     *
     * @param uri  the URI of the requested resource
     * @param name the name of the resource (used only for logging)
     * @param base the base URI of the resource (used only for logging)
     *
     * @return the input stream
     */
    private InputStream getResourceStream(URI uri, String name, String base) {
        try {
            return uri.toURL().openStream();
        } catch (Exception e) {
            throw new XsdGeneratorException("Unable to locate resource '" + name + "' with base='" + base
                    + "' using the URI '" + uri + "'", e);
        }
    }
}
