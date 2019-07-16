
/*------------------------------------------
 * ExamShow - list all possible results for an exam
 * -----------------------------------------
 * modification history
 * 03/11/2006 - ross - changed title
 * 			         - version is now 1.0 
 *------------------------------------------*/


package org.isf.exa.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.exa.gui.ExamRowEdit.ExamRowListener;
import org.isf.exa.manager.ExamRowBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

public class ExamShow extends JDialog implements ExamRowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton closeButton = null;
	private Exam exam = null;
	private JButton newButton = null;
	private JButton deleteButton = null;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.common.descriptionm")
	};
	private int[] pColumwidth = {50,250};
	private DefaultTableModel model ;
	private JTable table;
	private ExamRow examRow = null;
	private ArrayList<ExamRow> pExamRow;
	private JDialog myFrame;
	
	public ExamShow(JFrame owner, Exam aExam){
		super(owner,true);
		myFrame = this;
		exam = aExam;
		initialize();
	}
	
	private void initialize(){
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
        final int pfrmBase = 10;
        final int pfrmWidth = 3;
        final int pfrmHeight = 3;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setContentPane(getJContentPane());
		this.setTitle(exam.getDescription() + " " + MessageBundle.getMessage("angal.exa.results"));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.NORTH);  
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);  
		}
		return jContentPane;
	}
	
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			dataPanel= new JPanel();
                        
			model = new ExamRowBrowsingModel(exam.getCode());
			table = new JTable(model);
			table.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			table.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);			
			jContentPane.add(new JScrollPane(table),BorderLayout.CENTER);
		}
		return dataPanel;
	}
	
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getNewButton(), null);  
			buttonPanel.add(getDeleteButton());  
			buttonPanel.add(getCloseButton());
		}
		return buttonPanel;
	}
	
	private JButton getNewButton(){
		if(newButton == null){
			newButton = new JButton();
			newButton.setText(MessageBundle.getMessage("angal.common.new"));  // Generated
            newButton.setMnemonic(KeyEvent.VK_N);
			newButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					examRow = new ExamRow();
					ExamRowEdit newrecord = new ExamRowEdit(myFrame, examRow, exam);
					newrecord.addExamListener(ExamShow.this);
					newrecord.setVisible(true);
				}
			});
		}
		return newButton;
	}
	
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(MessageBundle.getMessage("angal.common.close"));  // Generated
            closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
				}
			});
		}
		return closeButton;
	}
	
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setText(MessageBundle.getMessage("angal.common.delete"));  // Generated
            deleteButton.setMnemonic(KeyEvent.VK_D);
			deleteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (table.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(				
		                        null,
		                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
		                        MessageBundle.getMessage("angal.hospital"),
		                        JOptionPane.PLAIN_MESSAGE);				
						return;									
					}else {
						ExamRowBrowsingManager manager = new ExamRowBrowsingManager();
						ExamRow row = (ExamRow)(((ExamRowBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(
	                        null,
	                        MessageBundle.getMessage("angal.exa.deleteexamresult")+" \""+row.getDescription()+"\" ?",
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.YES_NO_OPTION);

						if ((n == JOptionPane.YES_OPTION)){
							try {
								boolean deleted = manager.deleteExamRow(row);
								
								if (true == deleted) {
									examRowDeleted();
								}
							} catch (OHServiceException e1) {
								OHServiceExceptionUtil.showMessages(e1);
							}
						}
					}
				}
			});
		}
		return deleteButton;
	}
	
	
class ExamRowBrowsingModel extends DefaultTableModel {
		
	private static final long serialVersionUID = 1L;

            public ExamRowBrowsingModel(String aCode) {
                ExamRowBrowsingManager manager = new ExamRowBrowsingManager();
                try {
                    pExamRow = manager.getExamRowByExamCode(aCode);
                } catch (OHServiceException e) {
                    pExamRow = null;
                    OHServiceExceptionUtil.showMessages(e);
                }
            }

            public int getRowCount() {
                if (pExamRow == null)
                    return 0;
                return pExamRow.size();
            }

            public String getColumnName(int c) {
                return pColums[c];
            }

            public int getColumnCount() {
                return pColums.length;
            }

            public Object getValueAt(int r, int c) {
                ExamRow examRow = pExamRow.get(r);
                if(c==-1){
                    return examRow;
                }
                else if (c == 0) {
                    return examRow.getCode();
                } else if (c == 1) {
                    return examRow.getDescription();
                } 
                return null;
            }

            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                //return super.isCellEditable(arg0, arg1);
                return false;
            }
	}


	@Override
	public void examRowInserted(AWTEvent e) {
		pExamRow.add(0, examRow);
		((ExamRowBrowsingModel) table.getModel()).fireTableDataChanged();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	
	public void examRowDeleted() {
		pExamRow.remove(table.getSelectedRow());
		model.fireTableDataChanged();
		table.updateUI();
	}
}
