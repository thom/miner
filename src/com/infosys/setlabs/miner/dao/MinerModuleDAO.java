package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ObjectDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

/**
 * Miner Module DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface MinerModuleDAO extends ObjectDAO<MinerModule>, CreateTablesDAO {
	/**
	 * Finds a module by name.
	 * 
	 * @param moduleName
	 *            ID to find
	 * @return miner module
	 * @throws DataAccessException
	 */
	public MinerModule find(String moduleName) throws DataAccessException;

	/**
	 * Returns the number of modules
	 * 
	 * @return number of modules
	 */
	public int count();
}
