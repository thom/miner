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
	 * Initializes the miner files table
	 * 
	 * @throws DataAccessException
	 */
	public void initialize() throws DataAccessException;

	/**
	 * Returns the number of files
	 * 
	 * @param allFiles
	 *            count all files
	 * @return number of files
	 * @throws DataAccessException
	 */
	public int count(boolean allFiles) throws DataAccessException;

	/**
	 * Returns the number of files with a minimum of modifications
	 * 
	 * @param allFiles
	 *            count all files
	 * @param minimumModifications
	 *            minimum of modifications required
	 * @return number of files
	 * @throws DataAccessException
	 */
	public int count(boolean allFiles, int minimumModifications)
			throws DataAccessException;

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
