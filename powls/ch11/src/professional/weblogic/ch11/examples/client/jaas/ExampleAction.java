package professional.weblogic.ch11.examples.client.jaas;

import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import professional.weblogic.ch11.examples.client.ejb.IExampleEJB;
import weblogic.jndi.Environment;

public class ExampleAction implements PrivilegedExceptionAction<String>{

	private String url;
	
	public ExampleAction(String url) {
		super();
		this.url = url;
	}

	@Override
	public String run() throws Exception {
		
		Hashtable ht = new Hashtable();
		ht.put(InitialContext.PROVIDER_URL, url);
		ht.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		
		Environment env = new Environment(ht);
		
		Context ic = env.getInitialContext();
		
		
		Object obj = ic.lookup("ch11.example.ejb#professional.weblogic.ch11.examples.client.ejb.IExampleEJB");
		
		
		IExampleEJB ejb = (IExampleEJB)PortableRemoteObject.narrow(obj, IExampleEJB.class);
		
		return ejb.hello("JAAS Client");
		
	}

}
