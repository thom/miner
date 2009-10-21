package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.domain.MinerModule;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * Shiatsu manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ShiatsuManager extends Manager {
	/**
	 * Creates a new shiatsu manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public ShiatsuManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Massages the data
	 * 
	 * @throws MinerException
	 */
	public void massage() throws MinerException {
		try {
			this.getFactory().getMinerFileDAO(this.getSession()).createTables();
			this.getFactory().getMinerModuleDAO(this.getSession())
					.createTables();
			fillTables();
			
			// Create miner info table
			MinerInfoDAO minerInfoDAO = this.getFactory().getMinerInfoDAO(this.getSession());
			minerInfoDAO.createTables();
			MinerInfo minerInfo = new MinerInfo();
			minerInfo.setShiatsu(true);
			minerInfoDAO.update(minerInfo);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	private void fillTables() throws MinerException {
		try {
			RepositoryFileDAO repositoryFileDAO = this.getFactory().getFileDAO(
					this.getSession());
			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			MinerModuleDAO minerModuleDAO = this.getFactory()
					.getMinerModuleDAO(this.getSession());
			MinerFile minerFile = null;

			for (RepositoryFile repositoryFile : repositoryFileDAO.findAll()) {
				// Only add file if it is of type "code" 
				// TODO: Add option to fillTables to only add a file if it has been renamed
				// TODO: Fix this!
				if (repositoryFile.getType() == RepositoryFile.Type.CODE) {
					minerFile = new MinerFile();
					minerFile.setFileName(repositoryFile.getFileName());
					minerFile.setPath(repositoryFile.getPath());
					minerFile.setRepositoryFile(repositoryFile);
					minerFile.setModule(minerModuleDAO.find(minerModuleDAO
							.create(new MinerModule(repositoryFile
									.getDirectory()))));
					minerFileDAO.create(minerFile);
				}
			}
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}