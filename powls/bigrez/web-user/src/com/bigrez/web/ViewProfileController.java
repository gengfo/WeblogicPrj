package com.bigrez.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.GuestProfile;
import com.bigrez.domain.Reservation;
import com.bigrez.service.ProfileServices;

public class ViewProfileController extends SimpleFormController {
	
	private ProfileServices profileServices;
	private ViewProfileForm viewProfileForm;
	final Logger logger = LoggingHelper.getServerLogger();
	
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"ViewProfileController::onBindOnNewForm()");
		ViewProfileForm form = (ViewProfileForm) command;
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		GuestProfile profile = rezinfo.getGuestProfile();
		form.setProfile(profile);
		System.out.println("profile rez list: "+profile.getReservations());
		for (Reservation rez : profile.getReservations())
		{
			System.out.println("Rez: "+rez.getConfirmationNumber());
			System.out.println("Roomtype is "+rez.getRoomType());
			if (rez.getRoomType() != null)
			{
				System.out.println("Property is "+rez.getRoomType().getProperty());
			}
		}
		logger.log(Level.INFO,"ViewProfileController::onBindOnNewForm() complete");
	}

	public ProfileServices getProfileServices() {
		return profileServices;
	}

	public void setProfileServices(ProfileServices pProfileServices) {
		profileServices = pProfileServices;
	}

	public ViewProfileForm getViewProfileForm() {
		return viewProfileForm;
	}

	public void setViewProfileForm(ViewProfileForm pViewProfileForm) {
		viewProfileForm = pViewProfileForm;
	}

}
