package professional.weblogic.ch02.example3.objects;

public class Person {

	// Attributes

	private int id;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;

	public Person() {
		this(0,"","","","");
	}

	public Person(int pId, String pSalutation, String pFirstName, String pMiddleName, String pLastName) {
		setId(pId);
		setSalutation(pSalutation);
		setFirstName(pFirstName);
		setMiddleName(pMiddleName);
		setLastName(pLastName);
	}

	// Accessors 

	public int getId() {
		return id;
	}

	public void setId(int pValue) {
		id = pValue;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String pValue) {
		salutation = pValue;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String pValue) {
		firstName = pValue;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String pValue) {
		middleName = pValue;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String pValue) {
		lastName = pValue;
	}

	// End Accessors
}
