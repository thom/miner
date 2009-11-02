package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.domain.Commit;

/**
 * Commit Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitManager extends Manager {
	private boolean randomizedModules;

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
	 * Sets randomized modules
	 * 
	 * @return randomizedModules
	 */
	public boolean hasRandomizedModules() {
		return randomizedModules;
	}

	/**
	 * Sets randomized modules
	 * 
	 * @param randomizedModules
	 *            are the modules randomized?
	 */
	public void setRandomizedModules(boolean randomizedModules) {
		this.randomizedModules = randomizedModules;
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
	 * @return Commit
	 * @throws MinerException
	 */
	public Commit find(int id) throws MinerException {
		DAOTransaction trans = null;
		Commit result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getCommitDAO(this.getSession()).find(id);

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
	 * Finds existing persistent commit by its revision
	 * 
	 * @param rev
	 *            revision to find
	 * @return Commit
	 * @throws MinerException
	 */
	public Commit findByRev(String rev) throws MinerException {
		DAOTransaction trans = null;
		Commit result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getCommitDAO(this.getSession())
					.findByRev(rev);

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
