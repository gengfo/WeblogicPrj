package com.bigrez.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class SimpleSuccessFailureController extends AbstractController {

	private String successView;
	private String failureView;
	
	@Override
	protected ModelAndView 
	handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
	throws Exception 
	{
		return new ModelAndView(getSuccessView());
	}

	public String getSuccessView() {
		return successView;
	}
	public void setSuccessView(String pSuccessView) {
		successView = pSuccessView;
	}
	public String getFailureView() {
		return failureView;
	}
	public void setFailureView(String pFailureView) {
		failureView = pFailureView;
	}
}
