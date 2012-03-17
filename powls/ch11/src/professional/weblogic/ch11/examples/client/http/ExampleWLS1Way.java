package professional.weblogic.ch11.examples.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExampleWLS1Way {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Connecting to "+args[0]);
		
		URL url = new URL(args[0]);
		weblogic.net.http.HttpsURLConnection sslConn =
		new weblogic.net.http.HttpsURLConnection(url);
		sslConn.connect();
		
		InputStream in = sslConn.getInputStream();
		
		int c;
		
		while ((c=in.read())!=-1) {
			
			System.out.print((char)c);
			
		}
		
		

	}

}
