package com.infosys.setlabs.miner.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.Commit;
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
	 * @param mimimumCommitSize
	 *            minimum commit size to set
	 * @param maximumCommitSize
	 *            maximumCommitSize to set
	 * @return LinkedList<CommitMetrics>
	 * @throws MinerException
	 */
	public LinkedList<CommitMetrics> commitMetrics(ArrayList<String> ranges,
			IdType idType, int minimumCommitSize, int maximumCommitSize)
			throws MinerException {
		LinkedList<CommitMetrics> result = new LinkedList<CommitMetrics>();
		int id = 1;
		for (String range : ranges) {
			result.add(commitMetrics(range, idType, minimumCommitSize,
					maximumCommitSize));
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
	 * @param mimimumCommitSize
	 *            minimum commit size to set
	 * @param maximumCommitSize
	 *            maximumCommitSize to set
	 * @return metrics
	 */
	public CommitMetrics commitMetrics(String range, IdType idType,
			int minimumCommitSize, int maximumCommitSize) throws MinerException {
		CommitMetrics result = null;
		int startId = 0;
		int stopId = 0;
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

			CommitMetricsDAO commitMetricsDAO = this.getFactory()
					.getCommitMetricsDAO(this.getSession());

			// Get commits
			startId = getCommit(start, idType).getId();
			stopId = getCommit(stop, idType).getId();

			// If ID type is TAG, we need to extract 1 from the stop ID
			if (stopId > startId && idType == IdType.TAG) {
				stopId -= 1;
			}

			// Get metrics
			result = commitMetricsDAO.metrics(startId, stopId,
					minimumCommitSize, maximumCommitSize);

			// Set information about commits
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

	private Commit getCommit(String id, IdType idType) throws MinerException,
			DataAccessException {
		Commit result = null;
		CommitDAO commitDAO = this.getFactory().getCommitDAO(this.getSession());

		switch (idType) {
			case ID :
				try {
					result = commitDAO.find(Integer.parseInt(id));
				} catch (NumberFormatException e) {
					throw new MinerException(new Exception("'" + id
							+ "' is not a valid ID"));
				}
				break;
			case REV :
				result = commitDAO.findByRev(id);
				break;
			case TAG :
				result = commitDAO.findByTag(id);
				break;
		}

		if (result == null) {
			throw new MinerException(new Exception("No such "
					+ idType.toString() + " '" + id + "'"));
		}

		return result;
	}
}
