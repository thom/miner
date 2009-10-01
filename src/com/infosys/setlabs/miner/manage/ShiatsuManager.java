package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

public class ShiatsuManager extends Manager {
	/**
	 * Creates a new shiatsu manager
	 * 
	 * @param connectionArgs
	 * @throws MinerException
	 */
	public ShiatsuManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public void massage() throws MinerException {
		try {
			this.getFactory().getShiatsuDAO(this.getSession()).createTables();
			fillTables();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	private void fillTables() throws MinerException {
		try {
			// TODO:
			// * Get fileFactory
			// * Get all files
			// * Iterate over array, insert into miner_files table
			// * Insert module name into miner_modules table
			MinerModuleDAO moduleDAO = this.getFactory().getMinerModuleDAO(this.getSession());
			moduleDAO.create(new MinerModule("foo/bar"));
			moduleDAO.create(new MinerModule("bar/foo"));
			moduleDAO.create(new MinerModule("test"));
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}
