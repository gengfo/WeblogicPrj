package professional.weblogic.ch11.examples.client.rmi;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import professional.weblogic.ch11.examples.client.ejb.IExampleEJB;

public class ExampleWLSWithIdentity {

	/**
	 * @param args
	 * @throws NamingException
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 */
	public static void main(String[] args) throws NamingException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		
	
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new java.io.FileInputStream(args[0]), null);
		PrivateKey privateKey =
		(PrivateKey)ks.getKey(args[1], args[2].toCharArray());
		Certificate [] certChain = ks.getCertificateChain(args[1]);
		
		weblogic.jndi.Environment env = new weblogic.jndi.Environment();
		
		//Special Handling for client authentication
		//
		//weblogic.jndi.Environment uses one method - loadLocalIdentity - for the transport
		//and another - setSSLClientCertificate - for authentication at the client
		//This covers the case where you need to have 2-way SSL but want to use
		//username and password for authentication.  It doesn't assume that you're
		//user the same certificate
		java.io.InputStream [] certStreams = new java.io.InputStream [certChain.length];
		
		for (int i=0; i<certChain.length; i++) {
			
			certStreams[i] = new ByteArrayInputStream(certChain[i].getEncoded());
			
		}
		//Set the certificate and the password
		env.setSSLClientCertificate(certStreams);
		env.setSSLClientKeyPassword(args[2]);
		
		
		
		env.setProviderUrl(args[3]);
		env.loadLocalIdentity(certChain, privateKey);
		
		
		//This is a bit tricky, but using the keytool
		//C:\powls\ch09>keytool -printcert -file "c:\Oracle\Middleware\rc7\wlserver_10.3\server\lib\CertGenCA.der"
		
		//Owner: CN=CertGenCAB, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US
		//Issuer: CN=CertGenCAB, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US
		//Serial number: 234b5559d1fa0f3ff5c82bdfed032a87
		//Valid from: Thu Oct 24 11:54:45 EDT 2002 until: Tue Oct 25 11:54:45 EDT 2022
		//Certificate fingerprints:
		//  =====> MD5:  A2:18:4C:E0:1C:AB:82:A7:65:86:86:03:D0:B3:D8:FE
		//         SHA1: F8:5D:49:A4:12:54:78:C7:BA:42:A7:14:3E:06:F5:1E:A0:D4:C6:59
		//         Signature algorithm name: MD5withRSA
		//         Version: 3

		//Extensions:

		//#1: ObjectId: 2.5.29.15 Criticality=true
		//KeyUsage [
		//  Key_CertSign
		//]

		//#2: ObjectId: 2.5.29.19 Criticality=true
		//BasicConstraints:[
		//  CA:true
		//  PathLen:1
		//]
		
		//From above, just remove the :
		//MD5:  A2:18:4C:E0:1C:AB:82:A7:65:86:86:03:D0:B3:D8:FE
		//becomes A2184CE01CAB82A765868603D0B3D8FE
		env.setSSLRootCAFingerprints("A2184CE01CAB82A765868603D0B3D8FE");
		
		
		
		Context ctx = env.getContext();
		
				
		Object obj = ctx.lookup("ch11.example.ejb#professional.weblogic.ch11.examples.client.ejb.IExampleEJB");
		
		
		IExampleEJB ejb = (IExampleEJB)PortableRemoteObject.narrow(obj, IExampleEJB.class);
		
		System.out.println(ejb.hello("RMI+WLS Client"));
		

	}

}
