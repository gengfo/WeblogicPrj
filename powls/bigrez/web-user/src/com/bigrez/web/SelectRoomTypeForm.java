package com.bigrez.web;

import java.io.Serializable;
import java.util.List;

import com.bigrez.domain.Property;
import com.bigrez.service.ReservationServices.AvailabilityAndRates;

public class SelectRoomTypeForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    String id;
	private Property property;
	private List<AvailabilityAndRates> availRates;

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

	public List<AvailabilityAndRates> getAvailRates() {
		return availRates;
	}

	public void setAvailRates(List<AvailabilityAndRates> pAvailRates) {
		availRates = pAvailRates;
	}
	
}
