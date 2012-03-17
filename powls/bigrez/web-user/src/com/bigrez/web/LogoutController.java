package com.bigrez.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class LogoutController extends SimpleSuccessFailureController {
	
	@Override
	protected ModelAndView 
	handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
	throws Exception 
	{
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		if (rezinfo != null && rezinfo.getGuestProfile() != null)
		{
			rezinfo.setGuestProfile(null);
			rezinfo.setGuestProfileId("");
			return new ModelAndView(getSuccessView());
		}
		else
		{
			return new ModelAndView(getFailureView());
		}
	}

}
