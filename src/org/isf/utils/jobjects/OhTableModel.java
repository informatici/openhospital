package org.isf.utils.jobjects;

import java.util.ArrayList;
/**
 * @author u2g
 * This class builds products table with filter
 */
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.isf.accounting.model.BillItems;
import org.isf.generaldata.MessageBundle;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.priceslist.model.Price;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.exception.OHException;

public class OhTableModel<T> implements TableModel{

	
	List<T> dataList;	
	List<T> filteredList;
	String searchQuery="";
	boolean allowSearchByCode=false;
	
	public  OhTableModel(List<T> dataList) {
		this.dataList=dataList;
		this.filteredList=new ArrayList<T>();
		
		for (Iterator<T> iterator = dataList.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			this.filteredList.add(t);			
		}
//		Collections.copy(this.filteredList, this.dataList);
	}
	public  OhTableModel(List<T> dataList, boolean allowSearchByCode) {
		this.allowSearchByCode=allowSearchByCode;
		this.dataList=dataList;
		this.filteredList=new ArrayList<T>();
		
		for (Iterator<T> iterator = dataList.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			this.filteredList.add(t);			
		}
	}
	
	
	public T filter(String searchQuery) throws OHException{

		this.searchQuery=searchQuery;
		this.filteredList=new ArrayList<T>();
		
		for (Iterator<T> iterator = this.dataList.iterator(); iterator.hasNext();) { 
			Object object = (Object) iterator.next();
			if(object instanceof Price){
				Price price=(Price) object;
				String strItem=price.getItem()+price.getDesc();
				if( allowSearchByCode && searchQuery.equalsIgnoreCase(price.getItem())){
					T resPbj=(T)object;
					filteredList.clear();
					filteredList.add(resPbj);
					return resPbj;
				}
				strItem = strItem.toLowerCase();
				searchQuery = searchQuery.toLowerCase();
				if(strItem.indexOf(searchQuery)>=0){
					filteredList.add((T) object);
				}
			}
			
			if(object instanceof MedicalWard){
				MedicalWard mdw =(MedicalWard) object;
				String strItem = mdw.getMedical().getProd_code() + mdw.getMedical().getDescription();				

				if( allowSearchByCode && searchQuery.equalsIgnoreCase(mdw.getMedical().getProd_code())){
					T resPbj=(T)object;
					filteredList.clear(); 
					filteredList.add(resPbj);
					return resPbj;
				}
				
				strItem = strItem.toLowerCase();
				searchQuery = searchQuery.toLowerCase();
				if(strItem.indexOf(searchQuery)>=0){
					filteredList.add((T) object);
				}
			}
			
			if(object instanceof PricesOthers){
				PricesOthers priceO =(PricesOthers) object;
				String strItem = priceO.getCode() + priceO.getDescription();				

				if( allowSearchByCode && searchQuery.equalsIgnoreCase(priceO.getCode())){
					T resPbj=(T)object;
					filteredList.clear();
					filteredList.add(resPbj);
					return resPbj;
				}
				
				strItem = strItem.toLowerCase();
				searchQuery = searchQuery.toLowerCase();
				if(strItem.indexOf(searchQuery)>=0){
					filteredList.add((T) object);
				}
			}
			
			if(object instanceof BillItems){
				BillItems priceO =(BillItems) object;
				String strItem = priceO.getItemDisplayCode() + priceO.getItemDescription();				

				if( allowSearchByCode && searchQuery.equalsIgnoreCase(priceO.getItemDisplayCode())){
					T resPbj=(T)object;
					filteredList.clear();
					filteredList.add(resPbj);
					return resPbj;
				}
				
				strItem = strItem.toLowerCase();
				searchQuery = searchQuery.toLowerCase();
				if(strItem.indexOf(searchQuery)>=0){
					filteredList.add((T) object);
				}
			}

		}
		if(filteredList.size()==1){
			return filteredList.get(0);
		}
		return null;
	}
	
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		String columnLable="";
		switch (columnIndex) {
		case 0:
			columnLable= MessageBundle.getMessage("angal.disctype.codem");
			break;
		case 1:
			columnLable= MessageBundle.getMessage("angal.common.description");
			break;

		default:
			break;
		}
		return columnLable;
	}

	@Override
	public int getRowCount() {
		if(this.filteredList==null){
			return 0;
		}
		return this.filteredList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		String value="";
		if(rowIndex>=0 && rowIndex<this.filteredList.size()){
			T obj=this.filteredList.get(rowIndex);
			if(obj instanceof Price){
				Price priceObj=(Price)obj;
				if(columnIndex==0){
				value= priceObj.getItem()!=null ?priceObj.getItem():priceObj.getId()+"";
				}
				else{
					value=priceObj.getDesc();
				}
			}
			if(obj instanceof MedicalWard){
				MedicalWard mdwObj=(MedicalWard)obj;
				if(columnIndex==0){
					value= mdwObj.getMedical().getProd_code()!=null? mdwObj.getMedical().getProd_code() : mdwObj.getMedical().getCode()+"";
				}
				else{
					value=mdwObj.getMedical().getDescription();
				}
			}
			if(obj instanceof PricesOthers){
				PricesOthers mdwObj=(PricesOthers)obj;
				if(columnIndex==0){
					value= mdwObj.getCode()!=null?mdwObj.getCode():mdwObj.getId()+"";
				}
				else{
					value= mdwObj.getDescription();
				}
			}
			
			if(obj instanceof BillItems){
				BillItems mdwObj=(BillItems)obj;
				if(columnIndex==0){
					value= mdwObj.getItemDisplayCode()!=null?mdwObj.getItemDisplayCode():mdwObj.getId()+"";
				}
				else{
					value=mdwObj.getItemDescription();
				}
			}
			
		}
		return value;
	}
	
	public T getObjectAt(int rowIndex){
		if(rowIndex>=0 && rowIndex<this.filteredList.size()){
			return this.filteredList.get(rowIndex);
			
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}


	public String getSearchQuery() {
		return searchQuery;
	}

	

}
