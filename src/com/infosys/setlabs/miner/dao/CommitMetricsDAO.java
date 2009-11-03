package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DataAccessException;

/**
 * CommitMetrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface CommitMetricsDAO {
	/**
	 * Returns modularization metrics
	 * 
	 * @param start
	 *            commit ID to start with
	 * @param stop
	 *            commit ID to stop with
	 * @return modularization
	 * @throws DataAccessException
	 */
	public double modularization(int start, int stop)
			throws DataAccessException;

	/**
	 * Returns modularization metrics
	 * 
	 * @param startRev
	 *            commit revision to start with
	 * @param stopRev
	 *            commit revision to stop with
	 * @return modularization
	 * @throws DataAccessException
	 */
	public double modularizationRevs(String startRev, String stopRev)
			throws DataAccessException;

	/**
	 * Returns modularization metrics
	 * 
	 * @param startTag
	 *            commit tag to start with
	 * @param stopTag
	 *            commit tag to stop with
	 * @return modularization
	 * @throws DataAccessException
	 */
	public double modularizationTags(String startTag, String stopTag)
			throws DataAccessException;
}
