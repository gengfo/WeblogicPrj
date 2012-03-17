package professional.weblogic.ch09.example11.property;

import java.io.Serializable;

public class PropertyInfo implements Serializable {
	private static final long serialVersionUID = 1;
	private int id;
	private String description;
	private String features;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postalCode;
	private String phone;

	public int getId() {
		return id;
	}

	public void setId(int value) {
		this.id = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String value) {
		this.features = value;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String value) {
		this.address1 = value;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String value) {
		this.address2 = value;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String value) {
		this.city = value;
	}

	public String getState() {
		return state;
	}

	public void setState(String value) {
		this.state = value;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String value) {
		this.postalCode = value;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String value) {
		this.phone = value;
	}
}

