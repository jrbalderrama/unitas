package unitas.execution;

import grool.proxy.Proxy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.log4j.Logger;
import unitas.configuration.Constants;
import unitas.generator.jdl.JDLGenerator;

/**
 * DCIExecutor is used in case of computing infrastructure is an distributed
 * one. Each DCI can have many middlewares which are not probably compatible.
 * They have a different format of Job Description Language (JDL) used to submit
 * job.
 *
 * @author tram
 */
public abstract class DCIExecutor extends Executor {

    private static final Logger log = Logger.getLogger(DCIExecutor.class);
    // filename of the script containing all execution steps 
    // (download input, execute, upload output) 
    protected String scriptName;
    // filename of jdl corresponding
    protected String jdlName;
    // add job name to the jdl file or not
    protected boolean useJobName;

    public DCIExecutor() {

        this(false);
    }

    public DCIExecutor(boolean useJobName) {
        super(true);
        this.useJobName = useJobName;
        // generate execution script file
        generateScriptFile();
        // and then generate jdl file
        generateJDLFile();
    }

    /**
     * Generate jdl file, each executor should implement this function
     * corresponding to its middleware
     */
    private void generateJDLFile() {

        StringBuilder sb = new StringBuilder();

        if (this.useJobName) {
            sb.append("JobName = \"").append(this.applicationName).append("\";\n");
        }

        sb.append(JDLGenerator.generateJDL(scriptName, this.implementationContainer.getAttributes()));

        File jdlDir = new File(Constants.JDL_ROOT);
        if (!jdlDir.exists()) {
            jdlDir.mkdirs();
        }

        this.jdlName = scriptName.substring(0, scriptName.lastIndexOf(".")) + ".jdl";
        writeToFile(sb.toString(), Constants.JDL_ROOT + File.separator + this.jdlName);
    }

    /**
     * Generate execution script which can be executed on specified DIC
     */
    private void generateScriptFile() {

        StringBuilder script = new StringBuilder();

        scriptName = this.applicationName.replace(" ", "-");
        scriptName += "-" + System.nanoTime() + ".sh";

        File scriptsDir = new File(Constants.SCRIPT_ROOT);
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs();
        }

        writeToFile(script.toString(), Constants.SCRIPT_ROOT + File.separator + scriptName);

    }
    
    /**
     * Write a string to file
     * @param contents contents to be stored in file
     * @param filePath file path where contents is stored
     */
    private void writeToFile(String contents, String filePath) {

        try {
            FileWriter fstream = new FileWriter(filePath);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(contents);
            out.close();

        } catch (java.io.IOException ex) {
            log.error("Write to file error: " + ex.getMessage());
        }
    }
    
    /**
     * Get a user proxy from ProxyManager and verify its validity.
     * Depending on each DCI, user proxy could be a raw proxy or includes VOMS extension
     * @return a valid user proxy
     * @throws grool.proxy.ProxyInitializationException in case failed initialization
     * @throws grool.proxy.VOMSExtensionException in case failed append VOMS extension
     */
    protected abstract Proxy getUserProxy()
            throws
            grool.proxy.ProxyInitializationException,
            grool.proxy.VOMSExtensionException;
}
