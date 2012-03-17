package com.bigrez.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import weblogic.logging.LoggingHelper;

public class LoggingViewInterceptor extends HandlerInterceptorAdapter 
{
	final Logger logger = LoggingHelper.getServerLogger();

	@Override
	public boolean preHandle(HttpServletRequest pRequest,
			HttpServletResponse pResponse, Object pHandler) 
	throws Exception 
	{
		logger.log(Level.INFO,"Entering handler for "+pRequest.getRequestURI()+" with querystring "+pRequest.getQueryString());
		return super.preHandle(pRequest, pResponse, pHandler);
	}
	
	public void postHandle(HttpServletRequest pRequest,
			HttpServletResponse pResponse, Object pHandler,
			ModelAndView pModelAndView) 
	throws Exception 
	{
		logger.log(Level.INFO,"Exiting handler for "+pRequest.getRequestURI()+" with querystring "+pRequest.getQueryString());
		super.postHandle(pRequest, pResponse, pHandler, pModelAndView);
	}

	
}
