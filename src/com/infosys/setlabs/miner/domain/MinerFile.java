package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile extends RepositoryFile {
	private boolean deleted;
	private int modifications;
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
	}

	/**
	 * Was the file already deleted?
	 * 
	 * @return deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets a file to deleted
	 * 
	 * @param deleted
	 *            was the file already deleted?
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns how many modifications a file had
	 * 
	 * @return modifications
	 */
	public int getModifications() {
		return modifications;
	}

	/**
	 * Sets how many modifications a file had
	 * 
	 * @param modifications
	 *            number of modifications of this file
	 */
	public void setModifications(int modifications) {
		this.modifications = modifications;
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
		result += super.toString() + "\n";
		result += "Deleted?:\t" + isDeleted() + "\n";
		result += "Modifications:\t" + getModifications() + "\n\n";
		result += "Module:\n";
		result += getModule();
		return result;
	}
}
