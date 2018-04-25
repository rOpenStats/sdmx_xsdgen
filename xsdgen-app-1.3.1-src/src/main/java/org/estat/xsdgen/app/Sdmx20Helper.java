/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.estat.xsdgen.sdmx20.XsdGeneratorFactory;
import org.estat.xsdgen.sdmx20.XsdGenerator.XsdType;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

/**
 * Helper for SDMX 2.0 standard.
 */
public class Sdmx20Helper extends SdmxHelper implements Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7234506395833424922L;

	/**
     * The one and only one instance.
     */
    private static Sdmx20Helper instance = new Sdmx20Helper();

    /**
     * Constructs a new <code>SdmxSupport20</code> instance.
     */
    public Sdmx20Helper() {
        super(SdmxVersion.SDMX_2_0, new SdmxNamespaceContext("sdmx-ns-20.properties"),
                "/message:Structure/message:KeyFamilies/structure:KeyFamily");
    }

    /**
     * Getter for {@link #instance}.
     *
     * @return the value
     */
    protected static SdmxHelper getInstance() {
        return Sdmx20Helper.instance;
    }

    /**
     * Getter for a list of options which can be displayed to user for selecting the type of the XSD to generate.
     *
     * @param dsd the DSD document for which to get the options list
     *
     * @return the list with options
     *
     * @throws Exception if fails
     */
    public List<XsdOption> getXsdGenOptions(Document dsd) throws Exception {
        List<XsdOption> options = new ArrayList<XsdOption>();
        for (XsdType type : XsdType.values()) {
            options.add(new XsdOptionImpl(type));
        }
        return options;
    }

    /**
     * Getter for the recommended name to be used for a XSD file.
     *
     * @param info   the info about the source DSD document
     * @param option the option used for generating the XSD document
     *
     * @return the name
     */
    public String getXsdRecommendedName(DsdInfo info, XsdOption option) {
        String baseName = info.getBaseFileName();

        return baseName + "_" + ((XsdOptionImpl) option).getType().toString().toUpperCase() + "."
                + Constants.EXTENSION_XSD.toUpperCase();
    }

    /**
     * Generates a XSD using <code>XSD Generator</code> library.
     *
     * @param dsd    the DSD document for which the XSD must be generated
     * @param option the option which specifies the type of the XSD to generate
     *
     * @return the resulted XSD document
     *
     * @throws Exception if fails
     */
    @Override
    public void generateXsd(InputStream dsd, XsdOption option, StreamResult result) throws Exception {
        XsdGeneratorFactory.getInstance().newGeneratorInstance().generate(dsd,
                ((XsdOptionImpl) option).getType(), result);
    }

	@Override
	public Document generateXsd(Document dsd, XsdOption option)
			throws Exception {
		return null;
	}
    
	/**
     * The {@link XsdOption} implementation for SDMX 2.0 version.
     */
    class XsdOptionImpl implements XsdOption, Serializable {

        /**
         * Serial Version UID
		 */
		private static final long serialVersionUID = 5880757862080595337L;
		/**
         * The XSD type.
         */
        private XsdType type = null;

        /**
         * Constructs a new <code>XsdOptionSdmx20</code> instance.
         *
         * @param value the type
         */
        public XsdOptionImpl(XsdType value) {
            this.type = value;
        }

        /**
         * Returns a string representation of the option which can be used as a label.
         *
         * @return the result
         */
        public String toString() {
            return Globals.getMessages().getString("results.sdmx_2_0.option."
                    + this.type.toString().toLowerCase());
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         *
         * @param obj the reference object with which to compare
         *
         * @return <code>true</code> if this object is the same as the obj argument, <code>false</code> otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if ((obj != null) && (obj instanceof XsdOptionImpl)) {
                return ((XsdOptionImpl)obj).getType().equals(this.type);
            } else {
                return false;
            }
        }

        /**
         * Returns a hash code value for the object.
         *
         * @return the value
         */
        public int hashCode() {
            return this.type.hashCode();
        }

        /**
         * Getter for {@link #type} property.
         *
         * @return the value
         */
        public XsdType getType() {
            return this.type;
        }
    }
}
