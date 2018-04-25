package org.estat.xsdgen.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * Captures the output of a process
 *
 * @author viaseb
 */
public class ProcessOutputStreamHandler implements Runnable {

    CountDownLatch countDownLatch;
    ProcessResult processResult;
    boolean stdError;
    InputStream inputStream;
    BufferedReader reader;

    public ProcessOutputStreamHandler(
            CountDownLatch countDownLatch,
            ProcessResult processResult,
            boolean stdError,
            InputStream inputStream
    ) {
        this.countDownLatch = countDownLatch;
        this.processResult = processResult;
        this.stdError = stdError;
        this.inputStream = inputStream;
    }
    
    @Override
    public void run() {
        String s;
		StringBuffer buffer = new StringBuffer();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((s = reader.readLine()) != null) {
            	s += "\n";
                buffer.append(s);
            }
        } catch (IOException ex) {
        	System.err.println("Error reading " + (stdError ? "stdErr" : "stdOut") + " stream : " + ex.getMessage());
        }
        if (stdError) {
        	processResult.setErrorBuffer(buffer.toString());
        } else {
        	processResult.setBuffer(buffer.toString());
        }
        countDownLatch.countDown();
    }
}
