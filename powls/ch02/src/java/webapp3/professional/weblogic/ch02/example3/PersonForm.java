package professional.weblogic.ch02.example3;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import professional.weblogic.ch02.example3.objects.Person;

public class PersonForm 
implements Validator
{

    // Attributes

    private int id;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;

    // Public Methods

    public void setValues(Person pPerson) {
        setId(pPerson.getId());
        setSalutation(pPerson.getSalutation());
        setFirstName(pPerson.getFirstName());
        setMiddleName(pPerson.getMiddleName());
        setLastName(pPerson.getLastName());
    }

    public Person getPerson() {
        return new Person(getId(), getSalutation(), getFirstName(), getMiddleName(), getLastName());
    }

    public void
    reset()
    {
        setSalutation("");
        setFirstName("");
        setMiddleName("");
        setLastName("");    	
    }
    
    @Override 
	public void 
	validate(Object pCommand, Errors pErrors)
	{		
		System.out.println(">>> Person::validate()");
		PersonForm form = (PersonForm) pCommand;
        if ((form.getLastName() == null) || (form.getLastName().length() == 0)) 
        {
     		pErrors.reject("error.lastName.required");
		}		
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
    
	@SuppressWarnings("unchecked")
    @Override 
	public boolean supports(Class pClass)
	{
		return pClass.equals(PersonForm.class);
	}

}
