package professional.weblogic.ch02.example3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import professional.weblogic.ch02.example3.objects.Person;
import professional.weblogic.ch02.example3.services.PersonService;

public class EditPersonController extends SimpleFormController {

    private PersonForm aPersonForm;

	protected Object 
    formBackingObject(HttpServletRequest pRequest)
    throws Exception
    {     
		System.out.println(">>> EditPersonController::formBackingObject()");
		return getPersonForm();
    }
	
	protected void 
	onBindOnNewForm(HttpServletRequest pRequest, Object pCommand, BindException pErrors)
	throws Exception 
	{
		System.out.println(">>> EditPersonController::onBindOnNewForm(id="+pRequest.getParameter("id")+")");
		if (getPersonForm().getId() > 0)
		{
			Person lPerson = PersonService.findPersonById(getPersonForm().getId());
			getPersonForm().setValues(lPerson);
			System.out.println("Fetched Person and placed in form");
		}
		else if (getPersonForm().getId() == 0)
		{
			System.out.println("Empty PersonForm sent through to edit form");
			getPersonForm().reset();
		}
	}
	
	protected ModelAndView 
	onSubmit(HttpServletRequest pRequest, HttpServletResponse pResponse, Object pCommand, BindException pErrors)
	throws Exception 
	{	
		System.out.println(">>> EditPersonController::onSubmit()");
		if (!pErrors.hasErrors())
		{
			// no bind errors
			PersonForm lPersonForm = (PersonForm)pCommand;
			if (lPersonForm.getId() == 0)
			{
	            Person lPerson = lPersonForm.getPerson(); // use form to get Person bean
	            System.out.println("Creating person bean in database");
	            PersonService.createPerson(lPerson);
			}
			else
			{
	            Person lPerson = lPersonForm.getPerson(); // use form to get Person bean
	            System.out.println("Updating person bean in database");
	            PersonService.updatePerson(lPerson);
			}
		}
		return super.onSubmit(pRequest, pResponse, pCommand, pErrors);
	}	
    
	public PersonForm getPersonForm() {
		return aPersonForm;
	}

	public void setPersonForm(PersonForm pPersonForm) {
		aPersonForm = pPersonForm;
	}

}
