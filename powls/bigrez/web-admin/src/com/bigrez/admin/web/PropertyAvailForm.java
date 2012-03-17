package com.bigrez.admin.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.domain.RoomType;
import com.bigrez.utils.DateHelper;
import com.bigrez.utils.FormUtils;

public class PropertyAvailForm implements Validator {
	
	private String roomId;
	private RoomType roomType;
	private List<String> availability; // use string to allow empties
	private String startDate;
	private String editDate;
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String pRoomId) {
		roomId = pRoomId;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(RoomType pRoomType) {
		roomType = pRoomType;
	}
	public List<String> getAvailability() {
		return availability;
	}
	public void setAvailability(List<String> pAvailability) {
		availability = pAvailability;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String pStartDate) {
		startDate = pStartDate;
	}
	public String getEditDate() {
		return editDate;
	}
	public void setEditDate(String pEditDate) {
		editDate = pEditDate;
	}
	
	public String getEditDateWithDay()
	throws Exception
	{
		Date dd = DateHelper.parse3(getEditDate());
		return DateHelper.format1(dd);
	}
	
	@Override 
	public void validate(Object form, Errors errors)
	{		
		PropertyAvailForm availform = (PropertyAvailForm) form;
		for (String value : availform.getAvailability())
		{
			if (StringUtils.isNotEmpty(value))
			{
				FormUtils.assertInteger(errors, value, "error.avail.nonnumeric");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
    @Override 
    public boolean supports(Class pClass)
	{
		return pClass.equals(PropertyAvailForm.class);
	}

}
