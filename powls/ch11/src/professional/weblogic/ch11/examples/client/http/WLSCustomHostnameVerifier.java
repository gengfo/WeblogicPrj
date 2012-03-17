package professional.weblogic.ch11.examples.client.http;

public class WLSCustomHostnameVerifier implements weblogic.security.SSL.HostnameVerifierJSSE{

	@Override
	public boolean verify(String arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("============ WLSCustomHostnameVerifier ===========");
		System.out.println("Verifying "+arg0+" matches "+arg1);
		System.out.println("============ WLSCustomHostnameVerifier ===========");
		return true;
	}

}
