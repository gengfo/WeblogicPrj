package professional.weblogic.ch11.examples.client.jaas;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import weblogic.jndi.Environment;
import weblogic.security.Security;

import professional.weblogic.ch11.examples.client.ejb.*;

public class Example {

	/**
	 * @param args
	 * @throws LoginException 
	 * @throws NamingException 
	 * @throws PrivilegedActionException 
	 */
	public static void main(String[] args) throws LoginException, NamingException, PrivilegedActionException {
		// TODO Auto-generated method stub
		
		System.out.println("Getting Login Configuation from "+System.getProperty("java.security.auth.login.config"));
		
		LoginContext lc = new LoginContext("TestApp",new SampleCallbackHandler(args[0],args[1],args[2]));
		
		lc.login();
		
		Subject subject = lc.getSubject();
		
		
		String result = (String)Security.runAs(subject, new ExampleAction(args[2]));
		
		System.out.println("Result: "+result);
		
		
		
		

	}

}
