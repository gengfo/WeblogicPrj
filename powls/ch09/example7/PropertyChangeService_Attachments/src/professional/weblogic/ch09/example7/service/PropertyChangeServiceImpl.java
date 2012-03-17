package professional.weblogic.ch09.example7.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;

import professional.weblogic.ch09.example7.property.Ack;
import professional.weblogic.ch09.example7.property.PropertyChangeAcknowledgement;
import professional.weblogic.ch09.example7.property.PropertyChangeInfo;
import professional.weblogic.ch09.example7.property.PropertyChangeService;

@WebService(
		serviceName="PropertyChangeService", 
		targetNamespace="http://www.wrox.com/professional-weblogic/PropertyChangeService",
		endpointInterface="professional.weblogic.ch09.example7.property.PropertyChangeService"
)
@HandlerChain(
		file="handler-chain.xml"
)
public class PropertyChangeServiceImpl implements PropertyChangeService {
	@Resource
	private WebServiceContext context;
	private final static String FILE_LIST_KEY = "AttachedFilePathList";

	@SuppressWarnings("unchecked")
	public PropertyChangeAcknowledgement changePropertyDetails(PropertyChangeInfo propertyInfo) {
		System.out.println(getClass() + ".changePropertyDetails() invoked");
		int id = propertyInfo.getId();

    	if (id <= 0) {
    		throw new WebServiceException("PropertyChangeService search id is not specified");
    	}

		System.out.println(" Changing property '" + id + "' info with values (null means no change):");			
		System.out.println(" -Description: " + propertyInfo.getDescription());
		System.out.println(" -Features: " + propertyInfo.getFeatures());
		System.out.println(" -Address1: " + propertyInfo.getAddress1());
		System.out.println(" -Address2: " + propertyInfo.getAddress2());
		System.out.println(" -City: " + propertyInfo.getCity());
		System.out.println(" -State: " + propertyInfo.getState());
		System.out.println(" -PostalCode: " + propertyInfo.getPostalCode());
		System.out.println(" -Phone: " + propertyInfo.getPhone());

		// Get files which were saved from attachments in handler, printing size of each file
		List<String> filePathList = (List<String>) context.getMessageContext().get(FILE_LIST_KEY);

		for (String path : filePathList) {
			File file = new File(path);
			System.out.println("Using file attachement which was saved at '" + file.getAbsolutePath() + "', size=" + file.length());
		}
		
		PropertyChangeAcknowledgement ackResponse = new PropertyChangeAcknowledgement();
		ackResponse.setAck(Ack.SUCCESS);
		ackResponse.setReceiptCode(System.currentTimeMillis() + "-" + id);
		ackResponse.setComment("Successfully processed info change for property: " + id);
		return ackResponse;
	}
}
