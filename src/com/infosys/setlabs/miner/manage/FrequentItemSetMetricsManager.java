package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

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
	 * Returns metrics for a given mining
	 * 
	 * @return metrics
	 */
	public FrequentItemSetMetrics frequentItemSetMetrics()
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

			// Set miner info
			result.setMinerInfo(this.getFactory().getMinerInfoDAO(
					this.getSession()).find(getName()));

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
