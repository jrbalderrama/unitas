package unitas.configuration;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author tram
 */
public class UnitasConfiguration {
    private static final Logger log = Logger.getLogger(UnitasConfiguration.class);
    private static final String META_CONF = "/META-INF/unitas-config.xml";
    public static final String UNITAS_HOME = "UNITAS_HOME";
    private static final String UNITAS_PROPERTIES = "UNITAS_PROPERTIES";
    private static final String unitasrc = ".unitasrc";
    private static volatile UnitasConfiguration unitasConfiguration;
    private CompositeConfiguration configuration;

    static String getUnitasHomeDir(){
        return System.getenv(UNITAS_HOME);
    }
    
    static Configuration getConfiguration() {

        UnitasConfiguration _configuration = unitasConfiguration;

        if (_configuration == null) {
            _configuration = createConfiguration();
        }

        return _configuration.configuration;
    }

    static Configuration getConfiguration(File file) {

        UnitasConfiguration _configuration = unitasConfiguration;

        if (_configuration == null) {
            _configuration = createConfiguration(file);
        }

        return _configuration.configuration;
    }

    private static synchronized UnitasConfiguration createConfiguration() {

        return createConfiguration(null);
    }

    private static synchronized UnitasConfiguration createConfiguration(File file) {

        if (unitasConfiguration == null) {
            unitasConfiguration = new UnitasConfiguration(null, file);
        }

        return unitasConfiguration;
    }

    private UnitasConfiguration(String initialCofigurationName, File override) {

        log.info("Initializing unitas configuration.");
        configuration = new CompositeConfiguration();

        try {

            String configurationName = (initialCofigurationName == null) ? META_CONF : initialCofigurationName;
            DefaultConfigurationBuilder factory = new DefaultConfigurationBuilder(UnitasConfiguration.class.getResource(configurationName));
            Configuration initial = factory.getConfiguration();
            if (override != null) {

                Configuration overrideConfiguration = new PropertiesConfiguration(override);
                configuration.addConfiguration(overrideConfiguration);
            } else {

                String path = null;
                path = getUnitasHomeDir();
                if (path == null) {
                    path = System.getProperty("user.home", System.getenv("HOME"));
                } else {
                    log.info("Using environment variable to get directory where properties file is located.");
                    log.info("UNITAS_HOME=" + path);
                }

                File home = null;
                if (path != null) {

                    home = new File(path);
                    log.info("Using user's home to get properties file.");
                } else {
                    log.warn("UNITAS_HOME environment variable nor user's home were not resolved.");
                }

                if (home != null) {
                    if (home.exists()) {
                        if (home.isDirectory()) {

                            String properties = System.getenv(UNITAS_PROPERTIES);
                            if (properties == null) {
                                properties = unitasrc;
                            } else {
                                log.info("Using environment variable to set properties file name.");
                                log.info("UNITAS_PROPERTIES=" + properties);
                            }

                            override = new File(home, properties);
                            if (override.exists() && override.isFile() && override.canRead()) {

                                Configuration overrideConfiguration = new PropertiesConfiguration(override);
                                configuration.addConfiguration(overrideConfiguration);
                                log.info("Overriding default properties with " + override.getAbsolutePath());
                            }
                        }
                    }
                }
            }

            configuration.addConfiguration(initial);
            log.info("Initializing (remaining unset) properties with default values.");
        } catch (org.apache.commons.configuration.ConfigurationException ex) {

            configuration = null;
            log.error("Configuration initialization exception, setting to null the unitas configuration!");
            throw new IllegalStateException(ex);
        }

        Iterator iterator = configuration.getKeys();
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            Object value = configuration.getProperty(key);
            if (key.startsWith("unitas")) {
                log.debug("    " + key + "=" + value.toString());
            } else if (log.isTraceEnabled()) {
                log.trace("    " + key + "=" + value.toString());
            }
        }
    }
}
