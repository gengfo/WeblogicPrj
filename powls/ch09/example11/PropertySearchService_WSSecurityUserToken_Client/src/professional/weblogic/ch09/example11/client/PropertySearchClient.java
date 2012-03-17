package professional.weblogic.ch09.example11.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import professional.weblogic.ch09.example11.property.PropertyInfo;
import professional.weblogic.ch09.example11.property.PropertyInfoFaultException_Exception;
import professional.weblogic.ch09.example11.property.PropertySearchAddress;
import professional.weblogic.ch09.example11.property.PropertySearchService;
import professional.weblogic.ch09.example11.property.PropertySearchService_Service;

import weblogic.wsee.security.unt.ClientUNTCredentialProvider;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class PropertySearchClient {
	public static void main(String[] args) {
		try {
			new PropertySearchClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertySearchClient() throws PropertyInfoFaultException_Exception {
		// Construct request
		PropertySearchAddress searchAddress = new PropertySearchAddress();
		searchAddress.setAddress1("1 High Street");
		searchAddress.setPostalCode("BT1234");

		// Invoke service operation AFTER setting up credential provider with username/password
		PropertySearchService_Service service = new PropertySearchService_Service();
		PropertySearchService port = service.getPropertySearchServicePort();
		List<CredentialProvider> credProviders = new ArrayList<CredentialProvider>();
		CredentialProvider cp = new ClientUNTCredentialProvider("weblogic".getBytes(), "welcome1".getBytes());
		credProviders.add(cp);
		Map<String, Object> rc = ((BindingProvider) port).getRequestContext();
		rc.put(WSSecurityContext.CREDENTIAL_PROVIDER_LIST, credProviders);
		//rc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8001/PropertySearchService_WSSecUsrPswd/PropertySearchService");
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
