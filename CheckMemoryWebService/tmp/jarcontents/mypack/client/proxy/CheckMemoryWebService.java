
package mypack.client.proxy;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.5
 * Generated source version: 2.1
 * 
 */
@WebService(name = "CheckMemoryWebService", targetNamespace = "http://gengfo/ws/CheckMemoryWebService")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface CheckMemoryWebService {


    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "check", targetNamespace = "http://gengfo/ws/CheckMemoryWebService", className = "mypack.client.proxy.Check")
    @ResponseWrapper(localName = "checkResponse", targetNamespace = "http://gengfo/ws/CheckMemoryWebService", className = "mypack.client.proxy.CheckResponse")
    public void check();

}
