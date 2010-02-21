package com.infosys.setlabs.miner.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
	private HashMap<String, String> connectionArgs = null;
	private int minimumCommitSize;
	private int maximumCommitSize;

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
		this.connectionArgs = connectionArgs;
	}

	/**
	 * Returns the minimum commit size
	 * 
	 * @return minimumCommitSize
	 */
	public int getMinimumCommitSize() {
		return minimumCommitSize;
	}

	/**
	 * Sets the minimum commit size
	 * 
	 * @param minimumCommitSize
	 *            minimum commit size to set
	 */
	public void setMinimumCommitSize(int minimumCommitSize) {
		this.minimumCommitSize = minimumCommitSize;
	}

	/**
	 * Returns the maximum commit size
	 * 
	 * @return maximumCommitSize
	 */
	public int getMaximumCommitSize() {
		return maximumCommitSize;
	}

	/**
	 * Sets the maximum commit size
	 * 
	 * @param maximumCommitSize
	 *            maximum commit size to set
	 */
	public void setMaximumCommitSize(int maximumCommitSize) {
		this.maximumCommitSize = maximumCommitSize;
	}

	/**
	 * Returns commit metrics for a list of ranges
	 * 
	 * @param databases
	 *            list of databases
	 * @param mimimumCommitSize
	 *            minimum commit size to set
	 * @param maximumCommitSize
	 *            maximumCommitSize to set
	 * @return LinkedList<CommitMetrics>
	 * @throws MinerException
	 */
	public LinkedList<CommitMetrics> commitMetrics(ArrayList<String> databases,
			boolean allFiles, int minimumCommitSize, int maximumCommitSize)
			throws MinerException {
		LinkedList<CommitMetrics> result = new LinkedList<CommitMetrics>();
		CommitMetricsManager commitMetricsManager = null;

		int id = 1;

		try {
			for (String database : databases) {
				// Configure database name
				connectionArgs.put("database", database);

				// Connect to the database
				commitMetricsManager = new CommitMetricsManager(connectionArgs);

				// Get commit metrics
				commitMetricsManager.setMinimumCommitSize(minimumCommitSize);
				commitMetricsManager.setMaximumCommitSize(maximumCommitSize);
				CommitMetrics cm = commitMetricsManager.commitMetrics(allFiles);

				cm.setId(id);
				cm.setDatabase(database);
				result.add(cm);
				id++;

				commitMetricsManager.close();
			}
		} finally {
			if (commitMetricsManager != null) {
				commitMetricsManager.close();
			}
		}

		return result;
	}

	/**
	 * Returns metrics for a given range of commits
	 * 
	 * @return metrics
	 */
	public CommitMetrics commitMetrics(boolean allFiles) throws MinerException {
		CommitMetrics result = null;
		DAOTransaction trans = null;

		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			CommitMetricsDAO commitMetricsDAO = this.getFactory()
					.getCommitMetricsDAO(this.getSession());

			// Get metrics
			commitMetricsDAO.setMinimumCommitSize(minimumCommitSize);
			commitMetricsDAO.setMaximumCommitSize(maximumCommitSize);
			result = commitMetricsDAO.metrics(allFiles);

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
