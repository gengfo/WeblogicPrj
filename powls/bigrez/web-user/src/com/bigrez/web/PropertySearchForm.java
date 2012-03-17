package com.bigrez.web;

import java.util.List;

import com.bigrez.domain.Property;

public class PropertySearchForm {
	
	private String city;
	private String stateCode;
	private List<Property> propertyList;
	
	public String getCity() {
		return city;
	}
	public void setCity(String pCity) {
		city = pCity;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String pStateCode) {
		stateCode = pStateCode;
	}
	public List<Property> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<Property> pPropertyList) {
		propertyList = pPropertyList;
	}

}
