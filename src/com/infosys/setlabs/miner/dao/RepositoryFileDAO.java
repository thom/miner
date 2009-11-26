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
	 * Returns the path of a repository file
	 * 
	 * @param id
	 *            ID to find path for
	 * @return path
	 * @throws DataAccessException
	 */
	public String getPath(int id) throws DataAccessException;
}
