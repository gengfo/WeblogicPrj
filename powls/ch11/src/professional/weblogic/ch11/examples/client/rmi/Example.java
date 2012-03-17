package professional.weblogic.ch11.examples.client.rmi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import professional.weblogic.ch11.examples.client.ejb.IExampleEJB;

public class Example {

	/**
	 * @param args
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws NamingException {
		// TODO Auto-generated method stub
		
		Hashtable ht = new Hashtable();
		ht.put(Context.INITIAL_CONTEXT_FACTORY,
		"weblogic.jndi.WLInitialContextFactory");
		ht.put(Context.PROVIDER_URL, args[0]);
		InitialContext ctx = new InitialContext(ht);
		
				
		Object obj = ctx.lookup("ch11.example.ejb#professional.weblogic.ch11.examples.client.ejb.IExampleEJB");
		
		
		IExampleEJB ejb = (IExampleEJB)PortableRemoteObject.narrow(obj, IExampleEJB.class);
		
		System.out.println(ejb.hello("RMI Client"));
		

	}

}
