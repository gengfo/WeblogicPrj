package professional.weblogic.ch09.example10.property;

import java.io.Serializable;

public class PropertySearchId implements Serializable {
	private static final long serialVersionUID = 1;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int value) {
		this.id = value;
	}
}
