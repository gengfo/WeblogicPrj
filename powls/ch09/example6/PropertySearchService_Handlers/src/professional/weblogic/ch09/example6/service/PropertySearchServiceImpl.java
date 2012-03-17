package professional.weblogic.ch09.example6.service;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import professional.weblogic.ch09.example6.property.PropertyInfo;
import professional.weblogic.ch09.example6.property.PropertyInfoFault;
import professional.weblogic.ch09.example6.property.PropertyInfoFault_Exception;
import professional.weblogic.ch09.example6.property.PropertySearchAddress;
import professional.weblogic.ch09.example6.property.PropertySearchId;
import professional.weblogic.ch09.example6.property.PropertySearchService;

@WebService(
		serviceName="PropertySearchService", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertySearchService",
		endpointInterface="professional.weblogic.ch09.example6.property.PropertySearchService"
)
@HandlerChain(
		file="handler-chain.xml"
)
public class PropertySearchServiceImpl implements PropertySearchService {
	private final static PropertyInfo onlyExistingProperty = new PropertyInfo();
	private final static int ONLY_PROPERTY_ID = 1;
	private final static String ONLY_PROPERTY_ADDRESS1 = "1 High Street";
	private final static String ONLY_PROPERTY_POSTCODE = "BT1234";

	public PropertyInfo getPropertyDetailsByAddress(PropertySearchAddress searchAddress) throws PropertyInfoFault_Exception {
		System.out.println(getClass() + ".getPropertyDetailsByAddress() called");
		String address1 = searchAddress.getAddress1();
		String postalCode = searchAddress.getPostalCode();

		if ((address1 == null) || (address1.trim().length() <= 0) ||
				(postalCode == null) || (postalCode.trim().length() <= 0)) {
			throwFaultException();
		}

		PropertyInfo property = null;

		if ((address1.trim().equalsIgnoreCase(ONLY_PROPERTY_ADDRESS1)) && 
				(postalCode.trim().equalsIgnoreCase(ONLY_PROPERTY_POSTCODE))) {
			property = onlyExistingProperty;
		}

		return property;
	}

	public PropertyInfo getPropertyDetailsById(PropertySearchId searchId) throws PropertyInfoFault_Exception {
		System.out.println(getClass() + ".getPropertyDetailsById() invoked");
		int id = searchId.getId();

		if (id <= 0) {
			throwFaultException();
		}

		PropertyInfo property = null;

		if (id == ONLY_PROPERTY_ID) {
			property = onlyExistingProperty;
		}

		return property;
	}

	private void throwFaultException() throws PropertyInfoFault_Exception {
		String error = "PropertySearchService search criteria elements are emtpy";
		PropertyInfoFault fault = new PropertyInfoFault();
		fault.setCode("BIGREZERR-01234");
		fault.setMessage(error);
		throw new PropertyInfoFault_Exception(error, fault);
	}

	static {
		onlyExistingProperty.setId(ONLY_PROPERTY_ID);
		onlyExistingProperty.setDescription("A nice place");
		onlyExistingProperty.setFeatures("Luxury bedrooms, swimming pool");
		onlyExistingProperty.setAddress1(ONLY_PROPERTY_ADDRESS1);
		onlyExistingProperty.setAddress2("Hillside");
		onlyExistingProperty.setCity("Bigtown");
		onlyExistingProperty.setCity("Sunnystate");
		onlyExistingProperty.setPostalCode(ONLY_PROPERTY_POSTCODE);
		onlyExistingProperty.setPhone("01-1322-2323");
	}
}
