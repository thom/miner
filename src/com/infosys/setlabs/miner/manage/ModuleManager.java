package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.Module;

/**
 * Module Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ModuleManager extends Manager {
	/**
	 * Creates a new module manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public ModuleManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Creates table for modules
	 * 
	 * @throws MinerException
	 */
	public void createTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession())
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
	 * Finds existing persistent module by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @return Module
	 * @throws MinerException
	 */
	public Module find(int id) throws MinerException {
		DAOTransaction trans = null;
		Module result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerModuleDAO(this.getSession())
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
	 * Finds all existing persistent modules
	 * 
	 * @return Collection<Module>
	 * @throws MinerException
	 */
	public Collection<Module> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<Module> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerModuleDAO(this.getSession())
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
	 * Creates a new persistent module
	 * 
	 * @param module
	 *            miner module to create
	 * @throws MinerException
	 */
	public void create(Module module) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).create(
					module);

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
	 * Deletes an existing persistent module
	 * 
	 * @param module
	 *            miner module to delete
	 * @throws MinerException
	 */
	public void delete(Module module) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).delete(
					module);

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
	 * Updates an existing persistent module
	 * 
	 * @param module
	 *            miner module to update
	 * @throws MinerException
	 */
	public void update(Module module) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).update(
					module);

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
