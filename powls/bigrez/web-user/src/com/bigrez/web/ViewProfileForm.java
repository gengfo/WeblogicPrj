package com.bigrez.web;

import java.io.Serializable;

import com.bigrez.domain.GuestProfile;


public class ViewProfileForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private GuestProfile profile;

	public GuestProfile getProfile() {
		return profile;
	}
	public void setProfile(GuestProfile pProfile) {
		profile = pProfile;
	}

}
