package org.estat.xsdgen.app;

import java.util.concurrent.CountDownLatch;

/** 
 * Class allowing to run a command on system command line.
 *
 * @author viaseb
 */
public class ProcessExecutor {

    CountDownLatch countDownLatch;

    /**
	 * Runs a command on and send back the result in an instance
	 * of the class ProcessResult.
	 * @param commandName Name with full path of the command to run.
	 * @param argsArray Array of String containing the parameters
	 * of the command.
	 * @throws Exception Throws an exception if an error occurs.
	 * @return Returns the result of the command execution.
	 */
	public static ProcessResult execute (String[] command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        return execute(processBuilder);
	}


    /**
     * executes the command of the given process builder
     * @param pb the process builder holding the command
     * @return the ProcessResult containing the stdout and stderr
     * 
     * @throws InvalidCommandException
     * @throws ProcessOutputNotReadableException 
     */
    public static synchronized ProcessResult execute(ProcessBuilder pb) throws Exception {
        ProcessResult processResult = new ProcessResult();
        Process p;

        try {
            p = pb.start();
            CountDownLatch cdl = new CountDownLatch(2);
            new Thread(new ProcessOutputStreamHandler(cdl, processResult, false, p.getInputStream())).start();
            new Thread(new ProcessOutputStreamHandler(cdl, processResult, true,  p.getErrorStream())).start();
            p.waitFor();
            cdl.await();
            processResult.setReturnCode(p.exitValue());
        } catch (Exception ex) {
        	processResult.setReturnCode(-1);
            processResult.setErrorBuffer(ex.getMessage());
        } 

        return processResult;
    }
    
}
