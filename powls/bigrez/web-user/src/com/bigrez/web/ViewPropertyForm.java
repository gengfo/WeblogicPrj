package com.bigrez.web;

import java.io.Serializable;

import com.bigrez.domain.Property;

public class ViewPropertyForm implements Serializable {
	
	private static final long serialVersionUID = 1;
	
    String id;
	private Property property;

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property pProperty) {
		property = pProperty;
	}
	
}
