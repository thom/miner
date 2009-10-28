package com.infosys.setlabs.miner.dao;


/**
 * Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface MetricsDAO {
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
	 * Returns modularization metrics
	 * 
	 * @return modularization
	 */
	public double modularization();
}
