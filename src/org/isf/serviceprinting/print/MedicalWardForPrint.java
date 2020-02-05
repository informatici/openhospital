package org.isf.serviceprinting.print;

import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.ward.model.Ward;

/**
 * @author mwithi
 * 
 */
public class MedicalWardForPrint implements Comparable<MedicalWardForPrint> {
	
	private Ward ward;
	private String code;
	private Medical medical;
	private Double qty;
	private Integer packets;
	
	public MedicalWardForPrint(MedicalWard med, Ward ward) {
		super();
		this.ward = ward;
		this.medical = null;
		this.medical = med.getMedical();
		this.qty = med.getQty();
		this.code = medical.getProd_code();
		this.packets = 0;
		int pcsPerPck = medical.getPcsperpck();
		if (pcsPerPck > 1) { 
			this.packets = (int) (qty / pcsPerPck);
		}
	}
	
	public Ward getWard() {
		return ward;
	}
	
	public String getCode() {
		return code;
	}

	public Medical getMedical() {
		return medical;
	}
	
	public Double getQty() {
		return qty;
	}
	
	public Integer getPackets() {
		return packets;
	}

	public int compareTo(MedicalWardForPrint o) {
		return medical.getDescription().toUpperCase().compareTo(o.getMedical().getDescription().toUpperCase());
	}
}
