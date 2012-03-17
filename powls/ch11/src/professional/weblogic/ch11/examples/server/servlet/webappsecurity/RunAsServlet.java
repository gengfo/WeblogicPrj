package professional.weblogic.ch11.examples.server.servlet.webappsecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.security.Security;

public class RunAsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().println("<html><body><h1>Run As Example</h1>");
		response.getWriter().println("The Run-As-User Is: "+Security.getCurrentSubject()+"<BR>");
		response.getWriter().println("The User in the Request is "+request.getUserPrincipal());
		response.getWriter().println("</body></html>");
		
		
	}

}
