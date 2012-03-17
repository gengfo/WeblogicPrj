package com.bigrez.admin.web;

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
import com.bigrez.service.ReservationServices;
import com.bigrez.service.ReservationServices.AvailabilitySummary;
import com.bigrez.utils.DateHelper;

public class PropertyAvailsController extends SimpleFormController {
	
	private ReservationServices reservationServices;
	private PropertyAvailsForm propertyAvailsForm;
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
		logger.log(Level.INFO,"PropertyAvailsController::onBindOnNewForm()");
		PropertyAvailsForm form = (PropertyAvailsForm) command;
		Property prop = (Property) request.getSession().getAttribute("currentProperty");
		if (prop != null)
		{
			String startDateAsString = form.getStartDate();
			Date startDate = new Date();
			if (StringUtils.isNotEmpty(startDateAsString))
			{
				try {
					startDate = DateHelper.parse3(startDateAsString);
				} catch (Exception ignore) {}
			}
			int numberOfMonths = 9;
			List<AvailabilitySummary> availsummaries =
				reservationServices.calculateAvailabilitySummary(prop, startDate, numberOfMonths);
			form.setAvailabilitySummaries(availsummaries);
			form.setStartDate(DateHelper.format3(startDate));
			form.setNumMonths(numberOfMonths);
		}
		logger.log(Level.INFO,"PropertyAvailsController::onBindOnNewForm() complete");
		return;
	}

	public ReservationServices getReservationServices() {
		return reservationServices;
	}

	public void setReservationServices(ReservationServices pReservationServices) {
		reservationServices = pReservationServices;
	}

	public PropertyAvailsForm getPropertyAvailsForm() {
		return propertyAvailsForm;
	}

	public void setPropertyAvailsForm(PropertyAvailsForm pPropertyAvailsForm) {
		propertyAvailsForm = pPropertyAvailsForm;
	}

	public String getOverridePropertyListView() {
		return overridePropertyListView;
	}

	public void setOverridePropertyListView(String overridePropertyListView) {
		this.overridePropertyListView = overridePropertyListView;
	}

}
