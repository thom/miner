package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.domain.Commit;
import com.infosys.setlabs.miner.domain.CommitMetrics.IdType;

/**
 * Commit Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitManager extends Manager {
	/**
	 * Creates a new commit manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public CommitManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Creates tables for commits
	 * 
	 * @throws MinerException
	 */
	public void createTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			CommitDAO commitDAO = this.getFactory().getCommitDAO(
					this.getSession());
			commitDAO.createTables();

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
	}

	/**
	 * Finds existing persistent commit by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @param idType
	 *            type of the ID
	 * @return Commit
	 * @throws MinerException
	 */
	public Commit find(String id, IdType idType) throws MinerException {
		DAOTransaction trans = null;
		Commit result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			CommitDAO commitDAO = this.getFactory().getCommitDAO(
					this.getSession());

			switch (idType) {
				case ID :
					try {
						result = commitDAO.find(Integer.parseInt(id));
					} catch (NumberFormatException e) {
						throw new MinerException(new Exception("'" + id
								+ "' is not a valid ID"));
					}
					break;
				case REV :
					result = commitDAO.findByRev(id);
					break;
				case TAG :
					result = commitDAO.findByTag(id);
					break;
			}

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
	 * Finds all existing persistent commits
	 * 
	 * @return Collection<Commit>
	 * @throws MinerException
	 */
	public Collection<Commit> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<Commit> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getCommitDAO(this.getSession())
					.findAll();

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
