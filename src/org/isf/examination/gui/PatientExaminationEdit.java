package org.isf.examination.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.isf.examination.manager.ExaminationBrowserManager;
import org.isf.examination.model.GenderPatientExamination;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.ExaminationParameters;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.VoIntegerTextField;
import org.isf.utils.jobjects.VoLimitedTextArea;

import com.toedter.calendar.JDateChooser;

public class PatientExaminationEdit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelExamination;
	private JPanel jPanelButtons;
	private JSlider jSliderHeight;
	private JSlider jSliderWeight;
	private VoIntegerTextField jTextFieldHR;
	private JTextField jTextFieldTemp;
	private JTextField jTextFieldSaturation;
	private VoLimitedTextArea jTextAreaNote;
	private VoIntegerTextField jTextFieldHeight;
	private JTextField jTextFieldWeight;
	private JPanel jPanelAPPanel;
	private JLabel jLabelAPMin;
	private JLabel jLabelAPSlash;
	private JLabel jLabelAPMax;
	private JSlider jSliderHR;
	private JSlider jSliderTemp;
	private JSlider jSliderSaturation;
	private JScrollPane jScrollPaneNote;
	private JDateChooser jDateChooserDate;
	private JSpinner jSpinnerAPmin;
	private JSpinner jSpinnerAPmax;
	private JLabel jLabelHeightAbb;
	private JLabel jLabelWeightAbb;
	private JCheckBox jCheckBoxToggleAP;
	private JCheckBox jCheckBoxToggleHR;
	private JCheckBox jCheckBoxToggleTemp;
	private JCheckBox jCheckBoxToggleSaturation;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private Action actionSavePatientExamination;
	private Action actionToggleAP;
	private Action actionToggleHR;
	private Action actionToggleTemp;
	private Action actionToggleSaturation;
	private JPanel jPanelGender;
	private JLabel jLabelGender;
	private JEditorPane jEditorPaneBMI;
	private JPanel jPanelSummary;
	private JEditorPane jEditorPaneSummary;
	
	private PatientExamination patex;
	private boolean isMale;
	private double bmi;
	
	private final String PATH_FEMALE_GENDER = "rsc/images/sagoma-donna-132x300.jpg"; //$NON-NLS-1$
	private final String PATH_MALE_GENDER = "rsc/images/sagoma-uomo-132x300.jpg"; //$NON-NLS-1$
	private final String SUMMARY_START_ROW = "<tr align=\"center\">";
	private final String SUMMARY_END_ROW = "</tr>";
	private final String STD = "<td>";
	private final String ETD = "</td>";
	private final String SUMMARY_HEADER = "" +
			"<html><head></head><body><table>"+
			SUMMARY_START_ROW+
			STD+MessageBundle.getMessage("angal.common.datem")+ETD+
			STD+MessageBundle.getMessage("angal.examination.height")+ETD+
			STD+MessageBundle.getMessage("angal.examination.weight")+ETD+
			STD+MessageBundle.getMessage("angal.examination.arterialpressureabbr")+ETD+
			STD+MessageBundle.getMessage("angal.examination.heartrateabbr")+ETD+
			STD+MessageBundle.getMessage("angal.examination.temperatureabbr")+ETD+
			STD+MessageBundle.getMessage("angal.examination.saturationabbr")+ETD+
			SUMMARY_END_ROW;
	private final String SUMMARY_FOOTER = "</table></body></html>";
	
	private final String DATE_FORMAT = "dd/MM/yy";

	/**
	 * Create the dialog.
	 */
	public PatientExaminationEdit() {
		super();
		initComponents();
		updateGUI();
	}
	
	public PatientExaminationEdit(GenderPatientExamination gpatex) {
		super();
		this.patex = gpatex.getPatex();
		this.isMale = gpatex.isMale();
		initComponents();
		updateGUI();
	}

	public PatientExaminationEdit(Frame parent, GenderPatientExamination gpatex) {
		super(parent, true);
		this.patex = gpatex.getPatex();
		this.isMale = gpatex.isMale();
		initComponents();
		updateGUI();
	}

	public PatientExaminationEdit(Dialog parent, GenderPatientExamination gpatex) {
		super(parent, true);
		this.patex = gpatex.getPatex();
		this.isMale = gpatex.isMale();
		initComponents();
		updateGUI();
	}
	
	private void initComponents() {
		ExaminationParameters.getExaminationParameters();
		getContentPane().add(getJPanelExamination(), BorderLayout.CENTER);
		getContentPane().add(getJPanelButtons(), BorderLayout.SOUTH);
		getContentPane().add(getJPanelGender(), BorderLayout.WEST);
		getContentPane().add(getJPanelSummary(), BorderLayout.EAST);
		updateSummary();
		updateBMI();
		pack();
		setResizable(false);
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}
	
	//TODO: try to use JDOM...
	private void updateBMI() {
		this.bmi = patex.getBMI();
		StringBuilder bmi = new StringBuilder();
		bmi.append("<html><body>");
		bmi.append("<strong>");
		bmi.append(MessageBundle.getMessage("angal.examination.bmi") + ":");
		bmi.append("<br />");
		bmi.append("" + this.bmi);
		bmi.append("<br /><br />");
		bmi.append("<font color=\"red\">");
		bmi.append(getBMIdescription(this.bmi));
		bmi.append("</font>");
		bmi.append("</strong>");
		bmi.append("</body></html>");
		jEditorPaneBMI.setText(bmi.toString());
	}
	
	private Object getBMIdescription(double bmi) {
		if (bmi < 16.5)
			return MessageBundle.getMessage("angal.examination.bmi.severeunderweight");
		if (bmi >= 16.5 && bmi < 18.5)
			return MessageBundle.getMessage("angal.examination.bmi.underweight");
		if (bmi >= 18.5 && bmi < 24.5)
			return MessageBundle.getMessage("angal.examination.bmi.normalweight");
		if (bmi >= 24.5 && bmi < 30)
			return MessageBundle.getMessage("angal.examination.bmi.overweight");
		if (bmi >= 30 && bmi < 35)
			return MessageBundle.getMessage("angal.examination.bmi.obesityclassilight");
		if (bmi >= 35 && bmi < 40)
			return MessageBundle.getMessage("angal.examination.bmi.obesityclassiimedium");
		if (bmi >= 40)
			return MessageBundle.getMessage("angal.examination.bmi.obesityclassiiisevere");
		return "";
	}

	//TODO: try to use JDOM...
	private void updateSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append(SUMMARY_HEADER);
		ExaminationBrowserManager examManager = Context.getApplicationContext().getBean(ExaminationBrowserManager.class);
		ArrayList<PatientExamination> patexList = null;
		try {
			patexList = examManager.getLastNByPatID(patex.getPatient().getCode(), ExaminationParameters.LIST_SIZE);
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		Collections.sort(patexList);
		if(patexList != null){
			for (PatientExamination patex : patexList) {
				summary.append(SUMMARY_START_ROW);
				summary.append(STD).append(new SimpleDateFormat(DATE_FORMAT).format(new Date(patex.getPex_date().getTime()))).append(ETD);
				summary.append(STD).append(patex.getPex_height()).append(ETD);
				summary.append(STD).append(patex.getPex_weight()).append(ETD);
				summary.append(STD).append(patex.getPex_pa_min()).append(" / ").append(patex.getPex_pa_max()).append(ETD);
				summary.append(STD).append(patex.getPex_fc()).append(ETD);
				summary.append(STD).append(patex.getPex_temp()).append(ETD);
				summary.append(STD).append(patex.getPex_sat()).append(ETD);
				summary.append(SUMMARY_END_ROW);
			}
		}
		summary.append(SUMMARY_FOOTER);
		jEditorPaneSummary.setText(summary.toString());
	}
	
	private void updateGUI() {
		jDateChooserDate.setDate(new Date(patex.getPex_date().getTime()));
		jTextFieldHeight.setText(String.valueOf(patex.getPex_height()));
		jSliderHeight.setValue(patex.getPex_height());
		jTextFieldWeight.setText(String.valueOf(patex.getPex_weight()));
		jSliderWeight.setValue(convertFromDoubleToInt(patex.getPex_weight(), ExaminationParameters.WEIGHT_MIN, ExaminationParameters.WEIGHT_STEP, ExaminationParameters.WEIGHT_MAX));
		jSpinnerAPmin.setValue(patex.getPex_pa_min());
		jSpinnerAPmax.setValue(patex.getPex_pa_max());
		jSliderHR.setValue(patex.getPex_fc());
		jTextFieldHR.setText(String.valueOf(patex.getPex_fc()));
		jSliderTemp.setValue(convertFromDoubleToInt(patex.getPex_temp(), ExaminationParameters.TEMP_MIN, ExaminationParameters.TEMP_STEP, ExaminationParameters.TEMP_MAX));
		jTextFieldTemp.setText(String.valueOf(patex.getPex_temp()));
		jSliderSaturation.setValue(convertFromDoubleToInt(patex.getPex_sat(), ExaminationParameters.SAT_MIN, ExaminationParameters.SAT_STEP, ExaminationParameters.SAT_MAX));
		jTextFieldSaturation.setText(String.valueOf(patex.getPex_sat()));
		jTextAreaNote.setText(patex.getPex_note());
		disableAP();
		disableHR();
		disableTemp();
		disableSaturation();
	}

	private JPanel getJPanelExamination() {
		if (jPanelExamination == null) {
			jPanelExamination = new JPanel();
			
			GridBagLayout gbl_jPanelExamination = new GridBagLayout();
			gbl_jPanelExamination.columnWidths = new int[] { 0, 0, 0, 0 };
			gbl_jPanelExamination.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			gbl_jPanelExamination.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0 };
			gbl_jPanelExamination.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
			jPanelExamination.setLayout(gbl_jPanelExamination);

			JLabel jLabelDate = new JLabel(MessageBundle.getMessage("angal.common.date")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelDate = new GridBagConstraints();
			gbc_jLabelDate.anchor = GridBagConstraints.WEST;
			gbc_jLabelDate.insets = new Insets(10, 5, 5, 5);
			gbc_jLabelDate.gridx = 1;
			gbc_jLabelDate.gridy = 0;
			jPanelExamination.add(jLabelDate, gbc_jLabelDate);
			
			GridBagConstraints gbc_jDateChooserDate = new GridBagConstraints();
			gbc_jDateChooserDate.anchor = GridBagConstraints.WEST;
			gbc_jDateChooserDate.insets = new Insets(10, 5, 5, 5);
			gbc_jDateChooserDate.gridx = 2;
			gbc_jDateChooserDate.gridy = 0;
			jPanelExamination.add(getJDateChooserDate(), gbc_jDateChooserDate);
			
			jLabelHeightAbb = new JLabel(MessageBundle.getMessage("angal.examination.heightabbr")); //$NON-NLS-1$
			GridBagConstraints gbc_lblh = new GridBagConstraints();
			gbc_lblh.insets = new Insets(5, 5, 5, 5);
			gbc_lblh.gridx = 0;
			gbc_lblh.gridy = 1;
			jPanelExamination.add(jLabelHeightAbb, gbc_lblh);

			JLabel jLabelHeight = new JLabel(MessageBundle.getMessage("angal.examination.height")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelHeight = new GridBagConstraints();
			gbc_jLabelHeight.anchor = GridBagConstraints.WEST;
			gbc_jLabelHeight.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelHeight.gridx = 1;
			gbc_jLabelHeight.gridy = 1;
			jPanelExamination.add(jLabelHeight, gbc_jLabelHeight);
			
			GridBagConstraints gbc_jSliderHeight = new GridBagConstraints();
			gbc_jSliderHeight.insets = new Insets(5, 5, 5, 5);
			gbc_jSliderHeight.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSliderHeight.gridx = 2;
			gbc_jSliderHeight.gridy = 1;
			jPanelExamination.add(getJSliderHeight(), gbc_jSliderHeight);
			
			JLabel jLabelHeightUnit = new JLabel(ExaminationParameters.HEIGHT_UNIT);
			GridBagConstraints gbc_jLabelHeightUnit = new GridBagConstraints();
			gbc_jLabelHeightUnit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelHeightUnit.gridx = 3;
			gbc_jLabelHeightUnit.gridy = 1;
			jPanelExamination.add(jLabelHeightUnit, gbc_jLabelHeightUnit);
			
			GridBagConstraints gbc_jTextFieldHeight = new GridBagConstraints();
			gbc_jTextFieldHeight.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldHeight.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldHeight.gridx = 4;
			gbc_jTextFieldHeight.gridy = 1;
			jPanelExamination.add(getJTextFieldHeight(), gbc_jTextFieldHeight);
			
			jLabelWeightAbb = new JLabel(MessageBundle.getMessage("angal.examination.weightabbr")); //$NON-NLS-1$
			GridBagConstraints gbc_lblw = new GridBagConstraints();
			gbc_lblw.insets = new Insets(5, 5, 5, 5);
			gbc_lblw.gridx = 0;
			gbc_lblw.gridy = 2;
			jPanelExamination.add(jLabelWeightAbb, gbc_lblw);

			JLabel jLabelWeight = new JLabel(MessageBundle.getMessage("angal.examination.weight")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelWeight = new GridBagConstraints();
			gbc_jLabelWeight.anchor = GridBagConstraints.WEST;
			gbc_jLabelWeight.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelWeight.gridx = 1;
			gbc_jLabelWeight.gridy = 2;
			jPanelExamination.add(jLabelWeight, gbc_jLabelWeight);

			GridBagConstraints gbc_jSliderWeight = new GridBagConstraints();
			gbc_jSliderWeight.insets = new Insets(5, 5, 5, 5);
			gbc_jSliderWeight.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSliderWeight.gridx = 2;
			gbc_jSliderWeight.gridy = 2;
			jPanelExamination.add(getJSliderWeight(), gbc_jSliderWeight);
			
			JLabel jLabelWeightUnit = new JLabel(ExaminationParameters.WEIGHT_UNIT);
			GridBagConstraints gbc_jLabelWeightUnit = new GridBagConstraints();
			gbc_jLabelWeightUnit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelWeightUnit.gridx = 3;
			gbc_jLabelWeightUnit.gridy = 2;
			jPanelExamination.add(jLabelWeightUnit, gbc_jLabelWeightUnit);
			
			GridBagConstraints gbc_jTextFieldWeight = new GridBagConstraints();
			gbc_jTextFieldWeight.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldWeight.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldWeight.gridx = 4;
			gbc_jTextFieldWeight.gridy = 2;
			jPanelExamination.add(getJTextFieldWeight(), gbc_jTextFieldWeight);
			
			GridBagConstraints gbc_lblap = new GridBagConstraints();
			gbc_lblap.insets = new Insets(5, 5, 5, 5);
			gbc_lblap.gridx = 0;
			gbc_lblap.gridy = 3;
			jPanelExamination.add(getJCheckBoxAP(), gbc_lblap);

			JLabel jLabelAPmin = new JLabel(MessageBundle.getMessage("angal.examination.arterialpressure")); //$NON-NLS-1$
			GridBagConstraints labelGbc_3 = new GridBagConstraints();
			labelGbc_3.insets = new Insets(5, 5, 5, 5);
			labelGbc_3.gridx = 1;
			labelGbc_3.gridy = 3;
			jPanelExamination.add(jLabelAPmin, labelGbc_3);
			
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 2;
			gbc_panel.gridy = 3;
			jPanelExamination.add(getJPanelAPPanel(), gbc_panel);
			
			JLabel jLabelAPUnit = new JLabel(ExaminationParameters.AP_UNIT);
			GridBagConstraints gbc_jLabelAPUnit = new GridBagConstraints();
			gbc_jLabelAPUnit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelAPUnit.gridx = 3;
			gbc_jLabelAPUnit.gridy = 3;
			jPanelExamination.add(jLabelAPUnit, gbc_jLabelAPUnit);
			
			GridBagConstraints gbc_lblhr = new GridBagConstraints();
			gbc_lblhr.insets = new Insets(5, 5, 5, 5);
			gbc_lblhr.gridx = 0;
			gbc_lblhr.gridy = 4;
			jPanelExamination.add(getJCheckBoxToggleHR(), gbc_lblhr);
			
			JLabel jLabelHR = new JLabel(MessageBundle.getMessage("angal.examination.heartrate")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelHR = new GridBagConstraints();
			gbc_jLabelHR.anchor = GridBagConstraints.WEST;
			gbc_jLabelHR.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelHR.gridx = 1;
			gbc_jLabelHR.gridy = 4;
			jPanelExamination.add(jLabelHR, gbc_jLabelHR);
			
			GridBagConstraints gbc_jSliderHR = new GridBagConstraints();
			gbc_jSliderHR.insets = new Insets(5, 5, 5, 5);
			gbc_jSliderHR.gridx = 2;
			gbc_jSliderHR.gridy = 4;
			jPanelExamination.add(getJSliderHR(), gbc_jSliderHR);
			
			JLabel jLabelHRUnit = new JLabel(ExaminationParameters.HR_UNIT);
			GridBagConstraints gbc_jLabelHRUnit = new GridBagConstraints();
			gbc_jLabelHRUnit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelHRUnit.gridx = 3;
			gbc_jLabelHRUnit.gridy = 4;
			jPanelExamination.add(jLabelHRUnit, gbc_jLabelHRUnit);
			
			GridBagConstraints gbc_jTextFieldHR = new GridBagConstraints();
			gbc_jTextFieldHR.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldHR.insets = new Insets(5, 0, 5, 0);
			gbc_jTextFieldHR.gridx = 4;
			gbc_jTextFieldHR.gridy = 4;
			jPanelExamination.add(getJTextFieldHR(), gbc_jTextFieldHR);
			
			GridBagConstraints gbc_lbltemp = new GridBagConstraints();
			gbc_lbltemp.insets = new Insets(5, 5, 5, 5);
			gbc_lbltemp.gridx = 0;
			gbc_lbltemp.gridy = 5;
			jPanelExamination.add(getJCheckBoxToggleTemp(), gbc_lbltemp);
			
			JLabel jLabelTemp = new JLabel(MessageBundle.getMessage("angal.examination.temperature")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelTemp = new GridBagConstraints();
			gbc_jLabelTemp.anchor = GridBagConstraints.WEST;
			gbc_jLabelTemp.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelTemp.gridx = 1;
			gbc_jLabelTemp.gridy = 5;
			jPanelExamination.add(jLabelTemp, gbc_jLabelTemp);
			
			GridBagConstraints gbc_jSliderTemp = new GridBagConstraints();
			gbc_jSliderTemp.insets = new Insets(5, 5, 5, 5);
			gbc_jSliderTemp.gridx = 2;
			gbc_jSliderTemp.gridy = 5;
			jPanelExamination.add(getJSliderTemp(), gbc_jSliderTemp);
			
			JLabel jLabelTempUnit = new JLabel(ExaminationParameters.TEMP_UNIT);
			GridBagConstraints gbc_jLabelTempUnit = new GridBagConstraints();
			gbc_jLabelTempUnit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelTempUnit.gridx = 3;
			gbc_jLabelTempUnit.gridy = 5;
			jPanelExamination.add(jLabelTempUnit, gbc_jLabelTempUnit);
			
			GridBagConstraints gbc_jTextFieldTemp = new GridBagConstraints();
			gbc_jTextFieldTemp.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldTemp.insets = new Insets(5, 0, 5, 0);
			gbc_jTextFieldTemp.gridx = 4;
			gbc_jTextFieldTemp.gridy = 5;
			jPanelExamination.add(getJTextFieldTemp(), gbc_jTextFieldTemp);
			
			GridBagConstraints gbc_lblsaturation = new GridBagConstraints();
			gbc_lblsaturation.insets = new Insets(5, 5, 5, 5);
			gbc_lblsaturation.gridx = 0;
			gbc_lblsaturation.gridy = 6;
			jPanelExamination.add(getJCheckBoxToggleSaturation(), gbc_lblsaturation);
			
			JLabel jLabelSaturation = new JLabel(MessageBundle.getMessage("angal.examination.saturation")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelSaturation = new GridBagConstraints();
			gbc_jLabelSaturation.anchor = GridBagConstraints.WEST;
			gbc_jLabelSaturation.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelSaturation.gridx = 1;
			gbc_jLabelSaturation.gridy = 6;
			jPanelExamination.add(jLabelSaturation, gbc_jLabelSaturation);

			GridBagConstraints gbc_jSliderSaturation = new GridBagConstraints();
			gbc_jSliderSaturation.insets = new Insets(5, 5, 5, 5);
			gbc_jSliderSaturation.gridx = 2;
			gbc_jSliderSaturation.gridy = 6;
			jPanelExamination.add(getJSliderSaturation(), gbc_jSliderSaturation);

			GridBagConstraints gbc_jTextFieldSaturation = new GridBagConstraints();
			gbc_jTextFieldSaturation.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldSaturation.insets = new Insets(5, 0, 5, 0);
			gbc_jTextFieldSaturation.gridx = 3;
			gbc_jTextFieldSaturation.gridy = 6;
			jPanelExamination.add(getJTextFieldSaturation(), gbc_jTextFieldSaturation);

			JLabel jLabelNote = new JLabel(MessageBundle.getMessage("angal.examination.note")); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelNote = new GridBagConstraints();
			gbc_jLabelNote.anchor = GridBagConstraints.NORTHEAST;
			gbc_jLabelNote.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelNote.gridx = 1;
			gbc_jLabelNote.gridy = 7;
			jPanelExamination.add(jLabelNote, gbc_jLabelNote);
			
			GridBagConstraints gbc_jScrollPaneNote = new GridBagConstraints();
			gbc_jScrollPaneNote.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneNote.insets = new Insets(5, 5, 5, 5);
			gbc_jScrollPaneNote.gridx = 2;
			gbc_jScrollPaneNote.gridy = 7;
			jPanelExamination.add(getJScrollPaneNote(), gbc_jScrollPaneNote);

		}
		return jPanelExamination;
	}
	
	private JCheckBox getJCheckBoxToggleSaturation() {
		if (jCheckBoxToggleSaturation == null) {
			jCheckBoxToggleSaturation = new JCheckBox(""); //$NON-NLS-1$
			jCheckBoxToggleSaturation.setAction(getActionToggleSaturation());
		}
		return jCheckBoxToggleSaturation;
	}
	
	private JCheckBox getJCheckBoxToggleTemp() {
		if (jCheckBoxToggleTemp == null) {
			jCheckBoxToggleTemp = new JCheckBox(""); //$NON-NLS-1$
			jCheckBoxToggleTemp.setAction(getActionToggleTemp());
		}
		return jCheckBoxToggleTemp;
	}

	private JCheckBox getJCheckBoxToggleHR() {
		if (jCheckBoxToggleHR == null) {
			jCheckBoxToggleHR = new JCheckBox(""); //$NON-NLS-1$
			jCheckBoxToggleHR.setAction(getActionToggleHR());
		}
		return jCheckBoxToggleHR;
	}

	private JCheckBox getJCheckBoxAP() {
		if (jCheckBoxToggleAP == null) {
			jCheckBoxToggleAP = new JCheckBox(""); //$NON-NLS-1$
			jCheckBoxToggleAP.setAction(getActionToggleAP());
		}
		return jCheckBoxToggleAP;
	}

	private JDateChooser getJDateChooserDate() {
		if (jDateChooserDate == null) {
			jDateChooserDate = new JDateChooser();
			//jDateChooserDate.setLocale(new Locale(GeneralData.LANGUAGE));
			jDateChooserDate.setLocale(new Locale("en")); //$NON-NLS-1$
			jDateChooserDate.setDateFormatString("dd/MM/yyyy - HH:mm"); //$NON-NLS-1$
			jDateChooserDate.addPropertyChangeListener("date", new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Date date = (Date) evt.getNewValue();
					jDateChooserDate.setDate(date);
					patex.setPex_date(new Timestamp(date.getTime()));
					
				}
			});
		}
		return jDateChooserDate;
	}

	private VoLimitedTextArea getJTextAreaNote() {
		if (jTextAreaNote == null) {
			jTextAreaNote = new VoLimitedTextArea(300, 6, 20);
			jTextAreaNote.setLineWrap(true);
			jTextAreaNote.setWrapStyleWord(true);
			jTextAreaNote.setMargin(new Insets(0, 5, 0, 0));
			jTextAreaNote.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					super.focusLost(e);
					patex.setPex_note(jTextAreaNote.getText());
				}
			});
		}
		return jTextAreaNote;
	}

	private JScrollPane getJScrollPaneNote() {
		if (jScrollPaneNote == null) {
			jScrollPaneNote = new JScrollPane();
			jScrollPaneNote.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPaneNote.setViewportView(getJTextAreaNote());
		}
		return jScrollPaneNote;
	}

	private JPanel getJPanelAPPanel() {
		if (jPanelAPPanel == null) {
			jPanelAPPanel = new JPanel();
			jLabelAPMin = new JLabel(MessageBundle.getMessage("angal.examination.ap.min")); //$NON-NLS-1$
			jPanelAPPanel.add(jLabelAPMin);
			jPanelAPPanel.add(getJSpinnerAPmin());
			jLabelAPSlash = new JLabel("/"); //$NON-NLS-1$
			jPanelAPPanel.add(jLabelAPSlash);
			jPanelAPPanel.add(getJSpinnerAPmax());
			jLabelAPMax = new JLabel(MessageBundle.getMessage("angal.examination.ap.max")); //$NON-NLS-1$
			jPanelAPPanel.add(jLabelAPMax);
		}
		return jPanelAPPanel;
	}

	private JSpinner getJSpinnerAPmin() {
		if (jSpinnerAPmin == null) {
			jSpinnerAPmin = new JSpinner(new SpinnerNumberModel(ExaminationParameters.AP_MIN,0,999,1));
			jSpinnerAPmin.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					patex.setPex_pa_min((Integer) jSpinnerAPmin.getValue());
				}
			});
		}
		return jSpinnerAPmin;
	}
	
	private JSpinner getJSpinnerAPmax() {
		if (jSpinnerAPmax == null) {
			jSpinnerAPmax = new JSpinner(new SpinnerNumberModel(ExaminationParameters.AP_MIN,0,999,1));
			jSpinnerAPmax.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					patex.setPex_pa_max((Integer) jSpinnerAPmax.getValue());
				}
			});
		}
		return jSpinnerAPmax;
	}

	private VoIntegerTextField getJTextFieldHeight() {
		if (jTextFieldHeight == null) {
			jTextFieldHeight = new VoIntegerTextField(0,5);
			jTextFieldHeight.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					int height = Integer.parseInt(jTextFieldHeight.getText());
					jSliderHeight.setValue(height);
					patex.setPex_height(height);
				}
			});
		}
		return jTextFieldHeight;
	}
	
	private JTextField getJTextFieldWeight() {
		if (jTextFieldWeight == null) {
			jTextFieldWeight = new JTextField(5);
			jTextFieldWeight.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					double weight = Double.parseDouble(jTextFieldWeight.getText());
					jSliderWeight.setValue(convertFromDoubleToInt(weight, ExaminationParameters.WEIGHT_MIN, ExaminationParameters.WEIGHT_STEP, ExaminationParameters.WEIGHT_MAX));
					patex.setPex_weight(weight);
				}
			});
		}
		return jTextFieldWeight;
	}
	
	private JTextField getJTextFieldTemp() {
		if (jTextFieldTemp == null) {
			jTextFieldTemp = new JTextField(5);
			jTextFieldTemp.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					double temp = Double.parseDouble(jTextFieldTemp.getText());
					jSliderTemp.setValue(convertFromDoubleToInt(temp, ExaminationParameters.TEMP_MIN, ExaminationParameters.TEMP_STEP, ExaminationParameters.TEMP_MAX));
					patex.setPex_temp(temp);
				}
			});
		}
		return jTextFieldTemp;
	}
	
	private JTextField getJTextFieldSaturation() {
		if (jTextFieldSaturation == null) {
			jTextFieldSaturation = new JTextField(5);
			jTextFieldSaturation.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					double sat = Double.parseDouble(jTextFieldSaturation.getText());
					jSliderSaturation.setValue(convertFromDoubleToInt(sat, ExaminationParameters.SAT_MIN, ExaminationParameters.SAT_STEP, ExaminationParameters.SAT_MAX));
					patex.setPex_sat(sat);
				}
			});
		}
		return jTextFieldSaturation;
	}
	
	private VoIntegerTextField getJTextFieldHR() {
		if (jTextFieldHR == null) {
			jTextFieldHR = new VoIntegerTextField(0,5);
			jTextFieldHR.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					int hr = Integer.parseInt(jTextFieldHR.getText());
					jSliderHR.setValue(hr);
					patex.setPex_fc(hr);
				}
			});
		}
		return jTextFieldHR;
	}

	private JSlider getJSliderHeight() {
		if (jSliderHeight == null) {
			jSliderHeight = new JSlider(0, 250, 0);
			jSliderHeight.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					int height = jSliderHeight.getValue();
					jTextFieldHeight.setText(String.valueOf(height));
					patex.setPex_height(height);
					updateBMI();
				}
			});
		}
		return jSliderHeight;
	}
	
	private JSlider getJSliderWeight() {
		if (jSliderWeight == null) {
			jSliderWeight = new JSlider(0, 4000, 0);
			jSliderWeight.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					int value = jSliderWeight.getValue();
					double weight = (double) value / 10;
					jTextFieldWeight.setText(String.valueOf(weight));
					patex.setPex_weight(weight);
					updateBMI();
				}
			});
		}
		return jSliderWeight;
	}
	
	private int convertFromDoubleToInt(double value, double min, double step, double max) {
		if (value > max) {
			return (int) (max * (1. / step));
		} else if (value < step) {
			return 0;
		} else {
			return (int) Math.round(value * (1. / step));
		}
	}
	
	private JSlider getJSliderTemp() {
		if (jSliderTemp == null) {
			jSliderTemp = new JSlider(0, 500, 0);
			jSliderTemp.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					int value = jSliderTemp.getValue();
					double temp = (double) value / 10;
					jTextFieldTemp.setText(String.valueOf(temp));
					patex.setPex_temp(temp);
				}
			});
		}
		return jSliderTemp;
	}
	
	private JSlider getJSliderSaturation() {
		if (jSliderSaturation == null) {
			jSliderSaturation = new JSlider(0, 1000, 0); //MAX / STEP
			jSliderSaturation.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					int value = jSliderSaturation.getValue();
					double sat = (double) value / 10;
					jTextFieldSaturation.setText(String.valueOf(sat));
					patex.setPex_sat(sat);
				}
			});
		}
		return jSliderSaturation;
	}
	
	private JSlider getJSliderHR() {
		if (jSliderHR == null) {
			jSliderHR = new JSlider(0, 200, 0);
			jSliderHR.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					int hr = jSliderHR.getValue();
					jTextFieldHR.setText(String.valueOf(hr));
					patex.setPex_fc(hr);
				}
			});
		}
		return jSliderHR;
	}
	
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setAction(getActionSavePatientExamination());
		}
		return jButtonOK;
	}
	
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton(MessageBundle.getMessage("angal.common.cancel")); //$NON-NLS-1$
			jButtonCancel.setMnemonic(KeyEvent.VK_C);
			jButtonCancel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return jButtonCancel;
	}
	
	private JPanel getJPanelGender() {
		if (jPanelGender == null) {
			jPanelGender = new JPanel();
			LayoutManager overlay = new OverlayLayout(jPanelGender);
			jPanelGender.setLayout(overlay);
			jPanelGender.add(getJEditorPaneBMI());
			jPanelGender.add(getJLabelImageGender());
			
		}
		return jPanelGender;
	}
	
	private JEditorPane getJEditorPaneBMI() {
		if (jEditorPaneBMI == null) {
			jEditorPaneBMI = new JEditorPane();
			jEditorPaneBMI.setFont(new Font("Arial", Font.BOLD, 14));
			jEditorPaneBMI.setContentType("text/html");
			jEditorPaneBMI.setEditable(false);
			jEditorPaneBMI.setOpaque(false);
			jEditorPaneBMI.setMinimumSize(new Dimension(132, 300));
			jEditorPaneBMI.setPreferredSize(new Dimension(132, 300));
			jEditorPaneBMI.setMaximumSize(new Dimension(132, 300));
		}
		return jEditorPaneBMI;
	}

	private JLabel getJLabelImageGender() {
		if (jLabelGender == null) {
			jLabelGender = new JLabel(); //$NON-NLS-1$
			if (isMale) 
				jLabelGender.setIcon(new ImageIcon(PATH_MALE_GENDER));
			else
				jLabelGender.setIcon(new ImageIcon(PATH_FEMALE_GENDER));
			jLabelGender.setAlignmentX(0.5f);
			jLabelGender.setAlignmentY(0.5f);
		}
		return jLabelGender;
	}
	
	private class SwingActionSavePatientExamination extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingActionSavePatientExamination() {
			putValue(NAME, MessageBundle.getMessage("angal.common.savem"));
			putValue(MNEMONIC_KEY, KeyEvent.VK_O);
			putValue(SHORT_DESCRIPTION, MessageBundle.getMessage("angal.examination.tooltip.savepatientexamination")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			
			ExaminationBrowserManager examManager = Context.getApplicationContext().getBean(ExaminationBrowserManager.class);
			try {
				examManager.saveOrUpdate(patex);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
			dispose();
		}
	}
	
	private Action getActionSavePatientExamination() {
		if (actionSavePatientExamination == null) {
			actionSavePatientExamination = new SwingActionSavePatientExamination();
		}
		return actionSavePatientExamination;
	}
	
	private void enableAP() {
		jSpinnerAPmin.setEnabled(true);
		patex.setPex_pa_min((Integer)jSpinnerAPmin.getValue());
		jSpinnerAPmax.setEnabled(true);
		patex.setPex_pa_max((Integer)jSpinnerAPmax.getValue());
	}

	private void disableAP() {
		jSpinnerAPmin.setEnabled(false);
		patex.setPex_pa_min(0);
		jSpinnerAPmax.setEnabled(false);
		patex.setPex_pa_max(0);
	}
	
	private class SwingActionToggleAP extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingActionToggleAP() {
			putValue(NAME, ""); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, MessageBundle.getMessage("angal.examination.tooltip.togglearterialpressureexamination")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			if (!jCheckBoxToggleAP.isSelected()) {
				disableAP();
			} else {
				enableAP();
			}
		}
	}
	
	private Action getActionToggleAP() {
		if (actionToggleAP == null) {
			actionToggleAP = new SwingActionToggleAP();
		}
		return actionToggleAP;
	}
	
	private void enableTemp() throws NumberFormatException {
		jSliderTemp.setEnabled(true);
		jTextFieldTemp.setEnabled(true);
		String text = jTextFieldTemp.getText();
		if (!text.equals("")) {
			patex.setPex_temp(Double.parseDouble(text));
		} else {
			patex.setPex_temp(0);
		}
	}

	private void disableTemp() {
		jSliderTemp.setEnabled(false);
		jTextFieldTemp.setEnabled(false);
		patex.setPex_temp(0);
	}
	
	private void enableSaturation() throws NumberFormatException {
		jSliderSaturation.setEnabled(true);
		jTextFieldSaturation.setEnabled(true);
		String text = jTextFieldSaturation.getText();
		if (!text.equals("")) {
			patex.setPex_sat(Double.parseDouble(text));
		} else {
			patex.setPex_sat(0);
		}
	}

	private void disableSaturation() {
		jSliderSaturation.setEnabled(false);
		jTextFieldSaturation.setEnabled(false);
		patex.setPex_sat(0);
	}
	
	private void enableHR() throws NumberFormatException {
		jSliderHR.setEnabled(true);
		jTextFieldHR.setEnabled(true);
		patex.setPex_fc(Integer.parseInt(jTextFieldHR.getText()));
	}

	private void disableHR() {
		jSliderHR.setEnabled(false);
		jTextFieldHR.setEnabled(false);
		patex.setPex_fc(0);
	}
	
	private class SwingActionToggleSaturation extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingActionToggleSaturation() {
			putValue(NAME, ""); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, MessageBundle.getMessage("angal.examination.tooltip.togglesaturationexamination")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			if (!jCheckBoxToggleSaturation.isSelected()) {
				disableSaturation();
			} else {
				enableSaturation();
			}
		}
	}
	
	private class SwingActionToggleTemp extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingActionToggleTemp() {
			putValue(NAME, ""); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, MessageBundle.getMessage("angal.examination.tooltip.toggletemperatureexamination")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			if (!jCheckBoxToggleTemp.isSelected()) {
				disableTemp();
			} else {
				enableTemp();
			}
		}
	}
	
	private class SwingActionToggleHR extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingActionToggleHR() {
			putValue(NAME, ""); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, MessageBundle.getMessage("angal.examination.tooltip.toggleheartrateexamination")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			if (!jCheckBoxToggleHR.isSelected()) {
				disableHR();
			} else {
				enableHR();
			}
		}
	}
	
	private Action getActionToggleHR() {
		if (actionToggleHR == null) {
			actionToggleHR = new SwingActionToggleHR();
		}
		return actionToggleHR;
	}
	
	private Action getActionToggleTemp() {
		if (actionToggleTemp == null) {
			actionToggleTemp = new SwingActionToggleTemp();
		}
		return actionToggleTemp;
	}
	
	private Action getActionToggleSaturation() {
		if (actionToggleSaturation == null) {
			actionToggleSaturation = new SwingActionToggleSaturation();
		}
		return actionToggleSaturation;
	}
	
	private JPanel getJPanelSummary() {
		if (jPanelSummary == null) {
			jPanelSummary = new JPanel();
			jPanelSummary.setBorder(new EmptyBorder(5, 5, 5, 5));
			jPanelSummary.setLayout(new BorderLayout(0, 0));
			jPanelSummary.add(getJEditorPaneSummary());
		}
		return jPanelSummary;
	}

	private JEditorPane getJEditorPaneSummary() {
		if (jEditorPaneSummary == null) {
			jEditorPaneSummary = new JEditorPane();
			jEditorPaneSummary.setFont(new Font("Arial", Font.PLAIN, 11));
			jEditorPaneSummary.setContentType("text/html");
			jEditorPaneSummary.setEditable(false);
			jEditorPaneSummary.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			
		}
		return jEditorPaneSummary;
	}
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		
//		/*
//		 * Default Values
//		 */
//		final int INITIAL_HEIGHT = 170;
//		final int INITIAL_WEIGHT = 80;
//		final int INITIAL_AP_MIN = 80;
//		final int INITIAL_AP_MAX = 120;
//		final int INITIAL_HR = 60;
//		
//		try {
//			
//			PatientExamination patex = new PatientExamination();
//			Patient patient = new Patient();
//			patient.setCode(1);
//			patex.setPatient(patient);
//			patex.setPex_date(new Timestamp(new Date().getTime()));
//			patex.setPex_height(INITIAL_HEIGHT);
//			patex.setPex_weight(INITIAL_WEIGHT);
//			patex.setPex_pa_min(INITIAL_AP_MIN);
//			patex.setPex_pa_max(INITIAL_AP_MAX);
//			patex.setPex_fc(INITIAL_HR);
//			
//			GenderPatientExamination gpatex = new GenderPatientExamination(patex, false);
//			
//			PatientExaminationEdit dialog = new PatientExaminationEdit(gpatex);
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.pack();
//			dialog.setLocationRelativeTo(null);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
