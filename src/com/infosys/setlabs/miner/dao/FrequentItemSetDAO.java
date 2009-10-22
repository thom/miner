package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

/**
 * Frequent Item Set DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface FrequentItemSetDAO
		extends
			ReadObjectDAO<FrequentItemSet>,
			CreateTablesDAO {
	/**
	 * Prefix of frequent item sets table
	 */
	public static String frequentItemSetsPrefix = "miner_fis_";
	
	/**
	 * Prefix of frequent items table
	 */
	public static String frequentItemsPrefix = "miner_fi_";
	
	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name);	

	/**
	 * Expects an input in the form of <code>5 23 42:10 23.4200</code> whereas
	 * before the <code>:</code> are file IDs and after the <code>:</code> are
	 * the absolute and relative item support.
	 * 
	 * @param frequentItemSetLine
	 *            frequent item set to create
	 * @return ID of newly created frequent item set
	 * @throws DataAccessException
	 */
	public int create(String frequentItemSetLine) throws DataAccessException;

	/**
	 * Sets the number of modules touched for a given frequent item set
	 * 
	 * @param id
	 *            ID of frequent item set to set the number of modules touched
	 *            for
	 */
	void setNumberOfModulesTouched(int id);
}
