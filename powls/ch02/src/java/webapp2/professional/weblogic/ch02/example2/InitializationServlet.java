package professional.weblogic.ch02.example2;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import professional.weblogic.ch02.example2.objects.Salutation;
import professional.weblogic.ch02.example2.services.PersonService;


@SuppressWarnings("serial")
public class InitializationServlet extends HttpServlet {
    
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
    System.out.println("Initializing constants and collections");
    performInitialization(config);
  }

  private void performInitialization(ServletConfig config)
  {  
    ServletContext context = config.getServletContext();
    List<Salutation> salutationList = PersonService.getSalutationList(); 
    System.out.println("Initializing saluations: "+salutationList);
    context.setAttribute("salutationList", salutationList);
    return;
  }
  
}
