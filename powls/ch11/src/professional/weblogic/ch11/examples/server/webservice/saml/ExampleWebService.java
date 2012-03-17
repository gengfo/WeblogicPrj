package professional.weblogic.ch11.examples.server.webservice.saml;



import javax.jws.WebMethod;
import javax.jws.WebService;



import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.security.Security;

@WebService()
@Policies(
		{@Policy(uri="policy:Wssp1.2-2007-Saml2.0-SenderVouches-Wss1.1-Asymmetric.xml"),
		 @Policy(uri="policy:Wssp1.2-2007-EncryptBody.xml")	
		})
public class ExampleWebService {
	
	@WebMethod()
	public String hello(String in) {
		
		
		try {
			
			return "Hello "+Security.getCurrentSubject()+" in="+in;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
