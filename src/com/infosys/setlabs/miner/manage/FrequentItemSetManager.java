package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

/**
 * Frequent Item Set Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSetManager extends Manager {
	/**
	 * Creates a new miner frequent item set manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public FrequentItemSetManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Creates tables for frequent item sets
	 * 
	 * @throws MinerException
	 */
	public void createTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getFrequentItemSetDAO(this.getSession())
					.createTables();

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
	 * Finds existing persistent frequent item set by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @return FrequentItemSet
	 * @throws MinerException
	 */
	public FrequentItemSet find(int id) throws MinerException {
		DAOTransaction trans = null;
		FrequentItemSet result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getFrequentItemSetDAO(this.getSession())
					.find(id);

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
	 * Finds all existing persistent frequent item sets
	 * 
	 * @return Collection<FrequentItemSet>
	 * @throws MinerException
	 */
	public Collection<FrequentItemSet> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<FrequentItemSet> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getFrequentItemSetDAO(this.getSession())
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

	/**
	 * Creates a new persistent frequent item set.
	 * 
	 * @param frequentItemSetLine
	 *            expects an input in the form of
	 *            <code>5 23 42:10 23.4200</code> whereas before the
	 *            <code>:</code> are file IDs and after the <code>:</code> are
	 *            the absolute and relative item support.
	 * @throws MinerException
	 */
	public void create(String frequentItemSetLine) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			int id = this.getFactory().getFrequentItemSetDAO(this.getSession())
					.create(frequentItemSetLine);

			// Commit transaction
			trans.commit();

			// Start new transaction
			trans.begin();

			this.getFactory().getFrequentItemSetDAO(this.getSession())
					.setNumberOfModulesTouched(id);

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
}
