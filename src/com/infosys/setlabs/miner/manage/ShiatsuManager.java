package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.FileDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.domain.File;
import com.infosys.setlabs.miner.domain.MinerFile;
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
			FileDAO fileDAO = this.getFactory().getFileDAO(this.getSession());
			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			MinerModuleDAO minerModuleDAO = this.getFactory().getMinerModuleDAO(
					this.getSession());
			MinerFile minerFile = null;

			for (File file : fileDAO.findAll()) {
				minerFile = new MinerFile();
				minerFile.setFileName(file.getFileName());
				minerFile.setPath(file.getPath());
				minerFile.setFile(file);
				minerFile.setModule(minerModuleDAO.find(minerModuleDAO
						.create(new MinerModule(file.getDirectory()))));
				minerFileDAO.create(minerFile);
			}
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}