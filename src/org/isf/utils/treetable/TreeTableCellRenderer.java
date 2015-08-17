package org.isf.utils.treetable;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;

// 
// The renderer used to display the tree nodes, a JTree.  
//

public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int visibleRow;
	protected JTreeTable jTreeTable;
	
	public TreeTableCellRenderer(TreeModel model, JTreeTable jtreetable) { 
		super(model); 
		this.jTreeTable = jtreetable;
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, 0, w, this.jTreeTable.getHeight());
	}

	public void paint(Graphics g) {
		g.translate(0, -visibleRow * getRowHeight());
		super.paint(g);
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row, int column) {
		if(isSelected)
			setBackground(table.getSelectionBackground());
		else
			setBackground(table.getBackground());

		visibleRow = row;
		return this;
	}
	
}