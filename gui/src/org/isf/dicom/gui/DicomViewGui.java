package org.isf.dicom.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.imageio.plugins.dcm.DicomStreamMetaData;
import org.dcm4che2.io.DicomCodingException;
import org.imgscalr.Scalr;
import org.isf.dicom.manager.DicomManagerFactory;
import org.isf.dicom.model.FileDicom;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;

/**
 * 
 * Detail for DICOM image
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 * 
 */
public class DicomViewGui extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// status of fremereader
	private int patID = -1;
	private Patient ohPatient = null;
	private String serieNumber = "";
	private Long[] frames = null;

	// status of frame
	private int frameIndex = 0;
	private BufferedImage tmpImg = null;
	private DicomObject tmpDicom = null;
	private FileDicom tmpDbFile = null;

	// GUI
	private JPanel jPanelHeader = null;
	private JPanel jPanelCenter = null;
	private JPanel jPanelFooter = null;
	private JSlider jSliderZoom = null;
	private JSlider jSliderFrame = null;
	
	// GUI parameters
	private int x = -1;
	private int y = -1;
	private int totX = -1;
	private int totY = -1;
	private final static Color colScr = Color.LIGHT_GRAY;
	private final static int VGAP = 15;

	/**
	 * Construct a new detail for DICOM image
	 * 
	 * @param dettaglio
	 */
	public DicomViewGui(Patient patient, String serieNumber) {

		this.patID = (patient != null ? (patient.getAge()) : -1);
		this.serieNumber = serieNumber;
		this.ohPatient = patient;
		this.frameIndex = 0;

		addMouseListener(new DicomViewGuiMouseListener());
		addMouseMotionListener(new DicomViewGuiMouseMotionListener());

		// System.out.println("DicomViewGui "+idPaziente+" "+numeroSerie);

		if (patID >= 0){
			try {
				frames = DicomManagerFactory.getManager().getSerieDetail(patID, serieNumber);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}

		// System.out.println("DicomViewGui "+immagini);

		if (frames == null)
			frames = new Long[0];
		else
			refreshFrame();

		initComponent();

	}

	public void notifyChanges(Patient patient, String serieNumber) {

		this.patID = patient.getCode().intValue();
		this.ohPatient = patient;
		this.serieNumber = serieNumber;
		this.frameIndex = 0;

		if (serieNumber == null || serieNumber.trim().length() == 0)
			return;
		// System.out.println("notificaCambiamento "+idPaziente+" "+numeroSerie);

		if (patID >= 0){
			try {
				frames = DicomManagerFactory.getManager().getSerieDetail(patID, serieNumber);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}

		// System.out.println("notificaCambiamento "+immagini);

		if (frames == null)
			frames = new Long[0];

		jSliderZoom.setValue(100);

		if (frames.length > 0)
			refreshFrame();

		reInitComponent();

	}

	/**
	 * initialize GUI
	 */
	private void initComponent() {

		jPanelHeader = new JPanel();
		jPanelFooter = new JPanel();

		jSliderFrame = new JSlider(0, 0, 0);
		jSliderZoom = new JSlider(50, 300, 100);
		jSliderFrame.addChangeListener(new FrameListener());
		jSliderZoom.addChangeListener(new ZoomListener());
		jPanelHeader.setBackground(Color.BLACK);

		if (patID <= 0) {
			// centro = new JScrollPane();
			jPanelCenter = new JPanel();
			jSliderFrame.setEnabled(false);
			jSliderZoom.setEnabled(false);
		} else {
			// System.out.println("init per "+idPaziente+" immagini "+immagini.length);
			jSliderZoom.setEnabled(true);
			jSliderZoom.setPaintTicks(true);
			jSliderZoom.setMajorTickSpacing(10);
			
			if (frames.length > 1) {
				jSliderFrame.setMaximum(frames.length - 1);
				jSliderFrame.setEnabled(true);
				jSliderFrame.setPaintTicks(true);
				jSliderFrame.setMajorTickSpacing(1);
			} else
				jSliderFrame.setEnabled(false);
			
			// centro = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			// JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			jPanelCenter = new JPanel();

			// System.out.println(this.getWidth()+" "+this.getHeight());
			// centro.getViewport().add(composeCenter(this.getWidth(),this.getHeight()));

			jPanelCenter.add(composeCenter(jPanelCenter.getWidth(), jPanelCenter.getHeight(), true));

		}

		// centro.getViewport().setBackground(Color.BLACK);

		jPanelCenter.setBackground(Color.BLACK);

		JPanel fp1 = new JPanel();
		JPanel fp2 = new JPanel();
		fp1.setLayout(new BoxLayout(fp1, BoxLayout.Y_AXIS));
		fp2.setLayout(new BoxLayout(fp2, BoxLayout.Y_AXIS));
		fp1.setBorder(new TitledBorder("Zoom"));
		fp2.setBorder(new TitledBorder("Frames"));
		jPanelFooter.setLayout(new GridLayout(1, 2));
		fp1.add(Box.createRigidArea(new Dimension(5, 5)));
		fp1.add(jSliderZoom);
		fp1.add(Box.createRigidArea(new Dimension(5, 5)));
		fp2.add(Box.createRigidArea(new Dimension(5, 5)));
		fp2.add(jSliderFrame);
		fp2.add(Box.createRigidArea(new Dimension(5, 5)));
		jPanelFooter.add(fp1);
		jPanelFooter.add(fp2);
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);

		// add(intestazione,BorderLayout.NORTH);
		add(jPanelCenter, BorderLayout.CENTER);
		add(jPanelFooter, BorderLayout.SOUTH);

	}

	private void reInitComponent() {
		if (patID <= 0) {
			// centro = new JScrollPane();
			jPanelCenter = new JPanel();
			jSliderFrame.setEnabled(false);
			jSliderZoom.setEnabled(false);
		} else {
			// System.out.println("reInitComponent per "+idPaziente+" immagini "+immagini.length);

			// reset mouse relative position
			resetMouseRelativePosition();

			jSliderZoom.setEnabled(true);
			jSliderZoom.setPaintTicks(true);
			jSliderZoom.setMajorTickSpacing(10);
			
			if (frames.length > 1) {
				jSliderFrame.setMaximum(frames.length - 1);
				jSliderFrame.setEnabled(true);
				jSliderFrame.setPaintTicks(true);
				jSliderFrame.setMajorTickSpacing(1);
			} else
				jSliderFrame.setEnabled(false);

			jPanelCenter.removeAll();
			
//			int perc = jSliderZoom.getValue();
//			float value = (float) tmpImg.getWidth() * (float) perc / 100f;
//			BufferedImage immagineResized = Scalr.resize(tmpImg, Math.round(value));

			// System.out.println("reinitComponent x "+x+"y "+y);

			jPanelCenter.add(composeCenter(jPanelCenter.getWidth(), jPanelCenter.getHeight(), true));
			validate();

		}
		jSliderFrame.setValue(0);
		jSliderZoom.setValue(100);
	}

	// DRAWS METHODS

	/**
	 * Compose the panel of central image
	 * 
	 * @return, the panel
	 */
	private JPanel composeCenter(int w, int h, boolean calculate) {

		JPanel centerPanel = new JPanel(new BorderLayout(), true);
		BufferedImage imageCanvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		centerPanel.setBackground(Color.BLACK);
		int perc = jSliderZoom.getValue();
		float value = (float) tmpImg.getWidth() * (float) perc / 100f;
		BufferedImage immagineResized = Scalr.resize(tmpImg, Math.round(value));

		// if (immagineResized.getWidth()>immagineCanvas.getWidth())
		// immagineResized =
		// Scalr.resize(tmpImg,Math.round(immagineCanvas.getWidth()));
		// immagineCanvas = new
		// BufferedImage(immagineResized.getWidth(),immagineResized.getHeight(),BufferedImage.TYPE_INT_ARGB);

		// drawQuadrant(immagineResized.getGraphics(),
		// immagineResized.getHeight(),immagineResized.getWidth(),
		// Color.LIGHT_GRAY);

		Graphics2D canvas = (Graphics2D) imageCanvas.getGraphics();

		// design on canvas
		int width = immagineResized.getWidth();
		int height = immagineResized.getHeight();

		if (calculate) {
			x = (w - width) / 2;

			y = (h - height) / 2;
		}

		totX = x - (p1x - p2x);
		totY = y - (p1y - p2y);

		if (totX < -width)
			totX = -width;
		if (totY < -height)
			totY = -height;

		if (totX > imageCanvas.getWidth())
			totX = imageCanvas.getWidth();

		if (totY > imageCanvas.getHeight())
			totY = imageCanvas.getHeight();

		canvas.drawImage(immagineResized, totX, totY, this);

		// draws info

		drawPatientUpRight(canvas, imageCanvas.getWidth(), imageCanvas.getHeight());
		drawInfoFrameBottomLeft(canvas, imageCanvas.getWidth(), imageCanvas.getHeight());
		drawStudyUpRight(canvas, imageCanvas.getWidth(), imageCanvas.getHeight());
		drawSerieBottomRight(canvas, imageCanvas.getWidth(), imageCanvas.getHeight());

		JLabel centerImgLabel = new JLabel(new ImageIcon(imageCanvas));
		centerPanel.add(centerImgLabel, BorderLayout.CENTER);

		return centerPanel;
	}

	private void drawQuadrant(Graphics g, int h, int w, Color c) {

		Color originale = g.getColor();
		g.setColor(c);
		g.drawLine(0, 0, 0, h - 1);
		g.drawLine(0, 0, w - 1, 0);
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.setColor(originale);
	}

	private void drawPatientUpRight(Graphics2D canvas, int w, int h) {
		Color orig = canvas.getColor();
		canvas.setColor(colScr);
		int hi = 10;
		String txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.oh"), 10, hi);
		hi += VGAP;
		txt = ohPatient.getName();
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.name") + " : " + txt, 10, hi);
		hi += VGAP;
		txt = "" + ohPatient.getAge();
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.age") + " : " + txt, 10, hi);
		hi += VGAP;
		txt = "" + ohPatient.getSex();
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.sex") + " : " + txt, 10, hi);
		hi += VGAP;
		hi += VGAP;
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.dicom"), 10, hi);
		hi += VGAP;
		txt = tmpDicom.getString(Tag.PatientName);
		if (txt == null)
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.name") + " : " + txt, 10, hi);
		hi += VGAP;
		txt = tmpDicom.getString(Tag.PatientSex);
		if (txt == null)
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.sex") + " : " + txt, 10, hi);
		hi += VGAP;
		txt = tmpDicom.getString(Tag.PatientAge);
		if (txt == null)
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.patient.age") + " : " + txt, 10, hi);

		if (ohPatient.getPhoto() != null) {
			hi += VGAP;
			BufferedImage bi = new BufferedImage(ohPatient.getPhoto().getWidth(this), ohPatient.getPhoto().getHeight(this), BufferedImage.TYPE_INT_ARGB);
			bi.getGraphics().drawImage(ohPatient.getPhoto(), 0, 0, this);
			drawQuadrant(bi.getGraphics(), ohPatient.getPhoto().getHeight(this), ohPatient.getPhoto().getWidth(this), Color.WHITE);
			canvas.drawImage(Scalr.resize(bi, 100), 10, hi, this);
		}

		// canvas.drawImage(bi, 10, hi,this);
		canvas.setColor(orig);
	}

	private void drawInfoFrameBottomLeft(Graphics2D canvas, int w, int h) {

		Color orig = canvas.getColor();
		int hi = h - 20;
		canvas.setColor(colScr);
		String txt = jSliderZoom.getValue() + " %";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.zoom") + " : " + txt, 10, hi);
		hi -= VGAP;
		txt = "[" + (frameIndex + 1) + "]/" + frames.length;
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.frames") + " : " + txt, 10, hi);
		canvas.setColor(orig);
	}

	private void drawStudyUpRight(Graphics2D canvas, int w, int h) {

		Color orig = canvas.getColor();
		String txt = "";
		canvas.setColor(colScr);
		int hi = 10;
		int ws = w - 200;
		txt = tmpDicom.getString(Tag.InstitutionName);
		if (txt == null)
			txt = "";
		canvas.drawString(txt, ws, hi);
		hi += VGAP;
		txt = tmpDicom.getString(Tag.StudyID);
		if (txt == null)
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.studyid") + " : " + txt, ws, hi);
		txt = tmpDicom.getString(Tag.StudyDescription);
		hi += VGAP;
		if (txt == null)
			txt = "";
		canvas.drawString(txt, ws, hi);
		hi += VGAP;
		txt = "";
		Date d = tmpDicom.getDate(Tag.StudyDate);
		DateFormat df = DateFormat.getDateInstance();
		if (d != null)
			txt = df.format(d);
		else
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.common.date") + " : " + txt, ws, hi);
		canvas.setColor(orig);
	}

	private void drawSerieBottomRight(Graphics2D canvas, int w, int h) {
		Color orig = canvas.getColor();
		int ws = w - 200;
		int hi = h - 20;
		canvas.setColor(colScr);
		String txt = "";
		txt = tmpDicom.getString(Tag.SeriesDescription);
		if (txt == null)
			txt = "";
		canvas.drawString(txt, ws, hi);
		hi -= VGAP;
		txt = tmpDicom.getString(Tag.SeriesNumber);
		if (txt == null)
			txt = "";
		canvas.drawString(MessageBundle.getMessage("angal.dicom.image.serie.n") + " " + txt, ws, hi);
		canvas.setColor(orig);
	}

	/**
	 * Load actual frame from storage
	 * 
	 * @return
	 */
	private void refreshFrame() {
		Long id = frames[frameIndex];
		try {
			tmpDbFile = DicomManagerFactory.getManager().loadDettaglio(id, patID, serieNumber);
			getImageFromDicom(tmpDbFile);
		}catch(OHServiceException ex){
			if(ex.getMessages() != null){
				for(OHExceptionMessage msg : ex.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
	}

	/**
	 * get the BufferedImage from DICOM object
	 * 
	 * @param dett
	 * @return
	 */
	private void getImageFromDicom(FileDicom dett) {
		try {
			tmpImg = null;
			Iterator<?> iter = ImageIO.getImageReadersByFormatName("DICOM");
			ImageReader reader = (ImageReader) iter.next();
			DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(dett.getDicomData().getBinaryStream());
			reader.setInput(imageInputStream, false);

			try {
				tmpImg = reader.read(0, param);
				// System.out.println("Letta immagine dim : "+tmpImg.getHeight()+" "+tmpImg.getWidth());

			} catch (DicomCodingException dce) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.dicom.load.err") + " : " + dett.getFileName(), MessageBundle.getMessage("angal.dicom.err"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			imageInputStream.close();
			DicomStreamMetaData dsmd = (DicomStreamMetaData) reader.getStreamMetadata();
			this.tmpDicom = dsmd.getDicomObject();

		} catch (Exception ecc) {
			ecc.printStackTrace();
		}
	}

	private void refreshPan() {

		jPanelCenter.removeAll();
		jPanelCenter.add(composeCenter(jPanelCenter.getWidth(), jPanelCenter.getHeight(), false));
		validate();
	}

	private void refreshZoom() {

		jPanelCenter.removeAll();
		jPanelCenter.add(composeCenter(jPanelCenter.getWidth(), jPanelCenter.getHeight(), true));
		validate();
	}

	/**
	 * Set image in frame viewer
	 * 
	 * @param frame
	 *            , the frame to viusualize
	 */
	private void setFrame(int frame) {
		frameIndex = frame;
		refreshFrame();

		// centro.getViewport().removeAll();
		// centro.getViewport().add(composeCenter(this.getWidth(),this.getHeight()));

		resetMouseRelativePosition();
		jPanelCenter.removeAll();
		jPanelCenter.add(composeCenter(jPanelCenter.getWidth(), jPanelCenter.getHeight(), false));
		validate();
	}

	class ZoomListener implements ChangeListener {

		public ZoomListener() {

		}

		public void stateChanged(ChangeEvent e) {
			JSlider s1 = (JSlider) e.getSource();
			if (s1.getValueIsAdjusting()) {
				refreshZoom();
			}
		}
	}

	class FrameListener implements ChangeListener {

		public FrameListener() {

		}

		public void stateChanged(ChangeEvent e) {
			JSlider s1 = (JSlider) e.getSource();
			if (s1.getValueIsAdjusting()) {
				setFrame(s1.getValue());
			}

		}
	}

	private void resetMouseRelativePosition() {
		p1x = 0;
		p2x = 0;
		p1y = 0;
		p2y = 0;
	}

	/**
	 * relative X in mouse motion
	 */
	int p1x = 0;
	int p2x = 0;

	/**
	 * relative Y in mouse motion
	 */
	int p1y = 0;
	int p2y = 0;

	/**
	 * Mouse motion listener for DicomViewGui
	 */
	class DicomViewGuiMouseMotionListener implements MouseMotionListener {

		/**
		 * mouse dragged, if is also pressed a button calculate the displacement
		 * of position with point of initial position
		 */
		public void mouseDragged(MouseEvent e) {
			p2x = e.getXOnScreen();
			p2y = e.getYOnScreen();
			refreshPan();
		}

		/**
		 * mouse moved, NOT USED
		 */
		public void mouseMoved(MouseEvent e) {
		}

	}

	/**
	 * Mouse listener for DicomViewGui
	 */
	class DicomViewGuiMouseListener implements MouseListener {

		/**
		 * Mouse pressed, enable mouse motion and set relative X,Y with the
		 * click position
		 */
		public void mousePressed(MouseEvent e) {
			// System.out.println("mp["+e.getXOnScreen()+","+e.getYOnScreen()+"]");
			p1x = e.getXOnScreen();
			p1y = e.getYOnScreen();
		}

		/**
		 * Mouse released, disable mouse motion
		 */
		public void mouseReleased(MouseEvent e) {

			x = totX;

			y = totY;

		}

		/**
		 * mouse clicked in frame, NOT USED
		 */
		public void mouseClicked(MouseEvent e) {
		}

		/**
		 * mouse entred in frame, NOT USED
		 */
		public void mouseEntered(MouseEvent e) {
		}

		/**
		 * mouse exited of frame, NOT USED
		 */
		public void mouseExited(MouseEvent e) {
		}
	}

}
