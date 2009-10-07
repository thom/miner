package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;

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
			this.getFactory().getMinerFrequentItemSetDAO(this.getSession()).createTables();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}	
}
