package unitas.execution;

import grool.access.UserCredentials;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import unitas.transfer.UnitasFileSystemType;

/**
 * An executor is the interface for any execution strategy and should be
 * override to define new execution engines. For asynchronous execution,
 * execution in split in a
 * <code>submit</code> initiating the execution asynchronously, and the
 * <code>executionCompleted</code> callback should be invoked upon execution
 * completion. For asynchronous and synchronous cases, after execution the
 * result can be retrieved through the
 * <code>getResult</code> method.
 */
public abstract class Executor implements Broker {

    // asynchronous execution flag
    private boolean asynchronous;
    // CLI execution result 
    protected Result result;
     // name application  TODO: verify if it is necessary
    protected String applicationName;
    protected ApplicationContainer applicationContainer;
    protected ImplementationContainer implementationContainer;
    protected Map<String, String> properties; //for  replicas, etc
    // user data used for authentication
    protected UserCredentials credentials;
    protected File consoleOutput;
    // execution context (infrastructure, middelware, job type)
    protected Context context;
    // File system
    protected UnitasFileSystemType fileSystem;
    // execution termination flag
    protected boolean done;
   
    

  
    /**
     * Creates a synchronous executor.
     */
    public Executor() {
        this(false);
    }

    /**
     * Asynchronous executor.
     *
     * @param credentials execution credential
     * @param asynchronous flag indicating whether the execution will be
     * synchronous or asynchronous
     */
    public Executor(boolean asynchronous) {

        done = false;
        this.asynchronous = asynchronous;
        applicationContainer = new ApplicationContainer();
        implementationContainer = new ImplementationContainer();
        properties = new HashMap<String, String>();
        result = Result.getInstance();
    }

    /**
     * Returns asynchronous execution flag.
     *
     * @return
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Asynchronous execution completion callback. This method should be
     * redefined by any asynchronous executor to notify the availability of
     * results. A redefined method should invoke this super method to format the
     * result.
     */
    public void executionCompleted() {
      done = true;
    }

    /**
     * Returns execution completion flag. This flag is true when execution
     * completed in asynchronous mode. If the execution is not started or still
     * pending, the returned value is false.
     *
     * @return execution completion flag
     */
    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    public ApplicationContainer getApplicationContainer() {
        return applicationContainer;
    }

    public void setApplicationContainer(ApplicationContainer applicationContainer) {
        this.applicationContainer = applicationContainer;
    }

    public ImplementationContainer getImplementationContainer() {
        return implementationContainer;
    }

    public void setImplementationContainer(ImplementationContainer implementationContainer) {
        this.implementationContainer = implementationContainer;
    }

     public UnitasFileSystemType getFileSystemType() {
        return fileSystem;
    }

    public void setFileSystem(UnitasFileSystemType fileSystem) {
        this.fileSystem = fileSystem;
    }
    
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public File getConsoleOutput() {
        return consoleOutput;
    }

    public void setConsoleOutput(File consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

  
}
