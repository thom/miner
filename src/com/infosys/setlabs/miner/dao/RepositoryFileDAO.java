package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface RepositoryFileDAO extends ReadObjectDAO<RepositoryFile> {
	/**
	 * Gets the path of a file.
	 * 
	 * @param id
	 *            ID to find
	 * @return path
	 * @throws DataAccessException
	 */
	public String getPath(int id) throws DataAccessException;
}
