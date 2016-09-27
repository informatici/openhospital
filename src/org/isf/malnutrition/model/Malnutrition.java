package org.isf.malnutrition.model;

import java.util.GregorianCalendar;

/*------------------------------------------
 * Malnutrition - malnutrition control model
 * -----------------------------------------
 * modification history
 * 11/01/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="MALNUTRITIONCONTROL")
public class Malnutrition 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MLN_ID")
	private int code;

	@NotNull
	@Column(name="MLN_DATE_SUPP")
	private GregorianCalendar dateSupp;

	@Column(name="MNL_DATE_CONF")
	private GregorianCalendar dateConf;

	@NotNull
	@Column(name="MLN_ADM_ID")
	private int admId;

	@NotNull
	@Transient
	private int patId;

	@NotNull
	@Column(name="MLN_HEIGHT")
	private float height;

	@NotNull
	@Column(name="MLN_WEIGHT")
	private float weight;

	@NotNull
	@Column(name="MLN_LOCK")
	private int lock;

	@Transient
	private volatile int hashCode = 0;
	

	public Malnutrition() { }
	
	public Malnutrition(int aCode, GregorianCalendar aDateSupp,
			GregorianCalendar aDateConf, int aAdmId, float aHeight,
			float aWeight, int aLock) {
		code = aCode;
		dateSupp = aDateSupp;
		dateConf = aDateConf;
		admId = aAdmId;
		height = aHeight;
		weight = aWeight;
		lock = aLock;
	}
	
	public Malnutrition(int aCode, GregorianCalendar aDateSupp,
			GregorianCalendar aDateConf, int aAdmId, int aPatId, float aHeight,
			float aWeight, int aLock) {
		code = aCode;
		dateSupp = aDateSupp;
		dateConf = aDateConf;
		admId = aAdmId;
		patId = aPatId;
		height = aHeight;
		weight = aWeight;
		lock = aLock;
	}

	public void setCode(int aCode) {
		code = aCode;
	}

	public int getCode() {
		return code;
	}

	public void setLock(int aLock) {
		lock = aLock;
	}

	public int getLock() {
		return lock;
	}

	public void setDateSupp(GregorianCalendar aDateSupp) {
		dateSupp = aDateSupp;
	}

	public void setDateConf(GregorianCalendar aDateConf) {
		dateConf = aDateConf;
	}

	public void setAdmId(int aAdmId) {
		admId = aAdmId;
	}

	public void setPatId(int patId) {
		this.patId = patId;
	}
	
	public void setHeight(float aHeight) {
		height = aHeight;
	}

	public void setWeight(float aWeight) {
		weight = aWeight;
	}

	public GregorianCalendar getDateSupp() {
		return dateSupp;
	}

	public GregorianCalendar getDateConf() {
		return dateConf;
	}

	public int getAdmId() {
		return admId;
	}

	public int getPatId() {
		return patId;
	}
	
	public float getHeight() {
		return height;
	}

	public float getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if ((other == null) || (!(other instanceof Malnutrition)))
			return false;
		if ((getDateConf() == null)
				&& (((Malnutrition) other).getDateConf() == null))
			result = true;
		if ((getDateSupp() == null)
				&& (((Malnutrition) other).getDateSupp() == null))
			result = true;
		if (!result){
				if((getDateConf()==null)||(((Malnutrition)other).getDateConf()==null))return false;
				if((getDateSupp()==null)||(((Malnutrition)other).getDateSupp()==null))return false;
				if((getDateConf().equals(((Malnutrition) other).getDateConf()))
				&& (getDateSupp().equals(((Malnutrition) other).getDateSupp())))
			result = true;
		}
		if (result) {
			if (getAdmId() == (((Malnutrition) other).getAdmId())
					&& getHeight() == (((Malnutrition) other).getHeight())
					&& getWeight() == (((Malnutrition) other).getWeight())) {
				return true;
			} else
				return false;
		} else
			return false;
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
}
