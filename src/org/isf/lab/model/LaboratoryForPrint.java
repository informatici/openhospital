package org.isf.lab.model;

import java.util.GregorianCalendar;

import org.isf.exa.model.Exam;

public class LaboratoryForPrint {
	
	private String exam;
	private String date;
	private String result;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public LaboratoryForPrint(int aCode, Exam aExam,GregorianCalendar aDate,String aResult){
		code = aCode;
		exam=aExam.getDescription();
		date=getConvertedString(aDate);
		result=aResult;
	}
	
	private String getConvertedString(GregorianCalendar time){
		String string=String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		string+="/"+String.valueOf(time.get(GregorianCalendar.MONTH)+1);
		string+="/"+String.valueOf(time.get(GregorianCalendar.YEAR));
		string+="  "+String.valueOf(time.get(GregorianCalendar.HOUR_OF_DAY));
		string+=":"+String.valueOf(time.get(GregorianCalendar.MINUTE));
		string+=":"+String.valueOf(time.get(GregorianCalendar.SECOND));
		return string;
	}

    public String getDate() {
        return this.date;
    }

    public void setDate(String aDate) {
        this.date = aDate;
    }

    public String getExam() {
        return this.exam;
    }

    public void setExam(String aExam) {
        this.exam = aExam;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String aResult) {
        this.result = aResult;
    }


}
