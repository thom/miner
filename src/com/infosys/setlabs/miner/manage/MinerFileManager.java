package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

/**
 * Miner File Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFileManager extends Manager {
	private boolean randomizedModules;

	/**
	 * Creates a new miner file manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public MinerFileManager(HashMap<String, String> connectionArgs)
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
	 * Creates table for miner files
	 * 
	 * @throws MinerException
	 */
	public void createTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			minerFileDAO.createTables();

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
	 * Finds existing persistent miner file by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @return MinerFile
	 * @throws MinerException
	 */
	public MinerFile find(int id) throws MinerException {
		DAOTransaction trans = null;
		MinerFile result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			result = minerFileDAO.find(id);

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
	 * Finds all existing persistent miner modules
	 * 
	 * @return Collection<MinerFile>
	 * @throws MinerException
	 */
	public Collection<MinerFile> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<MinerFile> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			result = minerFileDAO.findAll();

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
	 * Creates a new persistent miner module
	 * 
	 * @param minerFile
	 *            miner file to create
	 * @throws MinerException
	 */
	public void create(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			minerFileDAO.create(minerFile);

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
	 * Deletes an existing persistent miner module
	 * 
	 * @param minerFile
	 *            miner file to delete
	 * @throws MinerException
	 */
	public void delete(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			minerFileDAO.delete(minerFile);

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
	 * Updates an existing persistent miner module
	 * 
	 * @param minerFile
	 *            miner file to update
	 * @throws MinerException
	 */
	public void update(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			minerFileDAO.setRandomizedModules(hasRandomizedModules());
			minerFileDAO.update(minerFile);

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
