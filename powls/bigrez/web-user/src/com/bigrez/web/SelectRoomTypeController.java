package com.bigrez.web;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.RoomType;
import com.bigrez.service.PropertyServices;
import com.bigrez.service.ReservationServices;
import com.bigrez.service.ReservationServices.AvailabilityAndRates;

public class SelectRoomTypeController extends SimpleFormController {
	
	private ReservationServices reservationServices;
	private PropertyServices propertyServices;
	private SelectRoomTypeForm selectRoomTypeForm;
	private String overrideChoosePropertyView;
	private String overrideSelectDatesView;
	
	final Logger logger = LoggingHelper.getServerLogger();
		
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		if (rezinfo.getProperty() == null)
		{
			return new ModelAndView(getOverrideChoosePropertyView());
		}
		else if (rezinfo.getArriveDate() == null || rezinfo.getDepartDate() == null)
		{
			return new ModelAndView(getOverrideSelectDatesView());
		}
		return super.showForm(request, response, errors, controlModel);
	}

	@Override
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"SelectRoomTypeController::onBindOnNewForm()");
		SelectRoomTypeForm form = (SelectRoomTypeForm) command;
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		if (rezinfo.getProperty() != null && rezinfo.getArriveDate() != null && rezinfo.getDepartDate() != null)
		{
			// everything is fine, do the rate/availability search using the given info
			List<AvailabilityAndRates> availrates = reservationServices.calculateRatesAndAvailabilty(
				rezinfo.getProperty(), rezinfo.getArriveDate(),	rezinfo.getDepartDate());
			form.setProperty(rezinfo.getProperty());
			form.setAvailRates(availrates);
		}
		logger.log(Level.INFO,"SelectRoomTypeController::onBindOnNewForm() complete");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"SelectRoomTypeController::onSubmit()");
		SelectRoomTypeForm form = (SelectRoomTypeForm) command;
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		RoomType roomType = propertyServices.findRoomTypeByExternalIdentity(form.getId());
        List<ReservationServices.RateDetails> rateDetails =
        	reservationServices.calculateRates(roomType, rezinfo.getArriveDate(), rezinfo.getDepartDate());
        rezinfo.setRoomTypeId(form.getId());
        rezinfo.setRoomType(roomType);
        rezinfo.setRezRates(rateDetails);
		logger.log(Level.INFO,"SelectRoomTypeController::onSubmit() complete, calling super.onSubmit()");
		return super.onSubmit(request, response, command, errors);
	}


	public ReservationServices getReservationServices() {
		return reservationServices;
	}

	public void setReservationServices(ReservationServices pReservationServices) {
		reservationServices = pReservationServices;
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public SelectRoomTypeForm getSelectRoomTypeForm() {
		return selectRoomTypeForm;
	}

	public void setSelectRoomTypeForm(SelectRoomTypeForm pSelectRoomTypeForm) {
		selectRoomTypeForm = pSelectRoomTypeForm;
	}

	public String getOverrideChoosePropertyView() {
		return overrideChoosePropertyView;
	}

	public void setOverrideChoosePropertyView(String overrideChoosePropertyView) {
		this.overrideChoosePropertyView = overrideChoosePropertyView;
	}

	public String getOverrideSelectDatesView() {
		return overrideSelectDatesView;
	}

	public void setOverrideSelectDatesView(String overrideSelectDatesView) {
		this.overrideSelectDatesView = overrideSelectDatesView;
	}

}
