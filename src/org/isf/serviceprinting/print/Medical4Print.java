package org.isf.serviceprinting.print;

import org.isf.medicals.model.Medical;

public class Medical4Print {

	private String medicalDescription;
	private String medicalType;
	private double minQty;
	private double curQty;
	private String expiring;
	
	public Medical4Print(Medical medical){
		medicalDescription=medical.getDescription();
		medicalType=medical.getType().getDescription();
		minQty=medical.getMinqty();
		curQty=medical.getInitialqty()+medical.getInqty()-medical.getOutqty();
		if(curQty<minQty)expiring="Yes";
		else expiring="No";
	}

	public double getCurQty() {
		return curQty;
	}

	public void setCurQty(double curQty) {
		this.curQty = curQty;
	}
	public String getExpiring() {
		return expiring;
	}

	public void setExpiring(String expiring) {
		this.expiring = expiring;
	}

	public String getMedicalDescription() {
		return medicalDescription;
	}

	public void setMedicalDescription(String medicalDescription) {
		this.medicalDescription = medicalDescription;
	}

	public String getMedicalType() {
		return medicalType;
	}

	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}

	public double getMinQty() {
		return minQty;
	}

	public void setMinQty(double minQty) {
		this.minQty = minQty;
	}
	
}
