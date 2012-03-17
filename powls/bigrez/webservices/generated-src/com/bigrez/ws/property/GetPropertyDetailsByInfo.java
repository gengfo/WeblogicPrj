
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
 *         &lt;element ref="{http://www.bigrez.com/PropertyInfo}PropertySearchInfo"/>
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
    "propertySearchInfo"
})
@XmlRootElement(name = "GetPropertyDetailsByInfo")
public class GetPropertyDetailsByInfo {

    @XmlElement(name = "PropertySearchInfo", namespace = "http://www.bigrez.com/PropertyInfo", required = true)
    protected PropertySearchInfo propertySearchInfo;

    /**
     * Gets the value of the propertySearchInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PropertySearchInfo }
     *     
     */
    public PropertySearchInfo getPropertySearchInfo() {
        return propertySearchInfo;
    }

    /**
     * Sets the value of the propertySearchInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertySearchInfo }
     *     
     */
    public void setPropertySearchInfo(PropertySearchInfo value) {
        this.propertySearchInfo = value;
    }

}
