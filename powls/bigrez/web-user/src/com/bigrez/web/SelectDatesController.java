package com.bigrez.web;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.utils.DateHelper;

public class SelectDatesController extends SimpleFormController {
	
	private SelectDatesForm selectDatesForm;
	final Logger logger = LoggingHelper.getServerLogger();
		
	@Override
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"SelectDatesController::onBindOnNewForm()");
		// pre-populate the arrive and depart date on the form from the Reservation Info
		SelectDatesForm form = (SelectDatesForm) command;
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
        if (rezinfo.getArriveDate() != null) {
            form.setArriveDate(DateHelper.format1(rezinfo.getArriveDate()));
            form.setDepartDate(DateHelper.format1(rezinfo.getDepartDate()));
	    } else {
	        Calendar now = Calendar.getInstance();
	        now.set(Calendar.HOUR, 0);// get midnight date/time
	        now.set(Calendar.MINUTE, 0);
	        now.set(Calendar.SECOND, 0);
	        form.setArriveDate(DateHelper.format1(now.getTime()));
	        now.add(Calendar.DAY_OF_MONTH, 1);
	        form.setDepartDate(DateHelper.format1(now.getTime()));
	    }
		logger.log(Level.INFO,"SelectDatesController::onBindOnNewForm() complete");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"SelectDatesController::onSubmit()");
		SelectDatesForm form = (SelectDatesForm) command;
		// update the ReservationInfo in the session to reflect this location info
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		rezinfo.setArriveDate(DateHelper.parse1(form.getArriveDate()));
		rezinfo.setDepartDate(DateHelper.parse1(form.getDepartDate()));
		logger.log(Level.INFO,"SelectDatesController::onSubmit() complete");
		return super.onSubmit(request, response, command, errors);
	}

	public SelectDatesForm getSelectDatesForm() {
		return selectDatesForm;
	}

	public void setSelectDatesForm(SelectDatesForm pSelectDatesForm) {
		selectDatesForm = pSelectDatesForm;
	}

}
