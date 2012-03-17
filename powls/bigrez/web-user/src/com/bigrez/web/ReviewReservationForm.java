package com.bigrez.web;

import java.io.Serializable;
import java.util.List;

import com.bigrez.domain.Property;
import com.bigrez.domain.Reservation;
import com.bigrez.service.impl.ReservationServicesImpl;

public class ReviewReservationForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private Reservation reservation;
	private List<ReservationServicesImpl.RateDetails> reservationRates;
	private Property property;
	
	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation pReservation) {
		reservation = pReservation;
	}

	public List<ReservationServicesImpl.RateDetails> getReservationRates() {
		return reservationRates;
	}

	public void setReservationRates(List<ReservationServicesImpl.RateDetails> pReservationRates) {
		reservationRates = pReservationRates;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property pProperty) {
		property = pProperty;
	}
		
}
