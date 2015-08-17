package org.isf.pricesothers.model;

/**
 * Others : represents an other entry for prices
 * @author Alex
 */
public class PricesOthers {
    
    private int id;
    private String Code;
    private String Description;
	private boolean opdInclude;
	private boolean ipdInclude;
	private boolean daily;
	private boolean discharge;
	private boolean undefined;
    
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

}