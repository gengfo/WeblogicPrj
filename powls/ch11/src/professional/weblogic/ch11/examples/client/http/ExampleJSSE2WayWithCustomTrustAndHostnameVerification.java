package professional.weblogic.ch11.examples.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class ExampleJSSE2WayWithCustomTrustAndHostnameVerification {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws NoSuchProviderException 
	 * @throws KeyManagementException 
	 */
	public static void main(String[] args) throws IOException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, NoSuchProviderException,
			KeyManagementException {
		// TODO Auto-generated method stub

		System.out.println("Connecting to " + args[4]);

		KeyStore identityKeyStore = KeyStore.getInstance("JKS");
		identityKeyStore.load(new java.io.FileInputStream(args[0]), null);

		KeyStore trustKeyStore = KeyStore.getInstance("JKS");
		trustKeyStore.load(new java.io.FileInputStream(args[2]), args[3]
				.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509",
				"SunJSSE");
		kmf.init(identityKeyStore, args[1].toCharArray());
		
		//Get the democa from the trust-store and create the demo TrustManager
		X509Certificate trustedCertificate = (X509Certificate)trustKeyStore.getCertificate("democa");
		TrustManager tm = new JSSECustomTrustManager(trustedCertificate);
		SSLContext ctx = SSLContext.getInstance("SSL");
		
		
		//Passing the TrustManager to the init method of the SSLContext
		ctx.init(kmf.getKeyManagers(), new TrustManager[] {tm}, null);
		
		
		SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
	
		
		URL url = new URL(args[4]);

		HttpsURLConnection sslConn = (HttpsURLConnection) url.openConnection();
		sslConn.setSSLSocketFactory(sslSocketFactory);
		
		//Set the Hostname Verifier
		sslConn.setHostnameVerifier(new JSSECustomHostnameVerifier());
		
		sslConn.connect();

		InputStream in = sslConn.getInputStream();

		int c;

		while ((c = in.read()) != -1) {

			System.out.print((char) c);

		}

	}

}
