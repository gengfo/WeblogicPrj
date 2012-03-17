package professional.weblogic.ch09.example6.client;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class InjectMagicHeaderKeySOAPHandler implements SOAPHandler<SOAPMessageContext> {
	private final static String NMSPC = "http://www.wrox.com/professional-weblogic/MagicKey";
	private final static String MAGIC_KEY_HEADER_NAME = "MagicKey";
	private final static String MAGIC_KEY_HEADER_CORRECT_VALUE = "012345";

	public boolean handleMessage(SOAPMessageContext context) {
		try {
			boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

			if (isOutboundDirection) {
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				
				if (header == null) {
					header = envelope.addHeader();
				}					
					
				SOAPHeaderElement newHeader = header.addHeaderElement(new QName(NMSPC, MAGIC_KEY_HEADER_NAME));
				newHeader.setValue(MAGIC_KEY_HEADER_CORRECT_VALUE);
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
