package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.domain.File;

public class FileManager extends Manager {
	/**
	 * Creates a new file manager
	 * 
	 * @throws MinerException
	 */
	public FileManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public File find(int id) throws MinerException {
		try {
			return this.getFactory().getFileDAO(this.getSession()).find(id);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	public Collection<File> findAll(File file) throws MinerException {
		try {
			return this.getFactory().getFileDAO(this.getSession()).findAll();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}
