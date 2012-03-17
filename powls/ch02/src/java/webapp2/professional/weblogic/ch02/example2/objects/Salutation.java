package professional.weblogic.ch02.example2.objects;

public class Salutation {

	// Attributes

	private String salutation;

	public Salutation(String pSalutation) {
		setSalutation(pSalutation);
	}

	public String toString() {
		return salutation;
	}

	// Accessors 

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String pValue) {
		salutation = pValue;
	}

	// End Accessors
}
