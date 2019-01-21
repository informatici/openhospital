package org.isf.therapy.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.menu.manager.Context;
import org.isf.therapy.manager.TherapyManager;
import org.isf.therapy.model.Therapy;
import org.isf.therapy.model.TherapyRow;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.IconButton;
import org.isf.utils.time.TimeTools;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.toedter.calendar.JDateChooser;

/**
 * @author Mwithi
 * 
 */
public class TherapyEntryForm extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Managers
	 */
	private MedicalBrowsingManager medBrowser = new MedicalBrowsingManager();
	private ArrayList<Medical> medArray = null;

	/*
	 * Constants
	 */
	private static final int sliderMinValue = 0;
	private static final int sliderMaxValue = 500;
	private static final int sliderMajorStepValue = 250;
	private static final int sliderMinorStepValue = 50;
	private final int preferredSpinnerWidth = 100;
	private final int oneLineComponentsHeight = 30;
	private final int visibleMedicalsRows = 5;
	private final int frequencyInDayOptions = 4;

	/*
	 * Attributes
	 */
	private Therapy therapy;
	private TherapyRow thRow = null;
	private JList medicalsList;
	private JScrollPane medicalListscrollPane;
	private JPanel dayWeeksMonthsPanel;
	private JPanel FrequencyInDayPanel;
	private JPanel FrequencyInPeriodPanel;
	private JPanel quantityPanel;
	private JPanel medicalsPanel;
	private JPanel therapyPanelWest;
	private JPanel therapyPanelEast;
	private JPanel startEndDatePanel;
	private JPanel startDatePanel;
	private JPanel endDatePanel;
	private JPanel notePanel;
	private JScrollPane noteScrollPane;
	private JTextArea noteTextArea;
	private JPanel iconMedicalPanel;
	private JPanel iconFrequenciesPanel;
	private JPanel iconPeriodPanel;
	private JPanel iconNotePanel;
	private JPanel frequenciesPanel;
	private JSlider jSliderQty;
	private JSpinner jSpinnerQty;
	private ArrayList<JRadioButton> radioButtonSet;
	private JSpinner jSpinnerFreqInPeriod;
	private JDateChooser therapyStartdate;
	private GregorianCalendar therapyEndDate;
	private JSpinner jSpinnerDays;
	private JSpinner jSpinnerWeeks;
	private JSpinner jSpinnerMonths;
	private JLabel endDateLabel;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy"); //$NON-NLS-1$
	private final String[] radioButtonLabels = {MessageBundle.getMessage("angal.therapy.one"), MessageBundle.getMessage("angal.therapy.two"), MessageBundle.getMessage("angal.therapy.three"), MessageBundle.getMessage("angal.therapy.four")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private JButton buttonCancel;
	private JButton buttonOK;
	private JPanel buttonPanel;
	private JPanel therapyPanel;
	private int freqInDay;
	private int patID;
	
	/**
	 * Create the dialog.
	 */
	public TherapyEntryForm(JDialog owner, int patID, Therapy th) {
		super(owner, true);
		try {
			this.medArray = medBrowser.getMedicals();
		} catch (OHServiceException e) {
			this.medArray = new ArrayList<Medical>();
			OHServiceExceptionUtil.showMessages(e, TherapyEntryForm.this);
		}
		this.therapy = th;
		this.patID = patID;

		initComponents();
		

		if (therapy != null) {
			fillFormWithTherapy(therapy);
		} else {
			therapy = new Therapy();
			radioButtonSet.get(0).setSelected(true);
			endDateLabel.setText(dateFormat.format(new Date()));
		}
	}

	private void initComponents() {
		if (therapy == null) {
			setTitle(MessageBundle.getMessage("angal.therapy.titlenew")); //$NON-NLS-1$
		} else {
			setTitle(MessageBundle.getMessage("angal.therapy.titleedit")); //$NON-NLS-1$
			getContentPane().setBackground(Color.RED);
		}
		setSize(new Dimension(740, 400));
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getTherapyPanel(), BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private void fillFormWithTherapy(Therapy th) {
		/*
		 * Medicals
		 */
		medicalsList.setSelectedValue(th.getMedical(), true);

		/*
		 * Quantity
		 */
		jSpinnerQty.setValue(th.getQty());

		/*
		 * Frequency Within Day
		 */
		radioButtonSet.get(th.getFreqInDay() - 1).setSelected(true);
		
		/*
		 * Calendars
		 */
		fillCalendarsFromTherapy(th);

		/*
		 * Note
		 */
		noteTextArea.setText(th.getNote());
	}

	private void fillCalendarsFromTherapy(Therapy th) {
		GregorianCalendar[] dates = th.getDates();
		int datesLength = dates.length;
		GregorianCalendar firstDay = dates[0];
		GregorianCalendar lastDay = dates[datesLength - 1];
		GregorianCalendar secondDay;
		
		if (datesLength > 1) {
			secondDay = dates[1];
		} else {
			secondDay = firstDay;
		}
		int days = TimeTools.getDaysBetweenDates(firstDay, secondDay, true);

		jSpinnerFreqInPeriod.setValue(days > 0 ? days : 1);
		therapyStartdate.setDate(firstDay.getTime());
		endDateLabel.setText(dateFormat.format(lastDay.getTime()));

		fillDaysWeeksMonthsFromDates(firstDay, lastDay); 
	}

	private void fillDaysWeeksMonthsFromDates(GregorianCalendar firstDay, GregorianCalendar lastDay) {
		DateTime dateFrom = new DateTime(firstDay);
		DateTime dateTo = new DateTime(lastDay);
		Period period = new Period(dateFrom, dateTo, PeriodType.standard());
		
		jSpinnerMonths.setValue(period.getMonths());
		jSpinnerWeeks.setValue(period.getWeeks());
		jSpinnerDays.setValue(period.getDays());
	}

	private JList getMedicalsList() {
		if (medicalsList == null) {
			medicalsList = new JList(medArray.toArray());
			medicalsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return medicalsList;
	}

	private JScrollPane getMedicalListscrollPane() {
		if (medicalListscrollPane == null) {
			medicalListscrollPane = new JScrollPane(getMedicalsList());
			medicalListscrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
			medicalListscrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE,
					oneLineComponentsHeight * visibleMedicalsRows));
		}
		return medicalListscrollPane;
	}

	private JSpinner getSpinnerQty() {
		Double startQty = 0.;
		Double minQty = 0.;
		Double stepQty = 0.5;
		Double maxQty = null;
		jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty, minQty,
				maxQty, stepQty));
		jSpinnerQty.setFont(new Font("Dialog", Font.BOLD, 14)); //$NON-NLS-1$
		jSpinnerQty.setAlignmentX(Component.LEFT_ALIGNMENT);
		jSpinnerQty.setPreferredSize(new Dimension(preferredSpinnerWidth,
				oneLineComponentsHeight));
		jSpinnerQty.setMaximumSize(new Dimension(Short.MAX_VALUE,
				oneLineComponentsHeight));
		jSpinnerQty.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSpinner source = (JSpinner) e.getSource();
				double value = (Double) source.getValue();
				therapy.setQty(value);
				/*
				 * sebbene sia utile crea conflitto 
				 */
				//int intValue = new Double(value).intValue();
				//jSliderQty.setValue(intValue);
			}
		});
		return jSpinnerQty;
	}

	private JPanel getDaysWeeksMonthsPanel() {
		if (dayWeeksMonthsPanel == null) {
			dayWeeksMonthsPanel = new JPanel();
			dayWeeksMonthsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			dayWeeksMonthsPanel.setBorder(new TitledBorder(null, MessageBundle.getMessage("angal.therapy.period"), //$NON-NLS-1$
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			dayWeeksMonthsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE,
					Short.MAX_VALUE));
			dayWeeksMonthsPanel.add(getPeriodSpinners());
		}
		return dayWeeksMonthsPanel;
	}

	private JPanel getPeriodSpinners() {

		int startQty = 0;
		int minQty = 0;
		int maxQty = 99;
		int stepQty = 1;

		jSpinnerDays = new JSpinner(new SpinnerNumberModel(1, minQty,
				maxQty, stepQty));
		jSpinnerWeeks = new JSpinner(new SpinnerNumberModel(startQty, minQty,
				maxQty, stepQty));
		jSpinnerMonths = new JSpinner(new SpinnerNumberModel(startQty, minQty,
				maxQty, stepQty));

		JPanel daysPanel = new JPanel();
		BoxLayout daysLayout = new BoxLayout(daysPanel, BoxLayout.Y_AXIS);
		daysPanel.setLayout(daysLayout);
		JLabel labelDays = new JLabel(MessageBundle.getMessage("angal.therapy.days")); //$NON-NLS-1$
		labelDays.setAlignmentX(CENTER_ALIGNMENT);
		jSpinnerDays.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSpinner theSpinner = (JSpinner) e.getSource();
				if ((Integer) theSpinner.getValue() == 0) {
					/*
					 * Days must be at least one.
					 */
					if ((Integer) jSpinnerWeeks.getValue() == 0 && (Integer) jSpinnerMonths.getValue() == 0) {
						theSpinner.setValue(theSpinner.getNextValue());
					}
				}
				updateEndDateLabel();
			}
		});
		jSpinnerDays.setAlignmentX(CENTER_ALIGNMENT);
		daysPanel.add(labelDays);
		daysPanel.add(jSpinnerDays);
		JPanel weeksPanel = new JPanel();
		BoxLayout weeksLayout = new BoxLayout(weeksPanel, BoxLayout.Y_AXIS);
		weeksPanel.setLayout(weeksLayout);
		JLabel labelWeeks = new JLabel(MessageBundle.getMessage("angal.therapy.weeks")); //$NON-NLS-1$
		labelWeeks.setAlignmentX(CENTER_ALIGNMENT);

		jSpinnerWeeks.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				updateEndDateLabel();
			}
		});
		jSpinnerWeeks.setAlignmentX(CENTER_ALIGNMENT);
		weeksPanel.add(labelWeeks);
		weeksPanel.add(jSpinnerWeeks);
		JPanel monthsPanel = new JPanel();
		BoxLayout monthsLayout = new BoxLayout(monthsPanel, BoxLayout.Y_AXIS);
		monthsPanel.setLayout(monthsLayout);
		JLabel labelMonths = new JLabel(MessageBundle.getMessage("angal.therapy.months")); //$NON-NLS-1$
		labelMonths.setAlignmentX(CENTER_ALIGNMENT);

		jSpinnerMonths.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				updateEndDateLabel();
			}
		});
		jSpinnerMonths.setAlignmentX(CENTER_ALIGNMENT);
		monthsPanel.add(labelMonths);
		monthsPanel.add(jSpinnerMonths);
		JPanel daysWeeksMonthsPanel = new JPanel();
		daysWeeksMonthsPanel.add(daysPanel);
		daysWeeksMonthsPanel.add(weeksPanel);
		daysWeeksMonthsPanel.add(monthsPanel);
		return daysWeeksMonthsPanel;
	}

	private JPanel getFrequencyInDayPanel() {
		if (FrequencyInDayPanel == null) {
			FrequencyInDayPanel = new JPanel();
			FrequencyInDayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			FrequencyInDayPanel.setBorder(new TitledBorder(null,
					MessageBundle.getMessage("angal.therapy.frequencywithinday"), TitledBorder.LEADING, //$NON-NLS-1$
					TitledBorder.TOP, null, null));

			radioButtonSet = getRadioButtonSet(frequencyInDayOptions);
			for (JRadioButton radioButton : radioButtonSet) {
				FrequencyInDayPanel.add(radioButton);
			}
		}
		return FrequencyInDayPanel;
	}

	private JPanel getFrequencyInPeriodPanel() {
		if (FrequencyInPeriodPanel == null) {
			FrequencyInPeriodPanel = new JPanel();
			FrequencyInPeriodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			FrequencyInPeriodPanel.setBorder(new TitledBorder(null,
					MessageBundle.getMessage("angal.therapy.frequencywithinperiod"), TitledBorder.LEADING, //$NON-NLS-1$
					TitledBorder.TOP, null, null));

			JLabel labelPrefix = new JLabel(MessageBundle.getMessage("angal.therapy.every")); //$NON-NLS-1$
			FrequencyInPeriodPanel.add(labelPrefix);

			FrequencyInPeriodPanel.add(getSpinnerFreqInPeriod());

			JLabel labelSuffix = new JLabel(MessageBundle.getMessage("angal.therapy.daydays")); //$NON-NLS-1$
			FrequencyInPeriodPanel.add(labelSuffix);
		}
		return FrequencyInPeriodPanel;
	}

	private ArrayList<JRadioButton> getRadioButtonSet(int frequencyInDayOptions) {

		radioButtonSet = new ArrayList<JRadioButton>();
		ButtonGroup buttonGroup = new ButtonGroup();

		for (int i = 0; i < frequencyInDayOptions; i++) {
			JRadioButton radioButton = new JRadioButton(radioButtonLabels[i]);
			radioButtonSet.add(radioButton);
			buttonGroup.add(radioButton);
		}

		return radioButtonSet;
	}

	private JPanel getQuantityPanel() {
		if (quantityPanel == null) {
			quantityPanel = new JPanel();
			quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			quantityPanel.setLayout(new BoxLayout(quantityPanel,
					BoxLayout.X_AXIS));

			JLabel quantityLabel = new JLabel(MessageBundle.getMessage("angal.common.quantity")); //$NON-NLS-1$
			quantityPanel.add(quantityLabel);
			quantityPanel.add(getSpinnerQty());
			quantityPanel.add(getQuantitySlider());
		}
		return quantityPanel;
	}

	private JPanel getMedicalsPanel() {
		if (medicalsPanel == null) {
			medicalsPanel = new JPanel();
			medicalsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			medicalsPanel.setLayout(new BoxLayout(medicalsPanel,
					BoxLayout.Y_AXIS));
			medicalsPanel.setBorder(new TitledBorder(null, MessageBundle.getMessage("angal.therapy.pharmaceutical"), //$NON-NLS-1$
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			medicalsPanel.add(getMedicalListscrollPane());
			medicalsPanel.add(Box.createVerticalGlue());
			medicalsPanel.add(getQuantityPanel());
		}
		return medicalsPanel;
	}

	private JPanel getTherapyPanelWest() {
		if (therapyPanelWest == null) {
			therapyPanelWest = new JPanel();
			therapyPanelWest.setLayout(new BoxLayout(therapyPanelWest,
					BoxLayout.Y_AXIS));

			therapyPanelWest.add(getIconMedicalPanel());
			therapyPanelWest.add(getIconFrequenciesPanel());
		}
		return therapyPanelWest;
	}

	private JPanel getTherapyPanelEast() {
		if (therapyPanelEast == null) {
			therapyPanelEast = new JPanel();
			therapyPanelEast.setLayout(new BoxLayout(therapyPanelEast,
					BoxLayout.Y_AXIS));
			
			therapyPanelEast.add(getIconPeriodPanel());
			therapyPanelEast.add(getIconNotePanel());
		}
		return therapyPanelEast;
	}

	private JSpinner getSpinnerFreqInPeriod() {
		Integer startQty = 1;
		Integer minQty = 1;
		Integer stepQty = 1;
		Integer maxQty = 100;
		jSpinnerFreqInPeriod = new JSpinner(new SpinnerNumberModel(startQty,
				minQty, maxQty, stepQty));
		jSpinnerFreqInPeriod.setAlignmentX(Component.LEFT_ALIGNMENT);
		jSpinnerFreqInPeriod.setMaximumSize(new Dimension(Short.MAX_VALUE,
				oneLineComponentsHeight));
		jSpinnerFreqInPeriod.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {

			}
		});
		return jSpinnerFreqInPeriod;
	}

	private JDateChooser getStartDate() {
		if (therapyStartdate == null) {
			therapyStartdate = new JDateChooser(new Date());
			therapyStartdate.setLocale(new Locale(GeneralData.LANGUAGE));
			therapyStartdate.setDateFormatString(dateFormat.toPattern());

			therapyStartdate.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$

						public void propertyChange(PropertyChangeEvent evt) {

//							JDateChooser theChooser = (JDateChooser) evt.getSource();
//							newStartingDate(theChooser.getDate());
							updateEndDateLabel();
						}
					});
		}
		return therapyStartdate;
	}
	
	private void updateEndDateLabel() {
		
		int days = (Integer) jSpinnerDays.getValue();
		int weeks = (Integer) jSpinnerWeeks.getValue();
		int months = (Integer) jSpinnerMonths.getValue();
		
		therapyEndDate = new GregorianCalendar();
		therapyEndDate.setTime(therapyStartdate.getDate());
		therapyEndDate.add(GregorianCalendar.DAY_OF_YEAR, days - 1);
		therapyEndDate.add(GregorianCalendar.WEEK_OF_YEAR, weeks);
		therapyEndDate.add(GregorianCalendar.MONTH, months);
		
		endDateLabel.setText(dateFormat.format(therapyEndDate.getTime()));
	}

	private JPanel getStartEndDatePanel() {
		if (startEndDatePanel == null) {
			startEndDatePanel = new JPanel();
			startEndDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			startEndDatePanel.setBorder(new TitledBorder(null, MessageBundle.getMessage("angal.therapy.startsdashend"), //$NON-NLS-1$
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			startEndDatePanel
					.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

			startEndDatePanel.add(getStartDatePanel());
			startEndDatePanel.add(getEndDatePanel());

		}
		return startEndDatePanel;
	}

	private JPanel getStartDatePanel() {
		if (startDatePanel == null) {
			startDatePanel = new JPanel();
			startDatePanel.setLayout(new BoxLayout(startDatePanel,
					BoxLayout.Y_AXIS));
			JLabel startLabel = new JLabel(MessageBundle.getMessage("angal.therapy.start")); //$NON-NLS-1$
			startLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			startDatePanel.add(startLabel);
			startDatePanel.add(getStartDate());
		}
		return startDatePanel;
	}

	private JPanel getEndDatePanel() {
		if (endDatePanel == null) {
			endDatePanel = new JPanel();
			endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.Y_AXIS));
			JLabel endDateLabel = new JLabel(MessageBundle.getMessage("angal.therapy.end")); //$NON-NLS-1$
			endDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			endDatePanel.add(endDateLabel);
			endDatePanel.add(getEndDateField());
		}
		return endDatePanel;
	}

	private JPanel getNotePanel() {
		if (notePanel == null) {
			notePanel = new JPanel();
			notePanel.setAlignmentY(Component.TOP_ALIGNMENT);
			notePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			notePanel.setBorder(new TitledBorder(null, MessageBundle.getMessage("angal.therapy.note"), //$NON-NLS-1$
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			notePanel.setLayout(new BorderLayout(0, 0));
			notePanel.add(getNoteScrollPane());
		}
		return notePanel;
	}

	private JScrollPane getNoteScrollPane() {
		if (noteScrollPane == null) {
			noteScrollPane = new JScrollPane(getNoteTextArea());
			noteScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return noteScrollPane;
	}

	private JTextArea getNoteTextArea() {
		if (noteTextArea == null) {
			noteTextArea = new JTextArea();
			noteTextArea.setLineWrap(true);
		}
		return noteTextArea;
	}

	private JPanel getIconMedicalPanel() {
		if (iconMedicalPanel == null) {
			iconMedicalPanel = new JPanel();
			iconMedicalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			iconMedicalPanel.setLayout(new BoxLayout(iconMedicalPanel,
					BoxLayout.X_AXIS));

			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
			IconButton iconButton = new IconButton(new ImageIcon(
					"rsc/icons/medical_dialog.png")); //$NON-NLS-1$
			iconButton.setAlignmentY(Component.TOP_ALIGNMENT);
			iconPanel.add(iconButton);
			iconMedicalPanel.add(iconPanel);
			iconMedicalPanel.add(getMedicalsPanel());

		}
		return iconMedicalPanel;
	}

	private JPanel getIconFrequenciesPanel() {
		if (iconFrequenciesPanel == null) {
			iconFrequenciesPanel = new JPanel();
			iconFrequenciesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			iconFrequenciesPanel.setLayout(new BoxLayout(iconFrequenciesPanel,
					BoxLayout.X_AXIS));

			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
			IconButton iconButton = new IconButton(new ImageIcon(
					"rsc/icons/clock_dialog.png")); //$NON-NLS-1$
			iconButton.setAlignmentY(Component.TOP_ALIGNMENT);
			iconPanel.add(iconButton);
			iconFrequenciesPanel.add(iconPanel);
			iconFrequenciesPanel.add(getFrequenciesPanel());
		}
		return iconFrequenciesPanel;
	}

	private JPanel getIconPeriodPanel() {
		if (iconPeriodPanel == null) {
			iconPeriodPanel = new JPanel();
			iconPeriodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			iconPeriodPanel.setLayout(new BoxLayout(iconPeriodPanel,
					BoxLayout.X_AXIS));

			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
			IconButton iconButton = new IconButton(new ImageIcon(
					"rsc/icons/calendar_dialog.png")); //$NON-NLS-1$
			iconButton.setAlignmentY(Component.TOP_ALIGNMENT);
			iconPanel.add(iconButton);
			iconPeriodPanel.add(iconPanel);

			JPanel periodPanel = new JPanel();
			periodPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			periodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
			periodPanel.add(getDaysWeeksMonthsPanel());
			periodPanel.add(getStartEndDatePanel());
			iconPeriodPanel.add(periodPanel);
		}
		return iconPeriodPanel;
	}

	private JPanel getIconNotePanel() {
		if (iconNotePanel == null) {
			iconNotePanel = new JPanel();
			iconNotePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			iconNotePanel.setLayout(new BoxLayout(iconNotePanel,
					BoxLayout.X_AXIS));

			JPanel iconPanel = new JPanel();
			iconPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
			iconPanel.add(new IconButton(new ImageIcon(
					"rsc/icons/list_dialog.png"))); //$NON-NLS-1$
			iconNotePanel.add(iconPanel);
			iconNotePanel.add(getNotePanel());
		}
		return iconNotePanel;
	}

	private JPanel getFrequenciesPanel() {
		if (frequenciesPanel == null) {
			frequenciesPanel = new JPanel();
			frequenciesPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			frequenciesPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
			frequenciesPanel.setLayout(new BoxLayout(frequenciesPanel,
					BoxLayout.Y_AXIS));
			frequenciesPanel.add(getFrequencyInDayPanel());
			frequenciesPanel.add(getFrequencyInPeriodPanel());
		}
		return frequenciesPanel;
	}

	private JSlider getQuantitySlider() {
		if (jSliderQty == null) {
			jSliderQty = new JSlider(sliderMinValue, sliderMaxValue);
			jSliderQty.setFont(new Font("Arial", Font.BOLD, 8)); //$NON-NLS-1$
			jSliderQty.setValue(sliderMinValue);
			jSliderQty.setMajorTickSpacing(sliderMajorStepValue);
			jSliderQty.setMinorTickSpacing(sliderMinorStepValue);
			jSliderQty.setPaintLabels(true);

			jSliderQty.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					double value = (double) source.getValue();
					jSpinnerQty.setValue(value);
					therapy.setQty(value);
				}
			});
		}
		return jSliderQty;
	}
	
	private JLabel getEndDateField() {
		if (endDateLabel == null) {
			endDateLabel = new JLabel(""); //$NON-NLS-1$
			endDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			updateEndDateLabel();
		}
		return endDateLabel;
	}
	
	private JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new JButton(MessageBundle.getMessage("angal.common.cancel")); //$NON-NLS-1$
			buttonCancel.setMnemonic(KeyEvent.VK_N);
			buttonCancel.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return buttonCancel;
	}
	
	private JButton getButtonOK() {
		if (buttonOK == null) {
			buttonOK = new JButton(MessageBundle.getMessage("angal.common.ok")); //$NON-NLS-1$
			buttonOK.setMnemonic(KeyEvent.VK_O);
			buttonOK.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					/*
					 * estrapolazione Dati
					 */
					
					GregorianCalendar startDate = new GregorianCalendar();
					startDate.setTime(therapyStartdate.getDate());
					GregorianCalendar endDate = therapyEndDate;
					Medical medical = (Medical) medicalsList.getSelectedValue();
					if (medical == null) {
						JOptionPane.showMessageDialog(TherapyEntryForm.this, MessageBundle.getMessage("angal.therapy.selectapharmaceutical"), MessageBundle.getMessage("angal.therapy.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						return;
					}
					Double qty = (Double) jSpinnerQty.getValue();
					if (qty == 0.) {
						JOptionPane.showMessageDialog(TherapyEntryForm.this, MessageBundle.getMessage("angal.therapy.pleaseinsertaquantitygreaterthanzero"), MessageBundle.getMessage("angal.therapy.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						return;
					}
					int therapyID = 0;
					int unitID = 0; //TODO: UoM table
					int freqInDay = getFreqInDay();
					int freqInPeriod = Integer.parseInt(jSpinnerFreqInPeriod.getValue().toString());
					String note = noteTextArea.getText();
					boolean notify = false;
					boolean sms = false;

					TherapyManager thManager = Context.getApplicationContext().getBean(TherapyManager.class);
					try {
						thRow = thManager.newTherapy(therapyID, patID, startDate, endDate, medical, qty, unitID, freqInDay, freqInPeriod, note, notify, sms);
						therapyID = thRow.getTherapyID();
					} catch (OHServiceException e){
						OHServiceExceptionUtil.showMessages(e, TherapyEntryForm.this);
					}
					if (therapyID > 0) {
						thRow.setTherapyID(therapyID);
					}
					setVisible(false);
					
				}
			});
		}
		return buttonOK;
	}
	
	private int getFreqInDay() {
		for (JRadioButton button : radioButtonSet) {
			if (button.isSelected()) {
				freqInDay = radioButtonSet.indexOf(button) + 1;
			}
		}
		return freqInDay;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getButtonOK());
			buttonPanel.add(getButtonCancel());
		}
		return buttonPanel;
	}
	
	private JPanel getTherapyPanel() {
		if (therapyPanel == null) {
			therapyPanel = new JPanel();
			therapyPanel.setLayout(new GridLayout(0, 2, 0, 0));
			therapyPanel.add(getTherapyPanelWest());
			therapyPanel.add(getTherapyPanelEast());
		}
		return therapyPanel;
	}

	public Therapy getTherapy() {
		return therapy;
	}

	public void setTherapy(Therapy therapy) {
		this.therapy = therapy;
	}

	public TherapyRow getThRow() {
		return thRow;
	}

	public void setThRow(TherapyRow thRow) {
		this.thRow = thRow;
	}
}
