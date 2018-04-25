/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.sdmx20;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.transform.stream.StreamResult;

import org.estat.xsdgen.sdmx20.XsdGenerator;
import org.estat.xsdgen.sdmx20.XsdGeneratorFactory;
import org.junit.Test;


/**
 * Some basic tests for SDMX 2.0 standard.
 */
public class XsdGeneratorSdmxTest extends XsdGeneratorTest {

    /**
     * Constructs a new <code>XsdGeneratorSdmx20Test</code> instance.
     */
    public XsdGeneratorSdmxTest() {
    }

    /**
     * Tests the generation of XSD for COMPACT format, using the STS key family as input.
     *
     * @throws Exception if fails
     */
    @Test
    public void testSTSCompact() throws Exception {
        XsdGenerator generator = XsdGeneratorFactory.getInstance().newGeneratorInstance();
		InputStream inputStream = new FileInputStream("sdmx_2_0/samples/estat_sts/ESTAT_STS_v2.2.xml");
		StreamResult streamResult = new StreamResult(new 
				FileOutputStream("sdmx_2_0/samples/estat_sts/result_sdmx2.0_compact.xml"));
		
        generator.generate(inputStream, XsdGenerator.XsdType.COMPACT, streamResult);
    }

    /**
     * Tests the generation of XSD for CROSS SECTIONAL format, using the STS key family as input.
     *
     * @throws Exception if fails
     */
    @Test
    public void testSTSCross() throws Exception {
        XsdGenerator generator = XsdGeneratorFactory.getInstance().newGeneratorInstance();
		InputStream inputStream = new FileInputStream("sdmx_2_0/samples/estat_sts/ESTAT_STS_v2.2.xml");
		StreamResult streamResult = new StreamResult(new 
				FileOutputStream("sdmx_2_0/samples/estat_sts/result_sdmx2.0_cross_sectional.xml"));
		
        generator.generate(inputStream, XsdGenerator.XsdType.CROSS_SECTIONAL, streamResult);
    }

    /**
     * Tests the generation of XSD for UTILITY format, using the STS key family as input.
     *
     * @throws Exception if fails
     */
    @Test
    public void testSTSUtility() throws Exception {
        XsdGenerator generator = XsdGeneratorFactory.getInstance().newGeneratorInstance();
		InputStream inputStream = new FileInputStream("sdmx_2_0/samples/estat_sts/ESTAT_STS_v2.2.xml");
		StreamResult streamResult = new StreamResult(new 
				FileOutputStream("sdmx_2_0/samples/estat_sts/result_sdmx2.0_utility.xml"));
		
        generator.generate(inputStream, XsdGenerator.XsdType.UTILITY, streamResult);
    }
}
