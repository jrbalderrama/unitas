package unitas.generator.script;

import unitas.configuration.Constants;
import unitas.configuration.UnitasDefaultValues;
import unitas.execution.ApplicationContainer;
import unitas.execution.Context;
import unitas.execution.ImplementationContainer;

/**
 *
 * Generate script for executing on a specific infrastructure
 *
 * @author tram
 */
public class ScriptGenerator {

    private static ScriptGenerator instance;

    /**
     * Get an instance of ScriptGenerator.
     *
     * @return Instance of ScriptGenerator
     */
    public synchronized static ScriptGenerator getInstance() {
        if (instance == null) {
            instance = new ScriptGenerator();
        }
        return instance;
    }

    private ScriptGenerator() {
    }

    /**
     * Generate execution script corresponding the execution context
     *
     * @param context execution context
     * @param implContainer implementation container contains all information
     * concerning this execution context
     * @param appContainer application container contains all inputs and
     * parameters associated to this execution
     * @return script string
     */
    public String generateScript(Context context, ImplementationContainer implContainer, ApplicationContainer appContainer) {

        StringBuilder script = new StringBuilder();

        script.append(interpreter());

        // all functions
        script.append(cleanupFunction());
        script.append(logFunctions());
        script.append(startLogFunction());
        script.append(stopLogFunction());
        script.append(downloadLFNFunction());
        script.append(uploadLFNFunction());
        script.append(getDeleteLFNFunction());

        // main
        script.append(header());
//        sb.append(uploadTest(uploads, regexs, defaultDir));
//        sb.append(inputs(release, downloads));
//        sb.append(applicationEnvironment(release));
//        sb.append(applicationExecution(parameters));
//        sb.append(resultsUpload(uploads, regexs, defaultDir));
//        sb.append(footer());



        return script.toString();
    }

    /**
     * Return the interpreter of bash
     *
     * @return interpreter of bash
     */
    private String interpreter() {

        return "#!/bin/bash -l\n\n";
    }

    /**
     * Returns the code of the cleanup function
     *
     * @return a string containing the code
     */
    private static String cleanupFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("function cleanup\n{\n");
        sb.append("startLog cleanup");
        sb.append("info \"=== ls -a ===\" \n");
        sb.append("ls -a \n");
        sb.append("info \"Cleaning up: rm * -Rf\"\n");
        sb.append("\\rm * -Rf \n");
        sb.append("info \"END date:\"\n");
        sb.append("date +%s\n");
        sb.append("stopLog cleanup");
        sb.append("\n}\n\n");

        return sb.toString();
    }

    /**
     * Generates the code of the log functions
     *
     * @return a String containing the code
     */
    private static String logFunctions() {

        StringBuilder sb = new StringBuilder();
        sb.append("function info {\n local D=`date`\n echo [ INFO - $D ] $*\n}\n");
        sb.append("function warning {\n local D=`date`\n echo [ WARN - $D ] $*\n}\n");
        sb.append("function error {\n local D=`date`\n echo [ ERROR - $D ] $* >&2\n}\n");
        return sb.toString();
    }

    /**
     * Starts a log section
     *
     * @return a String containing the bash function code
     */
    private static String startLogFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("function startLog {\n echo \"<$*>\" >&1; \n echo \"<$*>\" >&2;\n}\n");
        return sb.toString();
    }

    /**
     * Stops a log section
     *
     * @return string containing a bash function for log
     */
    private static String stopLogFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("function stopLog {\n local logName=$1\n echo \"</${logName}>\" >&1\n echo \"</${logName}>\" >&2\n}\n");
        return sb.toString();
    }

    private static String downloadLFNFunction() {

        StringBuilder sb = new StringBuilder();

        sb.append("function downloadLFN {\n");
        sb.append("\t local LFN=$1\n");
        sb.append("\t local LOCAL=${PWD}/`basename ${LFN}`\n");
        sb.append("\t info \"Removing file ${LOCAL} in case it is already here\"\n");
        sb.append("\t \\rm -f ${LOCAL}\n");
        sb.append("\t info \"Downloading file ${LFN}...\"\n");
        sb.append("\t LINE=\"lcg-cp -v --connect-timeout ").
                append(Constants.CONNECT_TIMEOUT).
                append(" --sendreceive-timeout ").
                append(Constants.SEND_RECEIVE_TIMEOUT).
                append(" --bdii-timeout ").
                append(Constants.BDII_TIMEOUT).
                append(" --srm-timeout ").
                append(Constants.SRM_TIMEOUT).
                append(" lfn:${LFN} file:`pwd`/`basename ${LFN}`\"\n");
        sb.append("\t info ${LINE}\n");
        sb.append("\t ${LINE} &> lcg-log\n");
        sb.append("\t if [ $? = 0 ];\n");
        sb.append("\t then\n");
        sb.append("\t\t info \"lcg-cp worked fine\";\n");
        sb.append("\t else\n");
        sb.append("\t\t error \"lcg-cp failed\"\n");
        sb.append("\t\t error \"`cat lcg-log`\"\n");
        sb.append("\t\t return 1\n");
        sb.append("\t fi\n");
        sb.append("\t \\rm lcg-log \n");
        sb.append("}\n");
        sb.append("export -f downloadLFN\n\n");

        return sb.toString();
    }

    /**
     * Generates a few functions to upload a file to the LFC. If the LFN file
     * needs to be replicated, a number of replicas is required. An error is
     * raised in case the file couldn't be copied at least once.
     *
     * @return A string containing the code
     */
    private static String uploadLFNFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("function nSEs {\n");
        sb.append("\t i=0\n");
        sb.append("\t for n in ${SELIST}\n");
        sb.append("\t do\n");
        sb.append("\t\t i=`expr $i + 1`\n");
        sb.append("\t done\n");
        sb.append("\t return $i\n");
        sb.append("}\n\n");

        sb.append("function getAndRemoveSE {\n");
        sb.append("\t local index=$1\n");
        sb.append("\t local i=0\n");
        sb.append("\t local NSE=\"\"\n");
        sb.append("\t RESULT=\"\"\n");
        sb.append("\t for n in ${SELIST}\n");
        sb.append("\t do\n");
        sb.append("\t\t if [ \"$i\" = \"${index}\" ]\n");
        sb.append("\t\t then\n");
        sb.append("\t\t\t RESULT=$n\n");
        sb.append("\t\t\t info \"result: $RESULT\"\n");
        sb.append("\t\t else\n");
        sb.append("\t\t\t NSE=\"${NSE} $n\"\n");
        sb.append("\t\t fi\n");
        sb.append("\t\t i=`expr $i + 1`\n");
        sb.append("\t done\n");
        sb.append("\t SELIST=${NSE}\n");
        sb.append("\t return 0\n");
        sb.append("}\n\n");

        sb.append("function chooseRandomSE {\n");
        sb.append("  nSEs\n");
        sb.append("  local n=$?\n");
        sb.append("  if [ \"$n\" = \"0\" ]\n");
        sb.append("  then\n");
        sb.append("    info \"SE list is empty\"\n");
        sb.append("    RESULT=\"\"\n");
        sb.append("  else\n");
        sb.append("    local r=${RANDOM}\n");
        sb.append("    local id=`expr $r  % $n`\n");
        sb.append("    getAndRemoveSE ${id}\n");
        sb.append("  fi\n");
        sb.append("}\n\n");

        sb.append("function uploadFile {\n");
        sb.append("  local LFN=$1\n");
        sb.append("  local FILE=$2\n");
        sb.append("  local nrep=$3\n");
        sb.append("  local SELIST=${SE}\n");
        sb.append("  local OPTS=\"--connect-timeout ").
                append(Constants.CONNECT_TIMEOUT).
                append(" --sendreceive-timeout ").
                append(Constants.SEND_RECEIVE_TIMEOUT).
                append(" --bdii-timeout ").
                append(Constants.BDII_TIMEOUT).
                append(" --srm-timeout ").
                append(Constants.SRM_TIMEOUT).
                append("\"\n");
        sb.append("  local DEST=\"\"\n");
        sb.append("  chooseRandomSE\n");
        sb.append("  DEST=${RESULT}\n");
        sb.append("  done=0\n");
        sb.append("  while [ $nrep -gt $done ] && [ \"${DEST}\" != \"\" ]\n");
        sb.append("  do\n");
        sb.append("    if [ \"${done}\" = \"0\" ]\n");
        sb.append("    then\n");
        sb.append("      lcg-del -v -a ${OPTS} lfn:${LFN} &>/dev/null;\n");
        sb.append("      lfc-ls ${LFN};\n");
        sb.append("      if [ \\$? = 0 ];\n");
        sb.append("      then\n");
        sb.append("        lfc-rename ${LFN} ${LFN}-garbage-${HOSTNAME}-${PWD};\n");
        sb.append("      fi;\n");
        sb.append("      lfc-mkdir -p `dirname ${LFN}`;\n");
        sb.append("      lcg-cr -v ${OPTS} -d ${DEST} -l lfn:${LFN} file:${FILE} &> lcg-log\n");
        sb.append("    else\n");
        sb.append("      lcg-rep -v ${OPTS} -d ${DEST} lfn:${LFN} &> lcg-log\n");
        sb.append("    fi\n");
        sb.append("    if [ $? = 0 ]\n");
        sb.append("    then\n");
        sb.append("      info \"lcg-cr/rep of ${LFN} to SE ${DEST} worked fine\"\n");
        sb.append("      done=`expr ${done} + 1`\n");
        sb.append("    else\n");
        sb.append("      error \"`cat lcg-log`\"\n");
        sb.append("      warning \"lcg-cr/rep of ${LFN} to SE ${DEST} failed\" \n");
        sb.append("    fi\n");
        sb.append("    \\rm lcg-log\n");
        sb.append("    chooseRandomSE\n");
        sb.append("    DEST=${RESULT}\n");
        sb.append("  done\n");
        sb.append("  if [ \"${done}\" = \"0\" ]\n");
        sb.append("  then\n");
        sb.append("    error \"Cannot lcg-cr file ${FILE} to lfn ${LFN}\"\n");
        sb.append("    error \"Exiting with return value 2\"\n");
        sb.append("    exit 2\n");
        sb.append("  fi\n");
        sb.append("}\n\n");

        return sb.toString();
    }

    /**
     * Generates the command to delete a file on LFC using low-level commands
     * instead of lcg-del
     *
     * @return String containing the code
     */
    protected static String getDeleteLFNFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("function deleteFile { \n");
        sb.append("\t lcg-del -a $1\n");
        sb.append("\t if [ $? != 0 ]\n");
        sb.append("\t then\n");
        sb.append("\t\t guid=$(lcg-lg $1)\n");
        sb.append("\t\t surls=$(lcg-lr $1) \n");
        sb.append("\t\t for surl in $surls \n");
        sb.append("\t\t do\n");
        sb.append("\t\t\t lcg-uf -v $guid $surl\n");
        sb.append("\t\t done\n");
        sb.append("\tfi\n\n");
        sb.append("}\n\n");
        return sb.toString();
    }
    
    /**
     * Generates the job header (function declarations and variable settings)
     *
     * @return A string containing the header
     */
    private String header() {

        StringBuilder sb = new StringBuilder();
        
        sb.append("startLog header\n");

        sb.append("START=`date +%s`; info \"START date is ${START}\"\n\n");

        // Builds the custom environment
        sb.append("export BASEDIR=${PWD}\n");
        sb.append("export SE=").append(UnitasDefaultValues.getStorageElements()).append("\n");
        // Creates execution directory
        sb.append("DIRNAME=`basename $0 .sh`;\n");
        sb.append("mkdir ${DIRNAME};\n");
        sb.append("if [ $? = 0 ];\n"
                + "then\n"
                + "  echo \"cd ${DIRNAME}\";\n"
                + "  cd ${DIRNAME};\n"
                + "else\n"
                + "  echo \"Unable to create directory ${DIRNAME}\";\n"
                + "  echo \"Exiting with return value 7\"\n"
                + "  exit 7;\n"
                + "fi\n\n");
        sb.append("BACKPID=\"\"\n\n");
        sb.append("stopLog header\n");
        return sb.toString();
    }

}
