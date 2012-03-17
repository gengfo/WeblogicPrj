package professional.weblogic.ch09.example11.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import professional.weblogic.ch09.example11.property.PropertyInfo;
import professional.weblogic.ch09.example11.property.PropertyInfoFault;
import professional.weblogic.ch09.example11.property.PropertyInfoFaultException;
import professional.weblogic.ch09.example11.property.PropertySearchAddress;
import professional.weblogic.ch09.example11.property.PropertySearchId;

import weblogic.jws.Policy;

@WebService(
		serviceName="PropertySearchService", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertySearchService"
)
@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT, 
		use=SOAPBinding.Use.LITERAL, 
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED
)
public class PropertySearchService {
	private final static PropertyInfo onlyExistingProperty = new PropertyInfo();
	private final static int ONLY_PROPERTY_ID = 1;
	private final static String ONLY_PROPERTY_ADDRESS1 = "1 High Street";
	private final static String ONLY_PROPERTY_POSTCODE = "BT1234";

	@WebMethod
	@Policy(uri="UsernameTokenPolicy.xml", direction=Policy.Direction.inbound)
	public PropertyInfo getPropertyDetailsByAddress(PropertySearchAddress searchAddress) throws PropertyInfoFaultException {
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

	@WebMethod
	@Policy(uri="UsernameTokenPolicy.xml", direction=Policy.Direction.inbound)
	public PropertyInfo getPropertyDetailsById(PropertySearchId searchId) throws PropertyInfoFaultException {
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

	private void throwFaultException() throws PropertyInfoFaultException {
		String error = "PropertySearchService search criteria element(s) are emtpy";
		PropertyInfoFault fault = new PropertyInfoFault();
		fault.setCode("BIGREZERR-01234");
		fault.setMessage(error);
		throw new PropertyInfoFaultException(error, fault);
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