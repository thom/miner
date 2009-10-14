package com.infosys.setlabs.miner.manage;

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
	 * Creates table for miner information
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
	 * Returns miner information
	 * 
	 * @return MinerInfo
	 * @throws MinerException
	 */
	public MinerInfo get() throws MinerException {
		DAOTransaction trans = null;
		MinerInfo result = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getMinerInfoDAO(this.getSession()).get();

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
	 * Creates new persistent miner information
	 * 
	 * @param minerInfo
	 *            miner info to write to the database
	 * @throws MinerException
	 */
	public void create(MinerInfo minerInfo) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerInfoDAO(this.getSession()).create(
					minerInfo);

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
