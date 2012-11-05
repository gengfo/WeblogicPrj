package mypack.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(serviceName = "CheckMemoryWebService", targetNamespace = "http://gengfo/ws/CheckMemoryWebService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class CheckMemoryWebService {

	private static List<MemoryConsumer> memoryContainer = new ArrayList<MemoryConsumer>();

	public void check() {

		System.out
				.println("Testing CheckMemoryWebService ... ");

		new Thread() {
			public void run() {
				
				//this.setDaemon(true);
				
				testMemLeak();
			}
		}.start();

	}

	public static void testMemLeak() {

		int times = 1;

		while (true) {

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Start to consumer memory ... times " + times);
			memoryContainer.add(new MemoryConsumer());

			times = times + 1;
		}

	}

	public static void main(String args[]) {

		testMemLeak();

	}

}
