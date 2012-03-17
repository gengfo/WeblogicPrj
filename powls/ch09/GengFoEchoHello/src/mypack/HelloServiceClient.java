package mypack;

import mypack.client.HelloService_Service;

public class HelloServiceClient {
	public static void main(String[] args) {
		try {
			new HelloServiceClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HelloServiceClient() throws Exception {

		// Example 1: Get service stub using default URL for the WSDL
		HelloService_Service service = new HelloService_Service();

		// Exampe 2: Get service stub using a different URL for the WSDL

		// URL wsdlURL = new
		// URL("http://localhost:9999/PropertySearchService_WSDLFirst/PropertySearchService?wsdl");
		// QName serviceQName = new
		// QName("http://www.wrox.com/professional-weblogic/PropertySearchService",
		// "PropertySearchService");
		// PropertySearchService_Service service = new
		// PropertySearchService_Service(wsdlURL, serviceQName);

		// Get service port
		mypack.client.HelloService port = service.getHelloServicePort();

		// Example: Change the URL of the actual service's endpoint to invoke
		// Map<String, Object> rc = ((BindingProvider)
		// port).getRequestContext();
		// rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
		// "http://localhost:7001/PropertySearchService_WSDLFirst/PropertySearchService");

		// Invoke service operation
		port.sayHelloTo("gengfo");
	}
}
