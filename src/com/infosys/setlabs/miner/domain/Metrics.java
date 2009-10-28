package com.infosys.setlabs.miner.domain;

/**
 * Metrics
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Metrics {
	private int filesInFrequentItemSet;
	private int files;
	private int modules;
	private double modularization;
	private MinerInfo minerInfo;

	/**
	 * Creates a new metrics object
	 * 
	 * @param minerInfo
	 *            miner info
	 */
	public Metrics(MinerInfo minerInfo) {
		this.minerInfo = minerInfo;
	}

	/**
	 * Returns the percentage of the included files in the frequent item set
	 */
	public double includedFilesInFrequentItemSet() {
		return 100 * (double) filesInFrequentItemSet / (double) files;
	}

	/**
	 * Returns the number of files in the frequent item set
	 * 
	 * @return filesInFrequentItemSet
	 */
	public int getFilesInFrequentItemSet() {
		return filesInFrequentItemSet;
	}

	/**
	 * Sets the number of files in the frequent item set
	 * 
	 * @param filesInFrequentItemSet
	 *            number of files in the frequent item set
	 */
	public void setFilesInFrequentItemSet(int filesInFrequentItemSet) {
		this.filesInFrequentItemSet = filesInFrequentItemSet;
	}

	/**
	 * Returns the number of code files
	 * 
	 * @return files
	 */
	public int getFiles() {
		return files;
	}

	/**
	 * Sets the number of code files
	 * 
	 * @param files
	 *            number of code files
	 */
	public void setFiles(int files) {
		this.files = files;
	}

	/**
	 * Returns the number of modules containing code files
	 * 
	 * @return modules
	 */
	public int getModules() {
		return modules;
	}

	/**
	 * Sets the number of modules containing code files
	 * 
	 * @param modules
	 *            number of modules containing code files
	 */
	public void setModules(int modules) {
		this.modules = modules;
	}

	/**
	 * Returns the modularization metrics
	 * 
	 * @return modularization
	 */
	public double getModularization() {
		return modularization;
	}

	/**
	 * Sets the modularization metrics
	 * 
	 * @param modularization
	 *            modularization metrics to set
	 */
	public void setModularization(double modularization) {
		this.modularization = modularization;
	}

	/**
	 * Returns the miner info
	 * 
	 * @return minerInfo
	 */
	public MinerInfo getMinerInfo() {
		return minerInfo;
	}

	/**
	 * Sets the miner info
	 * 
	 * @param minerInfo
	 *            miner info to set
	 */
	public void setMinerInfo(MinerInfo minerInfo) {
		this.minerInfo = minerInfo;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Code files:\t\t\t\t" + getFiles() + "\n";
		result += "Modules:\t\t\t\t" + getModules() + "\n";		
		result += "Files in frequent item set:\t\t"
				+ getFilesInFrequentItemSet() + "\n";
		result += "Percentage of files in FIS:\t\t"
				+ includedFilesInFrequentItemSet() + "%\n";
		result += "Modularization:\t\t\t\t" + getModularization() + "\n\n";
		result += "Miner information\n";
		result += minerInfo;
		return result;
	}
}
