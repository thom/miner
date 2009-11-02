package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.MetricsDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.ModuleDAO;
import com.infosys.setlabs.miner.domain.Metrics;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * Metrics Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MetricsManager extends Manager {
	private String name = MinerInfo.defaultName;

	/**
	 * Creates a new metrics manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public MetricsManager(HashMap<String, String> connectionArgs)
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
	 * Returns metrics for a given mining
	 * 
	 * @param name
	 *            mining to get metrics for
	 * @return metrics
	 */
	public Metrics metrics() throws MinerException {
		Metrics result = null;
		DAOTransaction trans = null;
		MinerInfo minerInfo = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			// Get miner info
			minerInfo = this.getFactory().getMinerInfoDAO(this.getSession())
					.find(getName());

			// Create new metrics
			result = new Metrics(minerInfo);

			// Set frequent item set files
			FrequentItemSetDAO frequentItemSetDAO = this.getFactory()
					.getFrequentItemSetDAO(this.getSession());
			frequentItemSetDAO.setName(getName());
			result.setFilesInFrequentItemSet(frequentItemSetDAO.countFiles());

			// Set miner files
			MinerFileDAO minerFileDAO = this.getFactory().getMinerFileDAO(
					this.getSession());
			result.setFiles(minerFileDAO.count(true));

			// Set miner modules
			ModuleDAO moduleDAO = this.getFactory()
					.getMinerModuleDAO(this.getSession());
			result.setModules(moduleDAO.count());

			// Set modularization
			MetricsDAO metricsDAO = this.getFactory().getMetricsDAO(
					this.getSession());
			metricsDAO.setName(getName());
			result.setModularization(metricsDAO.modularization());

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
