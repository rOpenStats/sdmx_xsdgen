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
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.estat.xsdgen.sdmx20.XsdGeneratorException;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;

/**
 * Base class for other test classes.
 */
abstract class XsdGeneratorTest {

    /**
     * Constructs a new <code>XsdGeneratorTest</code> instance.
     */
    protected XsdGeneratorTest() {
    }

    /**
     * Parses a XML test file.
     *
     * @param name the name of the test file
     *
     * @return the resulted document
     *
     * @throws Exception if fails
     */
    protected Document parseTestFile(String name) throws Exception {
        InputStream is = getClass().getResourceAsStream(name);

        if (is == null) {
            throw new XsdGeneratorException("Failed to locate test file: " + name);
        }

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            builderFactory.setValidating(false);

            return builderFactory.newDocumentBuilder().parse(is);
        } finally {
            is.close();
        }
    }

    /**
     * Validates a test XML message using a XML schema.
     *
     * @param xsd          the document with XML scheme definition
     * @param testFileName the name of the test XML file
     *
     * @throws Exception if fails
     */
    protected void validateMessage(Document xsd, String testFileName) throws Exception {
        DOMImplementationLS ls = (DOMImplementationLS) xsd.getImplementation().getFeature("LS", "3.0");

        if (ls == null) {
            throw new XsdGeneratorException("Failed to get the DOMImplementationLS instance from XSD document");
        }

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new TestURIResolver(getClass().getResource("sdmx_2_1/schemas").toURI(), ls));
        // Avoid using DOMSource and instead preferring StreamSource. See XSDGenerator JavaDoc for more details...
        Schema schema = factory.newSchema(new StreamSource(new StringReader(this.documentToString(xsd))));
        Validator validator = schema.newValidator();

        InputStream is = getClass().getResourceAsStream(testFileName);

        if (is == null) {
            throw new XsdGeneratorException("Failed to locate test file: " + testFileName);
        }

        try {
            validator.validate(new StreamSource(is));
        } finally {
            is.close();
        }
    }

    /**
     * Converts a <code>org.w3c.Document</code> instance to a string.
     *
     * @param doc the document
     *
     * @return the resulted string object
     *
     * @throws Exception if fails
     */
    protected String documentToString(Document doc) throws Exception {
        StringWriter result = new StringWriter();
        Transformer transformer = new org.apache.xalan.processor.TransformerFactoryImpl().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(result));
        return result.toString();
    }

    /**
     * Writes a document to a file.
     *
     * @param doc  the document to write
     * @param file the file where to write the document
     *
     * @throws Exception if fails
     */
    protected void writeDocToFile(Document doc, File file) throws Exception {
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(this.documentToString(doc));
        } finally {
            writer.close();
        }
    }

    /**
     * Writes a document to a file, but only if this is enabled using a VM parameter.
     *
     * @param doc      the document to write
     * @param fileName the name of the file where to write the document
     *
     * @throws Exception if fails
     */
    protected void writeDocToFileIfEnabled(Document doc, String fileName) throws Exception {
        String path = System.getProperty("xsdgen_test_write_results_path");
        if (path != null) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                this.writeDocToFile(doc, new File(dir, fileName));
            }
        }
    }
}
