package com.bigrez.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	private static String df1 = "MM/dd/yyyy";
	private static String df2 = "MMM dd, yyyy";
	private static String df3 = "MM/yy";
	
	// Public Methods

	public static String format1(java.util.Date dd) {
		return new SimpleDateFormat(df1).format(dd);
	}

	public static String format2(java.util.Date dd) {
		return new SimpleDateFormat(df2).format(dd);
	}
	
	public static String format3(java.util.Date dd) {
		return new SimpleDateFormat(df3).format(dd);
	}

	public static Date parse1(String pValue) throws ParseException {
		return new SimpleDateFormat(df1).parse(pValue);
	}

	public static Date parse2(String pValue) throws ParseException {
		return new SimpleDateFormat(df2).parse(pValue);
	}
	
	public static Date parse3(String pValue) throws ParseException {
		return new SimpleDateFormat(df3).parse(pValue);
	}

	public static Date getNow() {
		Calendar now = Calendar.getInstance();
		return now.getTime();
	}

	public static Date cleanTime(Date indate) {
		try {
			return parse1(format1(indate));		
		}
		catch (ParseException e) {
			return indate;
		}
	}

	public static Date getNowNoTime() {
		return cleanTime(getNow());
	}

	public static Date getFirstDayOfMonthNoTime() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, 1);
		return cleanTime(now.getTime());
	}
	
	public static Date addDays(Date oldDate, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(oldDate);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	public static Date addMonths(Date oldDate, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(oldDate);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

    public static int getNumberOfDays(Date d1, Date d2) {
		if (d1.compareTo(d2) > 0)
		    return 0;
		int numberOfDays = 0;
		Calendar c1 = getCalendarForDay(d1);
		Calendar c2 = getCalendarForDay(d2);
		while (!c1.equals(c2)) {
		    c1.add(Calendar.DATE, 1);
		    numberOfDays++;
		}
		return numberOfDays;
    }

    public static boolean isDateInDateInterval(Date d, Date start, Date end) {
		if (start.compareTo(d) > 0 || end.compareTo(d) < 0)
		    return false;
		return true;
    } 

    public static Date getNextDay(Date d) {
		Calendar c = getCalendarForDay(d);
		c.add(Calendar.DATE, 1);
		return c.getTime();
    }

    public static Calendar getCalendarForDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		// Clear all time-related fields
		c.clear(Calendar.HOUR_OF_DAY);
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		c.clear(Calendar.AM_PM);
		c.clear(Calendar.DST_OFFSET);
		c.clear(Calendar.ZONE_OFFSET);
		return c;
    }
}
