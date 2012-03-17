package professional.weblogic.ch09.example10.service;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebService;

import professional.weblogic.ch09.example10.property.PropertyInfo;
import professional.weblogic.ch09.example10.property.PropertyInfoFault;
import professional.weblogic.ch09.example10.property.PropertyInfoFault_Exception;
import professional.weblogic.ch09.example10.property.PropertySearchAddress;
import professional.weblogic.ch09.example10.property.PropertySearchId;
import professional.weblogic.ch09.example10.property.PropertySearchService;

@WebService(
		serviceName="PropertySearchService", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertySearchService",
		endpointInterface="professional.weblogic.ch09.example10.property.PropertySearchService"
)
public class PropertySearchServiceImpl implements PropertySearchService {
	private final static PropertyInfo onlyExistingProperty = new PropertyInfo();
	private final static int ONLY_PROPERTY_ID = 1;
	private final static String ONLY_PROPERTY_ADDRESS1 = "1 High Street";
	private final static String ONLY_PROPERTY_POSTCODE = "BT1234";

	// @RolesAllowed is currently not included in JAX-WS 2.1 spec
	@RolesAllowed(value = {"Administrators"})
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

	// @RolesAllowed is currently not included in JAX-WS 2.1 spec
	@RolesAllowed(value = {"Administrators"})    
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