package professional.weblogic.ch09.example5.client;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class PropertyChangeClient {
	private final static String WSDL_URL_SUFFIX = "?WSDL";
	private final static String WSDL_NMSP = "http://www.wrox.com/professional-weblogic/PropertyChangeService";
	private final static String WSDL_SRVC_PORT = "PropertyChangeServiceSOAP";
	private final static String WSDL_SRVC_NAME = "PropertyChangeService";
	private final static String XSD_NMSP = "http://www.wrox.com/professional-weblogic/PropertyChangeData";

	public static void main(String[] args) {		
		if ((args.length <= 0) || (args[0].trim().length() <= 0)) {
			throw new IllegalArgumentException("Main method must be provided with a service endpoint URL parameter");
		}
		
		try {
			new PropertyChangeClient(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertyChangeClient(String endpointURL) throws IOException, SAXException, TransformerException, ParserConfigurationException {
		// Construct request
		String requestText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<PropertyChangeInfo xmlns=\"http://www.wrox.com/professional-weblogic/PropertyChangeData\">" +
			"	<Id>1</Id>" + 
			"	<Description>Recently re-furbished</Description>" + 
			"</PropertyChangeInfo>";								
		Source request = new StreamSource(new StringReader(requestText));

		// Invoke service operation
		QName portQName = new QName(WSDL_NMSP, WSDL_SRVC_PORT);
		Service service = Service.create(new URL(endpointURL + WSDL_URL_SUFFIX), new QName(WSDL_NMSP, WSDL_SRVC_NAME));
		Dispatch<Source> dispatcher = service.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);        	
		Source response = dispatcher.invoke(request);

		// Process response		
		DOMResult dom = new DOMResult(); 
		Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
		transformer.transform(response, dom);
		Document doc = dom.getNode().getFirstChild().getOwnerDocument();
		Element topElement = doc.getDocumentElement();        		
		String ack = topElement.getElementsByTagNameNS(XSD_NMSP, "Ack").item(0).getFirstChild().getNodeValue();
		String receiptNum = topElement.getElementsByTagNameNS(XSD_NMSP, "ReceiptNumber").item(0).getFirstChild().getNodeValue();			
		String comment = topElement.getElementsByTagNameNS(XSD_NMSP, "Comment").item(0).getFirstChild().getNodeValue();
		System.out.println("Change property result: " + ack + " (Receipt=" + receiptNum + ", Comment=" + comment + ")");
	}
}
