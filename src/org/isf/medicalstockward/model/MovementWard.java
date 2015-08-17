package org.isf.medicalstockward.model;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.patient.model.Patient;
import org.isf.ward.model.Ward;

/**
 * 		   @author mwithi
 * 
 */
public class MovementWard {

	private int code;
	private Ward ward;
	private GregorianCalendar date;
	private boolean isPatient;
	private Patient patient;
	private int age;
	private float weight;
	private String description;
	private Medical medical;
	private Double quantity;
	private String units;

	/**
	 * 
	 * @param ward
	 * @param date
	 * @param isPatient
	 * @param patient
	 * @param age
	 * @param weight
	 * @param description
	 * @param medical
	 * @param quantity
	 * @param units
	 */
	public MovementWard(Ward ward, GregorianCalendar date, boolean isPatient,
			Patient patient, int age, float weight, String description, Medical medical,
			Double quantity, String units) {
		super();
		this.ward = ward;
		this.date = date;
		this.isPatient = isPatient;
		this.patient = patient;
		this.age = age;
		this.weight = weight;
		this.description = description;
		this.medical = medical;
		this.quantity = quantity;
		this.units = units;
	}

	public int getCode(){
		return code;
	}
	
	public Medical getMedical(){
		return medical;
	}
	
	public GregorianCalendar getDate(){
		return date;
	}
	
	public Double getQuantity(){
		return quantity;
	}
	
	public Ward getWard() {
		return ward;
	}

	public void setWard(Ward ward) {
		this.ward = ward;
	}

	public boolean isPatient() {
		return isPatient;
	}

	public void setPatient(boolean isPatient) {
		this.isPatient = isPatient;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setCode(int aCode){
		code=aCode;
	}
	
	public void setMedical(Medical aMedical){
		medical=aMedical;
	}
}
