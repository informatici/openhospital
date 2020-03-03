/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;

/**
 * @author Mwithi
 * 
 * JTimeTable is a JPanel showing 24 hours and
 * 5 range of minutes.
 * 
 * It helps to select a time through a mouse
 * listner and getters methods.
 * 
 */
public class JTimeTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hour;
	private int minute;

	private JLabel[] hours = new JLabel[24];
	private JLabel[] minutes = new JLabel[6];

	private JLabel hourLabel;
	private JLabel minuteLabel;
	
	private final int DIMENSION = 25;

	private JLabel selectedMinute;
	private JLabel selectedHour;

	public JTimeTable() {

		hourLabel = new JLabel(MessageBundle.getMessage("angal.common.hours"));
		hourLabel.setAlignmentX(CENTER_ALIGNMENT);

		JPanel hour = new JPanel();
		hour.setLayout(new GridLayout(3, 8));
		for (int i = 0; i < 24; i++) {
			hours[i] = new JLabel();
			hours[i].setText(String.valueOf(i));
			hours[i].setPreferredSize(new Dimension(DIMENSION, DIMENSION));
			hours[i].setHorizontalAlignment(JLabel.CENTER);
			hours[i].setBackground(Color.WHITE);
			hours[i].setOpaque(true);
			hours[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			hours[i].addMouseListener(new HourListener());
			hour.add(hours[i]);
		}

		minuteLabel = new JLabel(MessageBundle.getMessage("angal.common.minutes"));
		minuteLabel.setAlignmentX(CENTER_ALIGNMENT);

		JPanel minute = new JPanel();
		minute.setLayout(new GridLayout(0, 8));
		minute.add(Box.createHorizontalGlue());
		for (int i = 0; i < 60; i += 10) {
			minutes[i / 10] = new JLabel();
			minutes[i / 10].setText(String.valueOf(i));
			minutes[i / 10].setPreferredSize(new Dimension(DIMENSION, DIMENSION));
			minutes[i / 10].setHorizontalAlignment(JLabel.CENTER);
			minutes[i / 10].setBackground(Color.WHITE);
			minutes[i / 10].setOpaque(true);
			minutes[i / 10].setBorder(BorderFactory
					.createLineBorder(Color.BLACK));
			minutes[i / 10].addMouseListener(new MinuteListener());
			minute.add(minutes[i / 10]);
		}
		minute.add(Box.createHorizontalGlue());

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		this.add(hourLabel);
		this.add(hour);
		this.add(minuteLabel);
		this.add(minute);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.add(new JTimeTable());
		frame.setVisible(true);
		frame.pack();
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	private class HourListener implements MouseListener {

		public void mouseClicked(MouseEvent evt) {
			JLabel thisLabel = (JLabel) evt.getSource();
			String previousValue;
			if (selectedHour == null) {
				previousValue = "";
			} else {
				previousValue = selectedHour.getText();
				selectedHour.setBackground(Color.white);
			}
			firePropertyChange("hour", previousValue, thisLabel.getText());
			selectedHour = thisLabel;
			setHour(Integer.parseInt(thisLabel.getText()));
			thisLabel.setBackground(Color.GRAY);
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	private class MinuteListener implements MouseListener {

		public void mouseClicked(MouseEvent evt) {
			JLabel thisLabel = (JLabel) evt.getSource();
			String previousValue;
			if (selectedMinute == null) {
				previousValue = "";
			} else {
				previousValue = selectedHour.getText();
				selectedMinute.setBackground(Color.white);
			}
			firePropertyChange("minute", previousValue, thisLabel.getText());
			selectedMinute = thisLabel;
			setMinute(Integer.parseInt(thisLabel.getText()));
			thisLabel.setBackground(Color.GRAY);
			
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
