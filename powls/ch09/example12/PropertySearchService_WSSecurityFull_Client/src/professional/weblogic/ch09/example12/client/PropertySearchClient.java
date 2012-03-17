package professional.weblogic.ch09.example12.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;

import professional.weblogic.ch09.example12.property.PropertyInfo;
import professional.weblogic.ch09.example12.property.PropertySearchAddress;
import professional.weblogic.ch09.example12.property.PropertySearchService;
import professional.weblogic.ch09.example12.property.PropertySearchService_Service;

import weblogic.security.SSL.TrustManager;
import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class PropertySearchClient {
	private final static String KEYSTORE_TYPE = "JKS";
	private final static String CLIENT_KEYSTORE_PATH = "ClientIdentity.jks";
	private final static String CLIENT_KEYSTORE_PASSWD = "ClientStorePass";
	private final static String CLIENT_KEY_ALIAS = "clientcert";
	private final static String CLIENT_KEY_PASSWD = "ClientKeyPass";
	private final static String SERVERCERT_KEY_ALIAS = "servercert";	
	private final static String TRUST_KEYSTORE_RELPATH = File.separator + "server" + File.separator + "lib" + File.separator + "DemoTrust.jks";
	private final static String TRUST_KEYSTORE_PASSWD = "DemoTrustKeyStorePassPhrase";
	private final static String RSA_AUTH_TYPE = "RSA";

	public static void main(String[] args) {
		try {
			if ((args.length <= 0) || (args[0].trim().length() <= 0)) {
				System.err.println("WLS Home Path argument must be passed to main");
				return;		
			}

			new PropertySearchClient(args[0].trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertySearchClient(String wlsHomePath) throws Exception {
		// Construct request
		PropertySearchAddress searchAddress = new PropertySearchAddress();
		searchAddress.setAddress1("1 High Street");
		searchAddress.setPostalCode("BT1234");

		// Obtain handle on web service stub 
		PropertySearchService_Service service = new PropertySearchService_Service();
		PropertySearchService port = service.getPropertySearchServicePort();
		Map<String, Object> rc = ((BindingProvider) port).getRequestContext();

		// Load the server certificate from local keystore
		KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		keyStore.load(new FileInputStream(new File(CLIENT_KEYSTORE_PATH)), CLIENT_KEYSTORE_PASSWD.toCharArray());
		X509Certificate serverCert = (X509Certificate) keyStore.getCertificate(SERVERCERT_KEY_ALIAS);
		serverCert.checkValidity();  		
		
		// Set-up credential provider using client key and server certificate from the local keystore
		List<CredentialProvider> credProviders = new ArrayList<CredentialProvider>();
		CredentialProvider bstCP = new ClientBSTCredentialProvider(CLIENT_KEYSTORE_PATH, 
				CLIENT_KEYSTORE_PASSWD, CLIENT_KEY_ALIAS, CLIENT_KEY_PASSWD, KEYSTORE_TYPE, serverCert); 
		credProviders.add(bstCP);
		rc.put(WSSecurityContext.CREDENTIAL_PROVIDER_LIST, credProviders);
		
		// Provide a Trust Manager which will check any certificates against the WebLogic Demo Trust Key Store
		KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);				
		trustStore.load(new FileInputStream(new File(wlsHomePath + TRUST_KEYSTORE_RELPATH)), TRUST_KEYSTORE_PASSWD.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);		
		final X509TrustManager tm = (X509TrustManager) tmf.getTrustManagers()[0];		
		rc.put(WSSecurityContext.TRUST_MANAGER, new TrustManager() {
			public boolean certificateCallback(X509Certificate[] chain, int validateErr) {
				try {
					tm.checkServerTrusted(chain, RSA_AUTH_TYPE);
				} catch (CertificateException e) {
					e.printStackTrace();
					return false;
				}
		       
				return true;
			}
		});
		
		// Invoke Web Service
		//rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8001/PropertySearchService_WSSecCertificate/PropertySearchService");
		PropertyInfo property = port.getPropertyDetailsByAddress(searchAddress);

		// Process response
		if (property != null) {
			int id = property.getId();
			String city = property.getCity();		
			System.out.println("Found Property: Id=" + id + ", City=" + city);
		} else {
			System.out.println("No Property found");
		}
	}
}
