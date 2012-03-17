package professional.weblogic.ch11.examples.server.servlet.webappsecurity;

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import professional.weblogic.ch11.examples.client.ejb.IExampleEJB;
import weblogic.javaee.EJBReference;

public class EJBClientServlet extends HttpServlet{

	@Resource(name="example")
	IExampleEJB example;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.getWriter().println("<html><body><h1>EJB Client</h1>");
		try {
			String in = request.getParameter("in");
			int amount = Integer.valueOf(request.getParameter("amount"));
			String result = example.secureHello(in, amount);
			response.getWriter().println("<h2>Success</h2>"+result);
		} catch (Exception e) {
			response.getWriter().println("<h2>Failure</h2>"+e.getMessage());
		}
		response.getWriter().println("</body></html>");
	}

}
