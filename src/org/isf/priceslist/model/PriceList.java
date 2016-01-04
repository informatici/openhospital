package org.isf.priceslist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * List model: represent a List
 * @author alex
 *
 */
/*------------------------------------------
 * Bill - model for the bill entity
 * -----------------------------------------
 * modification history
 * ? - Mwithi - first version 
 * 25/08/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="PRICELISTS")
public class PriceList 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LST_ID")
	private int id;
	
	@Column(name="LST_CODE")
    private String code;
	
	@Column(name="LST_NAME")
    private String name;
	
	@Column(name="LST_DESC")
    private String description;
	
	@Column(name="LST_CURRENCY")
    private String currency;
	
	@Transient
	private volatile int hashCode = 0;
	
	
	public PriceList() {
		super();
	}
	 
    public PriceList(int id, String code, String name, String description, String currency) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.currency = currency;
	}

	public int getId() {
		return id;
	}
	
    public void setId(int id) {
		this.id = id;
	}
	
    public String getCode() {
		return code;
	}
	
    public void setCode(String code) {
		this.code = code;
	}
	
    public String getName() {
		return name;
	}
	
    public void setName(String name) {
		this.name = name;
	}
	
    public String getDescription() {
		return description;
	}
	
    public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return name;
	}      
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof PriceList)) {
			return false;
		}
		
		PriceList priceList = (PriceList)obj;
		return (id == priceList.getId());
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
