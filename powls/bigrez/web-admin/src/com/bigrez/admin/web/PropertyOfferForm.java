package com.bigrez.admin.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.domain.Offer;
import com.bigrez.utils.FormUtils;

public class PropertyOfferForm implements Validator {
	
	private String id;
	private Offer offer;
	
	public String getId() {
		return id;
	}
	public void setId(String pId) {
		id = pId;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer pOffer) {
		offer = pOffer;
	}
	
	@Override 
	public void validate(Object form, Errors errors)
	{		
		PropertyOfferForm offerform = (PropertyOfferForm) form;
		Offer offer = offerform.getOffer();
		FormUtils.assertNonEmpty(errors, offer.getCaption(), "error.offer.captionempty");
		FormUtils.assertNonEmpty(errors, offer.getDescription(), "error.offer.descriptionempty");
		FormUtils.assertNonEmpty(errors, offer.getImageFile(), "error.offer.imagefileempty");
	}
	
	@SuppressWarnings("unchecked")
    @Override 
    public boolean supports(Class pClass)
	{
		return pClass.equals(PropertyOfferForm.class);
	}
	
}
