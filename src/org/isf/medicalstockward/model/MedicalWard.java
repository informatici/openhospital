package org.isf.medicalstockward.model;

import org.isf.medicals.model.Medical;

public class MedicalWard implements Comparable<Object> {
	
	private Medical medical;
	private Double qty;
	
	public MedicalWard(Medical medical, Double qty) {
		super();
		this.medical = medical;
		this.qty = qty;
	}
	
	public Medical getMedical() {
		return medical;
	}
	
	public void setMedical(Medical medical) {
		this.medical = medical;
	}
	
	public Double getQty() {
		return qty;
	}
	
	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	public int compareTo(Object anObject) {
		if (anObject instanceof MedicalWard)
			return (medical.getDescription().toUpperCase().compareTo(
					((MedicalWard)anObject).getMedical().getDescription().toUpperCase()));
		else return 0;
	}
}
