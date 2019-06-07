package org.isf.utils.time;

import com.toedter.calendar.JDateChooser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.GregorianCalendar;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;

import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
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
	
	public static void main(String[] args) throws ParseException {
		GeneralData.getGeneralData();
		MessageBundle.initialize();
		GregorianCalendar dateFrom = new GregorianCalendar(2014, 10, 1);
		GregorianCalendar dateTo = new GregorianCalendar();
		System.out.println(""+getDate("311219", "ddMMyy").getTime());
		System.out.println("Formatted Age: " + getFormattedAge(dateFrom.getTime()));
		System.out.println("Days between: " + getDaysBetweenDates(dateFrom, dateTo));
		System.out.println("Weeks between: " + getWeeksBetweenDates(dateFrom, dateTo));
		System.out.println("Months between: " + getMonthsBetweenDates(dateFrom, dateTo));
		System.out.println("Actual Server Date: " + getServerDateTime());
		
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
		GregorianCalendar birthday = TimeTools.getServerDateTime();
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
	
	public static GregorianCalendar getDate(String strDate, String format) throws ParseException{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date=sdf.parse(strDate);
			if(date!=null){
				GregorianCalendar calDate=new GregorianCalendar();
				calDate.setTime(date);
				return calDate;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			if(!format.equals("dd/MM/yyyy")){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date date=sdf.parse(strDate);
				if(date!=null){
					GregorianCalendar calDate=new GregorianCalendar();
					calDate.setTime(date);
					return calDate;
				}
			}
		}
		return null;
	}
	/**
	 * Return the actual date and time of the server
	 * 
	 * @author hadesthanos 
	 * @return DateTime 
	 * @throws OHException 
	 * @throws ParseException 
	 */
	public static GregorianCalendar getServerDateTime()  {
		GregorianCalendar serverDate=new GregorianCalendar();
		String query = " SELECT NOW( ) as time ";

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			while (resultSet.next()) {
				String date = resultSet.getString("time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date utilDate = new java.util.Date();
				utilDate = sdf.parse(date);
				serverDate.setTime(utilDate);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (OHException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}  finally {
                    
                    try {
                        dbQuery.releaseConnection();
                    } catch (OHException e) {
                            e.printStackTrace();
                    }
		}
		return serverDate;
	}
   
    public static String getConvertedString(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.malnutrition.nodate");
		String string = String
				.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		string += "/" + String.valueOf(time.get(GregorianCalendar.MONTH) + 1);
		String year = String.valueOf(time.get(GregorianCalendar.YEAR));
		year = year.substring(2, year.length());
		string += "/" + year;
		return string;
	}
    public static String formatDateTimeReport(GregorianCalendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //$NON-NLS-1$
		return sdf.format(date.getTime());
	}
    public static String formatDateTimeReport(JDateChooser jDateChoose) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //$NON-NLS-1$
		return sdf.format(jDateChoose.getDate());
	}
  
public static GregorianCalendar convertToDate(String string) throws ParseException {
		GregorianCalendar date = TimeTools.getServerDateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		date.setTime(sdf.parse(string));
		return date;
	}

}
