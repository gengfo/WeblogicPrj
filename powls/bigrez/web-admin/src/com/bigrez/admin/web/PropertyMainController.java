package com.bigrez.admin.web;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.Property;
import com.bigrez.service.PropertyServices;

public class PropertyMainController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertyMainForm propertyMainForm;
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
		logger.log(Level.INFO,"PropertyMainController::onBindOnNewForm()");
		PropertyMainForm form = (PropertyMainForm) command;
		String id = form.getId();
		Property prop = null;
		if (StringUtils.isNotEmpty(id))
		{
			// specific property was chosen and passed to us, honor that choice
			prop = propertyServices.findPropertyByExternalIdentity(id);
			logger.log(Level.INFO,"Read property from database: "+prop);
			request.getSession().setAttribute("currentProperty", prop);
		}
		else
		{
			// no property choice specified, use Property object in session
			prop = (Property) request.getSession().getAttribute("currentProperty");
			logger.log(Level.INFO,"Grabbed property from session: "+prop);
		}
		// The form has copies of the property and address fields to avoid binding
		// into the actual session version of the property object
		BeanUtils.copyProperties(prop, form, new String[]{"roomTypes","offers","address"});
		BeanUtils.copyProperties(prop.getAddress(), form);
		logger.log(Level.INFO,"PropertyMainController::onBindOnNewForm() complete");
	}

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyMainController::onSubmit("+command+")");
		PropertyMainForm form = (PropertyMainForm) command;
		// Copy the field values back into the session version of the property
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		BeanUtils.copyProperties(form, prop, new String[]{"id"});
		BeanUtils.copyProperties(form, prop.getAddress());

		// update the property in the database
		propertyServices.createOrUpdate(prop);
		
		// re-fetch the entire Property lattice to update the session copy
		Property updatedprop = propertyServices.findPropertyByExternalIdentity(prop.getExternalIdentity());
		request.getSession().setAttribute("currentProperty", updatedprop);
		
		logger.log(Level.INFO,"PropertyMainController::onSubmit("+command+") complete");
		ModelAndView result = super.onSubmit(request, response, command, errors);
		result.addObject("success","true");
		return result;
	}


	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertyMainForm getPropertyMainForm() {
		return propertyMainForm;
	}

	public void setPropertyMainForm(PropertyMainForm pPropertyMainForm) {
		propertyMainForm = pPropertyMainForm;
	}

	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
