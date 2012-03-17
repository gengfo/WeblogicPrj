package com.bigrez.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import weblogic.logging.LoggingHelper;

public class ThankYouController extends SimpleSuccessFailureController {
	
	final Logger logger = LoggingHelper.getServerLogger();
	
	@Override
	protected ModelAndView 
	handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
	throws Exception 
	{
		logger.log(Level.INFO,"ThankYouController::handleRequestInternal()");

		ModelAndView result = new ModelAndView(getSuccessView());
		logger.log(Level.INFO,"ThankYouController::handleRequestInternal() complete, returning "+result);
		return result;
	}

}
