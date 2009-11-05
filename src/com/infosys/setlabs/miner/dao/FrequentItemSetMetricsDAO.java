package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.miner.domain.FrequentItemSetMetrics;

/**
 * FrequentItemSetMetrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface FrequentItemSetMetricsDAO {
	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name);

	/**
	 * Returns metrics
	 * 
	 * @return FrequentItemSetMetrics
	 */
	public FrequentItemSetMetrics metrics();
}
