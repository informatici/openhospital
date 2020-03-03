package org.isf.utils.time;
/*-------------------------------------------------------------------
 * static class RememberDates: useful class in order to remember the last date inserted when 
 * are performed recursive inserting of past datas. The aim of the class is to avoid the user
 * to manually select the date in each new window
 * -----------------------------------------
 * modification history
 * =====================
 * 08/11/06 - ross - creazione
 * 09/11/06 - ross - modificata per fornire, la prima volta, la data di sistema (metodi get gregorian)
 * 11/08/10 - claudia - inserita la voce per PATIENTVACCINE
 * 11/12/14 - mwithi - completely changed the behaviour: no more {Date} type, only {GregorianCalendar}
 * 					   and only date keeped, time is up to date.
 *-------------------------------------------------------------------*/



import java.util.GregorianCalendar;

public class RememberDates {

	private static GregorianCalendar lastOpdVisitDate=null;
	private static GregorianCalendar lastAdmInDate=null;
	private static GregorianCalendar lastLabExamDate=null;
	private static GregorianCalendar lastBillDate=null;
	private static GregorianCalendar lastPatientVaccineDate=null;

	//passare da Date a gregorian
	//visitDate.setTime(resultSet.getDate("OPD_DATE_VIS"));
	
	//passare da gregorian a date
	//java.sql.Date visitDate 	= (opd.getVisitDate()==null?null:new java.sql.Date(opd.getVisitDate().getTimeInMillis()));
	//GregorianCalendar time=new GregorianCalendar();//gets the current time

	
	//------------  opd attendance date -----------------------
//	public static GregorianCalendar getLastOpdVisitDate() 	{
//		return lastOpdVisitDate;
//	}
	public static GregorianCalendar getLastOpdVisitDateGregorian() 	{
		GregorianCalendar gc = new GregorianCalendar();
		if (lastOpdVisitDate != null) {
			gc.set(lastOpdVisitDate.get(GregorianCalendar.YEAR), 
					lastOpdVisitDate.get(GregorianCalendar.MONTH),
					lastOpdVisitDate.get(GregorianCalendar.DAY_OF_MONTH));
		} 
		return gc;
	}
//	public static void setLastOpdVisitDate(Date visitDate) 	{
//		lastOpdVisitDate.setTime(visitDate);
//	}
	public static void setLastOpdVisitDate(GregorianCalendar visitDate) 	{
		lastOpdVisitDate=visitDate;
	}

	//------------  laboratory exam -----------------------
//	public static Date getLastLabExamDate() 	{
//		return lastLabExamDate == null ? new Date() : lastLabExamDate;
//	}
	public static GregorianCalendar getLastLabExamDateGregorian() 	{
		GregorianCalendar gc = new GregorianCalendar();
		if (lastLabExamDate != null) {
			gc.set(lastLabExamDate.get(GregorianCalendar.YEAR), 
					lastLabExamDate.get(GregorianCalendar.MONTH),
					lastLabExamDate.get(GregorianCalendar.DAY_OF_MONTH));
		}
		return gc;
	}
//	public static void setLastLabExamDate(Date labDate) 	{
//		lastLabExamDate=labDate;
//	}
	public static void setLastLabExamDate(GregorianCalendar labDate) 	{
		lastLabExamDate=labDate;
	}

	//------------  admission date -----------------------
//	public static Date getLastAdmInDate() 	{
//		return lastAdmInDate;
//	}
	public static GregorianCalendar getLastAdmInDateGregorian() 	{
		GregorianCalendar gc = new GregorianCalendar();
		if (lastAdmInDate != null) {
			gc.set(lastAdmInDate.get(GregorianCalendar.YEAR), 
					lastAdmInDate.get(GregorianCalendar.MONTH),
					lastAdmInDate.get(GregorianCalendar.DAY_OF_MONTH));
		}
		return gc;
	}
//	public static void setLastAdmInDate(Date inDate) 	{
//		lastAdmInDate=inDate;
//	}
	public static void setLastAdmInDate(GregorianCalendar inDate) 	{
		lastAdmInDate=inDate;
	}
	
	//------------ bill date -----------------------
//	public static Date getLastBillDate() 	{
//		return lastBillDate == null ? new Date() : lastBillDate;
//	}
	public static GregorianCalendar getLastBillDateGregorian() 	{
		GregorianCalendar gc = new GregorianCalendar();
		if (lastBillDate != null) {
			gc.set(lastBillDate.get(GregorianCalendar.YEAR), 
					lastBillDate.get(GregorianCalendar.MONTH),
					lastBillDate.get(GregorianCalendar.DAY_OF_MONTH));
		}
		return gc;
	}
//	public static void setLastBillDate(Date inDate) 	{
//		lastBillDate=inDate;
//	}
	public static void setLastBillDate(GregorianCalendar billDate) 	{
		lastBillDate=billDate;
	}
	
	//------------  PAtient vaccine-----------------------
//	public static Date getLastPatientVaccineDate() 	{
//		return lastPatientVaccineDate == null ? new Date() : lastPatientVaccineDate;
//	}
	public static GregorianCalendar getLastPatientVaccineDateGregorian() 	{
		GregorianCalendar gc = new GregorianCalendar();
		if (lastPatientVaccineDate != null) {
			gc.set(lastPatientVaccineDate.get(GregorianCalendar.YEAR), 
					lastPatientVaccineDate.get(GregorianCalendar.MONTH),
					lastPatientVaccineDate.get(GregorianCalendar.DAY_OF_MONTH));
		}
		return gc;
	}
//	public static void setLastPatientVaccineDate(Date patVacDate) 	{
//		lastPatientVaccineDate=patVacDate;
//	}
	public static void setLastPatineVaccineDate(GregorianCalendar labDate) 	{
		lastPatientVaccineDate=labDate;
	}
}
