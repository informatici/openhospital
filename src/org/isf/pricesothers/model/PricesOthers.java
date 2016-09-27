package org.isf.pricesothers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Others : represents an other entry for prices
 * @author Alex
 */
 /*------------------------------------------
 * Others : represents an other entry for prices
 * -----------------------------------------
 * modification history
 * ? - Alex - first version 
 * 1/08/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="PRICESOTHERS")
public class PricesOthers 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OTH_ID")
    private int id;

	@NotNull
	@Column(name="OTH_CODE")  
    private String Code;

	@NotNull
	@Column(name="OTH_DESC") 
    private String Description;

	@NotNull
	@Column(name="OTH_OPD_INCLUDE") 
	private boolean opdInclude;

	@NotNull
	@Column(name="OTH_IPD_INCLUDE") 
	private boolean ipdInclude;

	@NotNull
	@Column(name="OTH_DAILY") 
	private boolean daily;
	
	@Column(name="OTH_DISCHARGE") 
	private boolean discharge;
	
	@Column(name="OTH_UNDEFINED") 
	private boolean undefined;
    
	@Transient
	private volatile int hashCode = 0;
	
	public PricesOthers() {
		super();
	}
	
    public PricesOthers(String code, String desc, boolean opdInclude,
			boolean ipdInclude, boolean discharge, boolean undefined) {
		super();
		Code = code;
		Description = desc;
		this.opdInclude = opdInclude;
		this.ipdInclude = ipdInclude;
		this.discharge = discharge;
		this.undefined = undefined;
	}

	public PricesOthers(int id, String code, String desc, boolean opdInclude,
			boolean ipdInclude, boolean discharge, boolean undefined) {
		super();
		this.id = id;
		Code = code;
		Description = desc;
		this.opdInclude = opdInclude;
		this.ipdInclude = ipdInclude;
		this.discharge = discharge;
		this.undefined = undefined;
	}

	public PricesOthers(int id, String code, String description,
			boolean opdInclude, boolean ipdInclude, boolean daily, boolean discharge, boolean undefined) {
		super();
		this.id = id;
		Code = code;
		Description = description;
		this.opdInclude = opdInclude;
		this.ipdInclude = ipdInclude;
		this.daily = daily;
		this.discharge = discharge;
		this.undefined = undefined;
	}

	public int getId() {
		return id;
	}
    
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return Code;
	}
	
	public void setCode(String code) {
		Code = code;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String desc) {
		Description = desc;
	}
	
	public boolean isOpdInclude() {
		return opdInclude;
	}
	
	public void setOpdInclude(boolean opdInclude) {
		this.opdInclude = opdInclude;
	}
	
	public boolean isIpdInclude() {
		return ipdInclude;
	}
	
	public void setIpdInclude(boolean ipdInclude) {
		this.ipdInclude = ipdInclude;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public boolean isDischarge() {
		return discharge;
	}

	public void setDischarge(boolean discharge) {
		this.discharge = discharge;
	}

	public boolean isUndefined() {
		return undefined;
	}

	public void setUndefined(boolean undefined) {
		this.undefined = undefined;
	}

	@Override
	public String toString() {
		return Description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof PricesOthers)) {
			return false;
		}
		
		PricesOthers priceOther = (PricesOthers)obj;
		return (id == priceOther.getId());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + id;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
}