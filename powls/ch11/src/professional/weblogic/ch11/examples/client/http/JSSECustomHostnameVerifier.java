package professional.weblogic.ch11.examples.client.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

public class JSSECustomHostnameVerifier implements HostnameVerifier{

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		
		System.out.println("=========== JSSECustomHostnameVerifier - verify - start");
		
		try {
			System.out.println(hostname+" matches "+session.getPeerCertificateChain()[0].getSubjectDN());
		} catch (SSLPeerUnverifiedException e) {
			// TODO Auto-generated catch block
			System.out.println("Error verifying hostname "+hostname);
			return false;
		}
		
		System.out.println("=========== JSSECustomHostnameVerifier - verify - end");
		return true;
		
	}

}
