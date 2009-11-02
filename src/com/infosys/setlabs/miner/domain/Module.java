package com.infosys.setlabs.miner.domain;

/**
 * Represents a module
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Module {
	private int id;
	private String moduleName;

	/**
	 * Creates a module with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public Module(int id) {
		setId(id);
	}

	/**
	 * Creates a new module with module name <code>moduleName</code>
	 * 
	 * @param moduleName
	 *            module name to create
	 */
	public Module(String moduleName) {
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

	@Override
	public String toString() {
		String result = "";
		result += "ID:\t\t" + getId() + "\n";
		result += "Module name:\t" + getModuleName();
		return result;
	}
}
