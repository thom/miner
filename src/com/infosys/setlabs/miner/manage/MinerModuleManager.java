package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.MinerModule;

public class MinerModuleManager extends Manager {
	/**
	 * Creates a new miner module manager
	 * 
	 * @param connectionArgs
	 * @throws MinerException
	 */
	public MinerModuleManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public MinerModule find(int id) throws MinerException {
		try {
			return this.getFactory().getMinerModuleDAO(this.getSession()).find(
					id);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public Collection<MinerModule> findAll() throws MinerException {
		try {
			return this.getFactory().getMinerModuleDAO(this.getSession())
					.findAll();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public void create(MinerModule object) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).create(
					object);

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

	public void delete(MinerModule object) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).delete(object);			

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

	public void update(MinerModule object) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerModuleDAO(this.getSession()).update(object);			

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
