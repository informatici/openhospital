/**
 * @(#) TipoFarmaco.java
 * 11-dec-2005
 * 14-jan-2006
 */

package org.isf.medtype.model;

/**
 * Defines a medical type: D: k: S: R:
 * @author  bob
 */
public class MedicalType {
	/**
	 * Code
	 */
	private String code;

	/**
	 * Description
	 */
	private String description;

	public MedicalType(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof MedicalType) ? false
				: (getCode().equalsIgnoreCase(
						((MedicalType) anObject).getCode()) && getDescription()
						.equalsIgnoreCase(
								((MedicalType) anObject).getDescription()));
	}

	public String toString() {
		return getDescription();
	}
}
