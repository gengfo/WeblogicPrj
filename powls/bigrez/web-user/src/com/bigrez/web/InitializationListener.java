package com.bigrez.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import weblogic.logging.LoggingHelper;

import com.bigrez.service.PropertyServices;

/**  
 * Class used to initialize certain context information for use in web application 
 */
public class InitializationListener implements ServletContextListener {
	@EJB private PropertyServices propertyServices;
	
	final Logger logger = LoggingHelper.getServerLogger();

	public void contextInitialized(ServletContextEvent event) {
		logger.log(Level.INFO,"Initializing constants and collections");
		performInitialization(event);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
	}
	
	/**
	 *  Method that performs any required web-application context initialization
	 *
	 * @param  event
	 */
	private void performInitialization(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		
		List<String> stateCodeList = propertyServices.getAllStateCodes();
		logger.log(Level.INFO,"Initializing states: " + stateCodeList);
		context.setAttribute("stateCodeList", LabelValue.makeLabelValues(stateCodeList));

		List<String> cityList = propertyServices.getAllCities();
		logger.log(Level.INFO,"Initializing cities: " + cityList);
		context.setAttribute("cityList", LabelValue.makeLabelValues(cityList));

		List<LabelValue> cardTypeList = new ArrayList<LabelValue>();
		cardTypeList.add(new LabelValue("American Express"));
		cardTypeList.add(new LabelValue("Diner's Club"));
		cardTypeList.add(new LabelValue("Mastercard"));
		cardTypeList.add(new LabelValue("Visa"));
		logger.log(Level.INFO,"Initializing card types: " + cardTypeList);
		context.setAttribute("cardTypeList", cardTypeList);

		List<LabelValue> cardExpList = new ArrayList<LabelValue>();
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MMM yyyy");
		for (int kk = 0; kk < 48; kk++) {
			String exp = df.format(now.getTime());
			cardExpList.add(new LabelValue(exp));
			now.add(Calendar.MONTH, 1);
		}
		logger.log(Level.INFO,"Initializing card expirations: " + cardExpList);
		context.setAttribute("cardExpList", cardExpList);
		
		return;
	}

}

