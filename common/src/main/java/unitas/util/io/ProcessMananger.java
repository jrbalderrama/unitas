package unitas.util.io;

import grool.proxy.Proxy;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author tram
 */
public class ProcessMananger {

    /**
     * Create a process for a command with specific environment variables
     *
     * @param userProxy user proxy to add in environment
     * @param commands command and its arguments to be executed
     * @return a process of this command
     * @throws java.io.IOException in case process cannot be created
     */
    public static Process getProcess(Proxy userProxy, String... commands) throws java.io.IOException {

        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectErrorStream(true);
        builder.environment().put("X509_USER_PROXY", userProxy.getProxy().getAbsolutePath());

        return builder.start();
    }
    
    /**
     * Get console output from a process
     * @param process process to get the console output
     * @return a buffer reader of the console output of input process
     */
    public static BufferedReader getBufferedReader(Process process) {
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
}
