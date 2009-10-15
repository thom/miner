package com.infosys.setlabs.miner.domain;

/**
 * Saves information about the miner, e.g. what arguments where used
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerInfo {
	// Did the data already get a massage?
	private boolean shiatsu;

	// Was miner run already?
	private boolean miner;

	// Minimal number of items per frequent item set
	private int minimalItems;

	// Minimal support
	private double minimalSupport;

	/**
	 * Did the data already get a massage?
	 * 
	 * @return shiatsu
	 */
	public boolean isShiatsu() {
		return shiatsu;
	}

	/**
	 * Sets shiatsu
	 * 
	 * @param shiatsu
	 *            did the data already get a massage?
	 */
	public void setShiatsu(boolean shiatsu) {
		this.shiatsu = shiatsu;
	}

	/**
	 * Was miner run already?
	 * 
	 * @return miner
	 */
	public boolean isMiner() {
		return miner;
	}

	/**
	 * Sets miner
	 * 
	 * @param miner
	 *            was miner run already?
	 */
	public void setMiner(boolean miner) {
		this.miner = miner;
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
		result += "Massaged data?\t\t\t\t" + isShiatsu() + "\n";
		result += "Miner run?\t\t\t\t" + isMiner();
		if (isMiner()) {
			result += "\nMinimal support per frequent item set:\t";
			if (getMinimalSupport() < 0) {
				result += -getMinimalSupport() + " (absolute)\n";
			} else {
				result += getMinimalSupport() + "% (relative)\n";
			}
			result += "Minimal items per frequent item set:\t"
					+ getMinimalItems();
		}
		return result;
	}
}
