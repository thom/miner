package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.Commit;

/**
 * Commit DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface CommitDAO extends ReadObjectDAO<Commit>, CreateTablesDAO {
	/**
	 * Finds a commit by revision name
	 * 
	 * @param rev
	 * @return commit
	 * @throws DataAccessException
	 */
	public Commit findByRev(String rev) throws DataAccessException;

	/**
	 * Finds a commit by tag name
	 * 
	 * @param tag
	 * @return commit
	 * @throws DataAccessException
	 */
	public Commit findByTag(String tag) throws DataAccessException;
}
