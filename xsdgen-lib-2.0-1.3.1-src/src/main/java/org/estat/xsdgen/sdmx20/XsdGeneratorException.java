/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

/**
 * The exception used by XSD Generator library for signalling errors.
 */
public class XsdGeneratorException extends RuntimeException {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7785865650847544771L;

	/**
     * Constructs a new <code>XsdGeneratorException</code> instance.
     */
    public XsdGeneratorException() {
    }

    /**
     * Constructs a new <code>XsdGeneratorException</code> instance.
     *
     * @param message the detail message
     */
    public XsdGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>XsdGeneratorException</code> instance.
     *
     * @param cause the cause
     */
    public XsdGeneratorException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>XsdGeneratorException</code> instance.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public XsdGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
