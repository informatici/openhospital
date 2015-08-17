package org.isf.vactype.model;

/*------------------------------------------
 * VaccineType - vaccine type class to model vaccine type
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 * 18/11/2011 - Cla - inserted print method
 *------------------------------------------*/

/**
 * Pure Model vaccineType (type of vaccines)
 * 
 * @author bob
 */
public class VaccineType {

	private String code;

	private String description;
	

	public VaccineType(String code, String description) {
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


	@Override
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof VaccineType) ? false
				: (getCode().equals(((VaccineType) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((VaccineType) anObject).getDescription()));
	}

	
	public String print() {
		return "vaccineType code=."+getCode()+". description=."+getDescription()+".";
	}

	public String toString() {
		return getDescription();
	}
	
}
