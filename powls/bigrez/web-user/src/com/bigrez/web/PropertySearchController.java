package com.bigrez.web;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.Property;
import com.bigrez.service.PropertyServices;

public class PropertySearchController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertySearchForm propertySearchForm;
	final Logger logger = LoggingHelper.getServerLogger();
		
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"PropertySearchController::onBindOnNewForm()");

		logger.log(Level.INFO,"PropertySearchController::onBindOnNewForm() complete");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"PropertySearchController::onSubmit()");
		PropertySearchForm form = (PropertySearchForm) command;
		List<Property> propertyList = propertyServices.findByCityAndState(form.getCity(), form.getStateCode());
		form.setPropertyList(propertyList);
		// update the ReservationInfo in the session to reflect this location info
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		rezinfo.setLastSearchCity(form.getCity());
		rezinfo.setLastSearchState(form.getStateCode());
		logger.log(Level.INFO,"PropertySearchController::onSubmit() complete");
		return super.onSubmit(request, response, command, errors);
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertySearchForm getPropertySearchForm() {
		return propertySearchForm;
	}

	public void setPropertySearchForm(PropertySearchForm pPropertySearchForm) {
		propertySearchForm = pPropertySearchForm;
	}


}
