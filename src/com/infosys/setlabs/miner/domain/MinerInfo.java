package com.infosys.setlabs.miner.domain;

/**
 * Saves information about the miner, e.g. what arguments where used
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerInfo {
	// Minimal number of items per frequent item set
	private int minimalItems;

	// Minimal support
	private double minimalSupport;

	/**
	 * Creates a new miner info object
	 * 
	 * @param minimalItems
	 *            minimal items to set
	 * @param minimalSupport
	 *            minimal support to set
	 */
	public MinerInfo(int minimalItems, double minimalSupport) {
		setMinimalItems(minimalItems);
		setMinimalSupport(minimalSupport);
	}

	/**
	 * Returns the minimal items per frequent item set
	 * 
	 * @return minimalItems
	 */
	public int getMinimalItems() {
		return minimalItems;
	}

	/**
	 * Sets minimal items per frequent item set
	 * 
	 * @param minimalItems
	 */
	public void setMinimalItems(int minimalItems) {
		this.minimalItems = minimalItems;
	}

	/**
	 * Returns the minimal support per frequent item set
	 * 
	 * @return minimalSupport
	 */
	public double getMinimalSupport() {
		return minimalSupport;
	}

	/**
	 * Sets the minimal support per frequent item set
	 * 
	 * @param minimalSupport
	 *            minimal support to set
	 */
	public void setMinimalSupport(double minimalSupport) {
		this.minimalSupport = minimalSupport;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Minimal support per frequent item set:\t";
		if (getMinimalSupport() < 0) {
			result += -getMinimalSupport() + " (absolute)\n";
		} else {
			result += getMinimalSupport() + "% (relative)\n";
		}
		result += "Minimal items per frequent item set:\t" + getMinimalItems();
		return result;
	}
}
