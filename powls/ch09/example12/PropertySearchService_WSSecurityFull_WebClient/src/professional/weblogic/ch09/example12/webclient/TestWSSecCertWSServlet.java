package professional.weblogic.ch09.example12.webclient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

import professional.weblogic.ch09.example12.property.PropertyInfo;
import professional.weblogic.ch09.example12.property.PropertySearchAddress;
import professional.weblogic.ch09.example12.property.PropertySearchService;
import professional.weblogic.ch09.example12.property.PropertySearchService_Service;

public class TestWSSecCertWSServlet extends HttpServlet {
	//@WebServiceRef(wsdlLocation="http://localhost:7001/PropertySearchService_WSSecCertificate/PropertySearchService?wsdl")
	@WebServiceRef
	PropertySearchService_Service service;
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("Running servlet " + getClass());		
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			out.println("<HTML><BODY>");
			out.println("<H1>Runnining as: " + request.getUserPrincipal().getName() + "</H1>");
			
			// Construct request
			PropertySearchAddress searchAddress = new PropertySearchAddress();
			searchAddress.setAddress1("1 High Street");
			searchAddress.setPostalCode("BT1234");
	
			// Obtain handle on web service stub 
			PropertySearchService port = service.getPropertySearchServicePort();
			PropertyInfo property = port.getPropertyDetailsByAddress(searchAddress);
	
			// Process response
			if (property != null) {
				int id = property.getId();
				String city = property.getCity();		
				out.println("<P>Found Property: Id=" + id + ", City=" + city + "</P>");
			} else {
				out.println("<P>No Property found</P>");
			}
			
			out.println("</BODY></HTML>");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
