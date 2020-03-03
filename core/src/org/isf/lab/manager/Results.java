package org.isf.lab.manager;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import org.isf.generaldata.MessageBundle;

public class Results {

	public static ArrayList<JRadioButton> getResults(){
		ArrayList<JRadioButton> list=new ArrayList<JRadioButton>();
		list.add(new JRadioButton("P = " + MessageBundle.getMessage("angal.lab.positive")));
		list.add(new JRadioButton("N = " + MessageBundle.getMessage("angal.lab.negative") ));
		list.add(new JRadioButton("R = " + MessageBundle.getMessage("angal.lab.reactive")));
		list.add(new JRadioButton("NR = " + MessageBundle.getMessage("angal.lab.notreactive")));
		list.add(new JRadioButton("H = " + MessageBundle.getMessage("angal.lab.high")));
		list.add(new JRadioButton("L = " + MessageBundle.getMessage("angal.lab.low")));
		list.add(new JRadioButton("L5 = " + MessageBundle.getMessage("angal.lab.lessthan")+"%"));
		list.add(new JRadioButton("G5 = " + MessageBundle.getMessage("angal.lab.greaterthan")+"%"));
		list.get(0).setActionCommand("P");
		list.get(1).setActionCommand("N");
		list.get(2).setActionCommand("R");
		list.get(3).setActionCommand("NR");
		list.get(4).setActionCommand("H");
		list.get(5).setActionCommand("L");
		list.get(6).setActionCommand("L5");
		list.get(7).setActionCommand("G5");
		
		return list;
	}
	
	public static JComboBox getResults(JComboBox combo){
		combo.addItem("");
		combo.addItem("P = " + MessageBundle.getMessage("angal.lab.positive"));
		combo.addItem("N = " + MessageBundle.getMessage("angal.lab.negative"));
		combo.addItem("R = " + MessageBundle.getMessage("angal.lab.reactive"));
		combo.addItem("NR = " + MessageBundle.getMessage("angal.lab.notreactive"));
		combo.addItem("H = " + MessageBundle.getMessage("angal.lab.high"));
		combo.addItem("L = " + MessageBundle.getMessage("angal.lab.low"));
		combo.addItem("L5 = " + MessageBundle.getMessage("angal.lab.lessthan")+"%");
		combo.addItem("G5 = " + MessageBundle.getMessage("angal.lab.greaterthan")+"%");
		return combo;
	}

}
