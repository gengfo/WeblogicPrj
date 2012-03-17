package professional.weblogic.ch09.example7.client;

import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import professional.weblogic.ch09.example7.property.PropertyChangeAcknowledgement;
import professional.weblogic.ch09.example7.property.PropertyChangeInfo;
import professional.weblogic.ch09.example7.property.PropertyChangeService;
import professional.weblogic.ch09.example7.property.PropertyChangeService_Service;

public class PropertyChangeClient {
	public static void main(String[] args) {
		try {
			new PropertyChangeClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public PropertyChangeClient() {
		// Construct request
		PropertyChangeInfo propertyInfo = new PropertyChangeInfo();
		propertyInfo.setId(1);
		propertyInfo.setDescription("Recently re-furbished");
		
		// Invoke service operation AND set up handlers
		PropertyChangeService_Service service = new PropertyChangeService_Service();
		PropertyChangeService port = service.getPropertyChangeServiceImplPort();
		Binding binding = ((BindingProvider)port).getBinding();
		List<Handler> handlerList = binding.getHandlerChain();
		handlerList.add(new AddAttachmentSOAPHandler());
		binding.setHandlerChain(handlerList);		
		PropertyChangeAcknowledgement ackResponse = port.changePropertyDetails(propertyInfo);
		
		// Process response
		System.out.println("Change property result: " + ackResponse.getAck() + " (Receipt=" + ackResponse.getReceiptCode() + ", Comment=" + ackResponse.getComment() + ")");			
	}
}
