package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile extends RepositoryFile {
	private Module module;

	/**
	 * Creates a new miner file
	 */
	public MinerFile() {
	}

	/**
	 * Creates a new miner file
	 * 
	 * @param repositoryFile
	 */
	public MinerFile(RepositoryFile repositoryFile) {
		setId(repositoryFile.getId());
		setFileName(repositoryFile.getFileName());
		setPath(repositoryFile.getPath());
		setType(repositoryFile.getType());
		setModifications(repositoryFile.getModifications());
	}

	/**
	 * Returns the module
	 * 
	 * @return module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Sets the module
	 * 
	 * @param module
	 *            module to set
	 */
	public void setModule(Module module) {
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
