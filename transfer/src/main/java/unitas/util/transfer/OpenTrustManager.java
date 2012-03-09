package unitas.util.transfer;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;

/**
 *
 * @author javier
 */
public class OpenTrustManager implements X509TrustManager {

    private static final Logger log = Logger.getLogger(OpenTrustManager.class);
    private static SSLSocketFactory sslSocketFactory;

    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string)
            throws
            java.security.cert.CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string)
            throws
            java.security.cert.CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }    

    public static SSLSocketFactory getSocketFactory() {

        if (sslSocketFactory == null) {
            try {

                TrustManager[] manager = new TrustManager[]{new OpenTrustManager()};
                SSLContext context = SSLContext.getInstance("SSL");
                context.init(new KeyManager[0], manager, new SecureRandom());
                sslSocketFactory = (SSLSocketFactory) context.getSocketFactory();
            } catch (java.security.KeyManagementException ex) {
                log.warn(ex);
            } catch (java.security.NoSuchAlgorithmException ex) {
                log.warn(ex);
            }
        }

        return sslSocketFactory;
    }

    @Deprecated
    public static void acceptSelfSignedCertificates()
            throws
            java.security.KeyManagementException,
            java.security.NoSuchAlgorithmException {

        //System.getProperties().put("javax.net.debug", "ssl");

        // Create a trust manager that does not validate certificate chains
        TrustManager[] manager = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] xrc, String arg1)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
        };

        // Create all-trusting host name verifier
//        HostnameVerifier verifier = new HostnameVerifier() {
//
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };

        // Install the all-trusting trust manager
        SSLContext context = SSLContext.getInstance("SSL");//TLS
        context.init(null, manager, new java.security.SecureRandom());
        //HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        //HttpsURLConnection.setDefaultHostnameVerifier(verifier);
        // HttpsURLConnection.setDefaultAllowUserInteraction(false);
    }
}
