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
	 *            commit ID to begin with
	 * @param stop
	 *            commit ID to end with
	 * @return modularization
	 * @throws DataAccessException
	 */
	public double modularization(int start, int stop)
			throws DataAccessException;

	// TODO: modularizationRevs(String beginRev, String endRev) throws
	// DataAccessException;

	// TODO: modularizationTags(String beginTag, String endTag) throws
	// DataAccessException;
}
