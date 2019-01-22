package org.isf.dicom.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.isf.dicom.manager.AbstractThumbnailViewGui;
import org.isf.dicom.manager.DicomManagerFactory;
import org.isf.dicom.model.FileDicom;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;

/**
 * Component for DICOM Thumnails composizion and visualizzation
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 * 
 */
public class ThumbnailViewGui extends AbstractThumbnailViewGui {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int patID = -1;
	private DicomGui dicomViewer = null;
	private DicomThumbsModel dicomThumbsModel;
	boolean thumbnailViewEnabled = true;

	/**
	 * Initialize Component
	 * 
	 * @param patID
	 */
	public ThumbnailViewGui(int patID, DicomGui owner) {
		super();
		this.dicomViewer = owner;
		this.patID = patID;

		dicomThumbsModel = new DicomThumbsModel();
		setModel(dicomThumbsModel);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBackground(Color.DARK_GRAY);
		setCellRenderer(new ImageListCellRender());
		setLayoutOrientation(JList.HORIZONTAL_WRAP);

		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				if (thumbnailViewEnabled && e.getValueIsAdjusting() == false) {
					DefaultListSelectionModel sel = (DefaultListSelectionModel) e.getSource();

					if (sel.isSelectionEmpty())
						disableDeleteButton();
					else
						enableDeleteButton((FileDicom) getModel().getElementAt(sel.getLeadSelectionIndex()));

				}
			}
		});
		addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if (thumbnailViewEnabled && 2 == e.getClickCount()) {
					// double click
					detail();
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
	}

	public void initialize() {
		loadDicomFromDB();
		dicomViewer.enableLoadButton();
		thumbnailViewEnabled = true;
		dicomViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void disableLoadButton() {
		thumbnailViewEnabled = false;
		dicomViewer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dicomViewer.disableLoadButton();
	}

	private void disableDeleteButton() {
		dicomViewer.disableDeleteButton();
	}

	private void enableDeleteButton(FileDicom selectedDicom) {
		dicomViewer.enableDeleteButton(selectedDicom);
	}

	private void detail() {
		dicomViewer.detail();
	}

	private void loadDicomFromDB() {
		FileDicom[] fdb = null;
		try {
			fdb = DicomManagerFactory.getManager().loadFilesPaziente(patID);
		}catch(OHServiceException ex){
			if(ex.getMessages() != null){
				for(OHExceptionMessage msg : ex.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		if (fdb == null)
			fdb = new FileDicom[0];

		dicomThumbsModel.clear();

		for (int i = 0; i < fdb.length; i++)
			dicomThumbsModel.addInstance(fdb[i]);
		;

	}

	public static class DicomThumbsModel extends AbstractListModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private LinkedList<FileDicom> thumbnailList;

		public DicomThumbsModel() {

			thumbnailList = new LinkedList<FileDicom>();

		}

		public Object getElementAt(int index) {
			if (index < 0) {
				return null;
			} else {
				return thumbnailList.get(index);
			}
		}

		public int getSize() {
			return thumbnailList.size();
		}

		public void addInstance(FileDicom instance) {
			thumbnailList.addLast(instance);
			int size = thumbnailList.size();
			fireIntervalAdded(this, size, size);
		}

		public void clear() {
			int size = thumbnailList.size();
			if (size > 0) {
				thumbnailList.clear();
				fireIntervalRemoved(this, 0, size - 1);
			}
		}

	}

	private class ImageListCellRender implements ListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			JPanel p = new JPanel(new BorderLayout(), true);

			p.setBounds(0, 0, 120, 130);

			p.setBackground(Color.DARK_GRAY);

			FileDicom instance = (FileDicom) value;

			BufferedImage immagine = instance.getDicomThumbnailAsImage();

			JLabel jLab = new JLabel(new ImageIcon(immagine));

			// System.out.println("instance.getDicomThumbnailAsImage() "+instance.getDicomThumbnailAsImage());

			Dimension dim = new Dimension(100, 110);

			jLab.setPreferredSize(dim);

			jLab.setMaximumSize(dim);

			jLab.setOpaque(true);

			jLab.setVerticalTextPosition(SwingConstants.BOTTOM);

			jLab.setHorizontalTextPosition(SwingConstants.CENTER);

			jLab.setBackground(Color.DARK_GRAY);

			p.setToolTipText(getTooltipText(instance));

			p.add(jLab, BorderLayout.CENTER);

			// Footer of thumnail

			JLabel frames = new JLabel("[1/" + instance.getFrameCount() + "]");

			frames.setForeground(Color.YELLOW);

			p.add(frames, BorderLayout.SOUTH);

			// Header of thumnail
			JLabel top = new JLabel(instance.getDicomSeriesDescription());

			top.setForeground(Color.LIGHT_GRAY);

			p.add(top, BorderLayout.NORTH);

			// Colors of thumnail
			if (isSelected) {
				p.setBackground(Color.BLUE);
				p.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
				p.setForeground(Color.WHITE);
			} else {
				p.setBackground(Color.DARK_GRAY);
				p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				p.setForeground(Color.LIGHT_GRAY);
			}

			return p;

		}
	}

	public FileDicom getSelectedInstance() {
		DicomThumbsModel dicomThumbsModel = (DicomThumbsModel) getModel();
		FileDicom selected = (FileDicom) dicomThumbsModel.getElementAt(getSelectionModel().getMinSelectionIndex());
		return selected;
	}

	public DicomThumbsModel getDicomThumbsModel() {
		return dicomThumbsModel;
	}

	private String getTooltipText(FileDicom dicomFile) {
		String separator = ": ";
		String newline = " <br>";
		StringBuilder rv = new StringBuilder("<html>");
		rv.append(MessageBundle.getMessage("angal.dicom.thumbnail.patient")).append(separator).append(dicomFile.getDicomPatientName());
		if (isValorized(dicomFile.getDicomPatientAge()))
			rv.append("[").append(MessageBundle.getMessage("angal.dicom.thumbnail.age")).append(separator).append(sanitize(dicomFile.getDicomPatientAge())).append("]");
		rv.append(newline);
		rv.append(MessageBundle.getMessage("angal.dicom.thumbnail.modality")).append(separator).append(sanitize(dicomFile.getModality()));
		rv.append(" <br>");
		rv.append(MessageBundle.getMessage("angal.dicom.thumbnail.sernum")).append(separator).append(sanitize(dicomFile.getDicomSeriesNumber()));
		rv.append(" <br>");
		rv.append(MessageBundle.getMessage("angal.dicom.thumbnail.study")).append(separator).append(sanitize(dicomFile.getDicomStudyDescription()));
		rv.append(" <br>");
		rv.append(MessageBundle.getMessage("angal.dicom.thumbnail.series")).append(separator).append(sanitize(dicomFile.getDicomSeriesDescription()));
		rv.append(" <br>");
		rv.append(MessageBundle.getMessage("angal.common.date")).append(separator).append(sanitize(dicomFile.getDicomSeriesDate()));
		rv.append(" <br>");
		rv.append("</html>");
		return rv.toString();
	}

	private String sanitize(String val) {
		if (isValorized(val))
			return val;
		else
			return "";
	}

	private boolean isValorized(String val) {
		return (val != null && val.trim().length() > 0);
	}
}
