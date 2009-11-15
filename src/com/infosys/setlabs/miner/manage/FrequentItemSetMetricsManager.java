package com.infosys.setlabs.miner.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetMetricsDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.ModuleDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSetMetrics;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * FrequentItemSetMetrics Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSetMetricsManager extends Manager {
	private HashMap<String, String> connectionArgs = null;
	private String name = MinerInfo.defaultName;
	private int minimumModifications;

	/**
	 * Creates a new metrics manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public FrequentItemSetMetricsManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
		this.connectionArgs = connectionArgs;
	}

	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns minimum modifications for code files
	 * 
	 * @return minimumModifications
	 */
	public int getMinimumModifications() {
		return minimumModifications;
	}

	/**
	 * Sets minimum modifications for code files
	 * 
	 * @param minimumModifications
	 *            minimum modifications for code files
	 */
	public void setMinimumModifications(int minimumModifications) {
		this.minimumModifications = minimumModifications;
	}

	/**
	 * Returns frequent item set metrics for a list of minings
	 * 
	 * @param minings
	 *            list of minings
	 * @return LinkedList<FrequentItemSetMetrics>
	 * @throws MinerException
	 */
	public LinkedList<FrequentItemSetMetrics> frequentItemSetMetrics(
			ArrayList<String> minings) throws MinerException {
		LinkedList<FrequentItemSetMetrics> result = new LinkedList<FrequentItemSetMetrics>();
		FrequentItemSetMetricsManager frequentItemSetMetricsManager = null;
		MinerInfoManager minerInfoManager = null;

		int id = 1;

		try {
			for (String mining : minings) {
				// Set database and mining name
				String[] arguments = mining.split(":");
				String database = arguments[0];
				String name = arguments.length == 1
						? MinerInfo.defaultName
						: arguments[1];

				// Configure database name
				connectionArgs.put("database", database);

				// Connect to the database
				frequentItemSetMetricsManager = new FrequentItemSetMetricsManager(
						connectionArgs);
				minerInfoManager = new MinerInfoManager(connectionArgs);

				// Find miner info
				MinerInfo minerInfo = minerInfoManager.find(name);

				if (minerInfo == null
						|| !(minerInfo.isShiatsu() && minerInfo.isMiner())) {
					minerInfoManager.close();
					throw new MinerException(new Exception("No mining called '"
							+ name + "' in database '" + database
							+ "' found. The data must be mined before "
							+ "running metrics."));
				}

				// Get frequent item set metrics
				frequentItemSetMetricsManager.setName(name);
				frequentItemSetMetricsManager.setMinimumModifications(minerInfo
						.getMinimumModifications());
				FrequentItemSetMetrics fim = frequentItemSetMetricsManager
						.frequentItemSetMetrics();
				fim.setId(id);
				fim.setDatabase(database);
				fim.setMinerInfo(minerInfo);
				result.add(fim);
				id++;

				frequentItemSetMetricsManager.close();
				minerInfoManager.close();
			}
		} finally {
			if (frequentItemSetMetricsManager != null) {
				frequentItemSetMetricsManager.close();
			}
			if (minerInfoManager != null) {
				minerInfoManager.close();
			}
		}

		return result;
	}

	private FrequentItemSetMetrics frequentItemSetMetrics()
			throws MinerException {
		FrequentItemSetMetrics result = null;
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			// Get metrics
			FrequentItemSetMetricsDAO frequentItemSetMetricsDAO = this
					.getFactory().getFrequentItemSetMetricsDAO(
							this.getSession());
			frequentItemSetMetricsDAO.setName(getName());
			result = frequentItemSetMetricsDAO.metrics();

			// Set frequent item set files
			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			result.setFilesInFrequentItemSet(frequentItemSetDAO.countFiles());

			// Set miner files
			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			result.setFiles(minerFileDAO.count());
			result.setFilesModified(minerFileDAO
					.count(getMinimumModifications()));

			// Set miner modules
			ModuleDAO moduleDAO = this.getFactory().getMinerModuleDAO(
					this.getSession());
			result.setModules(moduleDAO.count());

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
		return result;
	}
}
