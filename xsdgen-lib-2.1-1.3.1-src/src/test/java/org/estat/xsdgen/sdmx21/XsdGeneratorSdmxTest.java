/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import org.estat.xsdgen.sdmx21.XsdGeneratorOptions;
import org.junit.Test;


/**
 * Some basic tests for SDMX 2.1 standard.
 */
public class XsdGeneratorSdmxTest extends XsdGeneratorTest {

    /**
     * Constructs a new <code>XsdGeneratorSdmx21Test</code> instance.
     */
    public XsdGeneratorSdmxTest() {
    }

    /**
     * Test for the demography sample.
     *
     * @throws Exception if fails
     */
    @Test
    public void testDemographySample() throws Exception {
        testSdmxSample("demography/demography.xml", "demography/demography_xs.xml",
                XsdGeneratorOptions.getOptionsForCrossMessages("DEMO", true), "xsdgen_demography_xs.xsd");
    }
}
