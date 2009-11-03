package com.infosys.setlabs.miner.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.CommitMetrics;
import com.infosys.setlabs.miner.domain.CommitMetrics.IdType;

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
	 * Returns commit metrics for a list of ranges
	 * 
	 * @param ranges
	 *            list of ranges
	 * @param idType
	 *            ID type
	 * @return LinkedList<CommitMetrics>
	 * @throws MinerException
	 */
	public LinkedList<CommitMetrics> commitMetrics(ArrayList<String> ranges,
			IdType idType) throws MinerException {
		LinkedList<CommitMetrics> result = new LinkedList<CommitMetrics>();
		int id = 1;
		for (String range : ranges) {
			result.add(commitMetrics(range, idType));
			result.getLast().setId(id);
			id++;
		}
		return result;
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
	public CommitMetrics commitMetrics(String range, IdType idType)
			throws MinerException {
		CommitMetrics result = null;
		DAOTransaction trans = null;

		String[] ids = range.split(":");
		if (ids.length < 2) {
			throw new MinerException(new Exception("'" + range
					+ "' is not a valid range of IDs"));
		}
		String start = ids[0];
		String stop = ids[1];

		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			// Create new metrics
			result = new CommitMetrics();

			// Set modularization
			CommitMetricsDAO commitMetricsDAO = this.getFactory()
					.getCommitMetricsDAO(this.getSession());

			switch (idType) {
				case ID :
					try {
						result.setModularization(commitMetricsDAO
								.modularization(Integer.parseInt(start),
										Integer.parseInt(stop)));
					} catch (NumberFormatException e) {
						throw new MinerException(new Exception("'" + start
								+ "' and/or '" + stop + "' are not valid IDs"));
					}
					break;
				case REV :
					result.setModularization(commitMetricsDAO
							.modularizationRevs(start, stop));
					break;
				case TAG :
					result.setModularization(commitMetricsDAO
							.modularizationTags(start, stop));
					break;
			}

			result.setStart(start);
			result.setStop(stop);
			result.setIdType(idType);

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
