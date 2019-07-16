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

import java.awt.AWTEvent;
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.isf.exa.gui.ExamEdit.ExamListener;
import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

public class ExamBrowser extends ModalJFrame implements ExamListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 
	
	private int selectedrow;
	private JComboBox pbox;
	private ArrayList<Exam> pExam;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.exa.typem"),
			MessageBundle.getMessage("angal.common.descriptionm"),
			MessageBundle.getMessage("angal.exa.procm"),
			MessageBundle.getMessage("angal.exa.defaultm")
	};
	private int[] pColumwidth = {60,330,160,60,130};
	private Exam exam;
	private DefaultTableModel model ;
	private JTable table;
	private final JFrame myFrame;
	private String pSelection;
	private JButton jButtonNew;
	private JButton jButtonEdit;
	private JButton jButtonClose;
	private JButton jButtonShow;
	private JButton jButtonDelete;
	private JPanel jContentPanel;
	private JPanel buttonPanel;
	private JTextField searchTextField;
	ArrayList<Exam> searchExam = new ArrayList<Exam>();
	
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
		
        
        this.setContentPane(getJContentPanel());
		setVisible(true);
	}

	private JPanel getJContentPanel() {
		if (jContentPanel == null) {
			jContentPanel = new JPanel();
			jContentPanel.setLayout(new BorderLayout());
			jContentPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPanel.add(new JScrollPane(getJTable()),BorderLayout.CENTER);
			
			JPanel panelSearch = new JPanel();
			jContentPanel.add(panelSearch, BorderLayout.NORTH);
			
			JLabel searchLabel = new JLabel(MessageBundle.getMessage("angal.exams.find"));
			panelSearch.add(searchLabel);
			
			searchTextField = new JTextField();
			searchTextField.setColumns(20);
			searchTextField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					filterExam();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					filterExam();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					filterExam();
				}
			});
			panelSearch.add(searchTextField);
                        //jContentPanel.add(panelSearch);
			validate();
		}
		return jContentPanel;
	}

	
	private JPanel getJButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(new JLabel(MessageBundle.getMessage("angal.exa.selecttype")));
			buttonPanel.add(getJComboBoxExamType());
			buttonPanel.add(getJButtonNew());
			buttonPanel.add(getJButtonEdit());
			buttonPanel.add(getJButtonDelete());
			buttonPanel.add(getJButtonShow());
			buttonPanel.add(getJButtonClose());
		}
		return buttonPanel;
	}

	private JComboBox getJComboBoxExamType() {
		if (pbox == null) {
			pbox = new JComboBox();
			pbox.addItem(MessageBundle.getMessage("angal.exa.all"));
			ExamBrowsingManager manager = new ExamBrowsingManager();
			ArrayList<ExamType> type;
			try {
				type = manager.getExamType();	//for efficiency in the sequent for
			} catch (OHServiceException e1) {
				type = null;
				OHServiceExceptionUtil.showMessages(e1);
			}
			if (null != type) {
				for (ExamType elem : type) {
					pbox.addItem(elem);
				}
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
		}
		return pbox;
	}

	private JTable getJTable() {
		if (table == null) {
			model = new ExamBrowsingModel();
			table = new JTable(model);
			table.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			table.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
			table.getColumnModel().getColumn(2).setMinWidth(pColumwidth[2]);
			table.getColumnModel().getColumn(3).setMinWidth(pColumwidth[3]);
			table.getColumnModel().getColumn(4).setMinWidth(pColumwidth[4]);
		}
		return table;
	}

	private JButton getJButtonDelete() {
		jButtonDelete = new JButton(MessageBundle.getMessage("angal.common.delete"));
		jButtonDelete.setMnemonic(KeyEvent.VK_D);
		jButtonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
							ExamBrowser.this,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}
				ExamBrowsingManager manager = new ExamBrowsingManager();
				Exam e = (Exam)(((ExamBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
				StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.exa.deletefolowingexam"))
						.append(" :")
						.append("\n")
						.append(MessageBundle.getMessage("angal.common.code"))
						.append("= ")
						.append(e.getCode())
						.append("\n")
						.append(MessageBundle.getMessage("angal.common.description"))
						.append("= ")
						.append(e.getDescription())
						.append("\n?");
				int n = JOptionPane.showConfirmDialog(
                        null,
                        message.toString(),
                        MessageBundle.getMessage("angal.hospital"),
                        JOptionPane.YES_NO_OPTION);
				if ((n == JOptionPane.YES_OPTION)){
					boolean deleted;
					
					try {
						deleted = manager.deleteExam(e);
					} catch (OHServiceException e1) {
						deleted = false;
						OHServiceExceptionUtil.showMessages(e1);
					}
					
					if (true == deleted) {
						pExam.remove(table.getSelectedRow());
						model.fireTableDataChanged();
						table.updateUI();
					}
				}
			}
		});
		return jButtonDelete;
	}

	private JButton getJButtonNew() {
            
		if (jButtonNew == null) {
			jButtonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
			jButtonNew.setMnemonic(KeyEvent.VK_N);
			jButtonNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					exam = new Exam("", "", new ExamType("", ""), 0, "");
					ExamEdit newrecord = new ExamEdit(myFrame, exam, true);
					newrecord.addExamListener(ExamBrowser.this);
					newrecord.setVisible(true);
				}
			});
		}
		return jButtonNew;
	}

	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton(MessageBundle.getMessage("angal.common.edit"));
			jButtonEdit.setMnemonic(KeyEvent.VK_E);
			jButtonEdit.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent event) {
					if (table.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(
								ExamBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"), 
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						selectedrow = table.getSelectedRow();
						exam = (Exam) (((ExamBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
						ExamEdit editrecord = new ExamEdit(myFrame, exam, false);
						editrecord.addExamListener(ExamBrowser.this);
						editrecord.setVisible(true);
					} 				
				}
			});
		}
		return jButtonEdit;
	}
	
	private JButton getJButtonShow() {
		if (jButtonShow == null) {
			jButtonShow = new JButton(MessageBundle.getMessage("angal.exa.results"));
			jButtonShow.setMnemonic(KeyEvent.VK_S);
			jButtonShow.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent event) {
					if (table.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(				
								ExamBrowser.this,
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
		}
		return jButtonShow;
	}
	
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton(MessageBundle.getMessage("angal.common.close"));
			jButtonClose.setMnemonic(KeyEvent.VK_C);
			jButtonClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return jButtonClose;
	}

	class ExamBrowsingModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExamBrowsingModel(String s) {
			ExamBrowsingManager manager = new ExamBrowsingManager();
			try {
				pExam = manager.getExams(s);
                                
			} catch (OHServiceException e) {
				pExam = null;
				OHServiceExceptionUtil.showMessages(e);
			}
                        searchExam = pExam;
		}
		public ExamBrowsingModel() {
			ExamBrowsingManager manager = new ExamBrowsingManager();
			try {
				pExam = manager.getExams();
			} catch (OHServiceException e) {
				pExam = null;
				OHServiceExceptionUtil.showMessages(e);
			}
                        searchExam = pExam;
		}
		public int getRowCount() {
			if (pExam == null)
				return 0;
			return searchExam.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			Exam exam = searchExam.get(r);
			if(c==-1){
				return exam;
			}
			else if (c == 0) {
				return exam.getCode();
			} else if (c == 1) {
				return exam.getExamtype().getDescription();
			} else if (c == 2) {
				return exam.getDescription();
			} else if (c == 3) {
				return exam.getProcedure();
			} else if (c == 4) {
				return exam.getDefaultResult();
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
	public void examUpdated(AWTEvent e) {
		searchExam.set(selectedrow, exam);
		((ExamBrowsingModel) table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow > -1)
			table.setRowSelectionInterval(selectedrow, selectedrow);
		
	}


	@Override
	public void examInserted(AWTEvent e) {
		searchExam.add(0, exam);
		((ExamBrowsingModel) table.getModel()).fireTableDataChanged();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	
	private void filterExam() {
		String s = searchTextField.getText().trim();
		searchExam = new ArrayList<Exam>();
                
		for (Exam exa : pExam) {
			if (!s.equals("")) {
				String name = exa.getSearchString();
				if (name.contains(s.toLowerCase()))
					searchExam.add(exa);
			} else {
				searchExam.add(exa);
			}
		}
		if (table.getRowCount() == 0) {
			exam = null;
		}
		if (table.getRowCount() == 1) {
			exam = (Exam) table.getValueAt(0, -1);
		}
                model.fireTableDataChanged();
		table.updateUI();
		searchTextField.requestFocus();
	}

}
