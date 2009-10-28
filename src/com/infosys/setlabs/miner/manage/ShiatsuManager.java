package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infosys.setlabs.dao.DAOTransaction;
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
		maxModuleDepthPattern[2] = Pattern
				.compile("(\\./.*?/.*?/|\\./.*?/|\\./)");
		maxModuleDepthPattern[3] = Pattern
				.compile("(\\./.*?/.*?/.*?/|\\./.*?/.*?/|\\./.*?/|\\./)");
	}

	/**
	 * Massages the data
	 * 
	 * @throws MinerException
	 */
	public void massage(int maxModuleDepth)
			throws MinerException {
		this.maxModuleDepth = maxModuleDepth;

		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			// Create table for miner files and modules
			this.getFactory().getMinerFileDAO(this.getSession()).createTables();
			this.getFactory().getMinerModuleDAO(this.getSession())
					.createTables();

			// Fill miner files and modules tables
			fillTables();

			// Fill separate miner files table with randomized modules
			randomizeModules();

			// Create miner info table
			MinerInfoDAO minerInfoDAO = this.getFactory().getMinerInfoDAO(
					this.getSession());
			minerInfoDAO.createTables();
			MinerInfo minerInfo = new MinerInfo();
			minerInfo.setName(MinerInfo.defaultName);
			minerInfo.setShiatsu(true);
			minerInfo.setMaximumModuleDepth(maxModuleDepth);
			minerInfoDAO.create(minerInfo);

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
	private void fillTables() throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

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

					if (maxModuleDepth > maxModuleDepthPattern.length) {
						throw new MinerException(new Exception("Size: "
								+ maxModuleDepthPattern.length));
					}

					Pattern p = maxModuleDepthPattern[maxModuleDepth];
					Matcher m = p.matcher(minerFile.getDirectory());
					m.find();
					String moduleName = m.group(1);

					minerModule = minerModuleDAO.find(moduleName);
					if (minerModule == null) {
						minerModule = new MinerModule(moduleName);
						minerFile.setModule(minerModuleDAO.create(minerModule));
					} else {
						minerFile.setModule(minerModule);
					}

					minerFileDAO.create(minerFile);
				}
			}

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

	private void randomizeModules() throws MinerException {
		Random random = null;
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			int modules = this.getFactory()
					.getMinerModuleDAO(this.getSession()).count();
			random = new Random();
			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			Collection<MinerFile> minerFiles = minerFileDAO.findAll();
			
			minerFileDAO.setRandomizedModules(true);
			minerFileDAO.createTables();

			for (MinerFile file : minerFiles) {
				file.changeModuleId(random.nextInt(modules) + 1);
				minerFileDAO.create(file);
			}

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