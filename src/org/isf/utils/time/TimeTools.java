package org.isf.utils.time;

import java.text.MessageFormat;
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
		System.out.println("Days between: " + getDaysBetweenDates(dateFrom, dateTo));
		System.out.println("Weeks between: " + getWeeksBetweenDates(dateFrom, dateTo));
		System.out.println("Months between: " + getMonthsBetweenDates(dateFrom, dateTo));
	}

	/**
	 * @author Mwithi
	 * 
	 * returns the difference in days between two dates
	 * @param from
	 * @param to
	 * @return the number of days
	 */
	public static int getDaysBetweenDates(GregorianCalendar from, GregorianCalendar to) {
		
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
	 * @return the number of days
	 */
	public static int getDaysBetweenDates(Date from, Date to) {
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
	 * @return the number of weeks
	 */
	public static int getWeeksBetweenDates(GregorianCalendar from, GregorianCalendar to) {
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
	 * @return the number of months
	 */
	public static int getMonthsBetweenDates(GregorianCalendar from, GregorianCalendar to) {
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
}
