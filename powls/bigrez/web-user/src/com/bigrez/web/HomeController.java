package com.bigrez.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class HomeController extends SimpleSuccessFailureController {
	
	@Override
	protected ModelAndView 
	handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
	throws Exception 
	{
		// Create the cross-application shared reservation info object in the session
		if (request.getSession(true).getAttribute("rezinfo") == null)
		{
			request.getSession(true).setAttribute("rezinfo", new ReservationInfo());
		}
		return super.handleRequestInternal(request, response); // success view
	}

}
