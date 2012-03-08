package unitas.proxy;

import grool.access.UserCredentials;
import grool.proxy.AbstractProxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tram
 */
public class ProxyManager {
    
    // proxy map
    private static Map<UserCredentials, AbstractProxy> proxyMap = new HashMap<UserCredentials, AbstractProxy>();
    
    /**
     * Getting the user proxy from the proxy map
     * @param userCredentials user credentials used for searching
     * @return a user proxy
     */
    public static AbstractProxy getUserProxy(UserCredentials userCredentials) {
        
        return proxyMap.get(userCredentials);
    }
    
    /**
     * Add a user proxy to the proxy map for the later uses
     * @param userCredentials user credentials
     * @param userProxy user proxy corresponding to this credential
     */
    public static void addUserProxy(UserCredentials userCredentials, AbstractProxy userProxy){
        
        proxyMap.put(userCredentials, userProxy);
    }
}
