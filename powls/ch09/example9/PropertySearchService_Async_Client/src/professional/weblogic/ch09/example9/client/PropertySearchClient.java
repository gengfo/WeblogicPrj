package professional.weblogic.ch09.example9.client;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Future;

import javax.xml.namespace.QName;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import professional.weblogic.ch09.example9.property.GetPropertyDetailsByAddressResponse;
import professional.weblogic.ch09.example9.property.PropertyInfo;
import professional.weblogic.ch09.example9.property.PropertySearchAddress;
import professional.weblogic.ch09.example9.property.PropertySearchService;
import professional.weblogic.ch09.example9.property.PropertySearchService_Service;

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

		// Example A: Get service handle using a WSDL from inside the web service client jar (resource local path)
		/*
		URL wsdlURL = Thread.currentThread().getContextClassLoader().getResource("META-INF/PropertySearchService.wsdl");
		System.out.println("Using WSDL URL: " + wsdlURL);
		QName serviceQName = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "PropertySearchService");
		PropertySearchService_Service service = new PropertySearchService_Service(wsdlURL, serviceQName);
		*/

		// Example B: Get service handle using the WSDL from inside the web service client jar (full path)
		/*
		File jarPath = new File("build/PropertySearchService_Async_Client.jar");
		URL wsdlURL = new URL("jar:file:" + jarPath.getCanonicalPath() + "!/META-INF/PropertySearchService.wsdl");
		System.out.println("Using WSDL URL: " + wsdlURL);
		QName serviceQName = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "PropertySearchService");
		PropertySearchService_Service service = new PropertySearchService_Service(wsdlURL, serviceQName);
		*/

		// Example C: Get service handle using a WSDL from a directory on the client machine
		/*
		File filePath = new File("tmp/META-INF/PropertySearchService.wsdl");
		URL wsdlURL = new URL("file:" + filePath.getCanonicalPath());
		System.out.println("Using WSDL URL: " + wsdlURL);
		QName serviceQName = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "PropertySearchService");
		PropertySearchService_Service service = new PropertySearchService_Service(wsdlURL, serviceQName);
		*/
		
		// Example D: Using default WSDL which was located during the clientgen buuld process		
		PropertySearchService_Service service = new PropertySearchService_Service();		

		// Invoke service operation		
		System.out.println("Loaded WSDL location: " + service.getWSDLDocumentLocation());
		PropertySearchService port = service.getPropertySearchServiceImplPort();

		// Old invocation version:
		//PropertyInfo property = port.getPropertyDetailsByAddress(searchAddress);

		// New invocation version 1: - Asynchronous, Polling		
		//Response<GetPropertyDetailsByAddressResponse> response = port.getPropertyDetailsByAddressAsync(searchAddress);		
		//System.out.println("Doing some work before we try to get the response - polling");
		//PropertyInfo property = response.get().getPropertyInfo();

		// New invocation version 2: - Asynchronous, Callback (using callback class below)
		ResponseCallbackHandler handler = new ResponseCallbackHandler(); 
		Future<?> response = port.getPropertyDetailsByAddressAsync(searchAddress, handler);
		System.out.println("Doing some work before we try to get the response - callback");
		response.get();  // See if response has come in - if not yet, block until it has
		PropertyInfo property = handler.getResponse();

		// Process response
		if (property != null) {
			int id = property.getId();
			String city = property.getCity();		
			System.out.println("Found Property: Id=" + id + ", City=" + city);			
		} else {
			System.out.println("No Property found");					
		}
	}

	class ResponseCallbackHandler implements AsyncHandler<GetPropertyDetailsByAddressResponse> {
		private PropertyInfo property;

		public void handleResponse(Response<GetPropertyDetailsByAddressResponse> response) {
			try {
				property = response.get().getPropertyInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		PropertyInfo getResponse() {
			return property;
		}
	}	
}
