package com.infosys.setlabs.miner.manage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.dao.ModuleDAO;
import com.infosys.setlabs.miner.domain.MinerFile;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.domain.Module;
import com.infosys.setlabs.miner.domain.MinerFile.Type;

/**
 * Shiatsu manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ShiatsuManager extends Manager {
	private Pattern[] maxModuleDepthPattern;
	private int maxModuleDepth;
	private String pathsToExclude;
	private String filesToExclude;

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

		String fs = System.getProperty("file.separator");

		// Ugly, uglier, this array...
		maxModuleDepthPattern = new Pattern[5];
		maxModuleDepthPattern[0] = Pattern.compile("(\\." + fs + ")");
		maxModuleDepthPattern[1] = Pattern.compile("(\\." + fs + ".*?" + fs
				+ "|\\." + fs + ")");
		maxModuleDepthPattern[2] = Pattern.compile("(\\." + fs + ".*?" + fs
				+ ".*?" + fs + "|\\." + fs + ".*?" + fs + "|\\." + fs + ")");
		maxModuleDepthPattern[3] = Pattern.compile("(\\." + fs + ".*?" + fs
				+ ".*?" + fs + ".*?" + fs + "|\\." + fs + ".*?" + fs + ".*?"
				+ fs + "|\\." + fs + ".*?" + fs + "|\\." + fs + ")");
		maxModuleDepthPattern[4] = Pattern.compile("(\\." + fs + ".*?" + fs
				+ ".*?" + fs + ".*?" + fs + ".*?" + fs + "|\\." + fs + ".*?"
				+ fs + ".*?" + fs + ".*?" + fs + "|\\." + fs + ".*?" + fs
				+ ".*?" + fs + "|\\." + fs + ".*?" + fs + "|\\." + fs + ")");
	}

	/**
	 * Massages the data
	 * 
	 * @param maxModuleDepth
	 *            maximum module depth
	 * @param pathsToExclude
	 *            regular expression of paths to exclude
	 * @param filesToExclude
	 *            regular expression of files to exclude
	 * @throws MinerException
	 */
	public void massage(int maxModuleDepth, String pathsToExclude,
			String filesToExclude) throws MinerException {
		this.maxModuleDepth = maxModuleDepth;
		this.pathsToExclude = pathsToExclude;
		this.filesToExclude = filesToExclude;

		// Check if specified maximum module depth is valid
		if (!(-1 <= maxModuleDepth && maxModuleDepth < maxModuleDepthPattern.length)) {
			throw new MinerException(new Exception(
					"Module depth must be between -1 and "
							+ (maxModuleDepthPattern.length - 1)));
		}

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

			// Create miner actions table
			// TODO: Create miner actions table
			// TODO: Update file IDs in miner actions table
			// TODO: Replace all uses of actions with miner actions

			// Create miner info table
			MinerInfoDAO minerInfoDAO = this.getFactory().getMinerInfoDAO(
					this.getSession());
			minerInfoDAO.createTables();
			MinerInfo minerInfo = new MinerInfo();
			minerInfo.setName(MinerInfo.defaultName);
			minerInfo.setShiatsu(true);
			minerInfo.setMaximumModuleDepth(maxModuleDepth);
			minerInfo.setPathsToExclude(pathsToExclude);
			minerInfo.setFilesToExclude(filesToExclude);
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

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			ModuleDAO moduleDAO = this.getFactory().getMinerModuleDAO(
					this.getSession());
			Module module = null;

			minerFileDAO.initialize();

			// Commit transaction
			trans.commit();

			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			for (MinerFile minerFile : minerFileDAO.findAll()) {
				// Get path and set miner file path
				minerFile.setPath(minerFileDAO.getPath(minerFile.getId()));

				// Delete miner files matching files to exclude or paths to
				// exclude
				if (minerFile.getFileName().matches(filesToExclude)
						|| minerFile.getPath().matches(pathsToExclude)) {
					minerFileDAO.delete(minerFile);
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

					if (minerFile.getType() == Type.CODE) {
						module.setCodeFiles(true);
					}

					minerFile.setModule(moduleDAO.create(module));
				} else {
					if (minerFile.getType() == Type.CODE
							&& !module.hasCodeFiles()) {
						module.setCodeFiles(true);
						moduleDAO.update(module);
					}

					minerFile.setModule(module);
				}

				minerFileDAO.update(minerFile);
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
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());

			minerFileDAO.setRandomizedModules(true);
			minerFileDAO.createTables();
			minerFileDAO.initialize();

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