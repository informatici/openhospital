/*------------------------------------------
 * ExamBrowser - list all exams. let the user select an exam to edit
 * -----------------------------------------
 * modification history
 * 11/12/2005 - bob  - first beta version 
 * 03/11/2006 - ross - changed button Show into Results 
 * 			         - version is now 1.0 
 * 10/11/2006 - ross - corretto eliminazione esame, prima non si cancellava mai
 *------------------------------------------*/

package org.isf.exa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.jobjects.ModalJFrame;

public class ExamBrowser extends ModalJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 
	
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<Exam> pExam;
	private String[] pColums = { MessageBundle.getMessage("angal.exa.codem"), MessageBundle.getMessage("angal.exa.typem"), MessageBundle.getMessage("angal.exa.descriptionm") , MessageBundle.getMessage("angal.exa.procm") , MessageBundle.getMessage("angal.exa.defaultm") };
	private int[] pColumwidth = {60,330,160,60,130};
	private Exam exam;
	private DefaultTableModel model ;
	private JTable table;
	private final JFrame myFrame;
	
	private String pSelection;
	
	public ExamBrowser() {
		myFrame=this;
		setTitle(MessageBundle.getMessage("angal.exa.exambrowsing") +" ("+VERSION+")");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
        final int pfrmBase = 20;
        final int pfrmWidth = 15;
        final int pfrmHeight = 8;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		model = new ExamBrowsingModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setMinWidth(pColumwidth[2]);
		table.getColumnModel().getColumn(3).setMinWidth(pColumwidth[3]);
		table.getColumnModel().getColumn(4).setMinWidth(pColumwidth[4]);
				
		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.exa.selecttype"));
		buttonPanel.add(selectlabel);
		
		ExamBrowsingManager manager = new ExamBrowsingManager();
		pbox = new JComboBox();
		pbox.addItem(MessageBundle.getMessage("angal.exa.all"));
		ArrayList<ExamType> type = manager.getExamType();	//for efficiency in the sequent for
		for (ExamType elem : type) {
			pbox.addItem(elem);
		}
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pSelection=pbox.getSelectedItem().toString();
				if (pSelection.compareTo(MessageBundle.getMessage("angal.exa.all")) == 0)
					model = new ExamBrowsingModel();
				else
					model = new ExamBrowsingModel(pSelection);
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);
				
		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				exam=new Exam("","",new ExamType("",""),0,"",0);
				Exam last=new Exam("","",new ExamType("",""),0,"",0);
				ExamEdit newrecord = new ExamEdit(myFrame,exam,true);
				newrecord.setVisible(true);
				if(!last.equals(exam)){
					//TODO: implement ExamListener
					pExam.add(0,exam);
					((ExamBrowsingModel)table.getModel()).fireTableDataChanged();
					//table.updateUI();
					if (table.getRowCount() > 0)
						table.setRowSelectionInterval(0, 0);
				}
			}
		});
		buttonPanel.add(buttonNew);

		JButton buttonEdit = new JButton(MessageBundle.getMessage("angal.common.edit"));
		buttonEdit.setMnemonic(KeyEvent.VK_E);
		buttonEdit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					exam = (Exam)(((ExamBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
					Exam last=new Exam(exam.getCode(),exam.getDescription(),exam.getExamtype(),exam.getProcedure(),
							exam.getDefaultResult(),exam.getLock());
					ExamEdit editrecord = new ExamEdit(myFrame,exam,false);
					editrecord.setVisible(true);
					if(!last.equals(exam)){
						pExam.set(selectedrow,exam);
						((ExamBrowsingModel)table.getModel()).fireTableDataChanged();
						table.updateUI();
						if ((table.getRowCount() > 0) && selectedrow >-1)
							table.setRowSelectionInterval(selectedrow,selectedrow);
					}
				}	 				
			}
		});
		buttonPanel.add(buttonEdit);
		
		JButton buttonDelete = new JButton(MessageBundle.getMessage("angal.common.delete"));
		buttonDelete.setMnemonic(KeyEvent.VK_D);
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}
				ExamBrowsingManager manager = new ExamBrowsingManager();
				Exam e = (Exam)(((ExamBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
				int n = JOptionPane.showConfirmDialog(
                        null,
                        MessageBundle.getMessage("angal.exa.deletefolowingexam") + " :" + 
                        "\n"+MessageBundle.getMessage("angal.exa.code")+"= " + e.getCode() +
                        "\n"+MessageBundle.getMessage("angal.exa.description")+" = " + e.getDescription() +
                        "\n?",
                        MessageBundle.getMessage("angal.hospital"),
                        JOptionPane.YES_NO_OPTION);
				if ((n == JOptionPane.YES_OPTION) && (manager.deleteExam(e))){
					pExam.remove(table.getSelectedRow());
					model.fireTableDataChanged();
					table.updateUI();
				}
			}
		});
		buttonPanel.add(buttonDelete);
		
		JButton buttonShow = new JButton(MessageBundle.getMessage("angal.exa.results"));
		buttonShow.setMnemonic(KeyEvent.VK_S);
		buttonShow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					exam = (Exam)(((ExamBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
					new ExamShow(myFrame, exam);
				}
			}
		});
		buttonPanel.add(buttonShow);
		
		JButton buttonClose = new JButton(MessageBundle.getMessage("angal.common.close"));
		buttonClose.setMnemonic(KeyEvent.VK_C);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPanel.add(buttonClose);

		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

		
	class ExamBrowsingModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExamBrowsingModel(String s) {
			ExamBrowsingManager manager = new ExamBrowsingManager();
			pExam = manager.getExams(s);
		}
		public ExamBrowsingModel() {
			ExamBrowsingManager manager = new ExamBrowsingManager();
			pExam = manager.getExams();
		}
		public int getRowCount() {
			if (pExam == null)
				return 0;
			return pExam.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if(c==-1){
				return pExam.get(r);
			}
			else if (c == 0) {
				return pExam.get(r).getCode();
			} else if (c == 1) {
				return pExam.get(r).getExamtype().getDescription();
			} else if (c == 2) {
				return pExam.get(r).getDescription();
			} else if (c == 3) {
				return pExam.get(r).getProcedure();
			} else if (c == 4) {
				return pExam.get(r).getDefaultResult();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

}
