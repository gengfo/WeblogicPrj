package professional.weblogic.ch09.example12.property;

import java.io.Serializable;

public class PropertySearchAddress implements Serializable {
	private static final long serialVersionUID = 1;
	private String address1;
	private String postalCode;

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String value) {
		this.address1 = value;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String value) {
		this.postalCode = value;
	}
}
