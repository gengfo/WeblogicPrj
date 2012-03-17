package com.bigrez.utils;

import java.text.ParseException;

import org.springframework.validation.Errors;


public class FormUtils {

	public static boolean isEmpty(String value) {
		return (value==null || value.equals(""));
	}

	public static boolean assertNonEmpty(Errors errors, String value, String errorkey) {
		if (isEmpty(value)) {
			errors.reject(errorkey);
			return false;
		} 
		return true;
	}

	public static boolean assertNonZero(Errors errors, Number value, String errorkey) {
		if (value==null || value.intValue()==0) {
			errors.reject(errorkey);
			return false;
		} 
		return true;
	}
	
	public static boolean assertInteger(Errors errors, String value, String errorkey) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) {
			errors.reject(errorkey);
			return false; 
		}
	}

    /**
     * Ensures the passed in email string has a few of the required pieces to be a valid email address
     * 
     * @param errors
     * @param value
     * @param errorkey
     * @return {@code true} if the email is valid.
     */
	public static boolean assertValidEmail(Errors errors, String value, String errorkey) {
		if (value == null ||
		    value.indexOf("@")==-1 ||  // at least one @
			value.indexOf(".")==-1 ||  // at least one dot
			value.substring(value.indexOf("@")+1).indexOf(".")==-1 ||    // a dot after @
			value.substring(value.indexOf("@")+1).indexOf("@")!=-1) {  // only one @

			errors.reject(errorkey);
			return false;
		}
		return true;
	}

    /**
     * Ensures the passed in date string matches the first format defined in DateHelper (MM/DD/YYYY)
     *
     * @param errors
     * @param value
     * @param errorkey
     * @return {@code true} if the date is valid.
     */
	public static boolean assertValidDate(Errors errors, String value, String errorkey) {
		try {
			DateHelper.parse1(value);
			return true;
		}
		catch (ParseException e) {
			errors.reject(errorkey);
			return false;
		}
        catch (NullPointerException e) {
          errors.reject(errorkey);
          return false;
      }
	}
}
