package mypack.client;

import mypack.client.proxy.CheckThreadWebService_Service;

public class CheckThreadWebServiceClient {
	public static void main(String[] args) {
		try {
			new CheckThreadWebServiceClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CheckThreadWebServiceClient() throws Exception {

		CheckThreadWebService_Service service = new CheckThreadWebService_Service();
		
		mypack.client.proxy.CheckThreadWebService port = service
				.getCheckThreadWebServicePort();
		
		port.check();
		
	}
}
