package professional.weblogic.ch11.examples.server.ejb;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;

import professional.weblogic.ch11.examples.client.ejb.*;
import weblogic.javaee.AllowRemoveDuringTransaction;
import weblogic.javaee.EJBReference;
import weblogic.security.Security;

import javax.annotation.security.*;

@Stateless(mappedName="ch11.example.ejb")
@Remote(professional.weblogic.ch11.examples.client.ejb.IExampleEJB.class)
@DeclareRoles({"SecureUsers"})
public class ExampleEJB implements IExampleEJB {

	
	@RolesAllowed({"SecureUsers"})
	public String secureHello(String in, int amount) {
		// TODO Auto-generated method stub
		return "Hello "+in+" - from "+Security.getCurrentSubject()+" with amount="+amount;
	}

	public String hello(String in) {
		return "Hello "+in+" - from "+Security.getCurrentSubject();
	}
}
