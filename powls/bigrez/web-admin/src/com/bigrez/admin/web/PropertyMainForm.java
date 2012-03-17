package com.bigrez.admin.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.utils.FormUtils;

public class PropertyMainForm  
implements Validator {
	
	private String id;
	private String description;
	private String address1;
	private String address2;
	private String city;
	private String stateCode;
	private String postalCode;
	private String phone;
	private String imageFile;
	private String features;
	
	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String pDescription) {
		description = pDescription;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String pAddress1) {
		address1 = pAddress1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String pAddress2) {
		address2 = pAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String pCity) {
		city = pCity;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String pStateCode) {
		stateCode = pStateCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String pPostalCode) {
		postalCode = pPostalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String pPhone) {
		phone = pPhone;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String pImageFile) {
		imageFile = pImageFile;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String pFeatures) {
		features = pFeatures;
	}

    @Override 
    public void validate(Object form, Errors errors)
	{		
		PropertyMainForm mainform = (PropertyMainForm) form;
		FormUtils.assertNonEmpty(errors, mainform.getDescription(), "error.property.descriptionempty");
		FormUtils.assertNonEmpty(errors, mainform.getAddress1(), "error.property.addressempty");
		FormUtils.assertNonEmpty(errors, mainform.getCity(), "error.property.cityempty");
		FormUtils.assertNonEmpty(errors, mainform.getStateCode(), "error.property.statecodeempty");
		FormUtils.assertNonEmpty(errors, mainform.getPostalCode(), "error.property.postalcodeempty");
		FormUtils.assertNonEmpty(errors, mainform.getPhone(), "error.property.phoneempty");
		FormUtils.assertNonEmpty(errors, mainform.getImageFile(), "error.property.imagefileempty");
		FormUtils.assertNonEmpty(errors, mainform.getFeatures(), "error.property.featuresempty");
	}
	
	@SuppressWarnings("unchecked")
    @Override 
    public boolean supports(Class pClass)
	{
		return pClass.equals(PropertyMainForm.class);
	}

}
