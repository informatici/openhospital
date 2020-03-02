package org.isf.utils.time;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * 
 * @author Mwithi
 * 
 * Some useful functions for time calculation.
 *
 */
public class TimeTools {
	
	
	public static void main(String[] args) {
		GeneralData.getGeneralData();
		MessageBundle.initialize();
		GregorianCalendar dateFrom = new GregorianCalendar(2014, 10, 1);
		GregorianCalendar dateTo = new GregorianCalendar();
		System.out.println("Formatted Age: " + getFormattedAge(dateFrom.getTime()));
		System.out.println("Days between: " + getDaysBetweenDates(dateFrom, dateTo, true));
		System.out.println("Weeks between: " + getWeeksBetweenDates(dateFrom, dateTo, true));
		System.out.println("Months between: " + getMonthsBetweenDates(dateFrom, dateTo, true));
	}

	/**
	 * @author Mwithi
	 * 
	 * returns the difference in days between two dates
	 * @param from
	 * @param to
	 * @param ignoreTime - if <code>True</code> only dates will be compared
	 * @return the number of days, negative if from is after to
	 */
	public static int getDaysBetweenDates(GregorianCalendar from, GregorianCalendar to, boolean ignoreTime) {
		
		if (ignoreTime) {
			from.set(GregorianCalendar.HOUR_OF_DAY, 0);
			from.set(GregorianCalendar.MINUTE, 0);
			from.set(GregorianCalendar.SECOND, 0);
			to.set(GregorianCalendar.HOUR_OF_DAY, 0);
			to.set(GregorianCalendar.MINUTE, 0);
			to.set(GregorianCalendar.SECOND, 0);
		}
		
		DateTime dateFrom = new DateTime(from);
		DateTime dateTo = new DateTime(to);
		Period period = new Period(dateFrom, dateTo, PeriodType.days());
		return period.getDays();
	}
	
	/**
	 * @author Mwithi
	 * 
	 * returns the difference in days between two dates
	 * @param from
	 * @param to
	 * @param ignoreTime - if <code>True</code> only dates will be compared
	 * @return the number of days, negative if from is after to
	 */
	public static int getDaysBetweenDates(Date from, Date to, boolean ignoreTime) {
		
		if (ignoreTime) {
			GregorianCalendar dateFrom = new GregorianCalendar(); 
			GregorianCalendar dateTo = new GregorianCalendar();
			dateFrom.setTime(from);
			dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
			dateFrom.set(GregorianCalendar.MINUTE, 0);
			dateFrom.set(GregorianCalendar.SECOND, 0);
			
			dateTo.setTime(to);
			dateTo.set(GregorianCalendar.HOUR_OF_DAY, 0);
			dateTo.set(GregorianCalendar.MINUTE, 0);
			dateTo.set(GregorianCalendar.SECOND, 0);
			
			from = dateFrom.getTime();
			to = dateFrom.getTime();
		}
		
		DateTime dateFrom = new DateTime(from);
		DateTime dateTo = new DateTime(to);
		Period period = new Period(dateFrom, dateTo, PeriodType.days());
		return period.getDays();
	}
	
	/**
	 * @author Mwithi
	 * 
	 * returns the difference in weeks between two dates
	 * @param from
	 * @param to
	 * @param ignoreTime - if <code>True</code> only dates will be compared
	 * @return the number of days, negative if from is after to
	 */
	public static int getWeeksBetweenDates(GregorianCalendar from, GregorianCalendar to, boolean ignoreTime) {

		if (ignoreTime) {
			from.set(GregorianCalendar.HOUR_OF_DAY, 0);
			from.set(GregorianCalendar.MINUTE, 0);
			from.set(GregorianCalendar.SECOND, 0);
			to.set(GregorianCalendar.HOUR_OF_DAY, 0);
			to.set(GregorianCalendar.MINUTE, 0);
			to.set(GregorianCalendar.SECOND, 0);
		}
		
		DateTime dateFrom = new DateTime(from);
		DateTime dateTo = new DateTime(to);
		Period period = new Period(dateFrom, dateTo, PeriodType.weeks());
		return period.getWeeks();
	}
	
	/**
	 * @author Mwithi
	 * 
	 * returns the difference in months between two dates
	 * @param from
	 * @param to
	 * @param ignoreTime - if <code>True</code> only dates will be compared
	 * @return the number of days, negative if from is after to
	 */
	public static int getMonthsBetweenDates(GregorianCalendar from, GregorianCalendar to, boolean ignoreTime) {
		
		if (ignoreTime) {
			from.set(GregorianCalendar.HOUR_OF_DAY, 0);
			from.set(GregorianCalendar.MINUTE, 0);
			from.set(GregorianCalendar.SECOND, 0);
			to.set(GregorianCalendar.HOUR_OF_DAY, 0);
			to.set(GregorianCalendar.MINUTE, 0);
			to.set(GregorianCalendar.SECOND, 0);
		}
		
		DateTime dateFrom = new DateTime(from);
		DateTime dateTo = new DateTime(to);
		Period period = new Period(dateFrom, dateTo, PeriodType.months());
		return period.getMonths();
	}
	
	/**
	 * Return the age in the format {years}y {months}m {days}d or with other locale pattern
	 * 
	 * @author Mwithi 
	 * @param birthDate - the birthdate
	 * @return string with the formatted age
	 */
	public static String getFormattedAge(Date birthDate) {
		GregorianCalendar birthday = new GregorianCalendar();
		String pattern = MessageBundle.getMessage("angal.common.agepattern");
		String age = "";
		if (birthDate != null) {
			birthday.setTime(birthDate);
			DateTime now = new DateTime();
			DateTime birth = new DateTime(birthday.getTime());
			Period period = new Period(birth, now, PeriodType.yearMonthDay());
			age = MessageFormat.format(pattern, period.getYears(), period.getMonths(), period.getDays());
		}
		return age;
	}
	
	/**
	 * Return a string representation of the dateTime with the given pattern
	 * @param dateTime - a GregorianCalendar object
	 * @param pattern - the pattern. If <code>null</code> "yyyy-MM-dd HH:mm:ss" will be used
	 * @return the String represetation of the GregorianCalendar
	 */
	public static String formatDateTime(GregorianCalendar dateTime, String pattern) {
		if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);  //$NON-NLS-1$
		return format.format(dateTime.getTime());
	}
	
	/**
	 * Return a string representation of the dateTime with the given pattern
	 * @param dateTime - a Date object
	 * @param pattern - the pattern. If <code>null</code> "yyyy-MM-dd HH:mm:ss" will be used
	 * @return the String represetation of the GregorianCalendar
	 */
	public static String formatDateTime(Date date, String pattern) {
		if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss";
		GregorianCalendar dateTime = new GregorianCalendar();
		dateTime.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat(pattern);  //$NON-NLS-1$
		return format.format(dateTime.getTime());
	}

	/**
	 * Return a string representation of the dateTime in the form "yyyy-MM-dd HH:mm:ss"
	 * @param dateTime - a GregorianCalendar object
	 * @return the String represetation of the GregorianCalendar
	 */
	public static String formatDateTimeReport(GregorianCalendar time) {
		return formatDateTime(time, null);
	}
	
	/**
	 * Return a string representation of the dateTime in the form "yyyy-MM-dd HH:mm:ss"
	 * @param dateTime - a Date object
	 * @return the String represetation of the Date
	 */
	public static String formatDateTimeReport(Date date) {
		GregorianCalendar time = new GregorianCalendar();
		time.setTime(date);
		return formatDateTime(time, null);
	}
	
	/**
	 * Return the first istant of the current date
	 * @return
	 */
	public static GregorianCalendar getDateToday0() {
		GregorianCalendar date = new GregorianCalendar();
		date.set(GregorianCalendar.HOUR_OF_DAY, 0);
		date.set(GregorianCalendar.MINUTE, 0);
		date.set(GregorianCalendar.SECOND, 0);
		return date;
	}
	
	/**
	 * Return the last istant of the current date
	 * @return
	 */
	public static GregorianCalendar getDateToday24() {
		GregorianCalendar date = new GregorianCalendar();
		date.set(GregorianCalendar.HOUR_OF_DAY, 23);
		date.set(GregorianCalendar.MINUTE, 59);
		date.set(GregorianCalendar.SECOND, 59);
		return date;
	}
	
	/**
	 * Return a {@link GregorianCalendar} representation of the string using the given pattern
	 * @param string - a String object to be passed
	 * @param pattern - the pattern. If <code>null</code> "yyyy-MM-dd HH:mm:ss" will be used
	 * @param noTime - if <code>True</code> the time will be 00:00:00, actual time otherwise.
	 * @return the String represetation of the GregorianCalendar
	 * @throws ParseException 
	 */
	public static GregorianCalendar parseDate(String string, String pattern, boolean noTime) throws ParseException {
		if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);  //$NON-NLS-1$
		Date date = format.parse(string);
		GregorianCalendar calendar = new GregorianCalendar();
		if (noTime) {
			calendar.setTime(date);
		} else {
			calendar.setTimeInMillis(date.getTime());
		}
		System.out.println(formatDateTime(calendar, null));

		return calendar;
	}
	
	
}
