package org.isf.utils.jobjects;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.operation.manager.OperationBrowserManager;
import org.isf.operation.model.Operation;
import org.isf.operation.model.OperationRow;
import org.isf.utils.exception.OHServiceException;

public class OhTableOperationModel<T> implements TableModel{

	List<T> dataList;	
	List<T> filteredList;
	OperationBrowserManager manageop = Context.getApplicationContext().getBean(OperationBrowserManager.class);
	
	public  OhTableOperationModel(List<T> dataList) {
		this.dataList = dataList;
		this.filteredList = new ArrayList<T>();
		
		if(dataList!=null){
			for (Iterator<T> iterator = dataList.iterator(); iterator.hasNext();) {
				T t = (T) iterator.next();
				this.filteredList.add(t);			
			}
		}
	}
	
	public int filter(String searchQuery){
		this.filteredList=new ArrayList<T>();
		
		for (Iterator<T> iterator = this.dataList.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if(object instanceof OperationRow){
				OperationRow price=(OperationRow) object;
				String strItem=price.getOperation().getCode()+price.getOpResult();
				strItem = strItem.toLowerCase();
				searchQuery = searchQuery.toLowerCase();
				if(strItem.indexOf(searchQuery)>=0){
					filteredList.add((T) object);
				}
			}
		}
		return filteredList.size();
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
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		String columnLable="";
		switch (columnIndex) {
		case 0:
			columnLable= MessageBundle.getMessage("angal.operationrowlist.date");
			//columnLable= "Date";
			break;
		case 1:
			columnLable= MessageBundle.getMessage("angal.operationrowlist.natureop");
			//columnLable= "Nature Operation";
			break;
		case 2:
			columnLable= MessageBundle.getMessage("angal.operationrowedit.result");
			//columnLable= "Resultat";
			break;
		case 3:
			columnLable= MessageBundle.getMessage("angal.operationrowedit.unitetrans");
			//columnLable= "Unite Trans";
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
		// TODO Auto-generated method stub
		String value="";
		if(rowIndex >=0 && rowIndex < this.filteredList.size()){
			T obj=this.filteredList.get(rowIndex);
			if(obj instanceof OperationRow){
				OperationRow opdObj=(OperationRow)obj;
				switch (columnIndex) {
				case 0:
					String dt = "";
					try {
						final DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH);
						dt = currentDateFormat.format(opdObj.getOpDate().getTime());
						value = dt;
					}
					catch (Exception ex){
						value=opdObj.getOpDate().getTime().toString();
					}
					
					break;
				case 1:
                                        Operation ope = null;
                                        try {
                                            //System.out.println("Looking operation whose code is " + opdObj.getOperation().getCode());
                                            ope = manageop.getOperationByCode(opdObj.getOperation().getCode());
                                        } catch (OHServiceException ex) {
                                            ex.printStackTrace();
                                        }
					if(ope != null)					
						value = ope.getDescription();
					else
						value = "";
					break;
				case 2:
					value=opdObj.getOpResult();
					break;
				case 3:
					value=opdObj.getTransUnit()+"";
					break;	
				default:
					break;
				}
			}			
		}
		return value;
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

}
