package org.isf.dicom.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;

import org.isf.admission.gui.PatientFolderBrowser;
import org.isf.dicom.manager.DicomManagerFactory;
import org.isf.dicom.manager.SourceFiles;
import org.isf.dicom.model.FileDicom;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;

/**
 * GUI for Dicom Viewer
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 * 
 */
public class DicomGui extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// STATUS
	private String lastDir = ".";

	// GUI COMPONENTS
	private final float factor = 8f / 11f;

	private JButton jButtonLoadDicom;
	private JButton jButtonDeleteDicom;
	private JButton jButtonExit;
	private JPanel jPanel1;
	private JPanel jPanelDetail;
	private JPanel jPanelButton;
	private JScrollPane jScrollPane2;
	private JSplitPane jSplitPane1;
	private JPanel jPanelMain;

	private ThumbnailViewGui thumbnail = null;
	private int patient = -1;
	private Patient ohPatient = null;
	private int position = 150;

	private JFrame myJFrame = null;

	private PatientFolderBrowser owner = null;

	/**
	 * Construct a GUI
	 * 
	 * @param, paziente the data wrapper for OH Patient
	 */
	public DicomGui(Patient paziente, PatientFolderBrowser owner) {
		super();
		this.patient = paziente.getCode().intValue();
		this.ohPatient = paziente;
		this.owner = owner;

		initialize();
		setVisible(true);
		addWindowListener(this);
		myJFrame = this;

		// TMP
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Save preference for DICOM window
	 */
	private void saveWindowSettings() {
		try {
			File f = new File("rsc/dicom.user.pref");
			ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(f));

			ous.writeInt(getX());
			ous.writeInt(getY());
			ous.writeInt(getHeight());
			ous.writeInt(getWidth());

			ous.writeInt(jSplitPane1.getDividerLocation());

			ous.writeUTF(lastDir);

			ous.flush();
			ous.close();
		} catch (Exception ec) {
		}
	}

	/**
	 * Load preferences for DICOM windows
	 */
	private void loadWindowSettings() {

		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;

		try {
			File f = new File("rsc/dicom.user.pref");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			x = ois.readInt();
			y = ois.readInt();
			h = ois.readInt();
			w = ois.readInt();
			position = ois.readInt();
			lastDir = ois.readUTF();
		} catch (Exception e) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screensize = kit.getScreenSize();
			h = Math.round(screensize.height * factor);
			w = Math.round(screensize.width * factor);
			x = Math.round((screensize.width - w) / 2);
			y = Math.round((screensize.height - h) / 2);
		}

		this.setBounds(x, y, w, h);

	}

	private void initialize() {
		loadWindowSettings();

		this.setTitle(MessageBundle.getMessage("angal.dicom.title"));

		initComponents();

	}

	private void initComponents() {

		jPanelMain = new JPanel();
		jPanel1 = new JPanel();
		jButtonLoadDicom = new JButton();
		jButtonDeleteDicom = new JButton();
		jButtonExit = new JButton();
		jSplitPane1 = new JSplitPane();
		jSplitPane1.setEnabled(false);
		jPanelDetail = new DicomViewGui(null, null);
		jPanelButton = new JPanel();
		jPanelButton.add(jButtonLoadDicom);
		jPanelButton.add(jButtonDeleteDicom);
		jPanelMain.setName("mainPanel");
		jPanel1.setName("jPanel1");
		jButtonLoadDicom.setText(MessageBundle.getMessage("angal.dicom.load.txt"));
		jButtonLoadDicom.setName("jButtonLoadDicom");

		jButtonDeleteDicom.setText(MessageBundle.getMessage("angal.dicom.delete.txt"));
		jButtonDeleteDicom.setName("jButtonDeleteDicom");
		jButtonDeleteDicom.setEnabled(false);

		jButtonExit.setText(MessageBundle.getMessage("angal.common.close"));
		jButtonExit.setName("jButtonExit");

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1Layout.setAutoCreateContainerGaps(true);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING))
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)).addComponent(jPanelButton)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
								.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jPanelButton))));

		jSplitPane1.setDividerLocation(position);
		jSplitPane1.setName("jSplitPane1");
		thumbnail = new ThumbnailViewGui(patient, this);
		jPanelDetail.setName("jPanelDetail");
		jSplitPane1.setRightComponent(jPanelDetail);
		jScrollPane2 = new JScrollPane();
		JPanel tmpPanel = new JPanel(new BorderLayout());
		tmpPanel.add(thumbnail, BorderLayout.CENTER);
		jScrollPane2.setViewportView(tmpPanel);
		jScrollPane2.setName("jScrollPane2");
		jSplitPane1.setLeftComponent(jScrollPane2);
		thumbnail.initialize();
		GroupLayout mainPanelLayout = new GroupLayout(jPanelMain);
		jPanelMain.setLayout(mainPanelLayout);

		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(mainPanelLayout.createSequentialGroup().addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE).addGap(10, 10, 10)));

		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				mainPanelLayout.createSequentialGroup().addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

		addEventListener();
		this.setContentPane(jPanelMain);

	}// Layered with Netbeans Designer

	// EVENT LISTENER

	private void addEventListener() {
		actionListenerJButtonLoadDicom();
		actionListenerJButtonDeleteDicom();
		actionListenerJButtonExit();
	}

	private void actionListenerJButtonLoadDicom() {

		jButtonLoadDicom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser(new File(lastDir));

				jfc.setFileFilter(new FileDicomFilter());
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				int status = jfc.showDialog(new JLabel(""), MessageBundle.getMessage("angal.dicom.open.txt"));

				if (status == JFileChooser.APPROVE_OPTION) {

					File selectedFile = jfc.getSelectedFile();

					File dir = null;

					try {
						dir = selectedFile.getParentFile();
					} catch (Exception ec) {
					}

					if (dir != null)
						lastDir = dir.getAbsolutePath();

					// System.out.println("Scelto "+scelto.getAbsolutePath());

					if (selectedFile.isDirectory()) {
						
						int numfiles = SourceFiles.countFiles(selectedFile, patient);
						thumbnail.disableLoadButton();
						new SourceFiles(selectedFile, patient, numfiles, thumbnail, new DicomLoader(numfiles, myJFrame));

					} else {
						// single file
						SourceFiles.loadDicom(selectedFile, patient);
						thumbnail.initialize();
					}
				}
			}
		});
	}

	private void actionListenerJButtonDeleteDicom() {
		jButtonDeleteDicom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { MessageBundle.getMessage("angal.dicom.delete.yes"), MessageBundle.getMessage("angal.dicom.delete.no") };

				int n = JOptionPane.showOptionDialog(DicomGui.this, MessageBundle.getMessage("angal.dicom.delete.request"), MessageBundle.getMessage("angal.dicom.delete.title"),
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "");

				if (n == 0){
					try {
						DicomManagerFactory.getManager().deleteSerie(patient, thumbnail.getSelectedInstance().getDicomSeriesNumber());
					}catch(OHServiceException ex){
						if(ex.getMessages() != null){
							for(OHExceptionMessage msg : ex.getMessages()){
								JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
							}
						}
					}
				}
				thumbnail.initialize();

			}
		});
	}

	private void actionListenerJButtonExit() {
		jButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(100);
				// this.dispose();
			}
		});
	}

	// WINDOW LISTENER

	/**
	 * Invoked the first time a window is made visible.
	 */
	public void windowOpened(WindowEvent e) {
		// System.out.println("windowOpened");
	}

	/**
	 * Invoked when the user attempts to close the window from the window's
	 * system menu.
	 */
	public void windowClosing(WindowEvent e) {
		saveWindowSettings();
	}

	/**
	 * Invoked when a window has been closed as the result of calling dispose on
	 * the window.
	 */
	public void windowClosed(WindowEvent e) {
		this.setVisible(false);
		this.dispose();
		owner.resetDicomViewer();
	}

	/**
	 * Invoked when a window is changed from a normal to a minimized state. For
	 * many platforms, a minimized window is displayed as the icon specified in
	 * the window's iconImage property.
	 * 
	 * @see java.awt.Frame#setIconImage
	 */
	public void windowIconified(WindowEvent e) {
		this.dispose();
	}

	/**
	 * Invoked when a window is changed from a minimized to a normal state.
	 */
	public void windowDeiconified(WindowEvent e) {

	}

	/**
	 * Invoked when the Window is set to be the active Window. Only a Frame or a
	 * Dialog can be the active Window. The native windowing system may denote
	 * the active Window or its children with special decorations, such as a
	 * highlighted title bar. The active Window is always either the focused
	 * Window, or the first Frame or Dialog that is an owner of the focused
	 * Window.
	 */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * Invoked when a Window is no longer the active Window. Only a Frame or a
	 * Dialog can be the active Window. The native windowing system may denote
	 * the active Window or its children with special decorations, such as a
	 * highlighted title bar. The active Window is always either the focused
	 * Window, or the first Frame or Dialog that is an owner of the focused
	 * Window.
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	// BOTTONI

	private FileDicom selectedElement = null;

	public void disableDeleteButton() {
		selectedElement = null;

		jButtonDeleteDicom.setEnabled(false);
	}

	public void enableDeleteButton(FileDicom selezionato) {

		this.selectedElement = selezionato;

		jButtonDeleteDicom.setEnabled(true);

	}

	public void disableLoadButton() {

		jButtonLoadDicom.setEnabled(false);
	}

	public void enableLoadButton() {

		jButtonLoadDicom.setEnabled(true);

	}

	public void detail() {

		String serie = "";

		try {
			serie = selectedElement.getDicomSeriesNumber();
		} catch (Exception ecc) {
		}

		((DicomViewGui) jPanelDetail).notifyChanges(ohPatient, serie);

	}

}
