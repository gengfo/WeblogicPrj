package professional.weblogic.ch09.example9.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import professional.weblogic.ch09.example9.property.PropertyUpdateRegister;
import professional.weblogic.ch09.example9.property.PropertyUpdateRegister_Service;

public class PropertySubscribeClient {
	private final static String CALLBACK_WSDL_URL = "http://localhost:7001/PropertyUpdateNotification_Callback_ClientReturn/PropertyUpdateReceiver?wsdl";
	private final static String CALLBACK_WSDL_NMSP = "http://www.wrox.com/professional-weblogic/PropertyUpdateReceiver";
	private final static String CALLBACK_WSDL_SRVC_PORT = "PropertyUpdateReceiverPort";
	private final static String CALLBACK_WSDL_SRVC_NAME = "PropertyUpdateReceiver";

	public static void main(String[] args) {
		try {
			new PropertySubscribeClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertySubscribeClient() throws MalformedURLException {
		// Get reference to the Web Service we want the callback to go to
		QName callbackPortQName = new QName(CALLBACK_WSDL_NMSP, CALLBACK_WSDL_SRVC_PORT);
		Service callbackService = Service.create(new URL(CALLBACK_WSDL_URL), new QName(CALLBACK_WSDL_NMSP, CALLBACK_WSDL_SRVC_NAME));
		Dispatch<Source> callbackDispatcher = callbackService.createDispatch(callbackPortQName, Source.class, Service.Mode.PAYLOAD);        	
		W3CEndpointReference callbackEndpointRef = callbackDispatcher.getEndpointReference(W3CEndpointReference.class);

		// Call register subscriber service passing endpoint address of callback service
		PropertyUpdateRegister_Service service = new PropertyUpdateRegister_Service();
		PropertyUpdateRegister port = service.getPropertyUpdateRegisterPort();
		port.registerInterest(1, callbackEndpointRef);

		System.out.println("Successfully called property update register");
	}
}
