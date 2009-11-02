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
	 * @param begin
	 *            commit ID to begin with
	 * @param end
	 *            commit ID to end with
	 * @return modularization
	 * @throws DataAccessException
	 */
	public double modularization(int begin, int end) throws DataAccessException;
}
