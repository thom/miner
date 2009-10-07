package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;

public class MinerFrequentItemSetManager extends Manager {
	/**
	 * Creates a new miner frequent item set manager
	 * 
	 * @param connectionArgs
	 * @throws MinerException
	 */
	public MinerFrequentItemSetManager(HashMap<String, String> connectionArgs)
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
