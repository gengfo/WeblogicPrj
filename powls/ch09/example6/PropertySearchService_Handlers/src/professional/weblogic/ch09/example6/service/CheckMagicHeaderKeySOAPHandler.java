package professional.weblogic.ch09.example6.service;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CheckMagicHeaderKeySOAPHandler implements SOAPHandler<SOAPMessageContext> {
	private final static String NMSPC = "http://www.wrox.com/professional-weblogic/MagicKey";
	private final static String MAGIC_KEY_HEADER_NAME = "MagicKey";
	private final static String MAGIC_KEY_HEADER_CORRECT_VALUE = "012345";
	
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
		
			if (!isOutboundDirection) {
				NodeList headers = context.getMessage().getSOAPHeader().getElementsByTagNameNS(NMSPC, MAGIC_KEY_HEADER_NAME);
				String value = null;
				
				if ((headers != null) && (headers.getLength() > 0)) {
					Node node = headers.item(0);
					
					if (node != null) {
						value = node.getFirstChild().getNodeValue();
					}
				}
				
				if ((value == null) || (!value.equals(MAGIC_KEY_HEADER_CORRECT_VALUE))) {
					throw new ProtocolException("Mandatory SOAP header '" + MAGIC_KEY_HEADER_NAME + "' not specified with correct value");
				}
			}
		} catch (SOAPException e) {
			throw new ProtocolException(e);
		}
		
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	public void close(MessageContext context) {
	}
}
