package com.bigrez.admin.web;

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

import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.service.PropertyServices;

public class PropertyRatesController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertyRatesForm propertyRatesForm;
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
		logger.log(Level.INFO,"PropertyRatesController::onBindOnNewForm()");
		PropertyRatesForm form = (PropertyRatesForm) command;
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop != null)
		{
			List<RoomType> roomTypes = prop.getRoomTypes();
			for (RoomType room : roomTypes)
			{
				logger.log(Level.INFO,"Calling findRatesByRoomtype with roomtype "+room);
				List<Rate> rates = propertyServices.findRatesByRoomType(room);
				for (Rate rate : rates)
				{
					logger.log(Level.INFO,"Rate: "+rate);
				}
				form.addRateListToMap(room, rates);
			}
			logger.log(Level.INFO,"Final map contents: "+form.getRatesByRoomType());
			form.setRoomTypes(roomTypes);
		}
		logger.log(Level.INFO,"PropertyRatesController::onBindOnNewForm() complete");
		return;
	}
	
	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertyRatesForm getPropertyRatesForm() {
		return propertyRatesForm;
	}

	public void setPropertyRatesForm(PropertyRatesForm pPropertyRatesForm) {
		propertyRatesForm = pPropertyRatesForm;
	}
	
	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
