package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile extends RepositoryFile {
	// TODO: add all columns from repository file and remove repositoryFile:
	// +: "fileName", "path", "type", "renamed"
	// -: "repositoryFile"
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

	@Override
	public String toString() {
		String result = "";
		result += super.toString() + "\n\n";
		result += "Module:\n";
		result += getModule();
		return result;
	}
}
