package professional.weblogic.ch02.example3;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.mvc.SimpleFormController;

import professional.weblogic.ch02.example3.services.PersonService;

public class ShowPeopleController extends SimpleFormController {

	private PersonList aPersonList;
		
	protected void 
	onBindOnNewForm(HttpServletRequest pRequest, Object pCommand, BindException pErrors)
	throws Exception 
	{
		System.out.println(">>> ShowPeopleController::onBindOnNewForm()");
		((PersonList)pCommand).setResults(PersonService.getPersonList());
	}
		
	public PersonList getPersonList() {
		return aPersonList;
	}

	public void setPersonList(PersonList pPersonList) {
		aPersonList = pPersonList;
	}

}
