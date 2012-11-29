package gengfo.mypack.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldWeblogicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int i =0;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		
		
		

		// Must set the content type first
		res.setContentType("text/html");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Now obtain a PrintWriter to insert HTML into
		PrintWriter out = res.getWriter();
		out.println("<html><head><title>" + "Hello World!</title></head>");
		out.println("<body><h1>");
		out.println("Hello World ");
		out.println(System.currentTimeMillis());
		out.println("!</h1></body></html>");

	}

}
