package com.bigrez.admin.web;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.Property;
import com.bigrez.service.PropertyServices;

public class PropertyListController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertyListForm propertyListForm;
	final Logger logger = LoggingHelper.getServerLogger();
		
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyListController::onBindOnNewForm()");
		PropertyListForm form = (PropertyListForm) command;
		List<Property> propertyList = propertyServices.findAll();
		form.setPropertyList(propertyList);
		// Load the username in the session to make it easier for page to find it.
		String username = request.getRemoteUser();
		if (StringUtils.isNotEmpty(username))
		{
			request.getSession().setAttribute("username", username);
		}
		logger.log(Level.INFO,"PropertyListController::onBindOnNewForm() complete");
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertyListForm getPropertyListForm() {
		return propertyListForm;
	}

	public void setPropertyListForm(PropertyListForm pPropertyListForm) {
		propertyListForm = pPropertyListForm;
	}


}
