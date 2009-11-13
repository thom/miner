package com.infosys.setlabs.miner.domain;

/**
 * Saves information about the miner, e.g. what arguments where used
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerInfo {
	public static String defaultName = "default";

	// ID
	private int id;

	// Name
	private String name;

	// Did the data already get a massage?
	private boolean shiatsu;

	// Maximum module depth used
	private int maximumModuleDepth;

	// Minimum modifications
	private int minimumModifications;

	// Minimum commit size
	private int minimumCommitSize;

	// Maximum commit size
	private int maximumCommitSize;

	// Was miner run already?
	private boolean miner;

	// Minimum number of items per frequent item set
	private int minimumItems;

	// Maximum number of items per frequent item set
	private int maximumItems;

	// Minimum support
	private double minimumSupport;

	// Have the files randomized modules?
	private boolean randomizedModules;

	/**
	 * Returns the ID
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the ID
	 * 
	 * @param id
	 *            id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * Sets maximum module depth
	 * 
	 * @return maximumModuleDepth
	 */
	public int getMaximumModuleDepth() {
		return maximumModuleDepth;
	}

	/**
	 * Gets maximum module depth
	 * 
	 * @param maximumModuleDepth
	 *            maximum module depth
	 */
	public void setMaximumModuleDepth(int maximumModuleDepth) {
		this.maximumModuleDepth = maximumModuleDepth;
	}

	/**
	 * Returns minimum modifications of code files
	 * 
	 * @return minimumModifications
	 */
	public int getMinimumModifications() {
		return minimumModifications;
	}

	/**
	 * Sets minimum modifications of code files
	 * 
	 * @param minimumModifications
	 *            minimum modifications of code files
	 */
	public void setMinimumModifications(int minimumModifications) {
		this.minimumModifications = minimumModifications;
	}

	/**
	 * Returns the minimum commit size
	 * 
	 * @return minimumCommitSize
	 */
	public int getMinimumCommitSize() {
		return minimumCommitSize;
	}

	/**
	 * Sets minimum commit size
	 * 
	 * @param minimumCommitSize
	 */
	public void setMinimumCommitSize(int minimumCommitSize) {
		this.minimumCommitSize = minimumCommitSize;
	}

	/**
	 * Returns the maximum commit size
	 * 
	 * @return maximumCommitSize
	 */
	public int getMaximumCommitSize() {
		return maximumCommitSize;
	}

	/**
	 * Sets maximum commit size
	 * 
	 * @param maximumCommitSize
	 */
	public void setMaximumCommitSize(int maximumCommitSize) {
		this.maximumCommitSize = maximumCommitSize;
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
	 * Returns the minimum items per frequent item set
	 * 
	 * @return minimumItems
	 */
	public int getMinimumItems() {
		return minimumItems;
	}

	/**
	 * Sets minimum items per frequent item set
	 * 
	 * @param minimumItems
	 */
	public void setMinimumItems(int minimumItems) {
		this.minimumItems = minimumItems;
	}

	/**
	 * Returns the maximum items per frequent item set
	 * 
	 * @return maximumItems
	 */
	public int getMaximumItems() {
		return maximumItems;
	}

	/**
	 * Sets maximum items per frequent item set
	 * 
	 * @param maximumItems
	 */
	public void setMaximumItems(int maximumItems) {
		this.maximumItems = maximumItems;
	}

	/**
	 * Returns the minimum support per frequent item set
	 * 
	 * @return minimumSupport
	 */
	public double getMinimumSupport() {
		return minimumSupport;
	}

	/**
	 * Sets the minimum support per frequent item set
	 * 
	 * @param minimumSupport
	 *            minimum support to set
	 */
	public void setMinimumSupport(double minimumSupport) {
		this.minimumSupport = minimumSupport;
	}

	/**
	 * Have the modules been randomized?
	 * 
	 * @return randomizedModules
	 */
	public boolean hasRandomizedModules() {
		return randomizedModules;
	}

	/**
	 * Sets randomized
	 * 
	 * @param randomizedModules
	 *            have the modules been randomized?
	 */
	public void setRandomizedModules(boolean randomizedModules) {
		this.randomizedModules = randomizedModules;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Name\t\t\t\t\t" + getName() + "\n";
		result += "Massaged data?\t\t\t\t" + isShiatsu() + "\n";
		result += "Maximum module depth:\t\t\t" + val(getMaximumModuleDepth())
				+ "\n";
		result += "Minimum modifications (commits):\t"
				+ getMinimumModifications() + "\n";
		result += "Minimum commit size:\t\t\t" + val(getMinimumCommitSize())
				+ "\n";
		result += "Maximum commit size:\t\t\t" + val(getMaximumCommitSize())
				+ "\n";
		result += "Miner run?\t\t\t\t" + isMiner();
		if (isMiner()) {
			result += "\nRandomized?\t\t\t\t" + hasRandomizedModules() + "\n";
			result += "Minimum support per frequent item set:\t";
			if (getMinimumSupport() < 0) {
				result += -getMinimumSupport() + " (absolute)\n";
			} else {
				result += getMinimumSupport() + "% (relative)\n";
			}
			result += "Minimum items per frequent item set:\t"
					+ val(getMinimumItems()) + "\n";
			result += "Maximum items per frequent item set:\t"
					+ val(getMaximumItems());

		}
		return result;
	}

	private String val(int value) {
		return value == -1 ? "Not set" : Integer.toString(value);
	}
}
