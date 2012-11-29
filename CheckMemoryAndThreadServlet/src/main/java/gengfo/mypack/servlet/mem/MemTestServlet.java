<<<<<<< HEAD
package gengfo.mypack.servlet.mem;

import gengfo.mypack.servlet.common.ServletHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static int i = 0;

	private static List<MemoryConsumer> memoryContainer = new ArrayList<MemoryConsumer>();

	public static void testMemLeak(HttpServletRequest req,
			HttpServletResponse res) throws IOException {

		int times = 1;

		while (true) {

			System.out.println("Start to consumer memory ... times " + times);
			memoryContainer.add(new MemoryConsumer());

			times = times + 1;

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void service(final HttpServletRequest req,
			final HttpServletResponse res) throws IOException {

		new Thread() {
			public void run() {

				try {
					ServletHelper.showHello(req, res);
					testMemLeak(req, res);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}
}
=======
package gengfo.mypack.servlet.mem;

import gengfo.mypack.servlet.common.ServletHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static int i = 0;

	private static List<MemoryConsumer> memoryContainer = new ArrayList<MemoryConsumer>();

	public static void testMemLeak(HttpServletRequest req,
			HttpServletResponse res) throws IOException {

		int times = 1;

		while (true) {

			System.out.println("Start to consumer memory ... times " + times);
			memoryContainer.add(new MemoryConsumer());

			times = times + 1;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void service(final HttpServletRequest req,
			final HttpServletResponse res) throws IOException {

		new Thread() {
			public void run() {

				try {
					ServletHelper.showHello(req, res);
					testMemLeak(req, res);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}
}
>>>>>>> d6d118f1223d33d97e9f610fd4a2bb5de59b016c
