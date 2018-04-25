/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Some tests for {@link Utils} class.
 */
public class UtilsTest {

    /**
     * Constructs a new <code>UtilsTest</code> instance.
     */
    public UtilsTest() {
    }

    /**
     * Basic test for the DOM related functions.
     *
     * @throws Exception if fails
     */
    @Test
    public void testDOMFunctions() throws Exception {
        Document dsd = Utils.readXMLResource("ESTAT_STS_v2.2.xml");
        DsdInfo dsdInfo = SdmxHelper.getInstance(SdmxVersion.SDMX_2_0).getDsdInfo(dsd);

        Assert.assertEquals("ESTAT", dsdInfo.getAgency());
        Assert.assertEquals("STS", dsdInfo.getId());
        Assert.assertEquals("2.2", dsdInfo.getVersion());
    }
}
