package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.File;

public interface FileDAO extends ReadObjectDAO<File> {
	/**
	 * Find the path of a file.
	 * 
	 * @param id
	 * @return path
	 */
	public String findPath(int id) throws DataAccessException;
	
	/**
	 * Find the path of a file.
	 * 
	 * @param file
	 * @return path
	 */
	public String findPath(File file) throws DataAccessException;
}
