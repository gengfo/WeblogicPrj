package professional.weblogic.ch09.example3.client;

import java.net.URL;

import javax.xml.namespace.QName;

import professional.weblogic.ch09.example3.property.PropertyInfo;
import professional.weblogic.ch09.example3.property.PropertySearchAddress;
import professional.weblogic.ch09.example3.property.PropertySearchService;
import professional.weblogic.ch09.example3.property.PropertySearchService_Service;

public class PropertySearchClient {
	public static void main(String[] args) {
		try {
			new PropertySearchClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertySearchClient() throws Exception {
		// Construct request
		PropertySearchAddress searchAddress = new PropertySearchAddress();
		searchAddress.setAddress1("1 High Street");
		searchAddress.setPostalCode("BT1234");

		// Example 1: Get service stub using default URL for the WSDL
		PropertySearchService_Service service = new PropertySearchService_Service();

		// Exampe 2: Get service stub using a different URL for the WSDL
		
		//URL wsdlURL = new URL("http://localhost:9999/PropertySearchService_WSDLFirst/PropertySearchService?wsdl");
		//QName serviceQName = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "PropertySearchService");
		//PropertySearchService_Service service = new PropertySearchService_Service(wsdlURL, serviceQName);
		

		// Get service port
		PropertySearchService port = service.getPropertySearchServicePort();

		// Example: Change the URL of the actual service's endpoint to invoke
		//Map<String, Object> rc = ((BindingProvider) port).getRequestContext();
		//rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:7001/PropertySearchService_WSDLFirst/PropertySearchService");

		// Invoke service operation
		PropertyInfo property = port.getPropertyDetailsByAddress(searchAddress);

		// Process response
		if (property != null) {
			int id = property.getId();
			String city = property.getCity();		
			System.out.println("Found Property: Id=" + id + ", City=" + city);
		} else {
			System.out.println("No Property found");
		}
	}
}
