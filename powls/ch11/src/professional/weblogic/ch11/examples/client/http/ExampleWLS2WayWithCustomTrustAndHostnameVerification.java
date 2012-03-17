package professional.weblogic.ch11.examples.client.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLContext;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.SSL.TrustManager;



public class ExampleWLS2WayWithCustomTrustAndHostnameVerification {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws javax.security.cert.CertificateException 
	 */
	public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, javax.security.cert.CertificateException {
		// TODO Auto-generated method stub
		
		System.out.println("Connecting to "+args[3]);
		
		SSLContext ctx = SSLContext.getInstance("https");
		
		
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new java.io.FileInputStream(args[0]), null);
		PrivateKey privateKey =
		(PrivateKey)ks.getKey(args[1], args[2].toCharArray());
		Certificate [] certChain = ks.getCertificateChain(args[1]);

		
		//Use the system properties to open the keystore are pull the democa certificate
		String keyStoreFile = System.getProperty("weblogic.security.CustomTrustKeyStoreFileName");
		String keyStorePassword = System.getProperty("weblogic.security.CustomTrustKeyStorePassPhrase");
		
		KeyStore trustKeyStore = KeyStore.getInstance("JKS");
		trustKeyStore.load(new java.io.FileInputStream(keyStoreFile), keyStorePassword
				.toCharArray());
		
		X509Certificate trustedCertificate = (X509Certificate)trustKeyStore.getCertificate("democa");
		
		//The certificate is needed to instantiate the custom trust manager
		TrustManager trustManager = new WLSCustomTrustManager(trustedCertificate);
		
		
		//Create the SSLSocketFactory and configure the client
		//TrustManager, HostnameVerifier and Client Identity
		SSLSocketFactory sslSocketFactory = ctx.getSocketFactoryJSSE();
		
		SSLClientInfo info = new SSLClientInfo();
		
		//Add the custom TrustManager and Hostname Verifier
		info.setHostnameVerifierJSSE(new WLSCustomHostnameVerifier());
		info.setTrustManager(trustManager);
		info.loadLocalIdentity(certChain, privateKey);
		
		sslSocketFactory.setSSLClientInfo(info);
		
		
		URL url = new URL(args[3]);
		weblogic.net.http.HttpsURLConnection sslConn =
		new weblogic.net.http.HttpsURLConnection(url);
		sslConn.setSSLSocketFactory(sslSocketFactory);
		
		
		
		
		
		
		sslConn.connect();
		
		InputStream in = sslConn.getInputStream();
		
		int c;
		
		while ((c=in.read())!=-1) {
			
			System.out.print((char)c);
			
		}
		
		

	}

}
