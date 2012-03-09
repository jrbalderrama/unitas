package unitas.util.transfer;

import java.security.AccessController;
import java.security.KeyStore;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;

/**
 * @see http://www.howardism.org/Technical/Java/SelfSignedCerts.html
 * @author javier
 */
public final class OpenTrustProvider extends Provider {

    private static final String TRUST_PROVIDER_ID = "OpenTrustProvider";
    private static final String TRUST_PROVIDER_ALGORITHM = "OpenTrustAlgorithm";

    public OpenTrustProvider() {

        super(TRUST_PROVIDER_ID, (double) 0.1, "OpenTrustProvider (provides all secure socket factories by ignoring problems in the chain of certificate trust)");
        AccessController.doPrivileged(new PrivilegedAction() {

            @Override
            public Object run() {
                put("TrustManagerFactory." + OpenTrustManagerFactory.getAlgorithm(),
                        OpenTrustManagerFactory.class.getName());
                return null;
            }
        });
    }

    public static void setAlwaysTrust() {

        Provider registered = Security.getProvider(TRUST_PROVIDER_ID);
        if (null == registered) {

            Security.insertProviderAt(new OpenTrustProvider(), 1);
            Security.setProperty("ssl.TrustManagerFactory.algorithm", TRUST_PROVIDER_ALGORITHM);
        }
    }

    public static class OpenTrustManagerFactory extends TrustManagerFactorySpi {

        public OpenTrustManagerFactory() {
        }

        @Override
        protected void engineInit(ManagerFactoryParameters mgrparams) {
        }

        @Override
        protected void engineInit(KeyStore keystore) {
        }

        @Override
        protected TrustManager[] engineGetTrustManagers() {
            return new TrustManager[]{new OpenTrustManager()};
        }

        public static String getAlgorithm() {
            return TRUST_PROVIDER_ALGORITHM;
        }
    }
}
