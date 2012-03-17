
package com.bigrez.ws.property;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bigrez.ws.property package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bigrez.ws.property
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByInfoResponse }
     * 
     */
    public GetPropertyDetailsByInfoResponse createGetPropertyDetailsByInfoResponse() {
        return new GetPropertyDetailsByInfoResponse();
    }

    /**
     * Create an instance of {@link PropertyInfo }
     * 
     */
    public PropertyInfo createPropertyInfo() {
        return new PropertyInfo();
    }

    /**
     * Create an instance of {@link PropertySearchId }
     * 
     */
    public PropertySearchId createPropertySearchId() {
        return new PropertySearchId();
    }

    /**
     * Create an instance of {@link PropertiesInfo }
     * 
     */
    public PropertiesInfo createPropertiesInfo() {
        return new PropertiesInfo();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByInfo }
     * 
     */
    public GetPropertyDetailsByInfo createGetPropertyDetailsByInfo() {
        return new GetPropertyDetailsByInfo();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsById }
     * 
     */
    public GetPropertyDetailsById createGetPropertyDetailsById() {
        return new GetPropertyDetailsById();
    }

    /**
     * Create an instance of {@link GetPropertyDetailsByIdResponse }
     * 
     */
    public GetPropertyDetailsByIdResponse createGetPropertyDetailsByIdResponse() {
        return new GetPropertyDetailsByIdResponse();
    }

    /**
     * Create an instance of {@link PropertySearchInfo }
     * 
     */
    public PropertySearchInfo createPropertySearchInfo() {
        return new PropertySearchInfo();
    }

}
