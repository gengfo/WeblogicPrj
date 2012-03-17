package professional.weblogic.ch09.example4.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

public class PropertyChangeClient {
	private final static String WSDL_URL_SUFFIX = "?WSDL";
	private final static String WSDL_NMSP = "http://www.wrox.com/professional-weblogic/PropertyChangeService";
	private final static String WSDL_SRVC_PORT = "PropertyChangeServiceSOAP";
	private final static String WSDL_SRVC_NAME = "PropertyChangeService";
	private final static String XSD_PRFX = "prpty";
	private final static String XSD_NMSP = "http://www.wrox.com/professional-weblogic/PropertyChangeData";
		
	public static void main(String[] args) {		
		if ((args.length <= 0) || (args[0].trim().length() <= 0)) {
			throw new IllegalArgumentException("Main method must be provided with a service endpoint URL parameter");
		}
		
		try {
			new PropertyChangeClient(args[0].trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertyChangeClient(String endpointURL) throws SOAPException, MalformedURLException {
		// Construct request
		SOAPMessage request = MessageFactory.newInstance().createMessage();
		SOAPEnvelope envelope = request.getSOAPPart().getEnvelope();
		Name messageRootName = envelope.createName("PropertyChangeInfo", XSD_PRFX, XSD_NMSP);
		SOAPElement topElement = request.getSOAPBody().addBodyElement(messageRootName);
		topElement.addChildElement(envelope.createName("Id", XSD_PRFX, XSD_NMSP)).addTextNode("1");
		topElement.addChildElement(envelope.createName("Description", XSD_PRFX, XSD_NMSP)).addTextNode("Recently re-furbished");

		// Invoke service operation
		QName portQName = new QName(WSDL_NMSP, WSDL_SRVC_PORT);
		Service service = Service.create(new URL(endpointURL + WSDL_URL_SUFFIX), new QName(WSDL_NMSP, WSDL_SRVC_NAME));
		Dispatch<SOAPMessage> dispatcher = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);
		SOAPMessage response = dispatcher.invoke(request);

		// Process response
		String ack = response.getSOAPBody().getElementsByTagNameNS(XSD_NMSP, "Ack").item(0).getFirstChild().getNodeValue();
		String receiptNum = response.getSOAPBody().getElementsByTagNameNS(XSD_NMSP, "ReceiptNumber").item(0).getFirstChild().getNodeValue();
		String comment = response.getSOAPBody().getElementsByTagNameNS(XSD_NMSP, "Comment").item(0).getFirstChild().getNodeValue();
		System.out.println("Change property result: " + ack + " (Receipt=" + receiptNum + ", Comment=" + comment + ")");
	}
}
