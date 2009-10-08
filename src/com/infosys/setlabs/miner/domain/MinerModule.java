package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner module
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerModule {
	private int id;
	private String moduleName;

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
}
