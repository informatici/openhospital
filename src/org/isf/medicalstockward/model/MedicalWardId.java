package org.isf.medicalstockward.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/*------------------------------------------
 * Medical Ward - model for the medical entity
 * -----------------------------------------
 * modification history
 * ? - ?
 * 17/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@SuppressWarnings("serial")
@Embeddable
public class MedicalWardId implements Serializable 
{	
	@Column(name="MDSRWRD_WRD_ID_A")
	private char ward_id;

	@NotNull
	@Column(name="MDSRWRD_MDSR_ID")
	private int medical_id;
	
	public MedicalWardId() 
	{
	}
	
	public MedicalWardId(char ward_id, int medical_id) 
	{
		this.ward_id = ward_id;
		this.medical_id = medical_id;
	}

	public char getWardId() {
		return this.ward_id;
	}
	
	public void setWardId(char ward_id) {
		this.ward_id = ward_id;
	}

	public int getMedicalId() {
		return this.medical_id;
	}
	
	public void setMedicalId(int medical_id) {
		this.medical_id = medical_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + medical_id;
		result = prime * result + ward_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MedicalWardId)) {
			return false;
		}
		MedicalWardId other = (MedicalWardId) obj;
		if (medical_id != other.medical_id) {
			return false;
		}
		if (ward_id != other.ward_id) {
			return false;
		}
		return true;
	}
}
