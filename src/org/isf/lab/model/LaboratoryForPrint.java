package org.isf.lab.model;

import java.util.GregorianCalendar;

import org.isf.exa.model.Exam;

public class LaboratoryForPrint {
	
	private String exam;
	private String date;
	private String result;
	private Integer code;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public LaboratoryForPrint(Integer aCode, Exam aExam,GregorianCalendar aDate,String aResult){
		code = aCode;
		exam=aExam.getDescription();
		date=getConvertedString(aDate);
		result=aResult;
	}
	
	private String getConvertedString(GregorianCalendar time){
		String string=String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		string+="/"+ (time.get(GregorianCalendar.MONTH) + 1);
		string+="/"+ time.get(GregorianCalendar.YEAR);
		string+="  "+ time.get(GregorianCalendar.HOUR_OF_DAY);
		string+=":"+ time.get(GregorianCalendar.MINUTE);
		string+=":"+ time.get(GregorianCalendar.SECOND);
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
