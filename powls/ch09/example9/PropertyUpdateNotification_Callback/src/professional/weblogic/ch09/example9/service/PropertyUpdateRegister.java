package professional.weblogic.ch09.example9.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import professional.weblogic.ch09.example9.callback.PropertyUpdateReceiver;
import professional.weblogic.ch09.example9.callback.PropertyUpdateReceiver_Service;

@WebService (
		serviceName="PropertyUpdateRegister", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertyUpdateRegister"
)
@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT, 
		use=SOAPBinding.Use.LITERAL, 
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED
)
public class PropertyUpdateRegister {
	public boolean registerInterest(int propId, W3CEndpointReference callbackReference) {
		System.out.println(getClass() + ".registerInterest() called for property id: " + propId);
		PropertyUpdateReceiver_Service service = new PropertyUpdateReceiver_Service();
		PropertyUpdateReceiver port = service.getPort(callbackReference, PropertyUpdateReceiver.class);
		port.receiveUpdate(propId, "Closed for re-furbishment");
		try { Thread.sleep(200); } catch (InterruptedException e) {	}
		port.receiveUpdate(propId, "Now fully furninshed");
		return true;
	}
}
