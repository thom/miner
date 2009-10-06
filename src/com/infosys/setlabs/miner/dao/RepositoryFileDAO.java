package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;

public interface RepositoryFileDAO extends ReadObjectDAO<RepositoryFile> {
	/**
	 * Find the path of a file.
	 * 
	 * @param id
	 * @return path
	 * @throws DataAccessException
	 */
	public String getPath(int id) throws DataAccessException;
}
