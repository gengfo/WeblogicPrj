package com.bigrez.admin.web;

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
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.service.PropertyServices;

public class PropertyRoomController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertyRoomForm propertyRoomForm;
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
		logger.log(Level.INFO,"PropertyRoomController::onBindOnNewForm()");
		PropertyRoomForm form = (PropertyRoomForm) command;
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop == null)
		{
			// need to shortcircuit here in case they bookmarked and have not chosen property..
			return;
		}
		// If the id was passed in, fetch the roomtype. If not, create a new blank one.
		if (StringUtils.isEmpty(form.getId()))
		{
			RoomType roomtype = new RoomType();
			form.setRoomType(roomtype);
		}
		else
		{
			// We fetch a separate copy of this object here, rather than finding it in the
			// session version of Property, so we can safely use it to back the form.
			RoomType roomtype = propertyServices.findRoomTypeByExternalIdentity(form.getId());
			form.setRoomType(roomtype);
		}
		logger.log(Level.INFO,"PropertyRoomController::onBindOnNewForm() complete");
		return;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyRoomController::onSubmit("+command+")");
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		// This form posts textual changes directly into the roomtype attached to the form
		PropertyRoomForm form = (PropertyRoomForm) command;
		RoomType roomtype = form.getRoomType();
		if ("delete".equals(request.getParameter("action")))
		{
			// Special case where the user deletes a roomtype instead of saving changes
			List<Rate> rates = propertyServices.findRatesByRoomType(roomtype);
			if (rates.size() > 0)
			{
				errors.reject("error.roomtype.ratesexist");
		        ModelAndView result = super.onSubmit(request, response, command, errors);
		        result.setViewName("propertyroom"); // redisplay the page again
		        return result;
			}
			try
			{
				propertyServices.delete(roomtype);
			}
			catch (Exception e)
			{
				errors.reject("error.roomtype.reservationsexist");
		        ModelAndView result = super.onSubmit(request, response, command, errors);
		        result.setViewName("propertyroom"); // redisplay the page again
		        return result;
			}
		}
		else 
		{
			// User saved changes, might be either insert or update
			if (StringUtils.isEmpty(form.getId()))
			{
				// We need to connect this new roomtype with our stored property
				roomtype.setProperty(prop);
			}
			propertyServices.createOrUpdate(roomtype);
		}
		
		// re-fetch the entire Property lattice to update the session copy
		Property updatedprop = propertyServices.findPropertyByExternalIdentity(prop.getExternalIdentity());
		request.getSession().setAttribute("currentProperty", updatedprop);
		
		logger.log(Level.INFO,"PropertyRoomController::onSubmit("+command+") complete");
		return super.onSubmit(request, response, command, errors);
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertyRoomForm getPropertyRoomForm() {
		return propertyRoomForm;
	}

	public void setPropertyRoomForm(PropertyRoomForm pPropertyRoomForm) {
		propertyRoomForm = pPropertyRoomForm;
	}
	
	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
