package unitas.generator.jdl;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import unitas.configuration.Constants;

/**
 *
 * @author tram
 */
public class JDLGenerator {

    /**
     * Generate a string under jdl format used to submit job to DCI
     *
     * @param scriptName name of executable to be executed on DCI
     * @param jobAttributes job attributes, for instance job requirements
     * @return resulting string
     */
    public static String generateJDL(String scriptName, Map<String, String> jobAttributes) {

        StringBuilder sb = new StringBuilder();
        String scriptPath = new File(Constants.SCRIPT_ROOT).getAbsolutePath();

        sb.append("Executable\t= \"").append(scriptName).append("\";\n");
        sb.append("StdOutput\t= \"std.out\";\n");
        sb.append("StdError\t= \"std.err\";\n");
        sb.append("InputSandbox\t= {\"").append(scriptPath).append(File.separator).append(scriptName).append("\"};\n");
        sb.append("OutputSandbox\t= {\"std.out\", \"std.err\"};\n");

        for (Entry<String, String> entry : jobAttributes.entrySet()) {
            sb.append(entry.getKey()).append("\t= \n\t{\n").
                    append(entry.getValue()).
                    append("\t\n\t};\n");
        }

        return sb.toString();
    }
}
