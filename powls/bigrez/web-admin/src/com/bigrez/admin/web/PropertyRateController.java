package com.bigrez.admin.web;

import java.math.BigDecimal;
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

import com.bigrez.domain.Money;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.service.PropertyServices;
import com.bigrez.utils.DateHelper;

public class PropertyRateController extends SimpleFormController {
	
	private PropertyServices propertyServices;
	private PropertyRateForm propertyRateForm;
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
		logger.log(Level.INFO,"PropertyRateController::onBindOnNewForm()");
		PropertyRateForm form = (PropertyRateForm) command;
		RoomType roomtype = propertyServices.findRoomTypeByExternalIdentity(form.getRoomId());
		form.setRoomType(roomtype); // owning roomtype
		List<Rate> rates = propertyServices.findRatesByRoomType(roomtype);
		form.setRates(rates); // all rates in this roomtype for display in page
		// If the id was passed in, fetch the Ratetype. If not, create a new blank one.
		if (StringUtils.isEmpty(form.getId()))
		{
			Rate rate = new Rate();
			rate.setPrice(new Money(new BigDecimal(0)));
			form.setRate(rate);
		}
		else
		{
			// We fetch a separate copy of this object here, rather than finding it in the
			// session version of Property, so we can safely use it to back the form.
			Rate rate = propertyServices.findRateByExternalIdentity(form.getId());
			form.setRate(rate);
            form.setStartDate(DateHelper.format1(rate.getStartDate()));
            form.setEndDate(DateHelper.format1(rate.getEndDate()));
		}
		logger.log(Level.INFO,"PropertyRateController::onBindOnNewForm() complete");
		return;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"PropertyRateController::onSubmit("+command+")");
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		// This form posts textual changes directly into the Rate attached to the form
		PropertyRateForm form = (PropertyRateForm) command;
		Rate rate = form.getRate();
		if ("delete".equals(request.getParameter("action")))
		{
			propertyServices.delete(rate);
		}
		else
		{
			rate.setStartDate(DateHelper.parse1(form.getStartDate()));
			rate.setEndDate(DateHelper.parse1(form.getEndDate()));
			if (StringUtils.isEmpty(form.getId()))
			{
				// We need to connect this new Rate with our stored roomtype
				rate.setRoomType(form.getRoomType());
			}
			// TODO handle Optimistic lock problems..
			propertyServices.createOrUpdate(rate); // handles both fine
		}
		
		// re-fetch the entire Property lattice to update the session copy
		Property updatedprop = propertyServices.findPropertyByExternalIdentity(prop.getExternalIdentity());
		request.getSession().setAttribute("currentProperty", updatedprop);
		
		logger.log(Level.INFO,"PropertyRateController::onSubmit("+command+") complete");
		return super.onSubmit(request, response, command, errors);
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public PropertyRateForm getPropertyRateForm() {
		return propertyRateForm;
	}

	public void setPropertyRateForm(PropertyRateForm pPropertyRateForm) {
		propertyRateForm = pPropertyRateForm;
	}
	
	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}


}
