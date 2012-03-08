package unitas.execution;

import grool.access.UserCredentials;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import jigsaw.core.mapper.MetaMapper;
import jigsaw.core.mapper.UriMetaMapper;
import jigsaw.databinding.types.Argument;
import jigsaw.databinding.types.Infrastructure;
import jigsaw.databinding.types.Job;
import jigsaw.databinding.types.Type;
import jigsaw.information.BundleInformant;
import jigsaw.information.JigsawInformant;
import jigsaw.process.ApplicationContainer;
import jigsaw.process.ImplementationContainer;
import jigsaw.transfer.JigsawFileSystemType;
import jigsaw.util.reflection.Introspector;
import org.apache.log4j.Logger;

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
public abstract class UnitasExecutor implements Broker {

    private static final Logger log = Logger.getLogger(UnitasExecutor.class);
    // related activity
    //TODO change to private Activity activity;
    protected Class output;
    // asynchronous execution flag
    private boolean asynchronous;
    // CLI execution result 
    protected Result result;
    protected ApplicationContainer applicationContainer;
    protected ImplementationContainer implementationContainer;
    protected Map<String, String> properties; //for  replicas, etc
    // user data used for authentication
    protected UserCredentials credentials;
    protected File consoleOutput;
    protected JigsawFileSystemType fileSystem;
    // execution termination flag
    protected boolean done;
    protected String release;
    protected Infrastructure infrastructure;
    protected Job job;
    protected Exception exception;
    private Object sinks;
    private String command;

    /**
     * Creates a synchronous executor.
     */
    public UnitasExecutor() {
        this(false);
    }

    /**
     * Asynchronous executor.
     *
     * @param credentials execution credential
     * @param asynchronous flag indicating whether the execution will be
     * synchronous or asynchronous
     */
    public UnitasExecutor(boolean asynchronous) {

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

        matchResults();
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

    public JigsawFileSystemType getFileSystemType() {
        return fileSystem;
    }

    public void setFileSystem(JigsawFileSystemType fileSystem) {
        this.fileSystem = fileSystem;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Infrastructure getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public File getConsoleOutput() {
        return consoleOutput;
    }

    public void setConsoleOutput(File consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

    /**
     * Format and return executed command execution result. This method only
     * need to be invoked after and asynchronous execution completed. In case of
     * synchronous execution, it is explicitely invoked through the the
     * synchronous fire method.
     */
    protected void matchResults() {
        //public void matchResults(Result results) {

        Boolean isWebService = (output != null);
        Map<String, Object> mappedResults = null;
       
        URI stdout = result.getOutput();
        URI stderr = result.getError();
        Introspector introspector = null;
        if (isWebService) {
            try {

                introspector = new Introspector(output);
                introspector.invoke("setStdout", stdout);
                introspector.invoke("setStderr", stderr);
            } catch (javax.management.ReflectionException ex) {
                exception = ex;
            }
        } else {

            mappedResults = new HashMap<String, Object>();
            mappedResults.put("stdout", stdout);
            mappedResults.put("stderr", stderr);
        }

        if (result.getStatus() == 0) {

            Map<Argument, Object> _sinks = applicationContainer.getSinks();
            for (Map.Entry<Argument, Object> entry : _sinks.entrySet()) {

                Argument argument = entry.getKey();
                Object filter = entry.getValue();
                Object filterResult = null;
                MetaMapper mapper = null;
                if (argument.getType() == Type.URI) {

                    mapper = new UriMetaMapper(argument.getMapper(), argument.getType());
                    mapper.setResults(result.getResults());
                } else {

                    mapper = new MetaMapper(argument.getMapper(), argument.getType());
                    mapper.setOutput(consoleOutput);
                }

                try {
                    filterResult = mapper.parse(argument.getNesting().getDimension(), filter);
                } catch (jigsaw.exception.JigsawException ex) {
                    exception = ex;
                }

                if (filterResult != null) {
                    if (isWebService) {

                        String filtername = BundleInformant.getJavaSetterName(argument);
                        try {
                            introspector.invoke(filtername, new Object[]{filterResult});
                        } catch (javax.management.ReflectionException ex) {
                            exception = ex;
                        }
                    } else {
                        mappedResults.put(argument.getLabel(), filterResult);
                    }
                } else {
                    exception = new unitas.exception.UnitasRuntimeException("Expected result cannot be resolved during matching of execution outputs");
                }
            }
        } else {
            if (JigsawInformant.includesCanceledExecutions()) {
                if (result.getStatus() == 9) {

                    log.info("Including canceled jobs during results matching");
                    Map<Argument, Object> _sinks = applicationContainer.getSinks();
                    for (Map.Entry<Argument, Object> entry : _sinks.entrySet()) {

                        //TODO add condition for WS
                        Argument argument = entry.getKey();
                        if (argument.getType() == Type.URI) {
                            //TODO add other types of arguments
                            try {
                                mappedResults.put(argument.getLabel(), new URI((String) entry.getValue()));
                            } catch (java.net.URISyntaxException ex) {
                                exception = ex;
                            }
                        }
                    }
                }
            } else {
                if (stdout != null && stderr != null) {
                    exception = new jigsaw.exception.JigsawRuntimeException("The execution finished generating an error. "
                            + "Exit status code: " + result.getStatus()
                            + ". See output details at " + stdout
                            + " and the execution message errors at " + stderr);
                } else {
                    exception = new jigsaw.exception.JigsawRuntimeException("The execution finished generating an error."
                            + "Exit status code: " + result.getStatus());
                }
            }
        }

        sinks = isWebService ? introspector.getObject() : mappedResults;

        log.debug("Results matching is done for " + command);
    }
}
