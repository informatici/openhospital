package org.isf.medicalstockward.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.isf.medicals.model.Medical;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;

/*------------------------------------------
 * Medical Ward - model for the medical entity
 * -----------------------------------------
 * modification history
 * ? - ?
 * 17/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRWARD")
public class MedicalWard implements Comparable<Object> 
{	
	@Autowired
	@Transient
	private DbJpaUtil jpa;
	
	@EmbeddedId 
	MedicalWardId id;
	
	@Column(name="MDSRWRD_IN_QTI")
	private float in_quantity;
	
	@Column(name="MDSRWRD_OUT_QTI")
	private float out_quantity;
	
	@Transient
	private Double qty = 0.0;
	
	@Transient
	private volatile int hashCode = 0;
	
	
	public MedicalWard() {
		super();
		this.id = new MedicalWardId(); 
	}
	
	public MedicalWard(Medical medical, Double qty) {
		super();
		this.id = new MedicalWardId(); 
		this.id.setMedicalId(medical.getCode());
		this.qty = qty;
	}
	
	public MedicalWard(char ward_id, int medical_id, float in_quantity, float out_quantity) {
		super();
		this.id = new MedicalWardId(ward_id, medical_id);  
		this.in_quantity = in_quantity;
		this.out_quantity = out_quantity;
	}
	
	public MedicalWardId getId() {
		return id;
	}
	
	public void setId(char ward_id, int medical_id) {
		this.id = new MedicalWardId(ward_id, medical_id); 
	}
	
	public Medical getMedical() throws OHException {
		
				
		
		jpa.beginTransaction();	
		Medical medical = (Medical)jpa.find(Medical.class, id.getMedicalId()); 
		jpa.commitTransaction();
		
		return medical;
	}
	
	public void setMedical(Medical medical) {
		this.id.setMedicalId(medical.getCode());
	}
	
	public Double getQty() {
		return qty;
	}
	
	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	public int compareTo(Object anObject) {
		
		Medical medical;
				
		
		try {
			jpa.beginTransaction();	
			medical = (Medical)jpa.find(Medical.class, id.getMedicalId());
			jpa.commitTransaction();
			if (anObject instanceof MedicalWard)
				return (medical.getDescription().toUpperCase().compareTo(
						((MedicalWard)anObject).getMedical().getDescription().toUpperCase()));
			else return 0;
		} catch (OHException e) {
			return 0;
		}		
	}

	public char getWardId() {
		return this.id.getWardId();
	}
	
	public void setWardId(char ward_id) {
		this.id.setWardId(ward_id);
	}

	public int getMedicalId() {
		return this.id.getMedicalId();
	}
	
	public void setMedicalId(int medical_id) {
		this.id.setMedicalId(medical_id);
	}
	
	public float getInQuantity() {
		return this.in_quantity;
	}
	
	public void setInQuantity(float in_quantity) {
		this.in_quantity = in_quantity;
	}
	
	public float getOutQuantity() {
		return this.out_quantity;
	}
	
	public void setOutQuantity(float out_quantity) {
		this.out_quantity = out_quantity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof MedicalWard)) {
			return false;
		}
		
		MedicalWard ward = (MedicalWard)obj;
		return (this.id.getMedicalId() == ward.id.getMedicalId());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + this.id.getMedicalId();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
