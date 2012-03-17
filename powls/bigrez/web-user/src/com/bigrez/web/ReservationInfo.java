package com.bigrez.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bigrez.domain.GuestProfile;
import com.bigrez.domain.Property;
import com.bigrez.domain.RoomType;
import com.bigrez.service.ReservationServices;

public class ReservationInfo implements Serializable {

	private String lastSearchCity = "";
	private String lastSearchState = "";

	private String propertyId;
	private Property property;

	private String roomTypeId;
	private RoomType roomType;

	private String guestProfileId = "";
	private GuestProfile guestProfile = new GuestProfile();

	private Date arriveDate;
	private Date departDate;

	private List<ReservationServices.RateDetails> rezRates;

	// Public Methods

	public ReservationInfo() {
		clear();
	}

	public void clear() {
		clearAllButProfile();
		clearProfile();
	}

	public void clearAllButProfile() {
		propertyId = "";
		property = null;
		arriveDate = null;
		departDate = null;
		lastSearchCity = "";
		lastSearchState = "";
		clearRoomType();
		clearRates();
	}

	public void clearProfile() {
		guestProfileId = "";
		guestProfile = null;
	}

	public void clearRoomType() {
		roomTypeId = "";
		roomType = null;
	}

	public void clearRates() {
		rezRates = new ArrayList<ReservationServices.RateDetails>();
	}

	public String getOfferHash() {
		// return a hash string of data used to fetch offers
		return "["+lastSearchCity+","+lastSearchState+","+propertyId+"]";
	}

	public String getLastSearchCity() {
		return lastSearchCity;
	}

	public void setLastSearchCity(String pLastSearchCity) {
		lastSearchCity = pLastSearchCity;
	}

	public String getLastSearchState() {
		return lastSearchState;
	}

	public void setLastSearchState(String pLastSearchState) {
		lastSearchState = pLastSearchState;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String pPropertyId) {
		propertyId = pPropertyId;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property pProperty) {
		property = pProperty;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String pRoomTypeId) {
		roomTypeId = pRoomTypeId;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType pRoomType) {
		roomType = pRoomType;
	}

	public String getGuestProfileId() {
		return guestProfileId;
	}

	public void setGuestProfileId(String pGuestProfileId) {
		guestProfileId = pGuestProfileId;
	}

	public GuestProfile getGuestProfile() {
		return guestProfile;
	}

	public void setGuestProfile(GuestProfile pGuestProfile) {
		guestProfile = pGuestProfile;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date pArriveDate) {
		arriveDate = pArriveDate;
	}

	public Date getDepartDate() {
		return departDate;
	}

	public void setDepartDate(Date pDepartDate) {
		departDate = pDepartDate;
	}

	public List<ReservationServices.RateDetails> getRezRates() {
		return rezRates;
	}

	public void setRezRates(List<ReservationServices.RateDetails> pRezRates) {
		rezRates = pRezRates;
	}
	
}
