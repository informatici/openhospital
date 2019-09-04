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

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.supplier.model.Supplier;
import org.isf.ward.model.Ward;

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

	@ManyToOne
	@JoinColumn(name="MMV_LT_ID_A")
	private Lot lot;

	@NotNull
	@Column(name="MMV_DATE")
	private GregorianCalendar date;

	@NotNull
	@Column(name="MMV_QTY")
	private int quantity;

	@ManyToOne(optional = true, targetEntity=Supplier.class)
	@JoinColumn(name="MMV_FROM")
	private Supplier supplier;

	@NotNull
	@Column(name="MMV_REFNO")
	private String refNo;
        
//        @NotNull
//	@ManyToOne
//	@JoinColumn(name="MMV_WRD_ID_A_TO")	
//	private Ward wardTo;
	
	@Transient
	private volatile int hashCode = 0;
	

	public Movement() { }
		
	public Movement(Medical aMedical,MovementType aType,Ward aWard,Lot aLot,GregorianCalendar aDate,int aQuantity,Supplier aSupplier, String aRefNo){
		medical = aMedical;
		type = aType;
		ward = aWard;
		lot = aLot;
		date = aDate;
		quantity = aQuantity;
		supplier = aSupplier;
		refNo=aRefNo;
                //this.wardTo = null;
	}

//        public Movement(Medical aMedical,MovementType aType,Ward aWard,Lot aLot,GregorianCalendar aDate,int aQuantity,Supplier aSupplier, String aRefNo, Ward wardTo){
//		medical = aMedical;
//		type = aType;
//		ward = aWard;
//		lot = aLot;
//		date = aDate;
//		quantity = aQuantity;
//		supplier = aSupplier;
//		refNo=aRefNo;
//		this.wardTo = wardTo;
//	}
	
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
	public GregorianCalendar getDate(){
		return date;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Supplier getOrigin(){
		return supplier;
	}
	public void setWard(Ward ward) {
		this.ward = ward;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
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
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

//    public Ward getWardTo() {
//        return wardTo;
//    }
//
//    public void setWardTo(Ward wardTo) {
//        this.wardTo = wardTo;
//    }
        
	public String toString(){
		return MessageBundle.getMessage("angal.medicalstock.medical")+
				":"+
				medical.toString()+
				MessageBundle.getMessage("angal.medicalstock.type")+
				":"+
				type.toString()+
				MessageBundle.getMessage("angal.common.quantity")+":"+quantity;
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
