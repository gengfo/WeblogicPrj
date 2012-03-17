package mypack.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(serviceName = "CheckThreadWebService", targetNamespace = "http://gengfo/ws/CheckThreadWebService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class CheckThreadWebService {

	public void check() {

		System.out.println("Testing EaterThread, hit Ctrl+c to exit.");

		Tool spoon = new Tool("Spoon");
		Tool fork = new Tool("Fork");

		new EaterThread("Alice", spoon, fork).start();
		new EaterThread("Bob", fork, spoon).start();

	}

}
