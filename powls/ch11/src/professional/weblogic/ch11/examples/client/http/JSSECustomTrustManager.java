package professional.weblogic.ch11.examples.client.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class JSSECustomTrustManager implements javax.net.ssl.X509TrustManager {

	private X509Certificate trustedCertificate;

	public JSSECustomTrustManager(X509Certificate trustedCertificate) {
		super();
		this.trustedCertificate = trustedCertificate;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] certChain, String arg1)
			throws CertificateException {

		System.out
				.println("============= JSEECustomTrustManager - start - checkClientTrusted ==============");

		for (int i = 0; i < certChain.length; i++) {

			if (!certChain[i].getIssuerDN().getName().equals(
					this.trustedCertificate.getIssuerDN().getName())) {
				throw new CertificateException("Invalid Certificate: "
						+ certChain[i].getSubjectDN());
			} else {
				System.out.println(certChain[i].getSubjectDN() + " is trusted");
			}

		}

		System.out
				.println("============= JSEECustomTrustManager - end - checkClientTrusted ==============");

	}

	@Override
	public void checkServerTrusted(X509Certificate[] certChain, String arg1)
			throws CertificateException {

		System.out
				.println("============= JSEECustomTrustManager - start - checkServerTrusted ==============");
		for (int i = 0; i < certChain.length; i++) {

			if (!certChain[i].getIssuerDN().getName().equals(
					this.trustedCertificate.getIssuerDN().getName())) {
				throw new CertificateException("Invalid Certificate: "
						+ certChain[i].getSubjectDN());
			}else {
				System.out.println(certChain[i].getSubjectDN() + " is trusted");
			}

		}
		System.out
				.println("============= JSEECustomTrustManager - end - checkServerTrusted ==============");

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return new X509Certificate[] { trustedCertificate };
	}

}
