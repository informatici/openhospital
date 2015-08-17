package org.isf.lab.model;

/*------------------------------------------
 * Laboratory - laboratory exam execution model
 * -----------------------------------------
 * modification history
 * 02/03/2006 - theo - first beta version
 * 10/11/2006 - ross - new fields data esame, sex, age, material, inout flag added
 *------------------------------------------*/


import java.util.GregorianCalendar;
import org.isf.exa.model.Exam;

public class Laboratory {

	private int code;
	private String material;
	private Exam exam;
	private GregorianCalendar registrationDate;
	private GregorianCalendar examDate;
	private String result;
	private int lock;
	private String note;
	private int patId;
	private String patName;
	private String InOutPatient;
	private int age;
	private String sex;

	public Laboratory() { }
	
	public Laboratory(int aCode,Exam aExam,GregorianCalendar aDate,String aResult,
			int aLock, String aNote, int aPatId, String aPatName){
		code=aCode;
		exam=aExam;
		registrationDate=aDate;
		result=aResult;
		lock=aLock;
		note=aNote;
		patId=aPatId;
		patName=aPatName;
	}
	public Exam getExam(){
		return exam;
	}
	public GregorianCalendar getDate(){
		return registrationDate;
	}
	public String getResult(){
		return result;
	}
	public int getCode(){
		return code;
	}
	public int getLock(){
		return lock;
	}
	public void setCode(int aCode){
		code=aCode;
	}
	public void setExam(Exam aExam){
		exam=aExam;
	}
	public void setLock(int aLock){
		lock=aLock;
	}
	public GregorianCalendar getExamDate() {
		return examDate;
	}
	public void setExamDate(GregorianCalendar exDate) {
		this.examDate = exDate;
	}	
	public void setDate(GregorianCalendar aDate){
		registrationDate=aDate;
	}
	public void setResult(String aResult){
		result=aResult;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getPatId() {
		return patId;
	}
	public void setPatId(int patId) {
		this.patId = patId;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getInOutPatient() {
		return InOutPatient;
	}
	public void setInOutPatient(String InOut) {
		if (InOut==null) InOut="";
		this.InOutPatient = InOut;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}

