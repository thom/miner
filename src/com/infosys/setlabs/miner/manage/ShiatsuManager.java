package com.infosys.setlabs.miner.manage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.dao.BasketFormatDAO.CodeFiles;
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
	private Pattern[] maxModuleDepthPattern;
	private int maxModuleDepth;

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

		maxModuleDepthPattern = new Pattern[4];
		maxModuleDepthPattern[0] = Pattern.compile("(\\./)");
		maxModuleDepthPattern[1] = Pattern.compile("(\\./.*?/|\\./)");
		maxModuleDepthPattern[2] = Pattern.compile("(\\./.*?/.*?/|\\./.*?/|\\./)");
		maxModuleDepthPattern[3] = Pattern
				.compile("(\\./.*?/.*?/.*?/|\\./.*?/.*?/|\\./.*?/|\\./)");
	}

	/**
	 * Massages the data
	 * 
	 * @throws MinerException
	 */
	public void massage(int maxModuleDepth) throws MinerException {
		this.maxModuleDepth = maxModuleDepth;

		try {
			this.getFactory().getMinerFileDAO(this.getSession()).createTables();
			this.getFactory().getMinerModuleDAO(this.getSession())
					.createTables();
			fillTables();

			// Create miner info table
			MinerInfoDAO minerInfoDAO = this.getFactory().getMinerInfoDAO(
					this.getSession());
			minerInfoDAO.createTables();
			MinerInfo minerInfo = new MinerInfo();
			minerInfo.setName(MinerInfo.defaultName);
			minerInfo.setShiatsu(true);
			minerInfo.setMaximumModuleDepth(maxModuleDepth);
			minerInfo.setCodeFiles(CodeFiles.NONE);
			minerInfoDAO.create(minerInfo);
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
			MinerModule minerModule = null;

			for (RepositoryFile repositoryFile : repositoryFileDAO.findAll()) {
				// Only save code files in miner file table
				if (repositoryFile.isCode()) {
					minerFile = new MinerFile();
					minerFile.setId(repositoryFile.getId());
					minerFile.setFileName(repositoryFile.getFileName());
					minerFile.setPath(repositoryFile.getPath());
					minerFile.setType(repositoryFile.getType());
					minerFile.setRenamed(repositoryFile.isRenamed());

					if (maxModuleDepth > maxModuleDepthPattern.length) {
						throw new MinerException(new Exception("Size: "
								+ maxModuleDepthPattern.length));
					}
					Pattern p = maxModuleDepthPattern[maxModuleDepth];
					Matcher m = p.matcher(minerFile.getDirectory());
					m.find();
					String moduleName = m.group(1);

					minerModule = minerModuleDAO.find(moduleName);
					if (minerModule != null) {
						if (minerFile.isRenamed()
								&& !minerModule.hasRenamedFiles()) {
							minerModule.setRenamedFiles(true);
							minerModuleDAO.update(minerModule);
						}

						minerFile.setModule(minerModule);
					} else {
						minerModule = new MinerModule(moduleName);
						minerModule.setRenamedFiles(minerFile.isRenamed());
						minerFile.setModule(minerModuleDAO.create(minerModule));
					}

					minerFileDAO.create(minerFile);
				}
			}
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}