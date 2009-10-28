package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * Frequent Item Set Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSetManager extends Manager {
	private String name = MinerInfo.defaultName;
	private boolean randomizedModules;

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
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name) {
		this.name = name;
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

			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			frequentItemSetDAO.setRandomizedModules(hasRandomizedModules());
			frequentItemSetDAO.createTables();

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

			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			frequentItemSetDAO.setRandomizedModules(hasRandomizedModules());
			result = frequentItemSetDAO.find(id);

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

			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			frequentItemSetDAO.setRandomizedModules(hasRandomizedModules());
			result = frequentItemSetDAO.findAll();

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

			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			frequentItemSetDAO.setRandomizedModules(hasRandomizedModules());
			int id = frequentItemSetDAO.create(frequentItemSetLine);

			// Commit transaction
			trans.commit();

			// Start new transaction
			trans.begin();

			frequentItemSetDAO.setNumberOfModulesTouched(id);

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
