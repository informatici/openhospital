package org.isf.opd.model;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.disease.model.Disease;
import org.isf.distype.model.DiseaseType;
import org.isf.patient.model.Patient;

/*------------------------------------------
 * Opd - model for OPD
 * -----------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  pupo
 * 21/11/2006 - ross - renamed from Surgery 
 *                   - added visit date, disease 2, diseas3
 *                   - disease is not mandatory if re-attendance
 * 			         - version is now 1.0 
 * 12/06/2008 - ross - added referral from / to
 * 16/06/2008 - ross - added patient detail
 * 05/09/2008 - alex - added fullname e notefield
 * 09/01/2009 - fabrizio - date field modified to type Date
 * 02/06/2015 - Antonio - ported to JPA
 *------------------------------------------*/
@Entity
@Table(name="OPD")
public class Opd 
{
	@Id 
	@Column(name="OPD_ID") 
	private int code;

	@NotNull
	@Column(name="OPD_DATE")
	private Date date;

	@NotNull
	@Column(name="OPD_DATE_VIS")
	private GregorianCalendar visitDate;

	@ManyToOne
	@JoinColumn(name="OPD_PAT_ID")
	private Patient patient;

	@NotNull
	@Column(name="OPD_AGE")
	private int age;

	@NotNull
	@Column(name="OPD_SEX")
	private char sex;

	@NotNull
	@Column(name="OPD_NOTE")
	private String note; //ADDED: Alex

	@NotNull
	@Column(name="OPD_PROG_YEAR")	
	private int year;
		
	@ManyToOne
	@JoinColumn(name="OPD_DIS_ID_A")
	private Disease disease;
	
	@ManyToOne
	@JoinColumn(name="OPD_DIS_ID_A_2")
	private Disease disease2;
	
	@ManyToOne
	@JoinColumn(name="OPD_DIS_ID_A_3")
	private Disease disease3;

	@NotNull
	@Column(name="OPD_LOCK")
	private int lock;

	@NotNull
	@Column(name="OPD_NEW_PAT")
	private char newPatient;	//n=NEW R=REATTENDANCE
	
	@Column(name="OPD_REFERRAL_FROM")
	private String referralFrom;	//R=referral from another unit; null=no referral from
	
	@Column(name="OPD_REFERRAL_TO")
	private String referralTo;		//R=referral to another unit; null=no referral to 

	@NotNull
	@Column(name="OPD_USR_ID_A")
	private String userID;
	
	@Transient
	private volatile int hashCode = 0;


	public char getNewPatient() {
		return newPatient;
	}

	public void setNewPatient(char newPatient) {
		this.newPatient = newPatient;
	}

	public Opd() {
	}
	
	/**
     * @param aYear
     * @param aSex
     * @param aDate
     * @param aAge
     * @param aDisease
     * @param aLock
     */
	
	public Opd(int aYear,char aSex,int aAge,Disease aDisease,int aLock) {
		year=aYear;
		sex=aSex;
		age=aAge;
		disease=aDisease;
		lock=aLock;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getFullName() {
		return patient.getName();
	}

	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public String getfirstName() {
		return patient.getFirstName();
	}

	public void setfirstName(String firstName) {
		this.patient.setFirstName(firstName);
	}

	public String getsecondName() {
		return patient.getSecondName();
	}

	public void setsecondName(String secondName) {
		this.patient.setSecondName(secondName);
	}

	public String getnextKin() {
		return patient.getNextKin();
	}

	public void setnextKin(String nextKin) {
		this.patient.setNextKin(nextKin);
	}
	
	public String getcity() {
		return patient.getCity();
	}

	public void setcity(String city) {
		this.patient.setCity(city);
	}

	public String getaddress() {
		return patient.getAddress();
	}

	public void setaddress(String address) {
		this.patient.setAddress(address);
	}

	public String getReferralTo() {
		return referralTo;
	}

	public void setReferralTo(String referralTo) {
		this.referralTo = referralTo;
	}

	public String getReferralFrom() {
		return referralFrom;
	}

	public void setReferralFrom(String referralFrom) {
		this.referralFrom = referralFrom;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Disease getDisease() {
		return disease;
	}
	public Disease getDisease2() {
		return disease2;
	}
	public Disease getDisease3() {
		return disease3;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
		if (disease!=null) {
			if (disease.equals("")) {
				this.disease=null;
			}
		}
	}
	public void setDisease2(Disease disease) {
		this.disease2 = disease;
		if (disease!=null) {
			if (disease.equals("")) {
				this.disease2=null;
			}
		}
	}
	public void setDisease3(Disease disease) {
		this.disease3 = disease;
		if (disease!=null) {
			if (disease.equals("")) {
				this.disease3=null;
			}
		}
	}
	public int getLock() {
		return lock;
	}
	public void setLock(int lock) {
		this.lock = lock;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public GregorianCalendar getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(GregorianCalendar visDate) {
		this.visitDate = visDate;
	}	
	
	public char getSex() {
		return sex;
	}
	public void setSex(char sex) {
		this.sex = sex;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDiseaseDesc() {
		return disease.getDescription();
	}

	public void setDiseaseDesc(String diseaseDesc) {
		this.disease.setDescription(diseaseDesc);
	}

	public String getDiseaseTypeDesc() {
		return disease.getType().getDescription();
	}

	public void setDiseaseTypeDesc(String diseaseTypeDesc) {
		this.disease.getType().setDescription(diseaseTypeDesc);
	}

	public DiseaseType getDiseaseType() {
		return disease.getType();
	}

	public void setDiseaseType(DiseaseType diseaseType) {
		this.disease.setType(diseaseType);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public int hashCode() {
		if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Opd)) {
			return false;
		}
		
		Opd opd = (Opd)obj;
		return (code == opd.getCode());
	}
}
