package org.isf.medicalstock.model;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.medicals.model.Medical;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.supplier.model.Supplier;
import org.isf.ward.model.Ward;
import org.isf.generaldata.MessageBundle;

/*------------------------------------------
 * Medical Stock Movement- model for the medical entity
 * -----------------------------------------
 * modification history
 * ? - ?
 * 17/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRSTOCKMOV")
public class Movement 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MMV_ID")
	private int code;

	@NotNull
	@ManyToOne
	@JoinColumn(name="MMV_MDSR_ID")
	private Medical medical;

	@NotNull
	@ManyToOne
	@JoinColumn(name="MMV_MMVT_ID_A")
	private MovementType type;

	@ManyToOne
	@JoinColumn(name="MMV_WRD_ID_A")
	private Ward ward;

	@Column(name="MMV_LT_ID_A")
	private String lot_id;
	@Transient
	private Lot lot;

	@NotNull
	@Column(name="MMV_DATE")
	private GregorianCalendar date;

	@NotNull
	@Column(name="MMV_QTY")
	private int quantity;

	@Column(name="MMV_FROM")
	private Integer supplier_id;
	@Transient
	private Supplier supplier;

	@NotNull
	@Column(name="MMV_REFNO")
	private String refNo;
	
	@Transient
	private volatile int hashCode = 0;
	

	public Movement() { }
		
	public Movement(Medical aMedical,MovementType aType,Ward aWard,Lot aLot,GregorianCalendar aDate,int aQuantity,Supplier aSupplier, String aRefNo){
		medical = aMedical;
		type = aType;
		ward = aWard;
		lot = aLot;
		lot_id = aLot.getCode();
		date = aDate;
		quantity = aQuantity;
		supplier = aSupplier;
		supplier_id = aSupplier.getSupId();
		refNo=aRefNo;
	}
	
	public int getCode(){
		return code;
	}
	public Medical getMedical(){
		return medical;
	}
	public MovementType getType(){
		return type;
	}
	public Ward getWard(){
		return ward;
	}
	public Lot getLot(){
		return lot;
	}
	public String getLotId(){
		return lot_id;
	}
	public GregorianCalendar getDate(){
		return date;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Integer getOrigin(){
		return supplier_id;
	}
	public void setWard(Ward ward) {
		this.ward = ward;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
		supplier_id = supplier.getSupId();		
	}
	public void setCode(int aCode){
		code=aCode;
	}
	public void setMedical(Medical aMedical){
		medical=aMedical;
	}
	public void setType(MovementType aType){
		type=aType;
	}
	public void setLot(Lot aLot){
		lot=aLot;
		lot_id = lot.getCode();
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String toString(){
		return MessageBundle.getMessage("angal.medicalstock.medical")+":"+medical.toString()+MessageBundle.getMessage("angal.medicalstock.type")+":"+type.toString()+MessageBundle.getMessage("angal.medicalstock.quantity")+":"+quantity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Movement)) {
			return false;
		}
		
		Movement movment = (Movement)obj;
		return (this.getCode() == movment.getCode());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
