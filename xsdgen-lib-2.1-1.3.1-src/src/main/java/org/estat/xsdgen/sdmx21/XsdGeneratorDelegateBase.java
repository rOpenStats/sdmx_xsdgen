/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import net.sf.saxon.Controller;
import net.sf.saxon.event.SequenceWriter;
import net.sf.saxon.om.Item;
import net.sf.saxon.trans.XPathException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Map;

/**
 * Base for delegate classes used for generating XML schema files using XSL transformations.
 */
class XsdGeneratorDelegateBase {

    /**
     * The factory used for creating {@link Transformer} instances.
     */
    private TransformerFactory transformerFactory = null;

    /**
     * Constructs a new <code>XsdGeneratorDelegateBase</code> instance.
     */
    public XsdGeneratorDelegateBase() {
        this.transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
    }

    /**
     * Constructs a new <code>XsdGeneratorDelegateBase</code> instance.
     *
     * @param factory the factory used for creating {@link Transformer} instances.
     */
    public XsdGeneratorDelegateBase(TransformerFactory factory) {
        this.transformerFactory = factory;
    }

    /**
     * Processes a XSLT file and returns back the resulted document.
     *
     * @param stylesheet the name of XSLT stylesheet file
     * @param source     the source document
     * @param params     an optional map with all parameters to be passed to XSL transformer instance
     * @param resolver   an optional URI resolver instance
     *
     * @return the resulted document
     *
     * @throws Exception if fails
     */
    @SuppressWarnings("rawtypes")
    protected Document transform(String stylesheet, Document source, Map<String, Object> params, URIResolver resolver)
            throws Exception {
    	
        InputStream is = this.getResourceAsStream("xslt/" + stylesheet);

        if (is == null) {
            throw new XsdGeneratorException("Failed to get XSLT stylesheet from classpath: " + stylesheet);
        }

        final StringBuffer messages = new StringBuffer();

        try {
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(is));
            if (params != null) {
                this.setTransformerParameters(transformer, params);
            }
            if (resolver != null) {
                transformer.setURIResolver(resolver);
            }
            this.setTransformerOutputParameters(transformer);
            DOMResult result = new DOMResult();
            ((Controller) transformer).setMessageEmitter(new SequenceWriter(null) {
                public void write(Item item) throws XPathException {
                    if (item != null) {
                        String text = item.getStringValue();
                        if (text != null) {
                            messages.append(text);
                        }
                    }
                }
            });
            transformer.transform(new DOMSource(source), result);
            return (Document) result.getNode();
        } catch (Exception e) {
            if (messages.length() > 0) {
                throw new XsdGeneratorException("Transformation failed: " + messages.toString(), e);
            } else {
                throw e;
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    /**
     * Finds a resource with a given name.
     *
     * @param name the name of the resource; see {@link Class#getResourceAsStream(String)} for details
     *
     * @return an <code>InputStream</code> for the resource, <code>null</code> if not found
     *
     * @throws Exception if fails
     */
    protected InputStream getResourceAsStream(String name) throws Exception {
        return XsdGeneratorImpl.class.getResourceAsStream(name);
    }

    /**
     * Sets the parameters for a transformer instance.
     *
     * @param transformer the transformer instance
     * @param params      the parameters
     *
     * @throws Exception if fails
     */
    protected void setTransformerParameters(Transformer transformer, Map<String, Object> params) throws Exception {
        for (String key : params.keySet()) {
            transformer.setParameter(key, params.get(key));
        }
    }

    /**
     * Sets the output parameters for a transformer instance.
     *
     * @param transformer the transformer instance
     *
     * @throws Exception if fails
     */
    protected void setTransformerOutputParameters(Transformer transformer) throws Exception {
    }
}
