
package mypack;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mypack package. 
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

    private final static QName _SayHelloTo_QNAME = new QName("http://gengfo/ws/HelloService", "sayHelloTo");
    private final static QName _SayHelloToResponse_QNAME = new QName("http://gengfo/ws/HelloService", "sayHelloToResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mypack
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SayHelloToResponse }
     * 
     */
    public SayHelloToResponse createSayHelloToResponse() {
        return new SayHelloToResponse();
    }

    /**
     * Create an instance of {@link SayHelloTo }
     * 
     */
    public SayHelloTo createSayHelloTo() {
        return new SayHelloTo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloTo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gengfo/ws/HelloService", name = "sayHelloTo")
    public JAXBElement<SayHelloTo> createSayHelloTo(SayHelloTo value) {
        return new JAXBElement<SayHelloTo>(_SayHelloTo_QNAME, SayHelloTo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloToResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://gengfo/ws/HelloService", name = "sayHelloToResponse")
    public JAXBElement<SayHelloToResponse> createSayHelloToResponse(SayHelloToResponse value) {
        return new JAXBElement<SayHelloToResponse>(_SayHelloToResponse_QNAME, SayHelloToResponse.class, null, value);
    }

}
