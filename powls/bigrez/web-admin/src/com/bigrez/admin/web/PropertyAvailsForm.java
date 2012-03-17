package com.bigrez.admin.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bigrez.service.ReservationServices.AvailabilitySummary;
import com.bigrez.utils.DateHelper;

public class PropertyAvailsForm {
	
	private List<AvailabilitySummary> availabilitySummaries;
	private String startDate;
	private int numMonths;
	
	public List<AvailabilitySummary> getAvailabilitySummaries() {
		return availabilitySummaries;
	}
	public void setAvailabilitySummaries(
			List<AvailabilitySummary> pAvailabilitySummaries) {
		availabilitySummaries = pAvailabilitySummaries;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String pStartDate) {
		startDate = pStartDate;
	}
	public int getNumMonths() {
		return numMonths;
	}
	public void setNumMonths(int pNumMonths) {
		numMonths = pNumMonths;
	}
	
	// Utility methods used by page for grabbing date strings
	
	public List<String> getDateList()
	{
		List<String> datelist = new ArrayList<String>();
		try
		{
			Date dd = DateHelper.parse3(getStartDate());
			for (int i=0; i<getNumMonths(); i++)
			{
				datelist.add(DateHelper.format3(dd)); // 02/09, 03/09, etc
				dd = DateHelper.addMonths(dd, 1);
			}
		}
		catch (Exception ignore) { ignore.printStackTrace(); }
		return datelist;
	}

	public String getEarlierStartDate()
	{
		String earlierdate = "";
		try
		{
			Date dd = DateHelper.parse3(getStartDate());
			dd = DateHelper.addMonths(dd, -numMonths);
			earlierdate = DateHelper.format3(dd);
		}
		catch (Exception ignore) { ignore.printStackTrace(); }
		return earlierdate;
	}
	
	public String getLaterStartDate()
	{
		String earlierdate = "";
		try
		{
			Date dd = DateHelper.parse3(getStartDate());
			dd = DateHelper.addMonths(dd, numMonths);
			earlierdate = DateHelper.format3(dd);
		}
		catch (Exception ignore) { ignore.printStackTrace(); }
		return earlierdate;
	}

}
