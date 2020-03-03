package org.isf.admission.model;

import org.isf.patient.model.Patient;

public class AdmittedPatient {

	private Patient patient;
	private Admission admission;
	
		
	public AdmittedPatient(Patient patient, Admission admission) {
		this.patient = patient;
		this.admission = admission;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Admission getAdmission() {
		return admission;
	}
	public void setAdmission(Admission admission) {
		this.admission = admission;
	}
	
}
