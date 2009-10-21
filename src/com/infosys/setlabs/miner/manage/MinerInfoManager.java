package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.MinerInfo;

public class MinerInfoManager extends Manager {
	/**
	 * Creates a new miner info manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public MinerInfoManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Creates table for miner info
	 * 
	 * @throws MinerException
	 */
	public void createTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerInfoDAO(this.getSession()).createTables();

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
	 * Finds existing persistent miner info by its ID
	 * 
	 * @param id
	 *            ID to find
	 * @return MinerInfo
	 * @throws MinerException
	 */
	public MinerInfo find(int id) throws MinerException {
		DAOTransaction trans = null;
		MinerInfo result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerInfoDAO(this.getSession()).find(
					id);

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
	 * Finds existing persistent miner info by its name
	 * 
	 * @param id
	 *            ID to find
	 * @return MinerInfo
	 * @throws MinerException
	 */
	public MinerInfo find(String minerInfoName) throws MinerException {
		DAOTransaction trans = null;
		MinerInfo result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerInfoDAO(this.getSession()).find(
					minerInfoName);

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
	 * Finds all existing persistent miner info
	 * 
	 * @return Collection<MinerInfo>
	 * @throws MinerException
	 */
	public Collection<MinerInfo> findAll() throws MinerException {
		DAOTransaction trans = null;
		Collection<MinerInfo> result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerInfoDAO(this.getSession())
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
	 * Creates a new persistent miner info
	 * 
	 * @param MinerInfo
	 *            miner info to create
	 * @throws MinerException
	 */
	public void create(MinerInfo MinerInfo) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerInfoDAO(this.getSession()).create(
					MinerInfo);

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
	 * Deletes an existing persistent miner info
	 * 
	 * @param MinerInfo
	 *            miner info to delete
	 * @throws MinerException
	 */
	public void delete(MinerInfo MinerInfo) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerInfoDAO(this.getSession()).delete(
					MinerInfo);

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
	 * Updates an existing persistent miner info
	 * 
	 * @param MinerInfo
	 *            miner info to update
	 * @throws MinerException
	 */
	public void update(MinerInfo MinerInfo) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerInfoDAO(this.getSession()).update(
					MinerInfo);

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
