package org.isf.priceslist.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class PricesListIoOperations {

	@Autowired
	private PriceListIoOperationRepository repository;
	
	@Autowired
	private PriceIoOperationRepository priceRepository;
	
	/**
	 * return the list of {@link List}s in the DB
	 * 
	 * @return the list of {@link List}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PriceList> getLists() throws OHServiceException {
		ArrayList<PriceList> pList = null;
			

		pList = new ArrayList<PriceList>(repository.findAll());
		
		return pList;
	}
	
	/**
	 * return the list of {@link Price}s in the DB
	 * 
	 * @return the list of {@link Price}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Price> getPrices() throws OHServiceException {
		ArrayList<Price> pPrice = null;
						

		pPrice = new ArrayList<Price>(priceRepository.findAllByOrderByDescriptionAsc());
		
		return pPrice;
	}

	/**
	 * updates all {@link Price}s in the specified {@link List}
	 * 
	 * @param list - the {@link List}
	 * @param prices - the list of {@link Price}s
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updatePrices(
			PriceList list, 
			ArrayList<Price> prices) throws OHServiceException {
		boolean result = true;
		
		
		result = _deletePricesInsideList(list.getId());
		
		result &= _insertNewPricesInsideList(list, prices);

		return result;
	}
	
	private boolean _deletePricesInsideList(
			int id) throws OHServiceException 
    {			
		boolean result = true;

		
		priceRepository.deleteWhereList(id);
        				
        return result;
    }
	
	private boolean _insertNewPricesInsideList(
			PriceList list,
			ArrayList<Price> prices) throws OHServiceException 
    {	
		boolean result = true;
        		
		
		for (Price price : prices) 
		{
			priceRepository.insertPrice(
					list.getId(), price.getGroup(), price.getItem(),
					price.getDesc(),
					price.getPrice());
		}
		
        return result;
    }

	/**
	 * insert a new {@link List} in the DB
	 * 
	 * @param list - the {@link List}
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newList(
			PriceList list) throws OHServiceException {
		boolean result = true;
        		
		
		repository.insertPriceList(list.getCode(), list.getName(), list.getDescription(), list.getCurrency());
		
		return result;
	}
	
	/**
	 * update a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateList(
			PriceList list) throws OHServiceException {
		boolean result = false;
        				

		if (repository.updatePriceList(list.getCode(), list.getName(), list.getDescription(), list.getCurrency(), list.getId()) > 0)
		{
			result = true;
		}		
		
		return result;
	}
	
	/**
	 * delete a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteList(
			PriceList list) throws OHServiceException {
		boolean result = true;

		
		result = _deletePricesInsideList(list.getId());

		result &= _deletePriceList(list.getId());
				
		return result;
	}	
	
	private boolean _deletePriceList(
			int id) throws OHServiceException 
    {	
		boolean result = true;
		
		
		repository.deleteWhereId(id);
		
        return result;
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
	public boolean copyList(
			PriceList list, 
			double factor, 
			double step) throws OHServiceException 
	{
    	PriceList newList = _insertNewPriceList(list);
		boolean result = true; 			

		
		List<Price> Prices = (List<Price>)priceRepository.findAllWhereList(list.getId());
		for (Price price: Prices) 
		{    
			Price newPrice = new Price();
			
			
			newPrice.setList(newList);
			newPrice.setGroup(price.getGroup());
			newPrice.setDesc(price.getDesc());
			if (step > 0) 
			{
				newPrice.setPrice(Math.round((price.getPrice() * factor) / step) * step);
			}
			else
			{
				newPrice.setPrice(price.getPrice() * factor);				
			}
			newPrice.setItem(price.getItem());			
			priceRepository.save(newPrice);
	    }        			
		
        return result;
    }

	private PriceList _insertNewPriceList(
			PriceList list) throws OHServiceException 
    {					
		PriceList newList = new PriceList();
		
		
		newList.setCode(list.getCode());
		newList.setName(list.getName());
		newList.setDescription(list.getDescription());
		newList.setCurrency(list.getCurrency());		
		repository.save(newList);
		
        return newList;
    }
}