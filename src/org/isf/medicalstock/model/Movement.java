package org.isf.medicalstock.model;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.supplier.model.Supplier;
import org.isf.ward.model.Ward;
import org.isf.generaldata.MessageBundle;

public class Movement {

	private int code;
	private Medical medical;
	private MovementType type;
	private Ward ward;
	private Lot lot;
	private GregorianCalendar date;
	private int quantity;
	private Supplier supplier;
	private String refNo;
	
	public Movement(Medical aMedical,MovementType aType,Ward aWard,Lot aLot,GregorianCalendar aDate,int aQuantity,Supplier aSupplier, String aRefNo){
		medical = aMedical;
		type = aType;
		ward = aWard;
		lot = aLot;
		date = aDate;
		quantity = aQuantity;
		supplier = aSupplier;
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
	public GregorianCalendar getDate(){
		return date;
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
	public void setLot(Lot aLot){
		lot=aLot;
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
}
