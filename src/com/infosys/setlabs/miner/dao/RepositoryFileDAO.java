package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface RepositoryFileDAO {
	/**
	 * Finds an existing persistent object by its ID.
	 * 
	 * @param id
	 *            ID to find
	 * @return object
	 */
	public RepositoryFile find(int id) throws DataAccessException;
}
