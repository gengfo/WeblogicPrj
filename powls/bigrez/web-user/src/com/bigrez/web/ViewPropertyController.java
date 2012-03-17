package com.bigrez.web;

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

public class ViewPropertyController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private ViewPropertyForm viewPropertyForm;
	final Logger logger = LoggingHelper.getServerLogger();
		
	@Override
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"ViewPropertyController::onBindOnNewForm()");
		String id = ((ViewPropertyForm) command).getId();
		Property prop = propertyServices.findPropertyByExternalIdentity(id);
		((ViewPropertyForm) command).setProperty(prop);
		logger.log(Level.INFO,"ViewPropertyController::onBindOnNewForm() complete");
	}

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"ViewPropertyController::onSubmit("+command+")");
		String id = ((ViewPropertyForm) command).getId();
		logger.log(Level.INFO, "ViewPropertyForm.getId()=", id);
		Property prop = propertyServices.findPropertyByExternalIdentity(id);
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		rezinfo.setProperty(prop);
		rezinfo.setPropertyId(prop.getExternalIdentity());
		rezinfo.clearRoomType();
		rezinfo.clearRates();
		logger.log(Level.INFO,"ViewPropertyController::onSubmit("+command+") complete");
		ModelAndView result = super.onSubmit(request, response, command, errors);
		// result.addObject("greg", "wow");
		return result;
	}


	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public ViewPropertyForm getViewPropertyForm() {
		return viewPropertyForm;
	}

	public void setViewPropertyForm(ViewPropertyForm viewPropertyForm) {
		this.viewPropertyForm = viewPropertyForm;
	}

}
