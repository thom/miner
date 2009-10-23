package com.infosys.setlabs.miner.domain;

import com.infosys.setlabs.miner.dao.BasketFormatDAO.CodeFiles;

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

	// Was miner run already?
	private boolean miner;

	// Minimum number of items per frequent item set
	private int minimumItems;

	// Minimum support
	private double minimumSupport;

	// Included files
	private CodeFiles codeFiles;

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
	 * Returns code files
	 * 
	 * @return codeFiles
	 */
	public CodeFiles getCodeFiles() {
		return codeFiles;
	}

	/**
	 * Sets code files
	 * 
	 * @param codeFiles
	 *            code files to set
	 */
	public void setCodeFiles(CodeFiles codeFiles) {
		this.codeFiles = codeFiles;
	}

	/**
	 * Sets code files
	 * 
	 * @param codeFilesName
	 *            code files to set
	 */
	public void setCodeFiles(String codeFilesName) {
		codeFiles = CodeFiles.valueOf(codeFilesName.toUpperCase());
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

	@Override
	public String toString() {
		String result = "";
		result += "ID\t\t\t\t\t" + getId() + "\n";
		result += "Name\t\t\t\t\t" + getName() + "\n";
		result += "Massaged data?\t\t\t\t" + isShiatsu() + "\n";
		result += "Maximum module depth:\t\t\t" + getMaximumModuleDepth() + "\n";
		result += "Miner run?\t\t\t\t" + isMiner();
		if (isMiner()) {
			result += "\nCode files:\t\t\t\t"
					+ getCodeFiles().toString().toLowerCase() + "\n";
			result += "Minimum support per frequent item set:\t";
			if (getMinimumSupport() < 0) {
				result += -getMinimumSupport() + " (absolute)\n";
			} else {
				result += getMinimumSupport() + "% (relative)\n";
			}
			result += "Minimum items per frequent item set:\t"
					+ getMinimumItems();
		}
		return result;
	}
}
