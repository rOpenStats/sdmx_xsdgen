package org.estat.xsdgen.sdmx20;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.transform.stream.StreamResult;

import org.estat.xsdgen.sdmx20.XsdGenerator;
import org.estat.xsdgen.sdmx20.XsdGeneratorFactory;

public class LunchXSDGenerator {
	
	public String Lunch(String sdmxmlQueryFile){
		XsdGenerator generator = XsdGeneratorFactory.getInstance().newGeneratorInstance();
        try {
        	InputStream is = new FileInputStream(sdmxmlQueryFile);
        	FileOutputStream os = new FileOutputStream(UUID.randomUUID() + ".xml");
			generator.generate(is, XsdGenerator.XsdType.COMPACT, new StreamResult(os));
			os.close();
			is.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL: "+e.getMessage();
		}
		return "OK";
	}

}
