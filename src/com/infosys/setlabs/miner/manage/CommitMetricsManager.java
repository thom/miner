package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.CommitMetrics;

/**
 * CommitMetrics Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitMetricsManager extends Manager {
	/**
	 * Creates a new metrics manager
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public CommitMetricsManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Returns metrics for a given range of commits
	 * 
	 * @param range
	 *            range of commit IDs
	 * @param idType
	 *            type of the IDs in the range
	 * @return metrics
	 */
	// TODO: replace with commitMetrics(String range, IdType idType)
	// Call methods in CommitMetricsDAO depending on idType
	public CommitMetrics commitMetrics(int begin, int end)
			throws MinerException {
		CommitMetrics result = null;
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			// Create new metrics
			result = new CommitMetrics();

			// Set modularization
			CommitMetricsDAO commitMetricsDAO = this.getFactory()
					.getCommitMetricsDAO(this.getSession());
			result.setModularization(commitMetricsDAO
					.modularization(begin, end));

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
