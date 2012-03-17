package professional.weblogic.ch02.example2;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import professional.weblogic.ch02.example2.objects.Person;

public final class PersonForm extends ActionForm  {

    // Attributes

    private String action;
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

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        this.id=0;
        this.action = null;
        this.salutation = null;
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
    }


    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        System.out.println(">>> PersonForm::validate()");
        ActionErrors errors = new ActionErrors();
        if ((getLastName() == null) || (getLastName().length() == 0)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.lastName.required"));
        }
        return errors;
    }

    // Accessors

    public String getAction() {
        return action;
    }
    public void setAction(String pValue) {
        action = pValue;
    }

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
}
