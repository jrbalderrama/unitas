package unitas.dirac.executor;

import grool.access.GridUserCredentials;
import grool.proxy.Proxy;
import grool.proxy.myproxy.GlobusMyproxy;
import java.io.BufferedReader;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import unitas.configuration.Constants;
import unitas.execution.DCIExecutor;
import unitas.proxy.ProxyManager;
import unitas.util.io.ProcessMananger;

/**
 *
 *
 * @author tram
 */
public final class DiracExecutor extends DCIExecutor {

    private static final Logger log = Logger.getLogger(DiracExecutor.class);

    public DiracExecutor() {
        super(true);

    }

    @Override
    public void submit(String command, URI location, URI path)
            throws
            unitas.exception.UnitasException {

        List<String> commands = new ArrayList<String>();

        commands.add("dirac-wms-job-submit");
        commands.add(Constants.JDL_ROOT + File.separator + this.jdlName);
        try {
            Process process = ProcessMananger.getProcess(getUserProxy(), commands.toArray(new String[]{}));

            BufferedReader br = ProcessMananger.getBufferedReader(process);
            String cout = "";
            String s;
            while ((s = br.readLine()) != null) {
                cout += s + "\n";
                String jobID = s.substring(s.lastIndexOf("=")
                        + 2, s.length()).trim();

                // TODO: add job to monitoring

                log.info("Dirac Executor Job ID: " + jobID + " for " + this.applicationName);

            }
            process.waitFor();
            br.close();

            if (process.exitValue() != 0) {
                log.error(cout);
            }
        } catch (java.io.IOException ex) {
            log.error("Unable to submit job: " + ex.getMessage());
        } catch (java.lang.InterruptedException ex) {
            log.error("Unable to submit job: " + ex.getMessage());
        } catch (grool.proxy.ProxyInitializationException ex) {
            log.error("Unable to init proxy for submitting job: " + ex.getMessage());
        } catch (grool.proxy.VOMSExtensionException ex) {
            log.error("Unable to init proxy for submitting job: " + ex.getMessage());
        }

    }

    @Override
    protected final Proxy getUserProxy()
            throws
            grool.proxy.ProxyInitializationException,
            grool.proxy.VOMSExtensionException {

        Proxy userProxy = null;
        boolean exists = true;
        boolean isValid = true;
        if (credentials != null) {

            // get user proxy from ProxyManager
            userProxy = ProxyManager.getInstance().getUserProxy((GridUserCredentials) this.credentials);
            // if user proxy does not exist in ProxyManager, create a new user proxy 
            if (userProxy == null) {

                userProxy = new GlobusMyproxy((GridUserCredentials) credentials);
                // add this new user proxy to proxy map
                ProxyManager.getInstance().addUserProxy((GridUserCredentials) this.credentials, userProxy);
                exists = false;
                log.info("Proxy is not found. Downloading a new proxy from myProxy server...");
            } else {
                
                if (!userProxy.isRawProxyValid()) {
                    isValid = false;
                    log.info("Proxy has expired. Downloading a new proxy from myProxy server...");
                }
            }
        } else {

            String proxyPath = System.getenv("X509_USER_PROXY");
            if (proxyPath != null && !proxyPath.isEmpty()) {
                userProxy = new GlobusMyproxy(new File(proxyPath));
            }
        }

        if (!exists || !isValid) {
            userProxy.initRawProxy();
        }

        return userProxy;
    }
}
