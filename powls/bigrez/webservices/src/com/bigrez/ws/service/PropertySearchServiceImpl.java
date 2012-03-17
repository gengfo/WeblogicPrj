package com.bigrez.ws.service;

import java.util.List;

import javax.jws.WebService;
import javax.ejb.EJB;
import javax.xml.ws.WebServiceException;

import com.bigrez.ws.property.PropertySearchService;
import com.bigrez.ws.property.PropertySearchInfo;
import com.bigrez.ws.property.PropertySearchId;
import com.bigrez.ws.property.PropertiesInfo;
import com.bigrez.ws.property.PropertyInfo;

import com.bigrez.domain.Property;
import com.bigrez.service.PropertyServices;
import com.bigrez.service.NotFoundException;


@WebService(serviceName = "PropertySearchService",
            targetNamespace = "http://www.bigrez.com/PropertySearchService",
            endpointInterface = "com.bigrez.ws.property.PropertySearchService")
public class PropertySearchServiceImpl implements PropertySearchService {
  @EJB private PropertyServices propertyServices;

  public PropertiesInfo getPropertyDetailsByInfo(PropertySearchInfo searchInfo) {
    PropertiesInfo propertiesInfo = new PropertiesInfo();
    List<PropertyInfo> propertyInfoList = propertiesInfo.getPropertyInfo();

    for (Property property :
                  propertyServices.findByCityAndState(searchInfo.getCity(),
                                                      searchInfo.getState())) {
      propertyInfoList.add(convertPropertyToPropertyInfo(property));
    }

    return propertiesInfo;
  }

  public PropertiesInfo getPropertyDetailsById(PropertySearchId searchId) {
    PropertiesInfo propertiesInfo = new PropertiesInfo();
    List<PropertyInfo> propertyInfoList = propertiesInfo.getPropertyInfo();

    try {
      Property property =
        propertyServices.findPropertyByExternalIdentity(searchId.getId());
      propertyInfoList.add(convertPropertyToPropertyInfo(property));
    }
    catch (IllegalArgumentException iae) {
      throw new WebServiceException(
        "Illegal format for property id: " + searchId.getId());
    }
    catch (NotFoundException nfe) {
      throw new WebServiceException(nfe.getMessage());
    }

    return propertiesInfo;
  }

  private static PropertyInfo convertPropertyToPropertyInfo(Property property) {
    PropertyInfo propertyInfo = new PropertyInfo();
    
    propertyInfo.setId(property.getExternalIdentity());
    propertyInfo.setDescription(property.getDescription());
    propertyInfo.setFeatures(property.getFeatures());
    propertyInfo.setAddress1(property.getAddress().getAddress1());
    propertyInfo.setAddress2(property.getAddress().getAddress2());
    propertyInfo.setCity(property.getAddress().getCity());
    propertyInfo.setState(property.getAddress().getStateCode());
    propertyInfo.setPostalCode(property.getAddress().getPostalCode());
    propertyInfo.setPhone(property.getPhone());
    
    return propertyInfo;
  }
}
