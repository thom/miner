package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ObjectDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

/**
 * Miner File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface MinerFileDAO extends ObjectDAO<MinerFile>, CreateTablesDAO {
	/**
	 * Sets randomized modules
	 * 
	 * @return randomizedModules
	 */
	public boolean hasRandomizedModules();

	/**
	 * Sets randomized modules
	 * 
	 * @param randomizedModules
	 *            are the modules randomized?
	 */
	public void setRandomizedModules(boolean randomizedModules);

	/**
	 * Returns the number of files
	 * 
	 * @return number of files
	 * @throws DataAccessException
	 */
	public int count() throws DataAccessException;

	/**
	 * Returns the number of files with a minimum of modifications
	 * 
	 * @param minimumModifications
	 *            minimum of modifications required
	 * @return number of files
	 * @throws DataAccessException
	 */
	public int count(int minimumModifications) throws DataAccessException;
}
