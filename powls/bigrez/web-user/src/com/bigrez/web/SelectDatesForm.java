package com.bigrez.web;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bigrez.utils.DateHelper;
import com.bigrez.utils.FormUtils;


public class SelectDatesForm 
  implements Serializable, Validator {
	
	private static final long serialVersionUID = 1L;
	
    private String arriveDate;
	private String departDate;
	
	public String getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(String pArriveDate) {
		arriveDate = pArriveDate;
	}
	public String getDepartDate() {
		return departDate;
	}
	public void setDepartDate(String pDepartDate) {
		departDate = pDepartDate;
	}
	
    @Override 
	public void validate(Object form, Errors errors)
	{		
		SelectDatesForm datesform = (SelectDatesForm) form;
		if (FormUtils.assertNonEmpty(errors, datesform.getArriveDate(), "error.selectdates.arriveempty")) {
			FormUtils.assertValidDate(errors, datesform.getArriveDate(), "error.selectdates.arriveinvalid");
		}
		if (FormUtils.assertNonEmpty(errors, datesform.getDepartDate(), "error.selectdates.departempty")) {
			FormUtils.assertValidDate(errors, datesform.getDepartDate(), "error.selectdates.departinvalid");
		}
		if (errors.getErrorCount() == 0)
		{
			try {
				Date arrive = DateHelper.parse1(datesform.getArriveDate());
				Date depart = DateHelper.parse1(datesform.getDepartDate());
				if (arrive.equals(depart) || arrive.after(depart)) {
					errors.reject("error.selectdates.arriveafterdepart");
				}
			} catch (ParseException e) {
				errors.reject("error.validationproblem");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
    @Override 
	public boolean supports(Class pClass)
	{
		return pClass.equals(SelectDatesForm.class);
	}
	
}
