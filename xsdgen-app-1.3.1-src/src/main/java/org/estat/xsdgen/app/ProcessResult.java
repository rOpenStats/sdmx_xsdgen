package org.estat.xsdgen.app;

/** 
 * Result of the execution of a command line.
 *
 * @author viaseb
 */
public class ProcessResult {
  
  /** Buffer containing the output of the command. */
  private String buffer = null;
  private String errorBuffer = null;

  /** Return code of the command. */
  private int returnCode;

  /**
   * Constructor of ProcessResult
   */
  public ProcessResult() throws Exception {
  }
  
  /**
   * @param buffer the buffer to set
   */
  public void setBuffer(String buffer) {
  	this.buffer = buffer;
  }


  /**
   * @param errorBuffer the errorBuffer to set
   */
  public void setErrorBuffer(String errorBuffer) {
  	this.errorBuffer = errorBuffer;
  }


  /**
   * @param returnCode the returnCode to set
   */
  public void setReturnCode(int returnCode) {
  	this.returnCode = returnCode;
  }
    
  /**
   * Getter for the return code of the command.
   * @return Returns the return code of the command.
   */
  public int getReturnCode() throws Exception {
    return returnCode;
  }

  /**
   * Getter for the output of the command.
   * @return Returns the output of the command.
   */
  public String getBuffer() throws Exception {
    return buffer;
  }
  
  /**
   * Getter for the error output of the command.
   * @return Returns the output of the command.
   */
  public String getErrorBuffer() throws Exception {
  	return errorBuffer;
  }


}
