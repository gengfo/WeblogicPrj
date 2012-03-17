package com.bigrez.admin.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.domain.RoomType;
import com.bigrez.utils.FormUtils;

public class PropertyRoomForm implements Validator {
	
	private String id;
	private RoomType roomType;
	
	public String getId() {
		return id;
	}
	public void setId(String pId) {
		id = pId;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(RoomType pRoomType) {
		roomType = pRoomType;
	}

    @Override 
	public void validate(Object form, Errors errors)
	{		
		PropertyRoomForm roomform = (PropertyRoomForm) form;
		RoomType roomtype = roomform.getRoomType();
		FormUtils.assertNonEmpty(errors, roomtype.getDescription(), "error.roomtype.descriptionempty");
		FormUtils.assertNonZero(errors, roomtype.getMaximumAdults(), "error.roomtype.maxadultsempty");
		FormUtils.assertNonZero(errors, roomtype.getNumberOfRooms(), "error.roomtype.numroomsempty");
		FormUtils.assertNonEmpty(errors, roomtype.getFeatures(), "error.roomtype.featuresempty");
	}
	
	@SuppressWarnings("unchecked")
    @Override 
	public boolean supports(Class pClass)
	{
		return pClass.equals(PropertyRoomForm.class);
	}
	
}
