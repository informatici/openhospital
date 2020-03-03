package org.isf.serviceprinting.print;

public class PriceForPrint implements Comparable<PriceForPrint> {

    private String list;
    private String currency;
    private String group;
    private String desc;
    private Double price;

    /**
     * Default constructor
     */
    public PriceForPrint() {}

	/**
	 * @return the list
	 */
	public String getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(String list) {
		this.list = list;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int compareTo(PriceForPrint o) {
		return this.group.compareTo(o.group);
	}
}
