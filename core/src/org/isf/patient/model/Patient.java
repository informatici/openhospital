package org.isf.patient.model;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.isf.opd.model.Opd;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/*------------------------------------------
 * Patient - model for the patient entity
 * -----------------------------------------
 * modification history
 * 05/05/2005 - giacomo  - first beta version 
 * 03/11/2006 - ross - added toString method
 * 11/08/2008 - Alessandro - added mother and father names textfield
 * 						   - added birthdate and age check
 * 19/08/2008 - Mex        - substitute EduLevel with BloodType
 * 22/08/2008 - Claudio    - added birth date field
 * 						   - modified age field
 * 01/01/2009 - Fabrizio   - modified age field type back to int
 *                         - removed unuseful super() call in constructor
 *                         - removed unuseful todo comment
 *                         - removed assignment to attribute hasInsurance
 *                           since it had no effect
 * 16/09/2009 - Alessandro - added equals override to support comparing
 * 							 and filtering
 * 17/10/2011 - Alessandro - added height and weight (from malnutritionalcontrol)
 * 
 *------------------------------------------*/
@Entity
@Table(name="PATIENT")
public class Patient {
	/*
	 * PAT_ID int NOT NULL AUTO_INCREMENT , PAT_FNAME varchar (50) NOT NULL ,
	 * --first name (nome) PAT_SNAME varchar (50) NOT NULL , --second name
	 * (cognome) PAT_AGE int NOT NULL , --age PAT_SEX char (1) NOT NULL , --sex :
	 * M or F PAT_ADDR varchar (50) NULL , --address (via , n.) PAT_CITY varchar
	 * (50) NOT NULL , --city PAT_NEXT_KIN varchar (50) NULL , --next kin
	 * (parente prossimo, figlio di..) PAT_TELE varchar (50) NULL , --telephone
	 * number PAT_MOTH char (1) NULL , --mother: D=dead, A=alive PAT_FATH char
	 * (1) NULL , --father: D=dead, A=alive PAT_LEDU char (1) NULL , --level of
	 * education: 1 or 2 or 3 or 4 PAT_ESTA char (1) NULL , --economic status:
	 * R=rich, P=poor PAT_PTOGE char (1) NULL , --parents together: Y or N
	 * PAT_LOCK int NOT NULL default 0, PRIMARY KEY ( PAT_ID )
	 */

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PAT_ID")
	private Integer code;

	@NotNull
	@Column(name="PAT_FNAME")
	private String firstName;

	@NotNull
	@Column(name="PAT_SNAME")
	private String secondName;

	@NotNull
	@Column(name="PAT_NAME")
	private String name;
	
	@Column(name="PAT_BDATE")
	private Date birthDate;

	@NotNull
	@Column(name="PAT_AGE")
	private int age;

	@NotNull
	@Column(name="PAT_AGETYPE")
	private String agetype;

	@NotNull
	@Column(name="PAT_SEX")
	private char sex;
	
	@Column(name="PAT_ADDR")
	private String address;

	@NotNull
	@Column(name="PAT_CITY")
	private String city;
	
	@Column(name="PAT_NEXT_KIN")
	private String nextKin;
	
	@Column(name="PAT_TELE")
	private String telephone;
	
	@Column(name="PAT_NOTE")
	private String note;

	@NotNull
	@Column(name="PAT_MOTH_NAME")
	private String mother_name; // mother's name
	
	@Column(name="PAT_MOTH")
	private char mother = ' '; // D=dead, A=alive

	@NotNull
	@Column(name="PAT_FATH_NAME")
	private String father_name; // father's name
	
	@Column(name="PAT_FATH")
	private char father = ' '; // D=dead, A=alive

	@NotNull
	@Column(name="PAT_BTYPE")
	private String bloodType; // (0-/+, A-/+ , B-/+, AB-/+)
	
	@Column(name="PAT_ESTA")
	private char hasInsurance = ' '; // Y=Yes, N=no
	
	@Column(name="PAT_PTOGE")
	private char parentTogether = ' '; // parents together: Y or N
	
	@Column(name="PAT_TAXCODE")
	private String taxCode;

	@NotNull
	@Column(name="PAT_DELETED")
	private String deleted = "N";

	@Transient
	private float height;
	
	@Transient
	private float weight;
	
	@Version
	@Column(name="PAT_LOCK")
	private int lock;
	
	@Column(name="PAT_PHOTO")
	@Lob
	private Blob photo;
	
	@Transient
	private Image photoImage;
	
	@Transient
	private volatile int hashCode = 0;
	

	public Patient() {
		
		this.firstName = "";
		this.secondName = ""; 
		this.name = this.firstName + " " + this.secondName;
		this.birthDate = null;
		this.age = 0;
		this.agetype = "";
		this.sex = ' ';
		this.address = "";
		this.city = "";
		this.nextKin = ""; 
		this.telephone = "";
		this.mother_name = "";
		this.mother = ' ';
		this.father_name = "";
		this.father = ' ';
		this.bloodType = "";
		this.hasInsurance = ' ';
		this.parentTogether = ' ';
		this.taxCode = "";
		this.height = 0;
		this.weight = 0;
	}
	
	public Patient(Opd opd) {
		
		this.firstName = opd.getfirstName();
		this.secondName = opd.getsecondName(); 
		this.name = this.firstName + " " + this.secondName;
		this.birthDate = null;
		this.age = opd.getAge();
		this.agetype = "";
		this.sex = opd.getSex();
		this.address = opd.getaddress();
		this.city = opd.getcity();
		this.nextKin = opd.getnextKin(); 
		this.telephone = "";
		this.mother_name = "";
		this.mother = ' ';
		this.father_name = "";
		this.father = ' ';
		this.bloodType = "";
		this.hasInsurance = ' ';
		this.parentTogether = ' ';
	}
	
	public Patient(String firstName, String secondName, Date birthDate, int age, String agetype, char sex,
			String address, String city, String nextKin, String telephone,
			String mother_name, char mother, String father_name, char father,
			String bloodType, char economicStatut, char parentTogether, String personalCode) { //Changed EduLev with bloodType
		this.firstName = firstName;
		this.secondName = secondName;
		this.name = this.firstName + " " + this.secondName;
		this.birthDate = birthDate;
		this.age = age;
		this.agetype = agetype;
		this.sex = sex;
		this.address = address;
		this.city = city;
		this.nextKin = nextKin;
		this.telephone = telephone;
		this.mother_name = mother_name;
		this.mother = mother;
		this.father_name = father_name;
		this.father = father;
		this.hasInsurance = economicStatut;
		this.bloodType = bloodType;
		this.parentTogether = parentTogether;
		this.taxCode = personalCode;
		this.height = 0;
		this.weight = 0;
	}
		
	public Patient(int code, String firstName, String secondName, String name, Date birthDate, int age, String agetype, char sex,
			String address, String city, String nextKin, String telephone, String note,
			String mother_name, char mother, String father_name, char father,
			String bloodType, char economicStatut, char parentTogether, String taxCode,
			float height, float weight, Blob photo, Image photoImage) { //Changed EduLev with bloodType
		this.code = code;
		this.firstName = firstName;
		this.secondName = secondName;
		this.name = name;
		this.birthDate = birthDate;
		this.age = age;
		this.agetype = agetype;
		this.sex = sex;
		this.address = address;
		this.city = city;
		this.nextKin = nextKin;
		this.telephone = telephone;
		this.note = note;
		this.mother_name = mother_name;
		this.mother = mother;
		this.father_name = father_name;
		this.father = father;
		this.hasInsurance = economicStatut;
		this.bloodType = bloodType;
		this.parentTogether = parentTogether;
		this.taxCode = taxCode;
		this.height = height;
		this.weight = weight;
		this.photo = photo;
		this.photoImage = photoImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getAge() {
		if (this.birthDate != null) {
			GregorianCalendar birthday = new GregorianCalendar();
			birthday.setTime(birthDate);
			DateTime now = new DateTime();
			DateTime birth = new DateTime(birthday.getTime());
			Period period = new Period(birth, now, PeriodType.yearMonthDay());
			age = period.getYears();
		}
		return age;
	}
	
	public int getMonths() {
		int months = 0;
		if (this.birthDate != null) {
			GregorianCalendar birthday = new GregorianCalendar();
			birthday.setTime(birthDate);
			DateTime now = new DateTime();
			DateTime birth = new DateTime(birthday.getTime());
			Period period = new Period(birth, now, PeriodType.months());
			months = period.getMonths();
		}
		return months;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAgetype(String agetype) {
		this.agetype = agetype;
	}

	public String getAgetype() {
		return agetype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.name = this.firstName + " " + this.secondName;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getNextKin() {
		return nextKin;
	}

	public void setNextKin(String nextKin) {
		this.nextKin = nextKin;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
		this.name = this.firstName + " " + this.secondName;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBloodType() {
	    return bloodType;
	}
	
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	
	public String getName() {
		return this.name;
	}

	public char getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(char hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public char getFather() {
		return father;
	}

	public void setFather(char father) {
		this.father = father;
	}

	public char getMother() {
		return mother;
	}

	public void setMother(char mother) {
		this.mother = mother;
	}

	public char getParentTogether() {
		return parentTogether;
	}

	public void setParentTogether(char parentTogether) {
		this.parentTogether = parentTogether;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String toString() {
		return getName();
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getMother_name() {
		return mother_name;
	}

	public void setMother_name(String mother_name) {
		this.mother_name = mother_name;
	}
	
	public Blob getBlobPhoto() {
		return photo;
	}
	
	public void setBlobPhoto(Blob photo) {
		this.photo = photo;
	}

	public Image getPhoto() {
		try {
			if (photo != null && photo.length() > 0) {
				BufferedInputStream is = new BufferedInputStream(photo.getBinaryStream());
				Image image = ImageIO.read(is);
				setPhoto(image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return photoImage;
	}
	
	public void setPhoto(Image image) {
		this.photoImage = image;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Patient)) {
			return false;
		}
		
		Patient patient = (Patient)obj;
		return (this.getCode().equals(patient.getCode()));
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + ((code == null) ? 0 : code);
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
	
	public String getSearchString() {
		StringBuffer sbName = new StringBuffer();
		sbName.append(getCode());
		sbName.append(" ");
		sbName.append(getFirstName().toLowerCase());
		sbName.append(" ");
		sbName.append(getSecondName().toLowerCase());
		sbName.append(" ");
		sbName.append(getCity().toLowerCase());
		sbName.append(" ");
		if (getAddress() != null) sbName.append(getAddress().toLowerCase()).append(" ");
		if (getTelephone() != null) sbName.append(getTelephone()).append(" ");
		if (getNote() != null) sbName.append(getNote().toLowerCase()).append(" ");
		if (getTaxCode() != null) sbName.append(getTaxCode().toLowerCase()).append(" ");
		return sbName.toString();
	}
	
	public String getInformations() {
		int i = 0;
		StringBuffer infoBfr = new StringBuffer();
		if (city != null && !city.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(city);
			i++;
		}
		if (address != null && !address.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(address);
			i++;
		}
		if (telephone != null && !telephone.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(telephone);
			i++;
		}
		if (note != null && !note.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(note);
			i++;
		}
		if (taxCode != null && !taxCode.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(taxCode);
			i++;
		}
		return infoBfr.toString();
	}
}

