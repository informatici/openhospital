/*
 * OpenHospital
 * http://openhospital.sourceforge.net/
 *
 * $Id: BusyState.java,v 1.1 2014/11/16 11:32:09 eppesuig Exp $ $Date: 2014/11/16 11:32:09 $ $Revision: 1.1 $
 */
package org.isf.utils.jobjects;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;

/**
 * Offer a method that enable or disable a component and all its children.
 * The cursor is changed to <code>WAIT_CURSOR</code> when disabling widgets, to
 * <code>DEFAULT_CURSOR</code> when enabling them.
 * 
 * The method is not recursive, so <i>grandchildren</i> aren't processed: it only
 * works on a component and its first level children.
 *
 * @author Giuseppe Sacco
 * @deprecated
 */
public class BusyState {
	static public void setBusyState(Container panel, boolean busy) {
		/*panel.setCursor(new Cursor( busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
		for (Component comp : panel.getComponents()) {
			comp.setEnabled(!busy);
		}*/
	}
}
