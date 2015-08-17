package org.isf.malnutrition.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.malnutrition.model.Malnutrition;
import org.isf.malnutrition.service.IoOperation;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Manager for malnutrition module.
 *
 */
public class MalnutritionManager {

	IoOperation ioOperation = new IoOperation();

	/**
	 * Retrieves all the {@link Malnutrition} associated to the given admission id.
	 * In case of wrong parameters an error message is shown and <code>null</code> value is returned.
	 * In case of error a message error is shown and an empty list is returned.
	 * @param aId the admission id to use as filter.
	 * @return all the retrieved malnutrition or <code>null</code> if the specified admission id is <code>null</code>.
	 */
	public ArrayList<Malnutrition> getMalnutrition(String aId){
		if(aId==""){
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.nonameselected"));
			return null;
		}
		try {
			return ioOperation.getMalnutritions(aId);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Malnutrition>();
		}
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 */
	public Malnutrition getLastMalnutrition(int patientID) {
		try {
			return ioOperation.getLastMalnutrition(patientID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * In case of wrong parameters an error message is shown and <code>false</code> value is returned.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored, <code>false</code> otherwise.
	 */
	public boolean newMalnutrition(Malnutrition malnutrition){

		if(malnutrition.getDateSupp()==null) {
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedatatypemustbeindateofcontrol"));
			return false;
		}

		if(malnutrition.getDateConf()==null) {
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedatatypemustbeindateofthenextcontrol"));
			return false;
		}

		if(malnutrition.getWeight()==0) {
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinweightfield"));
			return false;
		}

		if(malnutrition.getHeight()==0) {
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinheightfield"));
			return false;
		}

		GregorianCalendar gc = new GregorianCalendar();
		if((malnutrition.getDateSupp().get(GregorianCalendar.YEAR)>=gc.get(GregorianCalendar.YEAR))&&
				(malnutrition.getDateSupp().get(GregorianCalendar.MONTH)>=gc.get(GregorianCalendar.MONTH))&&
				(malnutrition.getDateSupp().get(GregorianCalendar.DAY_OF_MONTH)>gc.get(GregorianCalendar.DAY_OF_MONTH))){
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedateofthiscontrolwillbeafuturedate"));
			//System.out.println(malnutrition.getDateSupp().get(GregorianCalendar.YEAR));
			return false;
		}

		try {
			return ioOperation.newMalnutrition(malnutrition);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link Malnutrition}.
	 * In case of wrong parameters an error message is shown and <code>false</code> value is returned.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param malnutrition the malnutrition to update.
	 * @return <code>true</code> if the malnutrition has been updated, <code>false</code> otherwise.
	 */
	public boolean updateMalnutrition(Malnutrition malnutrition){
		if(malnutrition.getHeight()==0){
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinheightfield"));
			return false;
		}

		if(malnutrition.getWeight()==0){
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinweightfield"));
			return false;
		}
		try {
			int currentLock = ioOperation.getMalnutritionLock(malnutrition.getCode());
			if (currentLock>=0) {
				if (currentLock!=malnutrition.getLock()) {
					String msg = MessageBundle.getMessage("angal.malnutrition.thedatahasbeenupdatedbysomeoneelse") +
							MessageBundle.getMessage("angal.malnutrition.doyouwanttooverwritethedata");
					int response = JOptionPane.showConfirmDialog(null, msg, MessageBundle.getMessage("angal.malnutrition.select"), JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.OK_OPTION){
						return ioOperation.updateMalnutrition(malnutrition);
					}
				} else {
					return ioOperation.updateMalnutrition(malnutrition);
				}

			}else{				
				//the record was deleted since the last read
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.malnutrition.couldntfindthedataithasporbablybeendelete"));
			}
			return false;

		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Deletes the specified {@link Malnutrition}.
	 * In case of wrong parameters an error message is shown and <code>false</code> value is returned.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteMalnutrition(Malnutrition malnutrition){
		if(malnutrition==null){
			JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.common.pleaseselectarow"));
			return false;
		}

		try {
			return ioOperation.deleteMalnutrition(malnutrition);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
