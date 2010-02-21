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
	 * Returns the minimum commit size
	 * 
	 * @return minimumCommitSize
	 */
	public int getMinimumCommitSize();

	/**
	 * Sets the minimum commit size
	 * 
	 * @param minimumCommitSize
	 *            minimum commit size to set
	 */
	public void setMinimumCommitSize(int minimumCommitSize);

	/**
	 * Returns the maximum commit size
	 * 
	 * @return maximumCommitSize
	 */
	public int getMaximumCommitSize();

	/**
	 * Sets the maximum commit size
	 * 
	 * @param maximumCommitSize
	 *            maximum commit size to set
	 */
	public void setMaximumCommitSize(int maximumCommitSize);

	/**
	 * Returns commit metrics
	 * 
	 * @param allFiles
	 *            do we want the metrics for all files?
	 * @return CommitMetrics
	 * @throws DataAccessException
	 */
	public CommitMetrics metrics(boolean allFiles) throws DataAccessException;
}
