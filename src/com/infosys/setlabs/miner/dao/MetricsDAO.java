package com.infosys.setlabs.miner.dao;

/**
 * Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface MetricsDAO {
	/**
	 * Returns modularization metrics
	 * 
	 * @param hasRenamedFiles
	 *            modules with renamed files?
	 * @return modularization
	 */
	public double modularization(boolean hasRenamedFiles);
}
