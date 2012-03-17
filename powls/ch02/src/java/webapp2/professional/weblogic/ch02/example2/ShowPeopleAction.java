package professional.weblogic.ch02.example2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import professional.weblogic.ch02.example2.objects.Person;
import professional.weblogic.ch02.example2.services.PersonService;

public final class ShowPeopleAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response)
    throws IOException, ServletException {

        System.out.println(">>> ShowPeopleAction::execute()");

        System.out.println("***** Session id is "+request.getSession().getId());
        System.out.println("***** Session contents "+request.getSession().getAttribute("greg"));
         
        // Populate the form before forwarding to edit page
        PersonForm pform = (PersonForm)(form);
        
        request.getSession().setAttribute("greg", "xxx"+pform.getId());
        

        // retrieve business object and set form bean attributes
        if (pform.getAction().equals("update")) {
            Person p = PersonService.findPersonById(pform.getId());
            if (p==null) 
                return (mapping.findForward("failure"));
            pform.setValues(p);  // use PersonForm helper function
        }

        // Set a transactional control token to prevent double posting
        System.out.println("Setting transactional control token");
        saveToken(request);

        System.out.println("<<< ShowPeopleAction::execute() Forwarding to 'success' page.");
        return (mapping.findForward("success"));

    }

}
