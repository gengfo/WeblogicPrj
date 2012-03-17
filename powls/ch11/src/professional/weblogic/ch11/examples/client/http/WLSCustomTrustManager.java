package professional.weblogic.ch11.examples.client.http;

import java.security.cert.X509Certificate;

import weblogic.security.SSL.TrustManagerJSSE;

/**
 * 
 * The TrustManagerJSSE interface is depricated, but I implemented it just for
 * completeness.  You should use the weblogic.security.SSL.TrustManager interface.
 */
public class WLSCustomTrustManager implements weblogic.security.SSL.TrustManager, TrustManagerJSSE{

	private X509Certificate trustedCertificate;
	public WLSCustomTrustManager(X509Certificate trustedCertificate) {
		super();
		this.trustedCertificate = trustedCertificate;
	}
	
	@Override
	public boolean certificateCallback(
			java.security.cert.X509Certificate[] certChain, int arg1) {
		// TODO Auto-generated method stub
		
		System.out
		.println("============= WLSCustomTrustManager - start - certificateCallback ==============");
		
		for (int i=0; i<certChain.length; i++) {
			
			if (!certChain[i].getIssuerX500Principal().equals(this.trustedCertificate.getSubjectX500Principal())) {
				System.out.println("Invalid Certificate: "+certChain[i].getSubjectDN()+"issued by "+certChain[i].getIssuerDN()+" was not issued by "+this.trustedCertificate.getIssuerDN());
				return false;
			} else {
				System.out.println(certChain[i].getSubjectDN() + " is trusted");
			}
			
		}
		
		System.out
		.println("============= WLSCustomTrustManager - end - certificateCallback ==============");
		
		return true;


	}

	@Override
	public boolean certificateCallback(
			javax.security.cert.X509Certificate[] certChain, int arg1) {
		System.out
		.println("============= WLSCustomTrustManager - start - certificateCallback ==============");
		
		for (int i=0; i<certChain.length; i++) {
			
			if (!certChain[i].getIssuerDN().equals(this.trustedCertificate.getSubjectDN())) {
				System.out.println("Invalid Certificate: "+certChain[i].getSubjectDN()+"issued by "+certChain[i].getIssuerDN()+" was not issued by "+this.trustedCertificate.getIssuerDN());
				return false;
			} else {
				System.out.println(certChain[i].getSubjectDN() + " is trusted");
			}
			
		}
		
		System.out
		.println("============= WLSCustomTrustManager - end - certificateCallback ==============");
		
		return true;
	}
	
	

}
