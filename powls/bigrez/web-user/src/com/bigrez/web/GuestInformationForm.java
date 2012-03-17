package com.bigrez.web;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.domain.GuestProfile;
import com.bigrez.utils.FormUtils;


public class GuestInformationForm
  implements Serializable, Validator {
	
	private static final long serialVersionUID = 1L;
	
    private String profileId;
	private GuestProfile profile;
	private String existingLogon;
	private String existingPassword;
	private String desiredLogon;
	private String desiredPassword;
	
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String pProfileId) {
		profileId = pProfileId;
	}
	public GuestProfile getProfile() {
		return profile;
	}
	public void setProfile(GuestProfile pProfile) {
		profile = pProfile;
	}
	public String getExistingLogon() {
		return existingLogon;
	}
	public void setExistingLogon(String pExistingLogon) {
		existingLogon = pExistingLogon;
	}
	public String getExistingPassword() {
		return existingPassword;
	}
	public void setExistingPassword(String pExistingPassword) {
		existingPassword = pExistingPassword;
	}
	public String getDesiredLogon() {
		return desiredLogon;
	}
	public void setDesiredLogon(String pDesiredLogon) {
		desiredLogon = pDesiredLogon;
	}
	public String getDesiredPassword() {
		return desiredPassword;
	}
	public void setDesiredPassword(String pDesiredPassword) {
		desiredPassword = pDesiredPassword;
	}
	
    @Override 
	public void validate(Object form, Errors errors)
	{		
		GuestInformationForm guestform = (GuestInformationForm) form;
		// Three modes we need to handle
		//  1 - already logged in, might be updating profile info
		//  2 - logging in here
		//  3 - entering data here, creating login on the fly
		if (StringUtils.isNotEmpty(guestform.getExistingLogon()))
		{
			// mode 2, only login info matters
			FormUtils.assertNonEmpty(errors, guestform.getExistingPassword(), "error.guestinformation.passwordempty");
		}
		else
		{
			// Mode 1 and 3, need to validate guest info
			GuestProfile profile = guestform.getProfile();
			FormUtils.assertNonEmpty(errors, profile.getFirstName(), "error.guestinformation.firstnameempty");
			FormUtils.assertNonEmpty(errors, profile.getLastName(), "error.guestinformation.lastnameempty");
			FormUtils.assertNonEmpty(errors, profile.getPhone(), "error.guestinformation.phoneempty");
			FormUtils.assertNonEmpty(errors, profile.getEmail(), "error.guestinformation.emailempty");
			FormUtils.assertValidEmail(errors, profile.getEmail(), "error.guestinformation.emailinvalid");
			FormUtils.assertNonEmpty(errors, profile.getCard().getType(), "error.guestinformation.cardtypeempty");
			FormUtils.assertNonEmpty(errors, profile.getCard().getExpiry(), "error.guestinformation.cardexpempty");
			FormUtils.assertNonEmpty(errors, profile.getCard().getNumber(), "error.guestinformation.cardnumempty");
			if (StringUtils.isEmpty(guestform.getProfileId()))
			{
				// Mode 3, must specify desired logon/password
				FormUtils.assertNonEmpty(errors, guestform.getDesiredLogon(), "error.guestinformation.desiredlogonempty");
				FormUtils.assertNonEmpty(errors, guestform.getDesiredPassword(), "error.guestinformation.desiredpasswordempty");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
    @Override 
	public boolean supports(Class pClass)
	{
		return pClass.equals(GuestInformationForm.class);
	}

}
