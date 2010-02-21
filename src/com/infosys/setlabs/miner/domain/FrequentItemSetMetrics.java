package com.infosys.setlabs.miner.domain;

/**
 * FrequentItemSetMetrics
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSetMetrics {
	private int id;
	private String database;
	private int filesInFrequentItemSet;
	private int files;
	private int filesModified;
	private int filesAdded;
	private int filesMoved;
	private int modules;
	private int frequentItemSets;
	private double localization;
	private MinerInfo minerInfo;

	private boolean csv;

	/**
	 * Sets ID
	 * 
	 * @param id
	 *            ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns ID
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the database
	 * 
	 * @param database
	 *            database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Returns the database
	 * 
	 * @return database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Returns the percentage of the included files in the frequent item set
	 */
	public double includedFilesInFrequentItemSet() {
		return 100 * (double) filesInFrequentItemSet / (double) filesModified;
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
	 * Returns the number of code files that have been modified at least n times
	 * 
	 * @return filesModified
	 */
	public int getFilesModified() {
		return filesModified;
	}

	/**
	 * Sets the number of code files that have been modified at least n times
	 * 
	 * @param filesModified
	 *            number of code files modified at least n times
	 */
	public void setFilesModified(int filesModified) {
		this.filesModified = filesModified;
	}

	/**
	 * Returns the number of code files that have been added
	 * 
	 * @return filesAdded
	 */
	public int getFilesAdded() {
		return filesAdded;
	}

	/**
	 * Sets the number of code files that have been added
	 * 
	 * @param filesAdded
	 *            number of code files added
	 */
	public void setFilesAdded(int filesAdded) {
		this.filesAdded = filesAdded;
	}

	/**
	 * Returns the number of distinct code files that have been moved into
	 * another directory
	 * 
	 * @return filesMoved
	 */
	public int getFilesMoved() {
		return filesMoved;
	}

	/**
	 * Sets the number of distinct code files that have been moved into another
	 * directory
	 * 
	 * @param filesMoved
	 *            number of distinct code files moved into another directory
	 */
	public void setFilesMoved(int filesMoved) {
		this.filesMoved = filesMoved;
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
	 * Returns number of frequent item sets
	 * 
	 * @return frequentItemSets
	 */
	public int getFrequentItemSets() {
		return frequentItemSets;
	}

	/**
	 * Sets number of frequent item sets
	 * 
	 * @param frequentItemSets
	 *            number of frequent item sets to set
	 */
	public void setFrequentItemSets(int frequentItemSets) {
		this.frequentItemSets = frequentItemSets;
	}

	/**
	 * Returns the localization metrics
	 * 
	 * @return localization
	 */
	public double getLocalization() {
		return localization;
	}

	/**
	 * Sets the localization metrics
	 * 
	 * @param localization
	 *            localization metrics to set
	 */
	public void setLocalization(double localization) {
		this.localization = localization;
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

	/**
	 * Should the output be comma separated values?
	 * 
	 * @return csv
	 */
	public boolean isCSV() {
		return csv;
	}

	/**
	 * Sets whether the output should be comma separated values
	 * 
	 * @param csv
	 *            should the output be comma separated values?
	 */
	public void setCSV(boolean csv) {
		this.csv = csv;
	}

	@Override
	public String toString() {
		// TODO: indicate whether all files or only code files
		String result = "";
		if (isCSV()) {
			if (getId() == 1) {
				result += "ID,Database,Code files,Code files with >= "
						+ minerInfo.getMinimumModifications()
						+ " commits,Files in frequent item sets,Code files with >= "
						+ minerInfo.getMinimumModifications()
						+ " commits in FIS (%),Code files added,"
						+ "Code files moved to another directory,"
						+ "Modules,Frequent item sets," + "Localization,"
						+ minerInfo.getCSVLabels() + "\n";
			}
			result += getId() + "," + getDatabase() + "," + getFiles() + ","
					+ getFilesModified() + "," + getFilesInFrequentItemSet()
					+ "," + includedFilesInFrequentItemSet() + ","
					+ getFilesAdded() + "," + getFilesMoved() + ","
					+ getModules() + "," + getFrequentItemSets() + ","
					+ getLocalization() + ",";
			minerInfo.setCSV(true);
		} else {
			result += "Database\t\t\t\t" + getDatabase() + "\n";
			result += "Code files\t\t\t\t" + getFiles() + "\n";
			result += "Code files with >= "
					+ minerInfo.getMinimumModifications() + " commits:\t\t"
					+ getFilesModified() + "\n";
			result += "Files in frequent item set:\t\t"
					+ getFilesInFrequentItemSet() + "\n";
			result += "Code files with >= "
					+ minerInfo.getMinimumModifications()
					+ " commits in FIS:\t" + includedFilesInFrequentItemSet()
					+ "%\n";
			result += "Code files added:\t\t\t" + getFilesAdded() + "\n";
			result += "Code files moved to another directory:\t"
					+ getFilesMoved() + "\n";
			result += "Modules:\t\t\t\t" + getModules() + "\n";
			result += "Frequent item sets:\t\t\t" + getFrequentItemSets()
					+ "\n";
			result += "Localization:\t\t\t\t" + getLocalization() + "\n\n";
			result += "Miner information\n";
		}
		result += minerInfo;
		return result;
	}
}
