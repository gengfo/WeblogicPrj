package professional.weblogic.ch09.example9.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService (
		serviceName="PropertyUpdateReceiver", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertyUpdateReceiver"
)
@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT, 
		use=SOAPBinding.Use.LITERAL, 
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED
)
public class PropertyUpdateReceiver {
	public boolean receiveUpdate(int propId, String description) {
		System.out.println(getClass() + ".receiveUpdate() called for property id: " + propId);
		System.out.println(" Update Description: " + description);
		return true;
	}
}
