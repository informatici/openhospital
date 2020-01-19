/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.PropertyConfigurator;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.TextPrompt.Show;

/**
 * @author Nanni
 *
 */
public class JTextFieldSearchModel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int CODE_COLUMN_WIDTH = 100;
	
	private JTextField jTextFieldSearch;
	private Object selectedObject;
	private HashMap<String, Medical> medicalMap;
	private JDialog owner;

	private MedicalBrowsingManager medMan = Context.getApplicationContext().getBean(MedicalBrowsingManager.class);

	/**
	 * Creates a Dialog containing a JTextField 
	 * with search capabilities over a certain model class
	 * @param owner - the JFrame owner for Dialog modality
	 * @param model - the class to search
	 */
	public JTextFieldSearchModel(JDialog owner, Object model) {
		super();
		this.owner = owner;
		if (model == Medical.class || model instanceof Medical) {
			initializeMedical();
			if (model == Medical.class)
				add(getJTextFieldSearch(null), BorderLayout.CENTER);
			if (model instanceof Medical)
				add(getJTextFieldSearch((Medical) model), BorderLayout.CENTER);
		}
	}

	private void initializeMedical() {
		ArrayList<Medical> medicals = null;
		medicalMap = new HashMap<String, Medical>();
		try {
			medicals = medMan.getMedicalsSortedByCode();
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		if (medicals != null) {
			for (Medical med : medicals) {
				medicalMap.put(med.getProd_code(), med);
			}
		}
	}
	
	protected Medical chooseMedical(String text) {
		ArrayList<Medical> medList = new ArrayList<Medical>();
		for (Medical aMed : medicalMap.values()) {
			if (aMed.getProd_code().toLowerCase().contains(text) 
					|| aMed.getDescription().toLowerCase().contains(text))
				medList.add(aMed);
		}
		Collections.sort(medList);
		Medical med = null;
		
		if (!medList.isEmpty()) {
			JTable medTable = new JTable(new StockMedModel(medList));
			medTable.getColumnModel().getColumn(0).setMaxWidth(CODE_COLUMN_WIDTH);
			medTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JPanel panel = new JPanel();
			panel.add(new JScrollPane(medTable));
			
			int ok = JOptionPane.showConfirmDialog(owner,
					panel, 
					MessageBundle.getMessage("angal.medicalstock.chooseamedical"), 
					JOptionPane.YES_NO_OPTION);
			
			if (ok == JOptionPane.OK_OPTION) {
				int row = medTable.getSelectedRow();
				med = medList.get(row);
			}
			return med;
		}
		return null;
	}

	private JTextField getJTextFieldSearch(Medical medical) {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField(50);
			jTextFieldSearch.setPreferredSize(new Dimension(300, 30));
			jTextFieldSearch.setHorizontalAlignment(SwingConstants.LEFT);
			
			TextPrompt suggestion = new TextPrompt(MessageBundle.getMessage("angal.medicalstock.typeacodeoradescriptionandpressenter"), 
					jTextFieldSearch, 
					Show.FOCUS_LOST);
			{
				suggestion.setFont(new Font("Tahoma", Font.PLAIN, 14));
				suggestion.setForeground(Color.GRAY);
				suggestion.setHorizontalAlignment(JLabel.CENTER);
				suggestion.changeAlpha(0.5f);
				suggestion.changeStyle(Font.BOLD + Font.ITALIC);
			}
			if (medical != null) {
				selectedObject = medical;
				jTextFieldSearch.setText(medical.toString());
			}
			jTextFieldSearch.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String text = jTextFieldSearch.getText();
					Medical med = null;
					if (medicalMap.containsKey(text)) {
						// Medical found
						med = medicalMap.get(text);
					} else {
						
						med = chooseMedical(text.toLowerCase());
					}
					if (med != null) {
						selectedObject = med;
						jTextFieldSearch.setText(med.toString());
					}
				}
			});
		}
		return jTextFieldSearch;
	}
	
	class StockMedModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Medical> medList;

		public StockMedModel(ArrayList<Medical> meds) {
			medList = meds;
		}

		public int getRowCount() {
			if (medList == null)
				return 0;
			return medList.size();
		}

		public String getColumnName(int c) {
			if (c == 0) {
				return MessageBundle.getMessage("angal.common.code");
			}
			if (c == 1) {
				return MessageBundle.getMessage("angal.common.description");
			}
			return "";
		}

		public int getColumnCount() {
			return 2;
		}

		public Object getValueAt(int r, int c) {
			Medical med = medList.get(r);
			if (c == -1) {
				return med;
			} else if (c == 0) {
				return med.getProd_code();
			} else if (c == 1) {
				return med.getDescription();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}
	
	/**
	 * @return the selectedObject
	 */
	public Object getSelectedObject() {
		return selectedObject;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(new File("./rsc/log4j.properties").getAbsolutePath());
			GeneralData.getGeneralData();
			JDialog newDialog = new JDialog();
			JTextFieldSearchModel textField = new JTextFieldSearchModel(new JDialog(), Medical.class);
			newDialog.add(textField, BorderLayout.NORTH);
			newDialog.setVisible(true);
			newDialog.pack();
			newDialog.setLocationRelativeTo(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
