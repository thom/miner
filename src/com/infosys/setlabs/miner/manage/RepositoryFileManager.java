package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * Repository File Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class RepositoryFileManager extends Manager {
	/**
	 * Creates a new file manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public RepositoryFileManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Finds an existing persistent repository file by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @return RepositoryFile
	 * @throws MinerException
	 */
	public RepositoryFile find(int id) throws MinerException {
		DAOTransaction trans = null;
		RepositoryFile result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getFileDAO(this.getSession()).find(id);

			// Commit transaction
			trans.commit();
		} catch (DataAccessException de) {
			// Rollback transaction on failure
			try {
				if (trans != null)
					trans.abort();
			} catch (DataAccessException de2) {
				throw new MinerException(de2);
			}
			throw new MinerException(de);
		}
		return result;
	}

	/**
	 * Finds all existing persistent repository files
	 * 
	 * @return Collection<RepositoryFile>
	 * @throws MinerException
	 */
	public Collection<RepositoryFile> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<RepositoryFile> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getFileDAO(this.getSession()).findAll();

			// Commit transaction
			trans.commit();
		} catch (DataAccessException de) {
			// Rollback transaction on failure
			try {
				if (trans != null)
					trans.abort();
			} catch (DataAccessException de2) {
				throw new MinerException(de2);
			}
			throw new MinerException(de);
		}
		return result;
	}
}
