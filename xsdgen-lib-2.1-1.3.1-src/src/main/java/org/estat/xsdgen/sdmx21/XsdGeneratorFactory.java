/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

/**
 * Factory for creating {@link XsdGenerator} instances.
 */
public final class XsdGeneratorFactory {

    // This is somehow useless for now, because there's only one implementation which generates the XSDs
    // using XSL transformations. In future maybe other implementations will be available, using FreeMarker or Velocity
    // for example. Anyway, is better to keep the XsdGenerator implementation class package private.

    /**
     * The one and only one factory instance.
     */
    private static final XsdGeneratorFactory factoryInstance = new XsdGeneratorFactory();

    /**
     * Constructs a new <code>XsdGeneratorFactory</code> instance.
     */
    private XsdGeneratorFactory() {
    }

    /**
     * Getter for a <code>XsdGeneratorFactory</code> instance.
     *
     * @return the instance
     */
    public static XsdGeneratorFactory getInstance() {
        // There's no need to create every time a new factory instance...
        return XsdGeneratorFactory.factoryInstance;
    }

    /**
     * Creates a new {@link XsdGenerator} instance.
     *
     * @return the generator instance
     */
    public XsdGenerator newGeneratorInstance() {
        // This should be set as deprecated and instead a getGeneratorInstance should be provided,
        // which should return same instance...
        return new XsdGeneratorImpl();
    }
}
