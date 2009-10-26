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
	 * Name
	 * 
	 */
	public static String name = "miner_files";

	/**
	 * Returns the number of files
	 * 
	 * @param allFiles
	 *            all files or only the ones that have been renamed?
	 * @return number of files
	 * @throws DataAccessException
	 */
	public int count(boolean allFiles) throws DataAccessException;
}
