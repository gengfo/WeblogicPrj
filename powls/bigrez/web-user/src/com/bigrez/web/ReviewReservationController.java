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
import com.bigrez.domain.Reservation;
import com.bigrez.service.ProfileServices;
import com.bigrez.service.ReservationServices;

public class ReviewReservationController extends SimpleFormController {
	
	private ReservationServices reservationServices;
	private ProfileServices profileServices;
	private ReviewReservationForm reviewReservationForm;
	final Logger logger = LoggingHelper.getServerLogger();
	
	protected void 
	onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
	throws Exception 
	{
		logger.log(Level.INFO,"ReviewReservationController::onBindOnNewForm()");
		ReviewReservationForm form = (ReviewReservationForm) command;
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		Reservation reservation = new Reservation();
		reservation.setArrivalDate(rezinfo.getArriveDate());
		reservation.setDepartureDate(rezinfo.getDepartDate());
		reservation.setCard(rezinfo.getGuestProfile().getCard());
		reservation.setGuestProfile(rezinfo.getGuestProfile());
		reservation.setRoomType(rezinfo.getRoomType());
		form.setReservation(reservation); // ready for review
		form.setProperty(rezinfo.getProperty());
		form.setReservationRates(rezinfo.getRezRates());
		logger.log(Level.INFO,"ReviewReservationController::onBindOnNewForm() complete");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException errors) 
	throws Exception 
	{
		logger.log(Level.INFO,"ReviewReservationController::onSubmit()");
		ReviewReservationForm form = (ReviewReservationForm) command;
		Reservation finalreservation = reservationServices.createReservation(
				form.getReservation(), form.getReservationRates());
		ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
		rezinfo.clearAllButProfile();
		// re-fetch the latest profile to make sure our version number matches..
		GuestProfile profile = rezinfo.getGuestProfile();
		GuestProfile newprofile = profileServices.findByLogonAndPassword(profile.getLogon(), profile.getPassword());
		rezinfo.setGuestProfileId(newprofile.getExternalIdentity());
		rezinfo.setGuestProfile(newprofile);
		// Need to stash the completed reservation somewhere for the "thank you" page
		request.getSession().setAttribute("finalreservation", finalreservation);
		logger.log(Level.INFO,"ReviewReservationController::onSubmit() complete, calling super.onSubmit()");
		return super.onSubmit(request, response, command, errors);
	}


	public ReservationServices getReservationServices() {
		return reservationServices;
	}

	public void setReservationServices(ReservationServices pReservationServices) {
		reservationServices = pReservationServices;
	}

	public ProfileServices getProfileServices() {
		return profileServices;
	}

	public void setProfileServices(ProfileServices pProfileServices) {
		profileServices = pProfileServices;
	}

	public ReviewReservationForm getReviewReservationForm() {
		return reviewReservationForm;
	}

	public void setReviewReservationForm(ReviewReservationForm reviewReservationForm) {
		this.reviewReservationForm = reviewReservationForm;
	}

}
