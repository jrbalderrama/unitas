package unitas.execution;

import grool.access.UserCredentials;
import java.net.URI;

/**
 *
 * @author javier
 */
public interface Broker {

    /**
     * job-delegate-proxy
     * @param credentials 
     */
    public void setCredentials(UserCredentials credentials);

    /**
     * job-submit
     * @param command cli application name
     * @param location directory o of the binaries
     * @param path base directory of the execution results (on local execution
     * context it is the place of the execution)
     * and the stdout and stderr are contained after execution
     * @throws jigsaw.exception.JigsawException
     */
    public void submit(String command, URI location, URI path)
            throws
            unitas.exception.UnitasException;

    /**
     * job-status
     * @return 
     */
    public boolean isDone();

    /**
     * Returns the command result, or null if the execution did not complete yet.
     * job-output command execution result
     * @return 
     */
    public Result getResult();
}
