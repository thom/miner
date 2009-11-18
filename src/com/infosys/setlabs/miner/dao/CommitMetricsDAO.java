package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.domain.CommitMetrics;

/**
 * CommitMetrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface CommitMetricsDAO {
	/**
	 * Returns commit metrics
	 * 
	 * @param start
	 *            ID to start with
	 * @param stop
	 *            ID to stop with
	 * @param mimimumCommitSize
	 *            minimum commit size to set
	 * @param maximumCommitSize
	 *            maximumCommitSize to set
	 * @return CommitMetrics
	 * @throws DataAccessException
	 */
	public CommitMetrics metrics(int start, int stop, int minimumCommitSize,
			int maximumCommitSize) throws DataAccessException;
}
