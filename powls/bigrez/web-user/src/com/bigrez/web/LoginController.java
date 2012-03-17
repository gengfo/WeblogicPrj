package com.bigrez.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.GuestProfile;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.ProfileServices;

public class LoginController extends SimpleFormController {
	
	private ProfileServices profileServices;
	private LoginForm loginForm;
	final Logger logger = LoggingHelper.getServerLogger();
	
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"LoginController::onBindOnNewForm()");

		logger.log(Level.INFO,"LoginController::onBindOnNewForm() complete");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"LoginController::onSubmit()");
		LoginForm form = (LoginForm) command;
		try
		{
			GuestProfile profile = profileServices.findByLogonAndPassword(form.getLogon(), form.getPassword());
			ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
			rezinfo.setGuestProfileId(profile.getExternalIdentity());
			rezinfo.setGuestProfile(profile);
			logger.log(Level.INFO,"LoginController::onSubmit() complete, success, calling super.onSubmit()");
			return super.onSubmit(request, response, command, errors);
		}
		catch (NotFoundException e)
		{
			logger.log(Level.INFO,"LoginController::onSubmit() complete, no profile found, returning login view");
			errors.reject("error.login.usernotfound");
			ModelAndView result = super.onSubmit(request, response, command, errors);
			result.setViewName("login"); // redisplay the page again
			return result;
		}
	}

	public ProfileServices getProfileServices() {
		return profileServices;
	}

	public void setProfileServices(ProfileServices pProfileServices) {
		profileServices = pProfileServices;
	}

	public LoginForm getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(LoginForm pLoginForm) {
		loginForm = pLoginForm;
	}

}
