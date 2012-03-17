package com.bigrez.admin.web;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.utils.DateHelper;
import com.bigrez.utils.FormUtils;

public class PropertyRateForm implements Validator {
	
	private String id;
	private String roomId;
	private RoomType roomType;
	private List<Rate> rates;
	private Rate rate;
	private String startDate;
	private String endDate;
	
	public String getId() {
		return id;
	}
	public void setId(String pId) {
		id = pId;
	}
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
	public List<Rate> getRates() {
		return rates;
	}
	public void setRates(List<Rate> pRates) {
		rates = pRates;
	}
	public Rate getRate() {
		return rate;
	}
	public void setRate(Rate pRate) {
		rate = pRate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String pStartDate) {
		startDate = pStartDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String pEndDate) {
		endDate = pEndDate;
	}
	
    @Override 
	public void validate(Object form, Errors errors)
	{		
		PropertyRateForm rateform = (PropertyRateForm) form;
		Rate rate = rateform.getRate();
		if (FormUtils.assertNonEmpty(errors, rateform.getStartDate(), "error.rate.startempty")) {
			FormUtils.assertValidDate(errors, rateform.getStartDate(), "error.rate.startinvalid");
		}
		if (FormUtils.assertNonEmpty(errors, rateform.getEndDate(), "error.rate.endempty")) {
			FormUtils.assertValidDate(errors, rateform.getEndDate(), "error.rate.endinvalid");
		}
		if (errors.getErrorCount() == 0)
		{
			try {
				Date start = DateHelper.parse1(rateform.getStartDate());
				Date end = DateHelper.parse1(rateform.getEndDate());
				if (start.after(end)) {
					errors.reject("error.rate.endbeforestart");
				}
			} catch (ParseException e) {
				errors.reject("error.validationproblem");
			}
		}
		FormUtils.assertNonZero(errors, rate.getPrice().getAmount(), "error.rate.rateempty");
	}
	
	@SuppressWarnings("unchecked")
    @Override 
    public boolean supports(Class pClass)
	{
		return pClass.equals(PropertyRateForm.class);
	}
	
}
