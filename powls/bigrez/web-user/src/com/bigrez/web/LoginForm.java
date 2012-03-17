package com.bigrez.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.utils.FormUtils;


public class LoginForm 
implements Validator {
	
	private String logon;
	private String password;
	
	public String getLogon() {
		return logon;
	}
	public void setLogon(String pLogon) {
		logon = pLogon;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String pPassword) {
		password = pPassword;
	}
	
    @Override 
	public void validate(Object form, Errors errors)
	{		
		LoginForm loginform = (LoginForm) form;
		FormUtils.assertNonEmpty(errors, loginform.getLogon(), "error.login.logonempty");
		FormUtils.assertNonEmpty(errors, loginform.getPassword(), "error.login.passwordempty");
	}
	
	@SuppressWarnings("unchecked")
    @Override 
	public boolean supports(Class pClass)
	{
		return pClass.equals(LoginForm.class);
	}

}
