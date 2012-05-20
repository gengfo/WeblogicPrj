package gengfo.mypack.servlet;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldWeblogicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		// Must set the content type first
		res.setContentType("text/html");
		// Now obtain a PrintWriter to insert HTML into
		PrintWriter out = res.getWriter();
		out.println("<html><head><title>" + "Hello World!</title></head>");
		out.println("<body><h1>Hello World!</h1></body></html>");

	}

}
