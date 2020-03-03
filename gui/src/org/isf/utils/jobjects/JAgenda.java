/*
 *  JDayChooser.java  - A bean for choosing a day
 *  Copyright (C) 2004 Kai Toedter
 *  kai@toedter.com
 *  www.toedter.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


/**
 * JAgenda
 * 
 * @author Mwithi 
 * 
 * It's a substantial modification of JCalendar(r) by Kai Toedter
 * 
 * it allow to create a BIG JCalendar with an AgendaDayObject for
 * each day.
 * 
 */
public class JAgenda extends JPanel implements ActionListener, KeyListener,
		FocusListener {
	
	private static final long serialVersionUID = 5876398337018781820L;

	private JDialog owner;
	
	protected AgendaDayObject[] days;
	
	protected JButton[] weeks;

	protected AgendaDayObject selectedDay;

	protected JPanel weekPanel;

	protected JPanel dayPanel;

	protected int day;

	protected Color oldDayBackgroundColor;

	protected Color selectedColor;
	
	protected Color daySundayForeground;
	
	protected Color dayForegroundColor;

	protected Color sundayForeground;

	protected Color weekdayForeground;

	protected Color decorationBackgroundColor;
	
	protected Color therapyColor;
	
	protected String[] dayNames;

	protected Calendar calendar;

	protected Calendar today;

	protected Locale locale;

	protected boolean initialized;

	protected boolean weekOfYearVisible;

	protected boolean decorationBackgroundVisible = true;

	protected boolean decorationBordersVisible;

	protected boolean dayBordersVisible;

	private boolean alwaysFireDayProperty;

	protected Date minSelectableDate;

	protected Date maxSelectableDate;

	protected Date defaultMinSelectableDate;

	protected Date defaultMaxSelectableDate;

	protected int maxDayCharacters;
	
	protected Dimension initCellSize = new Dimension(50,50);

	/**
	 * Default JDayChooser constructor.
	 */
	public JAgenda(JDialog owner) {
		this(false);
		this.owner = owner;
	}

	/**
	 * JDayChooser constructor.
	 * 
	 * @param weekOfYearVisible
	 *            true, if the weeks of a year shall be shown
	 */
	public JAgenda(boolean weekOfYearVisible) {
		setName("JDayChooser");
		setBackground(Color.blue);
		this.weekOfYearVisible = weekOfYearVisible;
		locale = Locale.getDefault();
		//ALEX
		days = new AgendaDayObject[49]; 
		//ALEX
		selectedDay = null;
		calendar = Calendar.getInstance(locale);
		today = (Calendar) calendar.clone();

		setLayout(new BorderLayout());

		dayPanel = new JPanel();
		//dayPanel.setLayout(new GridLayout(6, 7));
		dayPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		daySundayForeground = new Color(164, 0, 0, 48);
		dayForegroundColor = new Color(0, 0 , 0, 48);
		sundayForeground = new Color(164, 0, 0);
		weekdayForeground = new Color(0, 90, 164);

		// decorationBackgroundColor = new Color(194, 211, 252);
		// decorationBackgroundColor = new Color(206, 219, 246);
		decorationBackgroundColor = new Color(210, 228, 238);
		int index = 0;
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				if (y == 0) {
					// Create a button that doesn't react on clicks or focus
					// changes.
					// Thanks to Thomas Schaefer for the focus hint :)
					days[index] = new AgendaDayObject(null);
					c.fill = GridBagConstraints.HORIZONTAL;
					c.weightx = 1;
					//c.weighty = 0;
				} else {
					days[index] = new AgendaDayObject(index);
					//days[index].getList().addMouseListener(new MyMouseListener());
					c.fill = GridBagConstraints.BOTH;
					c.weightx = 1;
					c.weighty = 1;
				}
				c.gridx = x;
				c.gridy = y;
				dayPanel.add(days[index], c);
				index++;
			}
		}

		weekPanel = new JPanel();
		weekPanel.setLayout(new GridLayout(7, 1));
		weeks = new JButton[7];

		for (int i = 0; i < 7; i++) {
			weeks[i] = new DecoratorButton();
			weeks[i].setMargin(new Insets(0, 0, 0, 0));
			weeks[i].setFocusPainted(false);
			weeks[i].setForeground(new Color(100, 100, 100));

			if (i != 0) {
				weeks[i].setText("0" + (i + 1));
			}

			weekPanel.add(weeks[i]);
		}

		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.set(1, 0, 1, 1, 1);
		defaultMinSelectableDate = tmpCalendar.getTime();
		minSelectableDate = defaultMinSelectableDate;
		tmpCalendar.set(9999, 0, 1, 1, 1);
		defaultMaxSelectableDate = tmpCalendar.getTime();
		maxSelectableDate = defaultMaxSelectableDate;

		init();

		setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		add(dayPanel, BorderLayout.CENTER);
		
		if (weekOfYearVisible) {
			add(weekPanel, BorderLayout.WEST);
		}
		initialized = true;
		updateUI();
		
	}

	/**
	 * Initilizes the locale specific names for the days of the week.
	 */
	protected void init() {
		JButton testButton = new JButton();
		oldDayBackgroundColor = testButton.getBackground();
		selectedColor = new Color(160, 160, 160);

		Date date = calendar.getTime();
		calendar = Calendar.getInstance(locale);
		calendar.setTime(date);

		drawDayNames();
		drawDays();
	}

	/**
	 * Draws the day names of the day columnes.
	 */
	private void drawDayNames() {
		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		dayNames = dateFormatSymbols.getShortWeekdays();

		int day = firstDayOfWeek;

		for (int i = 0; i < 7; i++) {
			if (maxDayCharacters > 0 && maxDayCharacters < 5) {
				if (dayNames[day].length() >= maxDayCharacters) {
					dayNames[day] = dayNames[day]
							.substring(0, maxDayCharacters);
				}
			}

			days[i].getLabel().setText(dayNames[day]);

			if (day == 1) {
				days[i].getLabel().setForeground(sundayForeground);
			} else {
				days[i].getLabel().setForeground(weekdayForeground);
			}

			if (day < 7) {
				day++;
			} else {
				day -= 6;
			}
		}
	}

	/**
	 * Initializes both day names and weeks of the year.
	 */
	protected void initDecorations() {
		for (int x = 0; x < 7; x++) {
			//days[x].setContentAreaFilled(decorationBackgroundVisible);
			//days[x].setBorderPainted(decorationBordersVisible);
			days[x].invalidate();
			days[x].repaint();
			weeks[x].setContentAreaFilled(decorationBackgroundVisible);
			weeks[x].setBorderPainted(decorationBordersVisible);
			weeks[x].invalidate();
			weeks[x].repaint();
		}
	}

	/**
	 * Hides and shows the week buttons.
	 */
	protected void drawWeeks() {
		Calendar tmpCalendar = (Calendar) calendar.clone();

		for (int i = 1; i < 7; i++) {
			tmpCalendar.set(Calendar.DAY_OF_MONTH, (i * 7) - 6);

			int week = tmpCalendar.get(Calendar.WEEK_OF_YEAR);
			String buttonText = Integer.toString(week);

			if (week < 10) {
				buttonText = "0" + buttonText;
			}

			weeks[i].setText(buttonText);

			if ((i == 5) || (i == 6)) {
				weeks[i].setVisible(days[i * 6].isVisible());
			}
		}
	}

	/**
	 * Hides and shows the day buttons.
	 */
	protected void drawDays() {
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalendar.set(Calendar.MINUTE, 0);
		tmpCalendar.set(Calendar.SECOND, 0);
		tmpCalendar.set(Calendar.MILLISECOND, 0);

		Calendar minCal = Calendar.getInstance();
		minCal.setTime(minSelectableDate);
		minCal.set(Calendar.HOUR_OF_DAY, 0);
		minCal.set(Calendar.MINUTE, 0);
		minCal.set(Calendar.SECOND, 0);
		minCal.set(Calendar.MILLISECOND, 0);

		Calendar maxCal = Calendar.getInstance();
		maxCal.setTime(maxSelectableDate);
		maxCal.set(Calendar.HOUR_OF_DAY, 0);
		maxCal.set(Calendar.MINUTE, 0);
		maxCal.set(Calendar.SECOND, 0);
		maxCal.set(Calendar.MILLISECOND, 0);

		int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;

		if (firstDay < 0) {
			firstDay += 7;
		}

		int i;

		for (i = 0; i < firstDay; i++) {
			days[i + 7].setVisible(false);
			days[i + 7].getLabel().setText("");
		}

		tmpCalendar.add(Calendar.MONTH, 1);

		Date firstDayInNextMonth = tmpCalendar.getTime();
		tmpCalendar.add(Calendar.MONTH, -1);

		Date day = tmpCalendar.getTime();
		int n = 0;
		//Color foregroundColor = getForeground();
		
		while (day.before(firstDayInNextMonth)) {
			days[i + n + 7].getLabel().setText(Integer.toString(n + 1));
			days[i + n + 7].setVisible(true);

			if ((tmpCalendar.get(Calendar.DAY_OF_YEAR) == today
					.get(Calendar.DAY_OF_YEAR))
					&& (tmpCalendar.get(Calendar.YEAR) == today
							.get(Calendar.YEAR))) {
				days[i + n + 7].getLabel().setForeground(daySundayForeground);
			} else {
				days[i + n + 7].getLabel().setForeground(dayForegroundColor);
			}

			if ((n + 1) == this.day) {
				days[i + n + 7].getLabel().setBackground(selectedColor);
				selectedDay = days[i + n + 7];
			} else {
				days[i + n + 7].getLabel().setBackground(oldDayBackgroundColor);
			}

			if (tmpCalendar.before(minCal) || tmpCalendar.after(maxCal)) {
				days[i + n + 7].setEnabled(false);
			} else {
				days[i + n + 7].setEnabled(true);
			}

			n++;
			tmpCalendar.add(Calendar.DATE, 1);
			day = tmpCalendar.getTime();
		}

		for (int k = n + i + 7; k < 49; k++) {
			days[k].setVisible(false);
			days[k].getLabel().setText("");
		}

		drawWeeks();
	}

	/**
	 * Returns the locale.
	 * 
	 * @return the locale value
	 * 
	 * @see #setLocale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 * 
	 * @param locale
	 *            the new locale value
	 * 
	 * @see #getLocale
	 */
	public void setLocale(Locale locale) {
		if (!initialized) {
			super.setLocale(locale);
		} else {
			this.locale = locale;
			super.setLocale(locale);
			init();
		}
	}

	/**
	 * Sets the day. This is a bound property.
	 * 
	 * @param d
	 *            the day
	 * 
	 * @see #getDay
	 */
	public void setDay(int d) {
		if (d < 1) {
			d = 1;
		}
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		tmpCalendar.add(Calendar.MONTH, 1);
		tmpCalendar.add(Calendar.DATE, -1);

		int maxDaysInMonth = tmpCalendar.get(Calendar.DATE);

		if (d > maxDaysInMonth) {
			d = maxDaysInMonth;
		}

		int oldDay = day;
		day = d;

		if (selectedDay != null) {
			selectedDay.setBackground(oldDayBackgroundColor);
			selectedDay.repaint();
		}

//		for (int i = 7; i < 49; i++) {
//			if (days[i].getText().equals(Integer.toString(day))) {
//				selectedDay = days[i];
//				selectedDay.setBackground(selectedColor);
//				break;
//			}
//		}
		
		
		if (alwaysFireDayProperty) {
			firePropertyChange("day", 0, day);
		} else {
			firePropertyChange("day", oldDay, day);
		}
	}

	/**
	 * this is needed for JDateChooser.
	 * 
	 * @param alwaysFire
	 *            true, if day property shall be fired every time a day is
	 *            chosen.
	 */
	public void setAlwaysFireDayProperty(boolean alwaysFire) {
		alwaysFireDayProperty = alwaysFire;
	}

	/**
	 * Returns the selected day.
	 * 
	 * @return the day value
	 * 
	 * @see #setDay
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Sets a specific month. This is needed for correct graphical
	 * representation of the days.
	 * 
	 * @param month
	 *            the new month
	 */
	public void setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int adjustedDay = day;
		if (day > maxDays) {
			adjustedDay = maxDays;
			setDay(adjustedDay);
		}

		drawDays();
	}

	/**
	 * Sets a specific year. This is needed for correct graphical representation
	 * of the days.
	 * 
	 * @param year
	 *            the new year
	 */
	public void setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		drawDays();
	}

	/**
	 * Sets a specific calendar. This is needed for correct graphical
	 * representation of the days.
	 * 
	 * @param calendar
	 *            the new calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		drawDays();
	}

	/**
	 * Sets the font property.
	 * 
	 * @param font
	 *            the new font
	 */
	public void setFont(Font font) {
		if (days != null) {
			for (int i = 0; i < 49; i++) {
				days[i].setFont(font);
			}
		}
		if (weeks != null) {
			for (int i = 0; i < 7; i++) {
				weeks[i].setFont(font);
			}
		}
	}

	/**
	 * Sets the foregroundColor color.
	 * 
	 * @param foreground
	 *            the new foregroundColor
	 */
	public void setForeground(Color foreground) {
		super.setForeground(foreground);

		if (days != null) {
			for (int i = 7; i < 49; i++) {
				days[i].setForeground(foreground);
			}

			drawDays();
		}
	}

	/**
	 * JDayChooser is the ActionListener for all day buttons.
	 * 
	 * @param e
	 *            the ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		int day = new Integer(buttonText).intValue();
		setDay(day);
	}

	/**
	 * JDayChooser is the FocusListener for all day buttons. (Added by Thomas
	 * Schaefer)
	 * 
	 * @param e
	 *            the FocusEvent
	 */
	/*
	 * Code below commented out by Mark Brown on 24 Aug 2004. This code breaks
	 * the JDateChooser code by triggering the actionPerformed method on the
	 * next day button. This causes the date chosen to always be incremented by
	 * one day.
	 */
	public void focusGained(FocusEvent e) {
		// JButton button = (JButton) e.getSource();
		// String buttonText = button.getText();
		//
		// if ((buttonText != null) && !buttonText.equals("") &&
		// !e.isTemporary()) {
		// actionPerformed(new ActionEvent(e.getSource(), 0, null));
		// }
	}

	/**
	 * Does nothing.
	 * 
	 * @param e
	 *            the FocusEvent
	 */
	public void focusLost(FocusEvent e) {
	}

	/**
	 * JDayChooser is the KeyListener for all day buttons. (Added by Thomas
	 * Schaefer and modified by Austin Moore)
	 * 
	 * @param e
	 *            the KeyEvent
	 */
	public void keyPressed(KeyEvent e) {
		int offset = (e.getKeyCode() == KeyEvent.VK_UP) ? (-7) : ((e
				.getKeyCode() == KeyEvent.VK_DOWN) ? (+7)
				: ((e.getKeyCode() == KeyEvent.VK_LEFT) ? (-1) : ((e
						.getKeyCode() == KeyEvent.VK_RIGHT) ? (+1) : 0)));

		int newDay = getDay() + offset;

		if ((newDay >= 1)
				&& (newDay <= calendar.getMaximum(Calendar.DAY_OF_MONTH))) {
			setDay(newDay);
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @param e
	 *            the KeyEvent
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Does nothing.
	 * 
	 * @param e
	 *            the KeyEvent
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Enable or disable the JDayChooser.
	 * 
	 * @param enabled
	 *            The new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		for (short i = 0; i < days.length; i++) {
			if (days[i] != null) {
				days[i].setEnabled(enabled);
			}
		}

		for (short i = 0; i < weeks.length; i++) {
			if (weeks[i] != null) {
				weeks[i].setEnabled(enabled);
			}
		}
	}

	/**
	 * In some Countries it is often usefull to know in which week of the year a
	 * date is.
	 * 
	 * @return boolean true, if the weeks of the year is shown
	 */
	public boolean isWeekOfYearVisible() {
		return weekOfYearVisible;
	}

	/**
	 * In some Countries it is often usefull to know in which week of the year a
	 * date is.
	 * 
	 * @param weekOfYearVisible
	 *            true, if the weeks of the year shall be shown
	 */
	public void setWeekOfYearVisible(boolean weekOfYearVisible) {
		if (weekOfYearVisible == this.weekOfYearVisible) {
			return;
		} else if (weekOfYearVisible) {
			add(weekPanel, BorderLayout.WEST);
		} else {
			remove(weekPanel);
		}

		this.weekOfYearVisible = weekOfYearVisible;
		validate();
		dayPanel.validate();
	}

	/**
	 * Returns the day panel.
	 * 
	 * @return the day panel
	 */
	public JPanel getDayPanel() {
		return dayPanel;
	}

	/**
	 * Returns the color of the decoration (day names and weeks).
	 * 
	 * @return the color of the decoration (day names and weeks).
	 */
	public Color getDecorationBackgroundColor() {
		return decorationBackgroundColor;
	}

	/**
	 * Sets the background of days and weeks of year buttons.
	 * 
	 * @param decorationBackgroundColor
	 *            The background to set
	 */
	public void setDecorationBackgroundColor(Color decorationBackgroundColor) {
		this.decorationBackgroundColor = decorationBackgroundColor;

		if (days != null) {
			for (int i = 0; i < 7; i++) {
				days[i].setBackground(decorationBackgroundColor);
			}
		}

		if (weeks != null) {
			for (int i = 0; i < 7; i++) {
				weeks[i].setBackground(decorationBackgroundColor);
			}
		}
	}

	/**
	 * Returns the Sunday foreground.
	 * 
	 * @return Color the Sunday foreground.
	 */
	public Color getSundayForeground() {
		return sundayForeground;
	}

	/**
	 * Returns the weekday foreground.
	 * 
	 * @return Color the weekday foreground.
	 */
	public Color getWeekdayForeground() {
		return weekdayForeground;
	}

	/**
	 * Sets the Sunday foreground.
	 * 
	 * @param sundayForeground
	 *            The sundayForeground to set
	 */
	public void setSundayForeground(Color sundayForeground) {
		this.sundayForeground = sundayForeground;
		drawDayNames();
		drawDays();
	}

	/**
	 * Sets the weekday foreground.
	 * 
	 * @param weekdayForeground
	 *            The weekdayForeground to set
	 */
	public void setWeekdayForeground(Color weekdayForeground) {
		this.weekdayForeground = weekdayForeground;
		drawDayNames();
		drawDays();
	}

	/**
	 * Requests that the selected day also have the focus.
	 */
	public void setFocus() {
		if (selectedDay != null) {
			this.selectedDay.requestFocus();
		}
	}

	/**
	 * The decoration background is the background color of the day titles and
	 * the weeks of the year.
	 * 
	 * @return Returns true, if the decoration background is painted.
	 */
	public boolean isDecorationBackgroundVisible() {
		return decorationBackgroundVisible;
	}

	/**
	 * The decoration background is the background color of the day titles and
	 * the weeks of the year.
	 * 
	 * @param decorationBackgroundVisible
	 *            true, if the decoration background shall be painted.
	 */
	public void setDecorationBackgroundVisible(
			boolean decorationBackgroundVisible) {
		this.decorationBackgroundVisible = decorationBackgroundVisible;
		initDecorations();
	}

	/**
	 * The decoration border is the button border of the day titles and the
	 * weeks of the year.
	 * 
	 * @return Returns true, if the decoration border is painted.
	 */
	public boolean isDecorationBordersVisible() {
		return decorationBordersVisible;
	}

	public boolean isDayBordersVisible() {
		return dayBordersVisible;
	}

	/**
	 * The decoration border is the button border of the day titles and the
	 * weeks of the year.
	 * 
	 * @param decorationBordersVisible
	 *            true, if the decoration border shall be painted.
	 */
	public void setDecorationBordersVisible(boolean decorationBordersVisible) {
		this.decorationBordersVisible = decorationBordersVisible;
		initDecorations();
	}

	public void setDayBordersVisible(boolean dayBordersVisible) {
		this.dayBordersVisible = dayBordersVisible;
		if (initialized) {
//			for (int x = 7; x < 49; x++) {
//				if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
//					//days[x].setContentAreaFilled(dayBordersVisible);
//					days[x].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//				} else {
//					//days[x].setContentAreaFilled(true);
//					days[x].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//				}
//				//days[x].setBorderPainted(dayBordersVisible);
//				days[x].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//			}
		}
	}

	/**
	 * Updates the UI and sets the day button preferences.
	 */
	public void updateUI() {
		super.updateUI();
		//setFont(Font.decode("Dialog Plain 11"));

		if (weekPanel != null) {
			weekPanel.updateUI();
		}
		if (initialized) {
			if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
				setDayBordersVisible(false);
				setDecorationBackgroundVisible(true);
				setDecorationBordersVisible(false);
			} else {
				setDayBordersVisible(true);
				setDecorationBackgroundVisible(decorationBackgroundVisible);
				setDecorationBordersVisible(decorationBordersVisible);
			}
		}
	}

	/**
	 * Sets a valid date range for selectable dates. If max is before min, the
	 * default range with no limitation is set.
	 * 
	 * @param min
	 *            the minimum selectable date or null (then the minimum date is
	 *            set to 01\01\0001)
	 * @param max
	 *            the maximum selectable date or null (then the maximum date is
	 *            set to 01\01\9999)
	 */
	public void setSelectableDateRange(Date min, Date max) {
		if (min == null) {
			minSelectableDate = defaultMinSelectableDate;
		} else {
			minSelectableDate = min;
		}
		if (max == null) {
			maxSelectableDate = defaultMaxSelectableDate;
		} else {
			maxSelectableDate = max;
		}
		if (maxSelectableDate.before(minSelectableDate)) {
			minSelectableDate = defaultMinSelectableDate;
			maxSelectableDate = defaultMaxSelectableDate;
		}
		drawDays();
	}

	/**
	 * Sets the maximum selectable date. If null, the date 01\01\9999 will be
	 * set instead.
	 * 
	 * @param max
	 *            the maximum selectable date
	 * 
	 * @return the maximum selectable date
	 */
	public Date setMaxSelectableDate(Date max) {
		if (max == null) {
			maxSelectableDate = defaultMaxSelectableDate;
		} else {
			maxSelectableDate = max;
		}
		drawDays();
		return maxSelectableDate;
	}

	/**
	 * Sets the minimum selectable date. If null, the date 01\01\0001 will be
	 * set instead.
	 * 
	 * @param min
	 *            the minimum selectable date
	 * 
	 * @return the minimum selectable date
	 */
	public Date setMinSelectableDate(Date min) {
		if (min == null) {
			minSelectableDate = defaultMinSelectableDate;
		} else {
			minSelectableDate = min;
		}
		drawDays();
		return minSelectableDate;
	}

	/**
	 * Gets the maximum selectable date.
	 * 
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return maxSelectableDate;
	}

	/**
	 * Gets the minimum selectable date.
	 * 
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return minSelectableDate;
	}

	/**
	 * Gets the maximum number of characters of a day name or 0. If 0 is
	 * returned, dateFormatSymbols.getShortWeekdays() will be used.
	 * 
	 * @return the maximum number of characters of a day name or 0.
	 */
	public int getMaxDayCharacters() {
		return maxDayCharacters;
	}

	/**
	 * Sets the maximum number of characters per day in the day bar. Valid
	 * values are 0-4. If set to 0, dateFormatSymbols.getShortWeekdays() will be
	 * used, otherwise theses strings will be reduced to the maximum number of
	 * characters.
	 * 
	 * @param maxDayCharacters
	 *            the maximum number of characters of a day name.
	 */
	public void setMaxDayCharacters(int maxDayCharacters) {
		if (maxDayCharacters == this.maxDayCharacters) {
			return;
		}

		if (maxDayCharacters < 0 || maxDayCharacters > 4) {
			this.maxDayCharacters = 0;
		} else {
			this.maxDayCharacters = maxDayCharacters;
		}
		drawDayNames();
		drawDays();
		invalidate();
	}

	/**
	 * Creates a JFrame with a JDayChooser inside and can be used for testing.
	 * 
	 * @param s
	 *            The command line arguments
	 */
	public static void main(String[] s) {
		JFrame frame = new JFrame("JDayChooser");
		frame.getContentPane().add(new JAgenda(null));
		frame.setSize(800,600);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		//frame.pack();
		frame.setVisible(true);
	}

	class DecoratorButton extends JButton {
		private static final long serialVersionUID = -5306477668406547496L;

		public DecoratorButton() {
			setBackground(decorationBackgroundColor);
			setContentAreaFilled(decorationBackgroundVisible);
			setBorderPainted(decorationBordersVisible);
		}

		public void addMouseListener(MouseListener l) {
		}

		public boolean isFocusable() {
			return false;
		}

		public void paint(Graphics g) {
			if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
				// this is a hack to get the background painted
				// when using Windows Look & Feel
				if (decorationBackgroundVisible) {
					g.setColor(decorationBackgroundColor);
				} else {
					g.setColor(days[7].getBackground());
				}
				g.fillRect(0, 0, getWidth(), getHeight());
				if (isBorderPainted()) {
					setContentAreaFilled(true);
				} else {
					setContentAreaFilled(false);
				}
			}
			super.paint(g);
		}
	};
	
	/**
	 * 
	 * @author Mwithi
	 * 
	 * AgendaDayObject is a JPanel showing a list
	 * and a label meant for a DAY_OF_MONTH number
	 * on background.
	 *
	 * A clickable icon with a magnifier allows to
	 * zoom in and out itself.
	 */
	public class AgendaDayObject extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int day;
		private JLabel label;
		private JList list;
		private JButton magnifier;
		private int magnifierSize = 16;
		
		public AgendaDayObject() {
		}
		
		public AgendaDayObject(Object obj) {
			this.setLayout(new GridLayout(1,1));
			label = new JLabel();
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			setBackground(decorationBackgroundColor);
			this.add(label);
		}

		public AgendaDayObject(int day) {

			this.day = day;
			this.setLayout(new GridLayout(1,1));
			
			
			//LABEL PANEL
			label = new JLabel();
			if (day != 0) label.setText(String.valueOf(day));
			label.setForeground(dayForegroundColor);
				
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setOpaque(false);
			
			//JLIST
			list = new JList();
			list.setModel(new DefaultListModel());
			list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			//JScrollPane scroll = new JScrollPane(list);
			
			//MAGNIFIER
			magnifier = new JButton();
			magnifier.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			magnifier.setOpaque(false);
			magnifier.setBorderPainted(false);
			magnifier.setFocusPainted(false);
			magnifier.setContentAreaFilled(false);
			magnifier.addActionListener(new MagnifierListener());
											
			//LAYER FOR OVERLAPPING
			FilledLayeredPane layers = new FilledLayeredPane();
			layers.add(label, JLayeredPane.PALETTE_LAYER);
			layers.add(list, JLayeredPane.DEFAULT_LAYER);
			layers.add(magnifier, JLayeredPane.POPUP_LAYER);
			
			this.add(layers);
				
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
			label.setText(String.valueOf(day));
		}

		public JList getList() {
			return list;
		}

		public void setList(JList list) {
			this.list = list;
		}
		
		public JLabel getLabel() {
			return label;
		}

		private class FilledLayeredPane extends JLayeredPane {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
		     * Layout each of the components in this JLayeredPane so that they all fill
		     * the entire extents of the layered pane -- from (0,0) to (getWidth(), getHeight())
		     */
		    public void doLayout() {
		        // Synchronizing on getTreeLock, because I see other layouts doing that.
		        // see BorderLayout::layoutContainer(Container)
		        synchronized(getTreeLock()) {
		            int w = getWidth();
		            int h = getHeight();
		            for(Component c : getComponents()) {
		            	c.setBounds(0, 0, w, h);
		            	if (c instanceof JLabel) {
		                	c.setFont(new Font("Serif", Font.PLAIN, (int) (h*0.8)));
		                }
		            	if (c instanceof JButton) {
		            		c.setBounds(w-magnifierSize-5, h-magnifierSize-5, magnifierSize, magnifierSize);
		            	}
		            	
		            }
		        }
		    }
		}
		
		@Override
		public void setVisible(boolean flag) {
			this.label.setVisible(flag);
			this.list.setVisible(flag);
			this.magnifier.setVisible(flag);
			this.setBorder(null);
		}
		
		public class MagnifierListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				
				JPanel thisDay = null;
				thisDay = AgendaDayObject.this.popUp();
				final JDialog dialog = new JDialog(owner);
				dialog.add(thisDay);
				dialog.setUndecorated(true);
				dialog.setSize(new Dimension(600,400));
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				dialog.setFocusable(true);
				dialog.addFocusListener(new FocusListener() {

					public void focusGained(FocusEvent e) {}

					public void focusLost(FocusEvent e) {
						dialog.setVisible(false);
					}
				});
			}
		}
		
		private JPanel popUp() {
			
			//A NEW PANEL
			JPanel copyPanel = new JPanel();
			copyPanel.setLayout(new GridLayout(1,1));
			JLabel label = new JLabel(this.label.getText());
			
			//LABEL
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setOpaque(false);
			label.setForeground(dayForegroundColor);
			
			//LIST WITH SCROLL
			ListModel model = this.list.getModel();
			JList list = new JList(model);
			list.setFixedCellHeight( 40 );
			list.setCellRenderer(new ListCellRenderer() {
				
				protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
				
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
				
					JLabel cell = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
					
					return cell;
				}
			});
			JScrollPane scroll = new JScrollPane(list);
			
			FilledLayeredPane layers = new FilledLayeredPane();
			layers.add(label, JLayeredPane.PALETTE_LAYER);
			layers.add(scroll, JLayeredPane.DEFAULT_LAYER);
			copyPanel.add(layers);
			
			return copyPanel;
		}
	}
	
	public void addElement(Object obj, int day) {

		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalendar.set(Calendar.MINUTE, 0);
		tmpCalendar.set(Calendar.SECOND, 0);
		tmpCalendar.set(Calendar.MILLISECOND, 0);
		
		int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		if (firstDay < 0) firstDay+=7;
		
		int realIndex = 6 + firstDay + day;
		
		DefaultListModel model = (DefaultListModel) days[realIndex].getList().getModel();
		model.addElement(obj);
	}
	
	public void removeElement(int index, int day) {
		
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalendar.set(Calendar.MINUTE, 0);
		tmpCalendar.set(Calendar.SECOND, 0);
		tmpCalendar.set(Calendar.MILLISECOND, 0);
		
		int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		
		int realIndex = 6 + firstDay + day;
		
		days[realIndex].getList().remove(index);
	}

	public void removeAll() {
		
		DefaultListModel model = new DefaultListModel();
		for (AgendaDayObject day : days) {
			if (day.getList() != null) {
				model = (DefaultListModel) day.getList().getModel();
				model.removeAllElements();
			}
		}
		
	}
	
}
