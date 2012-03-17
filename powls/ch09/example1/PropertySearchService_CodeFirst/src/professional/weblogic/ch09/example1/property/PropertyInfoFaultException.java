package professional.weblogic.ch09.example1.property;

public class PropertyInfoFaultException extends Exception {
	private static final long serialVersionUID = 1L;
	private PropertyInfoFault faultInfo;

	public PropertyInfoFaultException(String message, PropertyInfoFault faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public PropertyInfoFaultException(String message, PropertyInfoFault faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public PropertyInfoFault getFaultInfo() {
		return faultInfo;
	}
}
