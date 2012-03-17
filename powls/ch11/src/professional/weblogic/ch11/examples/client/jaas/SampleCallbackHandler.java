package professional.weblogic.ch11.examples.client.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import weblogic.security.auth.callback.URLCallback;

public class SampleCallbackHandler implements CallbackHandler{

	private String name;
	private String password;
	private String url;
	
	public SampleCallbackHandler(String name, String password, String url) {
		super();
		this.name = name;
		this.password = password;
		this.url = url;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		// TODO Auto-generated method stub
		
		for (int i=0; i<callbacks.length; i++) {
			
			if (callbacks[i] instanceof NameCallback) {
				((NameCallback)callbacks[i]).setName(name);
				System.out.println("In CallbackHandler:  Setting Name to "+name);
			} else if (callbacks[i] instanceof PasswordCallback) {
				System.out.println("In CallbackHandler:  Setting Password to ***********");
				((PasswordCallback)callbacks[i]).setPassword(password.toCharArray());
			} else if (callbacks[i] instanceof URLCallback) {
				((URLCallback)callbacks[i]).setURL(url);
				System.out.println("In CallbackHandler: Setting URL to "+url);
			} else {
				throw new UnsupportedCallbackException(callbacks[i]);
			}
			
		}
		
	}

}
