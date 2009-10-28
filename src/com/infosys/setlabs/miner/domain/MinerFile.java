package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile extends RepositoryFile {
	private MinerModule module;

	/**
	 * Creates a new miner file
	 */
	public MinerFile() {
		super();
	}

	/**
	 * Creates a new miner file with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public MinerFile(int id) {
		super(id);
	}

	/**
	 * Returns the module
	 * 
	 * @return module
	 */
	public MinerModule getModule() {
		return module;
	}

	/**
	 * Sets the module
	 * 
	 * @param module
	 *            module to set
	 */
	public void setModule(MinerModule module) {
		this.module = module;
	}

	/**
	 * Change module ID
	 * 
	 * @param id
	 *            module ID to set
	 */
	public void changeModuleId(int id) {
		this.module.setId(id);
	}

	@Override
	public String toString() {
		String result = "";
		result += super.toString() + "\n\n";
		result += "Module:\n";
		result += getModule();
		return result;
	}
}
