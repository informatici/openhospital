package org.isf.priceslist.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PricesListIoOperations {
	@Autowired
	private DbJpaUtil jpa;

	/**
	 * return the list of {@link List}s in the DB
	 * 
	 * @return the list of {@link List}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PriceList> getLists() throws OHException {
		
		ArrayList<PriceList> pList = null;
				
		
		jpa.beginTransaction();
		
		jpa.createQuery("SELECT * FROM PRICELISTS", PriceList.class, false);
		List<PriceList> priceLists = (List<PriceList>)jpa.getList();
		pList = new ArrayList<PriceList>(priceLists);			
		
		jpa.commitTransaction();
		
		return pList;
	}
	
	/**
	 * return the list of {@link Price}s in the DB
	 * 
	 * @return the list of {@link Price}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Price> getPrices() throws OHException {
		
		ArrayList<Price> pPrice = null;
				
		
		jpa.beginTransaction();
		
		jpa.createQuery("SELECT * FROM PRICES ORDER BY PRC_DESC", Price.class, false);
		List<Price> prices = (List<Price>)jpa.getList();
		pPrice = new ArrayList<Price>(prices);			
		
		jpa.commitTransaction();

		return pPrice;
	}

	/**
	 * updates all {@link Price}s in the specified {@link List}
	 * 
	 * @param list - the {@link List}
	 * @param prices - the list of {@link Price}s
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updatePrices(
			PriceList list, 
			ArrayList<Price> prices) throws OHException {
		boolean result = true;
		
		
		result = _deletePricesInsideList(jpa, list.getId());
		
		result &= _insertNewPricesInsideList(jpa, list, prices);

		return result;
	}
	
	private boolean _deletePricesInsideList(
			DbJpaUtil jpa,
			int id) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM PRICES WHERE PRC_LST_ID = ?", Price.class, false);
			params.add(id);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
        return result;
    }
	
	private boolean _insertNewPricesInsideList(
			DbJpaUtil jpa,
			PriceList list,
			ArrayList<Price> prices) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		for (Price price : prices) 
		{
			jpa.beginTransaction();		
	
			try {
				jpa.createQuery("INSERT INTO PRICES (PRC_LST_ID, PRC_GRP, PRC_ITEM, PRC_DESC, PRC_PRICE) VALUES (?,?,?,?,?)", Price.class, false);
				params = _addUpdatePriceParameters(list, price);
				jpa.setParameters(params, false);
				jpa.executeUpdate();
			}  catch (OHException e) {
				result = false;
				throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
			} 	
	
			jpa.commitTransaction();
		}		
		
        return result;
    }
	
	private ArrayList<Object> _addUpdatePriceParameters(
			PriceList list, 
			Price price) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		

		params.add(list.getId());
		params.add(price.getGroup());
		params.add(price.getItem());
		params.add(price.getDesc());
		params.add(price.getPrice());
        		
        return params;
    }

	/**
	 * insert a new {@link List} in the DB
	 * 
	 * @param list - the {@link List}
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newList(
			PriceList list) throws OHException {
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("INSERT INTO PRICELISTS (LST_CODE, LST_NAME, LST_DESC, LST_CURRENCY) VALUES (?,?,?,?)", PriceList.class, false);
			params = _addNewPriceListParameters(list);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();

		return result;
	}
	
	private ArrayList<Object> _addNewPriceListParameters(
			PriceList list) throws OHException 
    {	

		ArrayList<Object> params = new ArrayList<Object>();
		
			
		params.add(list.getCode());
		params.add(list.getName());
		params.add(list.getDescription());
		params.add(list.getCurrency());
        		
        return params;
    }

	/**
	 * update a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateList(
			PriceList list) throws OHException {
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			String query = "UPDATE PRICELISTS SET LST_CODE = ?, " +
						   "LST_NAME = ?, " +
						   "LST_DESC = ?, " +
						   "LST_CURRENCY = ? "+
						   "WHERE LST_ID = ?";
			jpa.createQuery(query, PriceList.class, false);
			params = _addUpdatePriceListParameters(list);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();
		
		return result;
	}

	private ArrayList<Object> _addUpdatePriceListParameters(
			PriceList list) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		
			
		params.add(list.getCode());
		params.add(list.getName());
		params.add(list.getDescription());
		params.add(list.getCurrency());
		params.add(list.getId());
        		
        return params;
    }
	
	/**
	 * delete a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteList(
			PriceList list) throws OHException {
		boolean result = true;

		
		result = _deletePricesInsideList(jpa, list.getId());

		result &= _deletePriceList(jpa, list.getId());
				
		return result;
	}	
	
	private boolean _deletePriceList(
			DbJpaUtil jpa,
			int id) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM PRICELISTS WHERE LST_ID = ? ", PriceList.class, false);
			params.add(id);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
        return result;
    }

	/**
	 * duplicate {@link list} multiplying by <code>factor</code> and rounding by <code>step</code>
	 * 
	 * @param list - the {@link list} to be duplicated
	 * @param factor - the multiplying factor
	 * @param step - the rounding step
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean copyList(
			PriceList list, 
			double factor, 
			double step) throws OHException 
	{
		boolean result = false;
		
				
		if (step > 0) 
		{
			result = _insertCopyListSteps(jpa, list, factor, step);
			
		}
		else
		{
			result = _insertCopyList(jpa, list, factor);			
		}

		return result;
	}
	
    @SuppressWarnings("unchecked")
	private boolean _insertCopyListSteps(
			DbJpaUtil jpa,
			PriceList list, 
			double factor, 
			double step) throws OHException 
    {	
    	PriceList newList = _insertNewPriceList(jpa, list);
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true; 		

		
		jpa.beginTransaction();			
		
		jpa.createQuery("SELECT * FROM PRICES WHERE PRC_LST_ID = ?", Price.class, false);
		params.add(list.getId());			
		jpa.setParameters(params, false);
		List<Price> Prices = (List<Price>)jpa.getList();
		for (Price price: Prices) 
		{    
			Price newPrice = new Price();
			
			
			newPrice.setList(newList);
			newPrice.setGroup(price.getGroup());
			newPrice.setDesc(price.getDesc());
			newPrice.setPrice(Math.round((price.getPrice() * factor) / step) * step);
			newPrice.setItem(price.getItem());
			
			jpa.persist(newPrice);			
	    }        
		
		jpa.commitTransaction();			
		
        return result;
    }

    @SuppressWarnings("unchecked")	
	private boolean _insertCopyList(
			DbJpaUtil jpa,
			PriceList list, 
			double factor) throws OHException 
    {	
    	PriceList newList = _insertNewPriceList(jpa, list);
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
				
		jpa.beginTransaction();			
		
		jpa.createQuery("SELECT * FROM PRICES WHERE PRC_LST_ID = ?", Price.class, false);
		params.add(list.getId());			
		jpa.setParameters(params, false);
		List<Price> Prices = (List<Price>)jpa.getList();
		for (Price price: Prices) 
		{    
			Price newPrice = new Price();
			
			
			newPrice.setList(newList);
			newPrice.setGroup(price.getGroup());
			newPrice.setDesc(price.getDesc());
			newPrice.setPrice(price.getPrice() * factor);
			newPrice.setItem(price.getItem());
								
			jpa.persist(newPrice);		
		}        
		
		jpa.commitTransaction();			
		
        return result;		
    }

	private PriceList _insertNewPriceList(
			DbJpaUtil jpa,
			PriceList list) throws OHException 
    {			
		jpa.beginTransaction();	
		
		PriceList newList = new PriceList();
		newList.setCode(list.getCode());
		newList.setName(list.getName());
		newList.setDescription(list.getDescription());
		newList.setCurrency(list.getCurrency());
		jpa.persist(newList);
		
    	jpa.commitTransaction();
		
        return newList;
    }
}