package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner module
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerModule {
	private int id;
	private String moduleName;
	private boolean codeFiles;
	private boolean renamedFiles;
	private boolean renamedCodeFiles;

	/**
	 * Creates a miner module with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public MinerModule(int id) {
		setId(id);
	}

	/**
	 * Creates a new miner module with module name <code>moduleName</code>
	 * 
	 * @param moduleName
	 *            module name to create
	 */
	public MinerModule(String moduleName) {
		setModuleName(moduleName);
	}

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
	 *            ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the module name
	 * 
	 * @return moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * Sets the module name
	 * 
	 * @param moduleName
	 *            module name to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * Has the module code files?
	 * 
	 * @return codeFiles
	 */
	public boolean hasCodeFiles() {
		return codeFiles;
	}

	/**
	 * Sets code files
	 * 
	 * @param codeFiles
	 *            has the module code files?
	 */
	public void setCodeFiles(boolean codeFiles) {
		this.codeFiles = codeFiles;
	}

	/**
	 * Has the module renamed files?
	 * 
	 * @return renamedFiles
	 */
	public boolean hasRenamedFiles() {
		return renamedFiles;
	}

	/**
	 * Sets renamed files
	 * 
	 * @param renamedFiles
	 *            has the module renamed files?
	 */
	public void setRenamedFiles(boolean renamedFiles) {
		this.renamedFiles = renamedFiles;
	}

	/**
	 * Has the module renamed code files?
	 * 
	 * @return renamed code files
	 */
	public boolean hasRenamedCodeFiles() {
		return renamedCodeFiles;
	}

	/**
	 * Sets renamed code files
	 * 
	 * @param renamedCodeFiles
	 *            has the module renamed code files?
	 */
	public void setRenamedCodeFiles(boolean renamedCodeFiles) {
		this.renamedCodeFiles = renamedCodeFiles;
	}

	@Override
	public String toString() {
		String result = "";
		result += "ID:\t\t\t" + getId() + "\n";
		result += "Module name:\t\t" + getModuleName() + "\n";
		result += "Code files:\t\t" + hasCodeFiles() + "\n";
		result += "Renamed files:\t\t" + hasRenamedFiles() + "\n";
		result += "Renamed code files:\t" + hasRenamedCodeFiles();
		return result;
	}
}
