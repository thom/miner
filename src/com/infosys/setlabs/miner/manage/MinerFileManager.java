package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.MinerFile;

public class MinerFileManager extends Manager {
	/**
	 * Creates a new miner file manager
	 * 
	 * @param connectionArgs
	 * @throws MinerException
	 */
	public MinerFileManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public MinerFile find(int id) throws MinerException {
		try {
			return this.getFactory().getMinerFileDAO(this.getSession())
					.find(id);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public Collection<MinerFile> findAll() throws MinerException {
		try {
			return this.getFactory().getMinerFileDAO(this.getSession())
					.findAll();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public void create(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerFileDAO(this.getSession()).create(minerFile);

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

	public void delete(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerFileDAO(this.getSession()).delete(minerFile);

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

	public void update(MinerFile minerFile) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getMinerFileDAO(this.getSession()).update(minerFile);

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
