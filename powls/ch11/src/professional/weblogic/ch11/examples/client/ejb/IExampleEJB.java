package professional.weblogic.ch11.examples.client.ejb;

import javax.ejb.Remote;


public interface IExampleEJB {

	//This is a public method
	public String hello(String in);
	
	//This is a secured method
	public String secureHello(String in, int amount);
}
