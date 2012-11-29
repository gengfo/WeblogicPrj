<<<<<<< HEAD
package gengfo.mypack.servlet.thread;

import gengfo.mypack.servlet.common.ServletHelper;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(final HttpServletRequest req,
			final HttpServletResponse res) throws IOException {
		
		ServletHelper.showHello(req, res);
		check();

	}

	public void check() {

		System.out.println("Testing EaterThread, hit Ctrl+c to exit.");

		Tool spoon = new Tool("Spoon");
		Tool fork = new Tool("Fork");

		new EaterThread("Alice", spoon, fork).start();
		new EaterThread("Bob", fork, spoon).start();

	}

}
=======
package gengfo.mypack.servlet.thread;

import gengfo.mypack.servlet.common.ServletHelper;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(final HttpServletRequest req,
			final HttpServletResponse res) throws IOException {
		
		ServletHelper.showHello(req, res);
		check();

	}

	public void check() {

		System.out.println("Testing EaterThread, hit Ctrl+c to exit.");

		Tool spoon = new Tool("Spoon");
		Tool fork = new Tool("Fork");

		new EaterThread("Alice", spoon, fork).start();
		new EaterThread("Bob", fork, spoon).start();

	}

}
>>>>>>> d6d118f1223d33d97e9f610fd4a2bb5de59b016c
