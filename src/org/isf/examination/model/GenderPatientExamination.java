/**
 * 
 */
package org.isf.examination.model;

/**
 * @author Mwithi
 *
 */
public class GenderPatientExamination {
	
	private PatientExamination patex;
	
	private boolean male;

	/**
	 * @param patex
	 * @param male
	 */
	public GenderPatientExamination(PatientExamination patex, boolean male) {
		super();
		this.male = male;
		this.patex = patex;
	}

	/**
	 * @return the male
	 */
	public boolean isMale() {
		return male;
	}

	/**
	 * @param male the male to set
	 */
	public void setMale(boolean male) {
		this.male = male;
	}

	/**
	 * @return the patex
	 */
	public PatientExamination getPatex() {
		return patex;
	}

	/**
	 * @param patex the patex to set
	 */
	public void setPatex(PatientExamination patex) {
		this.patex = patex;
	}
}
