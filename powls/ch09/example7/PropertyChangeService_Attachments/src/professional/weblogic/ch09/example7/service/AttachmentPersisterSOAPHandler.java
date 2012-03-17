package professional.weblogic.ch09.example7.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class AttachmentPersisterSOAPHandler implements SOAPHandler<SOAPMessageContext> {
	private final static String FILE_LIST_KEY = "AttachedFilePathList";
	
	@SuppressWarnings("unchecked")
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

			if (!isOutboundDirection) {
				Iterator<AttachmentPart> attachments = context.getMessage().getAttachments();
				List<String> filePathList = new ArrayList<String>();
				
				while (attachments.hasNext()) {
					AttachmentPart attachment = attachments.next();
					File tempFile = File.createTempFile("TmpAttmnt_", ".tmp");
					OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
					out.write(attachment.getRawContentBytes());					
					out.close();
					System.out.println("Saved attachment file to: " + tempFile.getAbsolutePath());
					filePathList.add(tempFile.getAbsolutePath());
				}

				context.put(FILE_LIST_KEY, filePathList);
				context.setScope(FILE_LIST_KEY, Scope.APPLICATION);
				context.getMessage().removeAllAttachments();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
