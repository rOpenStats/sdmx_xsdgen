/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx21;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.estat.xsdgen.sdmx21.XsdGeneratorOptions;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for all EXR samples included into SDMX 2.1 package downloadable from www.sdmx.org website.
 */
//@RunWith(Parameterized.class)
public class XsdGeneratorSdmxExrTest extends XsdGeneratorTest {

    /**
     * The enumeration with available EXR samples.
     */
    private enum SampleType {
        FLAT,
        TS,
        TS_GF,
        XS
    }

    /**
     * The name of the DSD file.
     */
    private String dsdFileName = null;

    /**
     * The name of the sample file.
     */
    private String sampleFileName = null;

    /**
     * The options to be used for testing.
     */
    private XsdGeneratorOptions options = null;

    /**
     * The name of the file where to write the resulted XSD file.
     */
    private String xsdFileName = null;

    /**
     * Creates the list with test parameters.
     *
     * @return the list
     */
    @Parameters
    public static Collection<Object[]> getParameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();

        String[] categories = new String[] {"ecb_exr_ng", "ecb_exr_sg", "ecb_exr_rg"};

        for (String category : categories) {
            for (SampleType sample : SampleType.values()) {
                for (boolean full : new boolean[] {false, true}) {
                    parameters.add(new Object[] {category, sample, full});
                }
            }
        }

        return parameters;
    }

    /**
     * Constructs a new <code>XsdGeneratorSdmx21ExrTest</code> instance.
     *
     * @param category   the category (ng, sg, rg)
     * @param sampleType the sample type
     * @param full       if <code>true</code> then the full DSD will be used for testing, otherwise the one with
     *                   external references
     */
    public XsdGeneratorSdmxExrTest(String category, SampleType sampleType, boolean full) {
        if (sampleType == SampleType.FLAT) {
            this.options = XsdGeneratorOptions.getOptionsForFlatMessages();
        } else if (sampleType == SampleType.TS) {
            this.options = XsdGeneratorOptions.getOptionsForTimeSeriesMessages();
        } else if (sampleType == SampleType.TS_GF) {
            this.options = XsdGeneratorOptions.getOptionsForCrossMessages("TIME_PERIOD", false);
        } else {
            this.options = XsdGeneratorOptions.getOptionsForCrossMessages("CURRENCY", false);
        }

        String sampleSuffix = category.equals("ecb_exr_sg") && sampleType.equals(SampleType.FLAT) ? "ts_gf_flat"
                : sampleType.toString().toLowerCase();

        String dsdSuffix = full ? "_full" : "";
        this.dsdFileName = "exr/" + category + "/" + category + dsdSuffix + ".xml";
        this.sampleFileName = "exr/" + category + "/structured/" + category + "_" + sampleSuffix + ".xml";
        this.xsdFileName = "xsdgen_" + category + dsdSuffix + "_" + sampleSuffix + ".xsd";
    }

    /**
     * Tests the current EXR sample.
     *
     * @throws Exception if fails
     */
    @Test
    public void executeTest() throws Exception {
        testSdmxSample(this.dsdFileName, this.sampleFileName, this.options, this.xsdFileName);
    }
}
