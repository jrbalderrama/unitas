package unitas.configuration;

/**
 *
 * @author tram
 */
public class Constants {

    private static final String UNITAS_HOME = UnitasConfiguration.getUnitasHomeDir();
    // timeouts used in lcg-c*
    public static int CONNECT_TIMEOUT = 10;
    public static int SEND_RECEIVE_TIMEOUT = 900;
    public static int BDII_TIMEOUT = 10;
    public static int SRM_TIMEOUT = 30;
    // Directories
    public static final String SCRIPT_ROOT = UNITAS_HOME + "/sh";
    public static final String JDL_ROOT = UNITAS_HOME + "/jdl";
    public static final String OUT_ROOT = UNITAS_HOME + "/out";
    public static final String ERR_ROOT = UNITAS_HOME + "/err";
    // Extensions
    public static final String OUT_EXT = ".out";
    public static final String OUT_APP_EXT = ".app" + OUT_EXT;
    public static final String ERR_EXT = ".err";
    public static final String ERR_APP_EXT = ".app" + ERR_EXT;
}
