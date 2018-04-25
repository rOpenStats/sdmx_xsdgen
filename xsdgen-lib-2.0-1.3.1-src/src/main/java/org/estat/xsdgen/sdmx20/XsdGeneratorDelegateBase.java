/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

import java.io.InputStream;
import java.util.Map;

import javanet.staxutils.StAXResult;
import javanet.staxutils.StAXSource;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.estat.xsdgen.sdmx20.XsdGeneratorImpl;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

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
        this.transformerFactory = TransformerFactory.newInstance();
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
    protected void transform(String stylesheet, final InputStream source, Map<String, Object> params, URIResolver resolver, StreamResult result)
            throws Exception {
    	
    	System.setProperty("javax.xml.stream.XMLInputFactory",
    			  "com.ctc.wstx.stax.WstxInputFactory");
    	System.setProperty("javax.xml.stream.XMLOutputFactory",
    			  "com.ctc.wstx.stax.WstxOutputFactory");
    	
        InputStream is = this.getResourceAsStream("xslt/" + stylesheet);

        if (is == null) {
            throw new XsdGeneratorException("Failed to get XSLT stylesheet from classpath: " + stylesheet);
        }

        final StringBuffer messages = new StringBuffer();

        try {
			XMLInputFactory inputFactory = new WstxInputFactory(); // XMLInputFactory.newInstance();
			XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(source) ;
			Source XML = new StAXSource(xmlStreamReader);
			
			XMLOutputFactory outputFactory = new WstxOutputFactory(); //XMLOutputFactory.newInstance();
			XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(result);
			XMLStreamWriter streamWriter = new IndentingXMLStreamWriter(xmlStreamWriter);
			Result XML_r = new StAXResult(streamWriter);
			
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(is));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");
			transformer.transform(XML, XML_r);
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
