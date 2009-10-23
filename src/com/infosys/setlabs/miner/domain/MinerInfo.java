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

	// Minimal number of items per frequent item set
	private int minimalItems;

	// Minimal support
	private double minimalSupport;

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
		result += "ID\t\t\t\t\t" + getId() + "\n";
		result += "Name\t\t\t\t\t" + getName() + "\n";
		result += "Massaged data?\t\t\t\t" + isShiatsu() + "\n";
		result += "Maximum module depth:\t\t\t" + getMaximumModuleDepth() + "\n";
		result += "Miner run?\t\t\t\t" + isMiner();
		if (isMiner()) {
			result += "\nCode files:\t\t\t\t"
					+ getCodeFiles().toString().toLowerCase() + "\n";
			result += "Minimal support per frequent item set:\t";
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
