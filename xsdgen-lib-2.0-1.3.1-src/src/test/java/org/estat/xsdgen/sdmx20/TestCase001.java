package org.estat.xsdgen.sdmx20;


import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;



public class TestCase001 extends Thread  {
	
	int numberUsers=10; //users calling at the same time to the API (using threads)
	int loopRun = 0; //0: just one time is run// 1: loop until timeHours
	int timeHours=1; //1: during 1 hour// 24: during 1 day
	int timeLapse=1; //(if loopRun==1) time Lapse between calling the API in minutes// 1: 1 minute//2:2 minutes
	String queryFile = "C:/xsd_gen/xslt/CE1.xml";
	
	static int loopCounter=1; //do not change
	
	@Test
	public void testLunch() throws Exception {
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		System.out.println("**START TEST**");
		//InvokeNative in = new InvokeNative();
		//in.createMemoryFile();
		MemoryCheck mc = new MemoryCheck();
		mc.getMemoryCheck();
		if (loopRun==0){
			for (int number = 1; number < numberUsers; number++) {
				TestCase001 lunchTest = new TestCase001();
				lunchTest.start();
				threads.add(lunchTest);
			}
			this.run();
			for (int i = 1; i < numberUsers; i++)
			{
				threads.get(i-1).join();
			}
		}else{
			Calendar cal= Calendar.getInstance();
			Calendar calLapse = Calendar.getInstance();
			cal.add(Calendar.HOUR, timeHours);		
			calLapse.add(Calendar.MINUTE, timeLapse);
			while (cal.compareTo(Calendar.getInstance())>0){
				if (calLapse.compareTo(Calendar.getInstance())<0){
					for (int number = 2; number <= numberUsers; number++) {
						TestCase001 lunchTest = new TestCase001();
						lunchTest.start();				
					}
					this.run();
					calLapse.add(Calendar.MINUTE, timeLapse);
				}				
			}
		}
		
		//in.createMemoryFile();
		
		System.out.println("**END TEST**");
		mc.getMemoryCheck();
	}
	
	public void run(){
		System.out.println("**INSTANCE NUMBER: "+loopCounter+++"**");
		LunchXSDGenerator tester = new LunchXSDGenerator();
		tester.Lunch(queryFile);
		//assertEquals("Result", "OK", tester.Lunch(queryFile));
	}

}
