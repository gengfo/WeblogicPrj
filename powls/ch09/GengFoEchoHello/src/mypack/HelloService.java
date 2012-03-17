package mypack;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(serviceName = "HelloService", targetNamespace = "http://gengfo/ws/HelloService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class HelloService {

	public String sayHelloTo(String name) {
		System.out.println("sayHelloTo(String name) " + name);
		return "Hello " + name;
	}
}
