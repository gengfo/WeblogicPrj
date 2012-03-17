package professional.weblogic.ch11.examples.client.http;

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

import weblogic.security.SSL.SSLContext;
import weblogic.security.SSL.SSLSocketFactory;

public class ExampleWLS2Way {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 */
	public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		// TODO Auto-generated method stub
		
		System.out.println("Connecting to "+args[3]);
		
		SSLContext ctx = SSLContext.getInstance("https");
		
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new java.io.FileInputStream(args[0]), null);
		PrivateKey privateKey =
		(PrivateKey)ks.getKey(args[1], args[2].toCharArray());
		Certificate [] certChain = ks.getCertificateChain(args[1]);
		
		ctx.loadLocalIdentity(certChain, privateKey);
		SSLSocketFactory sslSocketFactory = ctx.getSocketFactoryJSSE();
		
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
