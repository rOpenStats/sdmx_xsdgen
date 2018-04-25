/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.estat.xsdgen.app.Globals;
import org.estat.xsdgen.sdmx20.XsdGeneratorException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Implements various util methods.
 */
public final class Utils {

    /**
     * The document builder instance used for creating DOM documents.
     */
    private static DocumentBuilder documentBuilder = null;

    /**
     * The <code>Transformer</code> instance used for serializing DOM documents.
     */
    private static Transformer documentTransformer = null;

    static {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        try {
            Utils.documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XsdGeneratorException("Failed to create the DocumentBuilder instance", e);
        }

        try {
//            Utils.documentTransformer = new org.apache.xalan.processor.TransformerFactoryImpl().newTransformer();
            Utils.documentTransformer = TransformerFactory.newInstance().newTransformer();
            Utils.documentTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            throw new XsdGeneratorException("Failed to create the " +
                    "Transformer instance", e);
        }
    }

    /**
     * Private constructor which prevents instantiation.
     */
    private Utils() {
    }

    /**
     * Converts a text to HTML, this being useful for <code>JLabel</code> instances, which have some basic support for
     * word wrapping if HTML is used instead plain text.
     *
     * @param text    the text to convert
     * @param escape  if <code>true</code> then the argument is HTML escaped
     * @param justify if <code>true</code> then the paragraph element used to encapsulate the text will have
     *                <code>text-align:justify</code> style
     *
     * @return the resulted HTML code
     */
    public static String textToHTML(String text, boolean escape, boolean justify) {
        return "<html><body>"
                + (justify ? "<p style=\"text-align:justify\">" : "<p>")
                + (escape ? StringEscapeUtils.escapeHtml(text) : text)
                + "</p>"
                + "</body></html>";
    }

    /**
     * Helper for creating a <code>JButton</code> instance.
     *
     * @param labelKey the key of the message to be used as button's label
     * @param al       the <code>ActionListener</code> to be added to button; may be <code>null</code>
     *
     * @return the <code>JButton</code> instance
     */
    public static JButton createButton(String labelKey, ActionListener al) {
        JButton result = new JButton(Globals.getMessages().getString(labelKey));
        if (al != null) {
            result.addActionListener(al);
        }
        return result;
    }

    /**
     * Reads and parses an XML file.
     *
     * @param path the path to XML file.
     *
     * @return the resulted <code>org.w3c.Document</code> instance
     *
     * @throws Exception if fails
     */
    public static Document readXML(File path) throws Exception {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
            return Utils.documentBuilder.parse(fis);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * Reads and parses an XML file.
     *
     * @param is the stream from where to read the file
     *
     * @return the resulted <code>org.w3c.Document</code> instance
     *
     * @throws Exception if fails
     */
    public static Document readXML(InputStream is) throws Exception {
        return Utils.documentBuilder.parse(is);
    }

    /**
     * Parses an XML string.
     *
     * @param content the XML string to parse
     *
     * @return the resulted document
     *
     * @throws Exception if fails
     */
    public static Document parseXML(String content) throws Exception {
        return Utils.documentBuilder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
    }

    /**
     * Reads and parses an XML resource file.
     *
     * @param name the name of the resource (see <code>Class.getResourceAsSteam</code> for details)
     *
     * @return the resulted DOM document
     *
     * @throws Exception if fails
     */
    public static Document readXMLResource(String name) throws Exception {
        InputStream is = Utils.class.getResourceAsStream(name);

        if (is == null) {
            throw new XsdGeneratorException("Failed to get resource file: " + name);
        }

        try {
            return Utils.documentBuilder.parse(is);
        } finally {
            is.close();
        }
    }

    /**
     * The <code>toString</code> method for a DOM document.
     *
     * @param doc the DOM node for which to do the conversion
     *
     * @return the resulted string
     *
     * @throws Exception if something goes wrong and fails
     */
    public static String nodeToString(Node doc) throws Exception {
        StringWriter result = new StringWriter();
        Utils.documentTransformer.transform(new DOMSource(doc), new StreamResult(result));
        return result.toString();
    }

    /**
     * Writes an XML file.
     *
     * @param path the path where to write the file
     * @param doc  the DOM document to write
     *
     * @throws Exception if fails
     */
    public static void writeXML(File path, Document doc) throws Exception {
        Utils.documentTransformer.transform(new DOMSource(doc), new StreamResult(path));
    }
}
