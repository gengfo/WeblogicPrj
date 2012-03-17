
package com.bigrez.ws.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.bigrez.com/PropertyInfo}PropertiesInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "propertiesInfo"
})
@XmlRootElement(name = "GetPropertyDetailsByInfoResponse")
public class GetPropertyDetailsByInfoResponse {

    @XmlElement(name = "PropertiesInfo", namespace = "http://www.bigrez.com/PropertyInfo", required = true)
    protected PropertiesInfo propertiesInfo;

    /**
     * Gets the value of the propertiesInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesInfo }
     *     
     */
    public PropertiesInfo getPropertiesInfo() {
        return propertiesInfo;
    }

    /**
     * Sets the value of the propertiesInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesInfo }
     *     
     */
    public void setPropertiesInfo(PropertiesInfo value) {
        this.propertiesInfo = value;
    }

}
