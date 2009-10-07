package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

public class FrequentItemSetManager extends Manager {
	/**
	 * Creates a new miner frequent item set manager
	 * 
	 * @param connectionArgs
	 * @throws MinerException
	 */
	public FrequentItemSetManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public void createTables() throws MinerException {
		try {
			this.getFactory().getFrequentItemSetDAO(this.getSession())
					.createTables();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public FrequentItemSet find(int id) throws MinerException {
		try {
			return this.getFactory().getFrequentItemSetDAO(this.getSession())
					.find(id);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public Collection<FrequentItemSet> findAll() throws MinerException {
		try {
			return this.getFactory().getFrequentItemSetDAO(this.getSession())
					.findAll();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public void create(FrequentItemSet frequentItemSet) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getFrequentItemSetDAO(this.getSession()).create(
					frequentItemSet);

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
