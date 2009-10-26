package com.infosys.setlabs.miner.domain;

/**
 * Metrics
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Metrics {
	private int filesInFrequentItemSet;
	private int files;
	private int renamedFiles;
	private int modules;
	private int modulesWithRenamedFiles;
	private double modularization;
	private MinerInfo minerInfo;

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
	 * Returns the number of renamed code files
	 * 
	 * @return renamedFiles
	 */
	public int getRenamedFiles() {
		return renamedFiles;
	}

	/**
	 * Sets the number of renamed code files
	 * 
	 * @param renamedFiles
	 *            number of renamed code files
	 */
	public void setRenamedFiles(int renamedFiles) {
		this.renamedFiles = renamedFiles;
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
	 * Returns the number of modules containing renamed code files
	 * 
	 * @return modulesWithRenamedFiles
	 */
	public int getModulesWithRenamedFiles() {
		return modulesWithRenamedFiles;
	}

	/**
	 * Sets the number of modules containing renamed code files
	 * 
	 * @param modulesWithRenamedFiles
	 *            number of modules containing renamed code files
	 */
	public void setModulesWithRenamedFiles(int modulesWithRenamedFiles) {
		this.modulesWithRenamedFiles = modulesWithRenamedFiles;
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
		// TODO: string representation
		result += minerInfo;
		return result;
	}
}
