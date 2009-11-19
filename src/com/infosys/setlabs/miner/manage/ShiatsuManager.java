package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.dao.ModuleDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.domain.Module;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * Shiatsu manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ShiatsuManager extends Manager {
	private Pattern[] maxModuleDepthPattern;
	private int maxModuleDepth;
	private String excludePath;
	private String excludeFile;

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

		maxModuleDepthPattern = new Pattern[5];
		maxModuleDepthPattern[0] = Pattern.compile("(\\./)");
		maxModuleDepthPattern[1] = Pattern.compile("(\\./.*?/|\\./)");
		maxModuleDepthPattern[2] = Pattern
				.compile("(\\./.*?/.*?/|\\./.*?/|\\./)");
		maxModuleDepthPattern[3] = Pattern
				.compile("(\\./.*?/.*?/.*?/|\\./.*?/.*?/|\\./.*?/|\\./)");
		maxModuleDepthPattern[4] = Pattern
				.compile("(\\./.*?/.*?/.*?/.*?/|\\./.*?/.*?/.*?/|\\./.*?/.*?/|\\./.*?/|\\./)");
	}

	/**
	 * Massages the data
	 * 
	 * @throws MinerException
	 */
	public void massage(int maxModuleDepth, String excludePath,
			String excludeFile) throws MinerException {
		this.maxModuleDepth = maxModuleDepth;
		this.excludePath = excludePath;
		this.excludeFile = excludeFile;

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

			// Create commits table
			CommitDAO commitDAO = this.getFactory().getCommitDAO(
					this.getSession());
			commitDAO.createTables();

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
			ModuleDAO moduleDAO = this.getFactory().getMinerModuleDAO(
					this.getSession());
			MinerFile minerFile = null;
			Module module = null;

			for (RepositoryFile repositoryFile : repositoryFileDAO.findAll()) {
				// Only save code files in miner file table
				if (repositoryFile.isCode()
						&& !repositoryFile.getPath().matches(excludePath)
						&& !repositoryFile.getFileName().matches(excludeFile)) {
					minerFile = new MinerFile(repositoryFile);

					if (!(-1 <= maxModuleDepth && maxModuleDepth < maxModuleDepthPattern.length)) {
						throw new MinerException(new Exception(
								"Module depth must be between -1 and "
										+ (maxModuleDepthPattern.length - 1)));
					}

					String moduleName = null;
					if (maxModuleDepth == -1) {
						moduleName = minerFile.getDirectory();
					} else {
						Pattern p = maxModuleDepthPattern[maxModuleDepth];
						Matcher m = p.matcher(minerFile.getDirectory());
						m.find();
						moduleName = m.group(1);
					}

					module = moduleDAO.find(moduleName);
					if (module == null) {
						module = new Module(moduleName);
						minerFile.setModule(moduleDAO.create(module));
					} else {
						minerFile.setModule(module);
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