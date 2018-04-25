package org.estat.xsdgen.sdmx20;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.transform.stream.StreamResult;

import org.estat.xsdgen.sdmx20.XsdGenerator;
import org.estat.xsdgen.sdmx20.XsdGeneratorFactory;

public class XSDGenAPITest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			XsdGenerator xsdGenerator = XsdGeneratorFactory.
					getInstance().newGeneratorInstance();
			
			
			InputStream inputStream = new FileInputStream("c:/test/input_sdmx2.0.xml");
			StreamResult streamResult = new StreamResult(new 
					FileOutputStream("c:/test/result_sdmx2.0.xml"));
			
			xsdGenerator.generate(inputStream, 
					XsdGenerator.XsdType.COMPACT, streamResult);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
