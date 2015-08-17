/**
 * @(#) Farmaco.java
 * 11-dec-2005
 * 14-jan-2006
 */

package org.isf.medicals.model;

import org.isf.medtype.model.MedicalType;

/**
 * Pure Model Medical DSR (Drugs Surgery Rest): represents a medical
 * 
 * @author bob
 * 		   modified by alex:
 * 			- product code
 * 			- pieces per packet
 */
public class Medical implements Comparable<Medical> {
	/**
	 * Code of the medical
	 */
	private Integer code;

	/**
	 * Code of the product
	 */
	
	private String prod_code;

	/**
	 * Type of the medical
	 */
	private MedicalType type;

	/**
	 * Description of the medical
	 */
	private String description;

	/**
	 * initial quantity
	 */
	private double initialqty;
	
	/**
	 * pieces per packet
	 */
	private Integer pcsperpck;

	/**
	 * input quantity
	 */
	private double inqty;

	/**
	 * out quantity
	 */
	private double outqty;
	/**
	 * min quantity
	 */
	private double minqty;


	/**
	 * Lock control
	 */

	private Integer lock;

	/**
	 * Constructor
	 */
	public Medical(Integer code, MedicalType type, String prod_code, String description,
			double initialqty, Integer pcsperpck, double minqty, double inqty, double outqty, Integer lock) {
		super();
		this.code = code;
		this.type = type;
		this.prod_code = prod_code;
		this.description = description;
		this.initialqty = initialqty;
		this.pcsperpck = pcsperpck;
		this.minqty=minqty;
		this.inqty = inqty;
		this.outqty = outqty;
		this.lock = lock;
	}
	
	public double getTotalQuantity()
	{
		return initialqty + inqty - outqty;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getInitialqty() {
		return initialqty;
	}

	public void setInitialqty(double initialqty) {
		this.initialqty = initialqty;
	}

	public double getInqty() {
		return inqty;
	}

	public void setInqty(double inqty) {
		this.inqty = inqty;
	}
	public double getMinqty() {
		return minqty;
	}

	public void setMinqty(double minqty) {
		this.minqty = minqty;
	}

	public Integer getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public double getOutqty() {
		return outqty;
	}

	public void setOutqty(double outqty) {
		this.outqty = outqty;
	}

	public MedicalType getType() {
		return type;
	}

	public void setType(MedicalType type) {
		this.type = type;
	}

	public String getProd_code() {
		return prod_code;
	}

	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}

	public Integer getPcsperpck() {
		return pcsperpck;
	}

	public void setPcsperpck(Integer pcsperpck) {
		this.pcsperpck = pcsperpck;
	}

	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof Medical) ? false
				: (getCode().equals(((Medical) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((Medical) anObject).getDescription())
						&& getType().equals(((Medical) anObject).getType())
						&& getProd_code().equals(((Medical) anObject).getProd_code())
						&& getInitialqty()==(((Medical) anObject).getInitialqty()) 
						&& getInqty()==(((Medical) anObject).getInqty())
						&& getOutqty()==(((Medical) anObject).getOutqty())
						&& (getLock()
						.equals(((Medical) anObject).getLock())));
	}

	public String toString() {
		return getDescription();
	}

	public int compareTo(Medical o) {
		return this.description.compareTo(o.getDescription());
	}
}
