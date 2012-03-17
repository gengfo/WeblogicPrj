package professional.weblogic.ch09.example5.service;

import java.io.StringReader;

import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@WebServiceProvider(
		serviceName="PropertyChangeService", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertyChangeService",
		portName="PropertyChangeServiceSOAP",
		wsdlLocation="wsdls/PropertyChangeService.wsdl"
)
@ServiceMode(
		value=Service.Mode.PAYLOAD
)
public class PropertyChangeService implements Provider<Source> {
	private final static String NMSPC = "http://www.wrox.com/professional-weblogic/PropertyChangeData";
	
	public Source invoke(Source request) {
		System.out.println(getClass() + "-Provider<Source>.invoke() called");
		
		try {
			// Process request
			Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
			DOMResult dom = new DOMResult(); 
			transformer.transform(request, dom);
			Document doc = dom.getNode().getFirstChild().getOwnerDocument();
			Element topElement = doc.getDocumentElement();
			String id = getChildValue(topElement, "Id");

			if (id == null) {
				throw new SOAPException("Id field is unspecified - can't change data");
			}
			
			System.out.println(" Changing property '" + id + "' info with values (null means no change):");
			System.out.println(" -Description: " + getChildValue(topElement, "Description"));
			System.out.println(" -Features: " + getChildValue(topElement, "Features"));
			System.out.println(" -Address1: " + getChildValue(topElement, "Address1"));
			System.out.println(" -Address2: " + getChildValue(topElement, "Address2"));
			System.out.println(" -City: " + getChildValue(topElement, "City"));
			System.out.println(" -State: " + getChildValue(topElement, "State"));
			System.out.println(" -PostalCode: " + getChildValue(topElement, "PostalCode"));
			System.out.println(" -Phone: " + getChildValue(topElement, "Phone"));
			
			// Generate response
			String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<PropertyChangeAcknowledgement xmlns=\"http://www.wrox.com/professional-weblogic/PropertyChangeData\">" +
				"	<Ack>SUCCESS</Ack>" +
				"	<ReceiptNumber>" + System.currentTimeMillis() + "-" + id + "</ReceiptNumber>" +
				"	<Comment>Successfully processed info change for property: " + id + "</Comment>" + 
				"</PropertyChangeAcknowledgement>";								
			return new StreamSource(new StringReader(response));
		} catch (Exception e) {
			throw new WebServiceException("Error occurred in Provider JAX-WS web service implementaton", e);
		}
	}
	
	private String getChildValue(Element parent, String nodeName) {
		String value = null;
		NodeList children = parent.getElementsByTagNameNS(NMSPC, nodeName);

		if ((children != null) && (children.getLength() > 0)) {
			Node node = children.item(0);
			
			if ((node != null) && (node.getFirstChild() != null)) {
				value = node.getFirstChild().getNodeValue();
			}
		}
		
		return value;
	}	
}
