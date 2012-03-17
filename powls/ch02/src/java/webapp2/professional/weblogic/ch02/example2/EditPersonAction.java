package professional.weblogic.ch02.example2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import professional.weblogic.ch02.example2.objects.Person;
import professional.weblogic.ch02.example2.services.PersonService;

public final class EditPersonAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response)
    throws IOException, ServletException {

        System.out.println(">>> EditPersonAction::execute()");
        ActionMessages errors = new ActionMessages();

        PersonForm pform = (PersonForm) form;

        // check for the transaction token set in ShowPeopleAction
        if (!isTokenValid(request)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("error.transaction.token"));
        }
        
        // Report any errors we have discovered back to the original form
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        if (pform.getAction().equals("update")) {
            Person p = pform.getPerson(); // use form to get Person bean
            System.out.println("Updating person bean in database");
            PersonService.updatePerson(p);
        } else if (pform.getAction().equals("create")) {
            Person p = pform.getPerson(); // use form to get Person bean
            System.out.println("Creating person bean in database");
            PersonService.createPerson(p);
        }
        
        resetToken(request);

        // Forward control to the next page
        System.out.println("<<< EditPersonAction::execute() Forwarding to 'success' page.");
        return (mapping.findForward("success"));
    }
}
