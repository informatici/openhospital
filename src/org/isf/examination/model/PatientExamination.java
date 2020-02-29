/**
 * 
 */
package org.isf.examination.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.patient.model.Patient;

/**
 * @author Mwithi
 * 
 * the model for Patient Examination
 *
 */
@Entity
@Table(name="PATIENTEXAMINATION")
public class PatientExamination implements Serializable, Comparable<PatientExamination> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PEX_ID")
	private int pex_ID;

	@NotNull
	@Column(name="PEX_DATE")
	private Timestamp pex_date;

	@NotNull
	@ManyToOne
	@JoinColumn(name="PEX_PAT_ID")
	private Patient patient;
	
	@Column(name="PEX_HEIGHT")
	private double pex_height;
	
	@Column(name="PEX_WEIGHT")
	private int pex_weight;
	
	@Column(name="PEX_PA_MIN")
	private int pex_pa_min;
	
	@Column(name="PEX_PA_MAX")
	private int pex_pa_max;
	
	@Column(name="PEX_FC")
	private int pex_fc;
	
	@Column(name="PEX_TEMP")
	private double pex_temp;
	
	@Column(name="PEX_SAT")
	private double pex_sat;
	
	@Column(name="PEX_NOTE", length=300)
	private String pex_note;
	
	@Transient
	private volatile int hashCode = 0;

	/**
	 * 
	 */
	public PatientExamination() {
		super();
	}
	
	/**
	 * @param pex_date
	 * @param patient
	 * @param pex_height
	 * @param pex_weight
	 * @param pex_pa_min
	 * @param pex_pa_max
	 * @param pex_fc
	 * @param pex_temp
	 * @param pex_sat
	 * @param pex_note
	 */
	public PatientExamination(Timestamp pex_date, Patient patient, double pex_height, int pex_weight, int pex_pa_min, int pex_pa_max, int pex_fc, double pex_temp, double pex_sat, String pex_note) {
		super();
		this.pex_date = pex_date;
		this.patient = patient;
		this.pex_height = pex_height;
		this.pex_weight = pex_weight;
		this.pex_pa_min = pex_pa_min;
		this.pex_pa_max = pex_pa_max;
		this.pex_fc = pex_fc;
		this.pex_temp = pex_temp;
		this.pex_sat = pex_sat;
		this.pex_note = pex_note;
	}



	/**
	 * @return the pex_ID
	 */
	public int getPex_ID() {
		return pex_ID;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @param pex_ID the pex_ID to set
	 */
	public void setPex_ID(int pex_ID) {
		this.pex_ID = pex_ID;
	}

	/**
	 * @return the pex_date
	 */
	public Timestamp getPex_date() {
		return pex_date;
	}

	/**
	 * @param pex_date the pex_date to set
	 */
	public void setPex_date(Timestamp pex_date) {
		this.pex_date = pex_date;
	}

//	/**
//	 * @return the pex_pat_ID
//	 */
//	public int getPex_pat_ID() {
//		return pex_pat_ID;
//	}
//
//	/**
//	 * @param pex_pat_ID the pex_pat_ID to set
//	 */
//	public void setPex_pat_ID(int pex_pat_ID) {
//		this.pex_pat_ID = pex_pat_ID;
//	}

	/**
	 * @return the pex_height
	 */
	public double getPex_height() {
		return pex_height;
	}

	/**
	 * @param pex_height the pex_height to set
	 */
	public void setPex_height(double pex_height) {
		this.pex_height = pex_height;
	}

	/**
	 * @return the pex_weight
	 */
	public int getPex_weight() {
		return pex_weight;
	}

	/**
	 * @param weight the pex_weight to set
	 */
	public void setPex_weight(int weight) {
		this.pex_weight = weight;
	}

	/**
	 * @return the pex_pa_min
	 */
	public int getPex_pa_min() {
		return pex_pa_min;
	}

	/**
	 * @param pex_pa_min the pex_pa_min to set
	 */
	public void setPex_pa_min(int pex_pa_min) {
		this.pex_pa_min = pex_pa_min;
	}

	/**
	 * @return the pex_pa_max
	 */
	public int getPex_pa_max() {
		return pex_pa_max;
	}

	/**
	 * @param pex_pa_max the pex_pa_max to set
	 */
	public void setPex_pa_max(int pex_pa_max) {
		this.pex_pa_max = pex_pa_max;
	}

	/**
	 * @return the pex_fc
	 */
	public int getPex_fc() {
		return pex_fc;
	}

	/**
	 * @param pex_fc the pex_fc to set
	 */
	public void setPex_fc(int pex_fc) {
		this.pex_fc = pex_fc;
	}

	/**
	 * @return the pex_temp
	 */
	public double getPex_temp() {
		return pex_temp;
	}

	/**
	 * @param pex_temp the pex_temp to set
	 */
	public void setPex_temp(double pex_temp) {
		this.pex_temp = pex_temp;
	}

	/**
	 * @return the pex_sat
	 */
	public double getPex_sat() {
		return pex_sat;
	}

	/**
	 * @param pex_sat the pex_sat to set
	 */
	public void setPex_sat(double pex_sat) {
		this.pex_sat = pex_sat;
	}

	/**
	 * @return the pex_note
	 */
	public String getPex_note() {
		return pex_note;
	}

	/**
	 * @param pex_note the pex_note to set
	 */
	public void setPex_note(String pex_note) {
		this.pex_note = pex_note;
	}

	@Override
	public int compareTo(PatientExamination o) {
		return this.pex_date.compareTo(o.getPex_date());
	}
	
	public double getBMI() {
		if (pex_height != 0) {
			double temp = Math.pow(10, 2); // 2 <-- decimal digits;
			return Math.round((pex_weight / Math.pow(pex_height / 100, 2)) * temp) / temp ;
		} else return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof PatientExamination)) {
			return false;
		}
		
		PatientExamination price = (PatientExamination)obj;
		return (pex_ID == price.getPex_ID());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + pex_ID;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
}
