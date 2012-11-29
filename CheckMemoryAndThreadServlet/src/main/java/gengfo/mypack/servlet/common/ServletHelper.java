<<<<<<< HEAD
package gengfo.mypack.servlet.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHelper {

	public static void showHello(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
	
		// Must set the content type first
		res.setContentType("text/html");
		try {
			Thread.sleep(1);
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
=======
package gengfo.mypack.servlet.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHelper {

	public static void showHello(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
	
		// Must set the content type first
		res.setContentType("text/html");
		try {
			Thread.sleep(1);
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
>>>>>>> d6d118f1223d33d97e9f610fd4a2bb5de59b016c
