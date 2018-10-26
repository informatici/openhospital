package org.isf.priceslist.manager;

import java.util.ArrayList;
import java.util.Collections;

import org.isf.menu.gui.Menu;
import org.isf.priceslist.model.List;
import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.priceslist.service.PricesListIoOperations;
import org.isf.serviceprinting.print.PriceForPrint;
import org.isf.utils.exception.OHServiceException;

public class PriceListManager {

	private PricesListIoOperations ioOperations = Menu.getApplicationContext().getBean(PricesListIoOperations.class);
	
	/**
	 * return the list of {@link List}s in the DB
	 * @return the list of {@link List}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PriceList> getLists() throws OHServiceException {
        return ioOperations.getLists();
	}
	
	/**
	 * return the list of {@link Price}s in the DB
	 * @return the list of {@link Price}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Price> getPrices() throws OHServiceException {
        return ioOperations.getPrices();
	}

	/**
	 * updates all {@link Price}s in the specified {@link List}
	 * @param list - the {@link List}
	 * @param prices - the list of {@link Price}s
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updatePrices(PriceList list, ArrayList<Price> prices) throws OHServiceException {
        return ioOperations.updatePrices(list, prices);
	}

	/**
	 * insert a new {@link List} in the DB
	 * 
	 * @param list - the {@link List}
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newList(PriceList list) throws OHServiceException {
        return ioOperations.newList(list);
	}

	/**
	 * update a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateList(PriceList updateList) throws OHServiceException {
        return ioOperations.updateList(updateList);
	}

	/**
	 * delete a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteList(PriceList deleteList) throws OHServiceException {
        return ioOperations.deleteList(deleteList);
	}

	/**
	 * duplicate specified {@List list}
	 * 
	 * @param list
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean copyList(PriceList list) throws OHServiceException {
		return copyList(list, 1., 0.);
	}
	
	/**
	 * duplicate {@link list} multiplying by <code>factor</code> and rounding by <code>step</code>
	 * 
	 * @param list - the {@link list} to be duplicated
	 * @param factor - the multiplying factor
	 * @param step - the rounding step
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean copyList(PriceList list, double factor, double step) throws OHServiceException {
        return ioOperations.copyList(list, factor, step);
	}
	
	public ArrayList<PriceForPrint> convertPrice(PriceList listSelected, ArrayList<Price> prices) {
		ArrayList<PriceForPrint> pricePrint = new ArrayList<PriceForPrint>();
		for (Price price : prices) {
			if (price.getList().getId() == listSelected.getId() && price.getPrice() != 0.) {
				PriceForPrint price4print = new PriceForPrint();
				price4print.setList(listSelected.getName());
				price4print.setCurrency(listSelected.getCurrency());
				price4print.setDesc(price.getDesc());
				price4print.setGroup(price.getGroup());
				price4print.setPrice(price.getPrice());
				pricePrint.add(price4print);
			}
			Collections.sort(pricePrint);
		}
		return pricePrint;
	}
}