package com.bigrez.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.CardDetails;
import com.bigrez.domain.GuestProfile;
import com.bigrez.service.DuplicateKeyException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.ProfileServices;
import com.bigrez.service.ReservationServices;

public class GuestInformationController extends SimpleFormController {

  private ReservationServices reservationServices;
  private ProfileServices profileServices;
  private GuestInformationForm guestInformationForm;
  final Logger logger = LoggingHelper.getServerLogger();

  protected void
  onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
  throws Exception
  {
    logger.log(Level.INFO,"GuestInformationController::onBindOnNewForm()");
    GuestInformationForm form = (GuestInformationForm) command;
    ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
    if (rezinfo.getGuestProfile() != null)
    {
      GuestProfile profile = rezinfo.getGuestProfile();
      form.setProfileId(profile.getExternalIdentity());
      form.setProfile(profile);
    }
    else
    {
      form.setProfileId("");
      GuestProfile temp = new GuestProfile();
      temp.setCard(new CardDetails("","",""));
      form.setProfile(temp);
      System.out.println("onBindNewForm: form="+form);
    }
    logger.log(Level.INFO,"GuestInformationController::onBindOnNewForm() complete");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request,
      HttpServletResponse response, Object command,
      BindException errors)
  throws Exception
  {
    logger.log(Level.INFO,"GuestInformationController::onSubmit()");
    GuestInformationForm form = (GuestInformationForm) command;
    System.out.println("onSubmit: form="+form);
    ReservationInfo rezinfo = (ReservationInfo) request.getSession().getAttribute("rezinfo");
    // Three modes we need to handle
    //  1 - already logged in, might be updating profile info
    //  2 - logging in here
    //  3 - entering data here, creating login on the fly
    if (StringUtils.isNotEmpty(form.getProfileId()))
    {
      // Mode 1, update the profile in the database and push on
      GuestProfile updatedprofile = profileServices.createOrUpdate(form.getProfile());
      rezinfo.setGuestProfile(updatedprofile);
      // fall through to normal success view
    }
    else if (StringUtils.isNotEmpty(form.getExistingLogon()))
    {
      // Mode 2, try to log in here...
      try
      {
        GuestProfile profile = profileServices.findByLogonAndPassword(form.getExistingLogon(), form.getExistingPassword());
        rezinfo.setGuestProfileId(profile.getExternalIdentity());
        rezinfo.setGuestProfile(profile);
        // fall through to normal success view
      }
      catch (NotFoundException e)
      {
        errors.reject("error.guestinformation.usernotfound");
        // We want to re-display the form view, and we need to push the form back on the session since 
        // SimpleFormController logic assumes that we no longer need it after binding/validation is done.
        // Calling showForm() here takes care of putting the form in the session and returning formView.
        setGuestInformationForm(form);
        return showForm(request, response, errors);
      }
    }
    else
    {
      // Mode 3 - entering data, creating login on the fly
      try
      {
        GuestProfile profile = form.getProfile();
        profile.setLogon(form.getDesiredLogon());
        profile.setPassword(form.getDesiredPassword());
        profileServices.createOrUpdate(profile);
        GuestProfile newprofile = profileServices.findByLogonAndPassword(profile.getLogon(), profile.getPassword());
        rezinfo.setGuestProfileId(newprofile.getExternalIdentity());
        rezinfo.setGuestProfile(newprofile);
      }
      catch (DuplicateKeyException e)
      {
        errors.reject("error.guestinformation.logonnotunique");
        // We want to re-display the form view, and we need to push the form back on the session since 
        // SimpleFormController logic assumes that we no longer need it after binding/validation is done.
        // Calling showForm() here takes care of putting the form in the session and returning formView.
        setGuestInformationForm(form);
        return showForm(request, response, errors);
      }
    }
    logger.log(Level.INFO,"GuestInformationController::onSubmit() complete, calling super.onSubmit()");
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

  public GuestInformationForm getGuestInformationForm() {
    return guestInformationForm;
  }

  public void setGuestInformationForm(GuestInformationForm pGuestInformationForm) {
    guestInformationForm = pGuestInformationForm;
  }

}
