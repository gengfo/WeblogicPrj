package com.bigrez.admin.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.Property;
import com.bigrez.domain.RoomType;
import com.bigrez.service.PropertyServices;
import com.bigrez.service.ReservationServices;
import com.bigrez.utils.DateHelper;

public class PropertyAvailController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private ReservationServices reservationServices;
	private PropertyAvailForm propertyAvailForm;
	private String overridePropertyListView;
	
	final Logger logger = LoggingHelper.getServerLogger();
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop == null)
		{
			return new ModelAndView(getOverridePropertyListView());
		}
		return super.showForm(request, response, errors, controlModel);
	}
	
	@Override
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyAvailController::onBindOnNewForm()");
		PropertyAvailForm form = (PropertyAvailForm) command;
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop != null)
		{
			RoomType roomType = propertyServices.findRoomTypeByExternalIdentity(form.getRoomId());
			form.setRoomType(roomType);
			String editDateAsString = form.getEditDate();
			Date editDate = DateHelper.parse3(editDateAsString); // will be first of a month
			Date endDate = DateHelper.addMonths(editDate, 1); // query is exclusive on end date
			List<Integer> availabilityValues =
				reservationServices.calculateAvailability(roomType, editDate, endDate);
			List<String> availability = new ArrayList<String>();
			for (Integer value : availabilityValues)
			{
				if (value == ReservationServices.UNCONTROLLED)
				{
					// service returns -1 values for missing days, we show as blank
					availability.add(""); 
				}
				else
				{
					availability.add(value.toString());
				}
			}
			form.setAvailability(availability);
		}
		logger.log(Level.INFO,"PropertyAvailController::onBindOnNewForm() complete");
		return;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyAvailController::onSubmit("+command+")");
		PropertyAvailForm form = (PropertyAvailForm) command;
		RoomType roomtype = form.getRoomType();
		Date startDate = DateHelper.parse3(form.getEditDate());
		List<String> enteredStrings = form.getAvailability();
		List<Integer> availableRoomsByDay = new ArrayList<Integer>();
		for (String enteredString : enteredStrings)
		{
			if (StringUtils.isEmpty(enteredString))
			{
				availableRoomsByDay.add(Integer.valueOf(ReservationServices.UNCONTROLLED));
			}
			else
			{
				availableRoomsByDay.add(Integer.valueOf(enteredString)); // validator made sure it was numeric
			}
		}
		
		reservationServices.updateInventory(roomtype, startDate, availableRoomsByDay);
		
		logger.log(Level.INFO,"PropertyAvailController::onSubmit("+command+") complete");
		ModelAndView result = super.onSubmit(request, response, command, errors);
		result.addObject("startDate", form.getStartDate()); // help list page stay where it was
		return result;
	}
	
	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public ReservationServices getReservationServices() {
		return reservationServices;
	}

	public void setReservationServices(ReservationServices pReservationServices) {
		reservationServices = pReservationServices;
	}

	public PropertyAvailForm getPropertyAvailForm() {
		return propertyAvailForm;
	}

	public void setPropertyAvailForm(PropertyAvailForm pPropertyAvailForm) {
		propertyAvailForm = pPropertyAvailForm;
	}
	
	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
