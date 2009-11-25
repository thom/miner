package com.infosys.setlabs.miner.dao;

import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;
import com.infosys.setlabs.miner.domain.RepositoryFile.Type;

/**
 * Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface RepositoryFileDAO extends ReadObjectDAO<RepositoryFile> {
	/**
	 * Find all files of a certain type
	 * 
	 * @param type
	 *            type of files to find
	 * @return Collection<RepositoryFile>
	 * @throws DataAccessException
	 */
	public Collection<RepositoryFile> findAll(Type type)
			throws DataAccessException;
}
