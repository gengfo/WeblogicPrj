package professional.weblogic.ch09.example8.client;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import professional.weblogic.ch09.example8.property.PropertyInfo;
import professional.weblogic.ch09.example8.property.PropertySearchAddress;
import professional.weblogic.ch09.example8.property.PropertySearchService;
import professional.weblogic.ch09.example8.property.PropertySearchService_Service;

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

		// Invoke service operation
		PropertySearchService_Service service = new PropertySearchService_Service();
		PropertySearchService port = service.getPropertySearchServiceImplPort();
		Map<String, Object> rc = ((BindingProvider) port).getRequestContext();
		rc.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);
		//rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8001/PropertySearchService_Stateful/PropertySearchService");
		PropertyInfo property = port.getPropertyDetailsByAddress(searchAddress);
		property = port.getPropertyDetailsByAddress(searchAddress);
		property = port.getPropertyDetailsByAddress(searchAddress);

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
