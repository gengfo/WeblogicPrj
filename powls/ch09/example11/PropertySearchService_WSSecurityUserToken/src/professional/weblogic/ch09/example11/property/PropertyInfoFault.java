package professional.weblogic.ch09.example11.property;

import java.io.Serializable;

public class PropertyInfoFault implements Serializable {
	private static final long serialVersionUID = 1;
	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String value) {
		this.code = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String value) {
		this.message = value;
	}
}
