package org.isf.malnutrition.model;

import java.util.GregorianCalendar;

public class Malnutrition {

	private int code;

	private GregorianCalendar dateSupp;

	private GregorianCalendar dateConf;

	private int admId;
	
	private int patId;

	private float height;

	private float weight;

	private int lock;

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
}
