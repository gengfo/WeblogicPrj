package professional.weblogic.ch09.example7.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class AddAttachmentSOAPHandler implements SOAPHandler<SOAPMessageContext> {
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
					
			if (isOutboundDirection) {
				AttachmentPart attachment = context.getMessage().createAttachmentPart();
				File file = new File("./hotel_picture.png");
				attachment.setRawContent(new BufferedInputStream(new FileInputStream(file)), "image/png");
				attachment.setContentId(file.getName());
				context.getMessage().addAttachmentPart(attachment);
				System.out.println("Handler added attachement using file  '" + file.getCanonicalPath() + "', size=" + file.length());
			}
		} catch (Exception e) {
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
