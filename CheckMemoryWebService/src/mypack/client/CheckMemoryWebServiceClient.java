package mypack.client;

import java.util.ArrayList;
import java.util.List;

import mypack.client.proxy.CheckMemoryWebService_Service;
import mypack.service.MemoryConsumer;

public class CheckMemoryWebServiceClient {

	private static List<MemoryConsumer> memoryContainer = new ArrayList<MemoryConsumer>();

	public static void main(String[] args) {
		try {
			new CheckMemoryWebServiceClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CheckMemoryWebServiceClient() throws Exception {

		CheckMemoryWebService_Service service = new CheckMemoryWebService_Service();

		mypack.client.proxy.CheckMemoryWebService port = service
				.getCheckMemoryWebServicePort();

		port.check();

	}
}
