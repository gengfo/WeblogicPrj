package professional.weblogic.ch09.example4.service;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;

import org.w3c.dom.Node;

@WebServiceProvider(
		serviceName="PropertyChangeService",
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertyChangeService",
		portName="PropertyChangeServiceSOAP",
		wsdlLocation="wsdls/PropertyChangeService.wsdl"
)
@ServiceMode(
		value=Service.Mode.MESSAGE
)
public class PropertyChangeService implements Provider<SOAPMessage> {
	private final static String PREFX = "prpty";
	private final static String NMSPC = "http://www.wrox.com/professional-weblogic/PropertyChangeData";
	
	@SuppressWarnings("unchecked")
	public SOAPMessage invoke(SOAPMessage request) {
		System.out.println(getClass() + "-Provider<SOAPMessage>.invoke() called");
		
		try {
			// Process request
			Iterator<SOAPElement> children = request.getSOAPBody().getChildElements(new QName(NMSPC, "PropertyChangeInfo"));
			SOAPElement propertyChangeInfo = children.next();
			String id = getChildValue(propertyChangeInfo, "Id");
			
			if (id == null) {
				throw new WebServiceException("Id field is unspecified - can't change data");
			}
			
			System.out.println(" Changing property '" + id + "' info with values (null means no change):");
			System.out.println(" -Description: " + getChildValue(propertyChangeInfo, "Description"));
			System.out.println(" -Features: " + getChildValue(propertyChangeInfo, "Features"));
			System.out.println(" -Address1: " + getChildValue(propertyChangeInfo, "Address1"));
			System.out.println(" -Address2: " + getChildValue(propertyChangeInfo, "Address2"));
			System.out.println(" -City: " + getChildValue(propertyChangeInfo, "City"));
			System.out.println(" -State: " + getChildValue(propertyChangeInfo, "State"));
			System.out.println(" -PostalCode: " + getChildValue(propertyChangeInfo, "PostalCode"));
			System.out.println(" -Phone: " + getChildValue(propertyChangeInfo, "Phone"));
			
			// Generate response
			SOAPMessage response = MessageFactory.newInstance().createMessage();
			SOAPEnvelope envelope = response.getSOAPPart().getEnvelope();
			Name propChangeAckElementName = envelope.createName("PropertyChangeAcknowledgement", PREFX, NMSPC);
			SOAPElement propChangeAckNameElement = response.getSOAPBody().addBodyElement(propChangeAckElementName);
			Name ackElementName = envelope.createName("Ack", PREFX, NMSPC);
			propChangeAckNameElement.addChildElement(ackElementName).addTextNode("SUCCESS");
			Name receiptNumberElementName = envelope.createName("ReceiptNumber", PREFX, NMSPC);
			propChangeAckNameElement.addChildElement(receiptNumberElementName).addTextNode(System.currentTimeMillis() + "-" + id);
			Name commentElementName = envelope.createName("Comment", PREFX, NMSPC);
			propChangeAckNameElement.addChildElement(commentElementName).addTextNode("Successfully processed info change for property: " + id);			
			return response;
		} catch (SOAPException e) {
			throw new WebServiceException("Error occurred in Provider JAX-WS web service implementaton", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getChildValue(SOAPElement parent, String nodeName) {
		String value = null;
		Iterator<SOAPElement>  children = parent.getChildElements(new QName(NMSPC, nodeName));
		
		if ((children != null) && (children.hasNext())) {
			Node firstChild = children.next().getFirstChild();
			
			if (firstChild != null) {
				value = firstChild.getNodeValue();
			}
		}
		
		return value;
	}
}
