package professional.weblogic.ch09.example6.client;

import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import professional.weblogic.ch09.example6.property.PropertyInfo;
import professional.weblogic.ch09.example6.property.PropertyInfoFaultException;
import professional.weblogic.ch09.example6.property.PropertySearchAddress;
import professional.weblogic.ch09.example6.property.PropertySearchService;
import professional.weblogic.ch09.example6.property.PropertySearchService_Service;

public class PropertySearchClient {
	public static void main(String[] args) {
		try {
			new PropertySearchClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public PropertySearchClient() throws PropertyInfoFaultException {
		// Construct request
		PropertySearchAddress searchAddress = new PropertySearchAddress();
		searchAddress.setAddress1("1 High Street");
		searchAddress.setPostalCode("BT1234");

		// Invoke service operation AND set up handlers
		PropertySearchService_Service service = new PropertySearchService_Service();
		PropertySearchService port = service.getPropertySearchServiceImplPort();
		Binding binding = ((BindingProvider)port).getBinding();
		List<Handler> handlerList = binding.getHandlerChain();
		handlerList.add(new InjectMagicHeaderKeySOAPHandler());
		handlerList.add(new SearchAuditorLogicalHandler());
		binding.setHandlerChain(handlerList);
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
