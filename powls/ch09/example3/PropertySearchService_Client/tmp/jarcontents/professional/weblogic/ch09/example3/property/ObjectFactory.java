
package professional.weblogic.ch09.example3.property;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the professional.weblogic.ch09.example3.property package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PropertyInfoFaultException_QNAME = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "PropertyInfoFaultException");
    private final static QName _GetPropertyDetailsByAddress_QNAME = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "getPropertyDetailsByAddress");
    private final static QName _GetPropertyDetailsById_QNAME = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "getPropertyDetailsById");
    private final static QName _GetPropertyDetailsByIdResponse_QNAME = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "getPropertyDetailsByIdResponse");
    private final static QName _GetPropertyDetailsByAddressResponse_QNAME = new QName("http://www.wrox.com/professional-weblogic/PropertySearchService", "getPropertyDetailsByAddressResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: professional.weblogic.ch09.example3.property
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByAddress }
     * 
     */
    public GetPropertyDetailsByAddress createGetPropertyDetailsByAddress() {
        return new GetPropertyDetailsByAddress();
    }

    /**
     * Create an instance of {@link PropertyInfoFaultException }
     * 
     */
    public PropertyInfoFaultException createPropertyInfoFaultException() {
        return new PropertyInfoFaultException();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByIdResponse }
     * 
     */
    public GetPropertyDetailsByIdResponse createGetPropertyDetailsByIdResponse() {
        return new GetPropertyDetailsByIdResponse();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsById }
     * 
     */
    public GetPropertyDetailsById createGetPropertyDetailsById() {
        return new GetPropertyDetailsById();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByAddressResponse }
     * 
     */
    public GetPropertyDetailsByAddressResponse createGetPropertyDetailsByAddressResponse() {
        return new GetPropertyDetailsByAddressResponse();
    }

    /**
     * Create an instance of {@link PropertySearchAddress }
     * 
     */
    public PropertySearchAddress createPropertySearchAddress() {
        return new PropertySearchAddress();
    }

    /**
     * Create an instance of {@link PropertySearchId }
     * 
     */
    public PropertySearchId createPropertySearchId() {
        return new PropertySearchId();
    }

    /**
     * Create an instance of {@link PropertyInfo }
     * 
     */
    public PropertyInfo createPropertyInfo() {
        return new PropertyInfo();
    }

    /**
     * Create an instance of {@link PropertyInfoFault }
     * 
     */
    public PropertyInfoFault createPropertyInfoFault() {
        return new PropertyInfoFault();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyInfoFaultException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wrox.com/professional-weblogic/PropertySearchService", name = "PropertyInfoFaultException")
    public JAXBElement<PropertyInfoFaultException> createPropertyInfoFaultException(PropertyInfoFaultException value) {
        return new JAXBElement<PropertyInfoFaultException>(_PropertyInfoFaultException_QNAME, PropertyInfoFaultException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertyDetailsByAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wrox.com/professional-weblogic/PropertySearchService", name = "getPropertyDetailsByAddress")
    public JAXBElement<GetPropertyDetailsByAddress> createGetPropertyDetailsByAddress(GetPropertyDetailsByAddress value) {
        return new JAXBElement<GetPropertyDetailsByAddress>(_GetPropertyDetailsByAddress_QNAME, GetPropertyDetailsByAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertyDetailsById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wrox.com/professional-weblogic/PropertySearchService", name = "getPropertyDetailsById")
    public JAXBElement<GetPropertyDetailsById> createGetPropertyDetailsById(GetPropertyDetailsById value) {
        return new JAXBElement<GetPropertyDetailsById>(_GetPropertyDetailsById_QNAME, GetPropertyDetailsById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertyDetailsByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wrox.com/professional-weblogic/PropertySearchService", name = "getPropertyDetailsByIdResponse")
    public JAXBElement<GetPropertyDetailsByIdResponse> createGetPropertyDetailsByIdResponse(GetPropertyDetailsByIdResponse value) {
        return new JAXBElement<GetPropertyDetailsByIdResponse>(_GetPropertyDetailsByIdResponse_QNAME, GetPropertyDetailsByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertyDetailsByAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wrox.com/professional-weblogic/PropertySearchService", name = "getPropertyDetailsByAddressResponse")
    public JAXBElement<GetPropertyDetailsByAddressResponse> createGetPropertyDetailsByAddressResponse(GetPropertyDetailsByAddressResponse value) {
        return new JAXBElement<GetPropertyDetailsByAddressResponse>(_GetPropertyDetailsByAddressResponse_QNAME, GetPropertyDetailsByAddressResponse.class, null, value);
    }

}
