package unitas.proxy;

import grool.access.GridUserCredentials;
import grool.proxy.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ProxyManager is used to store all user proxies that have been created. When a
 * user proxy associated with a user credential is created, it will be added to
 * the proxy map for later use.
 *
 * @author tram
 */
public class ProxyManager {

    private static ProxyManager instance;
    // proxy map
    private Map<GridUserCredentials, Proxy> proxyMap;

    /**
     * Get an instance of ProxyManager.
     *
     * @return Instance of ProxyManager
     */
    public synchronized static ProxyManager getInstance() {
        if (instance == null) {
            instance = new ProxyManager();
        }
        return instance;
    }

    private ProxyManager() {
        proxyMap = new HashMap<GridUserCredentials, Proxy>();
    }

    /**
     * Getting the user proxy from the proxy map
     *
     * @param userCredentials user credentials used for searching
     * @return a user proxy
     */
    public Proxy getUserProxy(GridUserCredentials userCredentials) {

        for (Entry<GridUserCredentials, Proxy> entry : proxyMap.entrySet()) {
            GridUserCredentials credentials = entry.getKey();
            if (credentials.getLogin().equals(userCredentials.getLogin()) && 
                    userCredentials.getPassword().equals(userCredentials.getPassword()) &&
                    userCredentials.getVirtualOrganization().equals(userCredentials.getVirtualOrganization()) ){
                    return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Add a user proxy to the proxy map for later uses
     *
     * @param userCredentials user credentials
     * @param userProxy user proxy corresponding to this credential
     */
    public synchronized void addUserProxy(GridUserCredentials userCredentials, Proxy userProxy) {

        proxyMap.put(userCredentials, userProxy);
    }
}
