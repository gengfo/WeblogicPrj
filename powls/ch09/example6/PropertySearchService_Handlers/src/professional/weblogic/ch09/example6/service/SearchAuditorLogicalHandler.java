package professional.weblogic.ch09.example6.service;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SearchAuditorLogicalHandler implements LogicalHandler<LogicalMessageContext> {
	private final static String XSD_NMSP = "http://www.wrox.com/professional-weblogic/PropertyInfo";

	public boolean handleMessage(LogicalMessageContext context) {
		try {			
			boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

			if (isOutboundDirection) {		
				Source source = context.getMessage().getPayload();
				DOMSource domSource = (DOMSource)source;
				Document doc = domSource.getNode().getFirstChild().getOwnerDocument();
				NodeList idElements = doc.getElementsByTagNameNS(XSD_NMSP, "Id");

				if ((idElements != null) && (idElements.getLength() > 0)) {
					String propertyId =  idElements.item(0).getFirstChild().getNodeValue();
					System.out.println("AUDIT> Property search occurred on property id: " + propertyId);
				}
			}
		} catch (Exception e) {
			throw new ProtocolException(e);
		}

		return true;
	}

	public boolean handleFault(LogicalMessageContext context) {
		return true;
	}

	public void close(MessageContext context) {	
	}
}
