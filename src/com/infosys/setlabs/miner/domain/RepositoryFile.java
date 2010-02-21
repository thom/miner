package com.infosys.setlabs.miner.domain;

/**
 * Represents a repository file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class RepositoryFile extends MinerFile {
	private boolean deleted;

	/**
	 * Is the file deleted?
	 * 
	 * @return deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Set file as deleted/not deleted
	 * 
	 * @param deleted
	 *            file is deleted/not deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		String result = "";
		result += "This is a repository file, not a miner file!\n";
		result += "ID:\t\t" + getId() + "\n";
		result += "Deleted?\t" + isDeleted() + "\n";
		result += "File name:\t" + getFileName() + "\n";
		result += "Path:\t\t" + getPath() + "\n";
		result += "Directory:\t" + getDirectory() + "\n";
		result += "Type:\t\t" + getType().toString().toLowerCase();
		return result;
	}
}
