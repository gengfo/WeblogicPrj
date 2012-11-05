package gengfo.mypack.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mypack.service.MemoryConsumer;

public class HelloWorldWeblogicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int i = 0;

	private static List<MemoryConsumer> memoryContainer = new ArrayList<MemoryConsumer>();

	public static void testMemLeak(HttpServletRequest req, HttpServletResponse res) throws IOException {

		int times = 1;

		while (true) {

			System.out.println("Start to consumer memory ... times " + times);
			memoryContainer.add(new MemoryConsumer());

			showHello(req, res);
			times = times + 1;
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void service(final HttpServletRequest req, final HttpServletResponse res)
			throws IOException {
		
		new Thread() {
			public void run() {
				
				//this.setDaemon(true);
				
				try {
					testMemLeak( req,  res);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
	

	}

	public static void showHello(HttpServletRequest req, HttpServletResponse res) throws IOException {

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
