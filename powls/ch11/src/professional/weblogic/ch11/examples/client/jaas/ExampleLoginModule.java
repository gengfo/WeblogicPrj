package professional.weblogic.ch11.examples.client.jaas;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


import weblogic.security.auth.callback.URLCallback;
import weblogic.security.principal.WLSGroupImpl;

public class ExampleLoginModule implements LoginModule{

	private CallbackHandler theCallbackHandler;
	private Subject theSubject;
	
	@Override
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		// TODO Auto-generated method stub
		
		this.theSubject = subject;
		this.theCallbackHandler = callbackHandler;
		
	}

	@Override
	public boolean login() throws LoginException {
		// TODO Auto-generated method stub
		
		PasswordCallback pc = new PasswordCallback("Password",false);
		NameCallback nc = new NameCallback("Name");
		URLCallback uc = new URLCallback("URL");
		
		try {
			theCallbackHandler.handle(new Callback[] {pc,uc,nc});
			
			String username = nc.getName();
			char [] password = pc.getPassword();
			String url = uc.getURL();

			//TODO - Implement some actual custom login logic
			//But Note, in a client side login module, you can't
			//add any principals.  If you comment in the line
			//below, you'll get an invalid Subject.  
			
			//this.theSubject.getPrincipals().add(new WLSGroupImpl("Ch09ExampleGroup"));
			System.out.println("Example Login Module: Success.  Now calling WebLogic Server");
			
			weblogic.jndi.Environment env = new weblogic.jndi.Environment();
			env.setProviderUrl(url);
			env.setSecurityPrincipal(username);
			env.setSecurityCredentials(new String(password));
			weblogic.security.auth.Authenticate.authenticate(env, theSubject);
		
			
			
		
			
			return true;
			
		} catch (Exception e) {
			throw new LoginException("Error Logging In: "+e.getMessage());
		}
		
	}

	@Override
	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

}
