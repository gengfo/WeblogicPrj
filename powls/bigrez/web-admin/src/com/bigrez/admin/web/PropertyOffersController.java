package com.bigrez.admin.web;

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

public class PropertyOffersController extends SimpleFormController {
	
	private PropertyOffersForm propertyOffersForm;
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
		logger.log(Level.INFO,"PropertyOffersController::onBindOnNewForm()");
		PropertyOffersForm form = (PropertyOffersForm) command;
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop != null)
		{
			form.setOffers(prop.getOffers()); // just for convenience on page
		}
		logger.log(Level.INFO,"PropertyOffersController::onBindOnNewForm() complete");
		return;
	}
	
	public PropertyOffersForm getPropertyOffersForm() {
		return propertyOffersForm;
	}

	public void setPropertyOffersForm(PropertyOffersForm pPropertyOffersForm) {
		propertyOffersForm = pPropertyOffersForm;
	}
	
	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
