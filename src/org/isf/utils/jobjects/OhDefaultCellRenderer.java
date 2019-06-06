package org.isf.utils.jobjects;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OhDefaultCellRenderer extends DefaultTableCellRenderer{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color darkOrange=new Color(159, 188, 208);
	Color lightOrange=new Color(231, 236, 240);
	Color lightGray=new Color(242, 242, 242);
	int hoveredRow = -1;
	
	List<Integer> centeredColumns=new ArrayList<Integer>();
	
	public OhDefaultCellRenderer(List<Integer> centeredColumns) {
		super();
		this.centeredColumns=centeredColumns;
	}
	public  OhDefaultCellRenderer() {
		super();
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, java.lang.Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component cmp= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		JLabel lbl=(JLabel)cmp;
		
		boolean found=false;
		for (Iterator<Integer> iterator = this.centeredColumns.iterator(); iterator.hasNext();) {
			Integer centered = (Integer) iterator.next();
			if(centered==column){
				
				lbl.setHorizontalAlignment(CENTER);
				found=true;
				break;
			}
			
		}
		
		if(!found){
			lbl.setHorizontalAlignment(LEFT);
		}
		if(isSelected){
			cmp.setBackground(darkOrange);
		}
		else{
			if(row % 2== 0){
				cmp.setBackground(lightGray);
			}
			else{
				cmp.setBackground(lightOrange);
			}				
		}
		if(row == hoveredRow){
			cmp.setBackground(darkOrange);
	    }
		return cmp;
	}
	
	public void setHoveredRow(int hoveredRow) {
		// TODO Auto-generated method stub
		this.hoveredRow=hoveredRow;
	}
}
