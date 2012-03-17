package professional.weblogic.ch11.examples.server.servlet.saml;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;


import professional.weblogic.ch11.examples.client.webservice.saml.*;


public class SAMLClient extends HttpServlet{

	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		//ExampleWebServiceService service = new ExampleWebServiceService();
		//ExampleWebService exampleWebService = service.getExampleWebServicePort();
		System.out.println("Calling SAML");
		//response.getWriter().println(exampleWebService.hello("From SAML Client"));
		response.getWriter().println("Test");
		
		
	}

	
	
}
