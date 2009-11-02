package com.infosys.setlabs.miner.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a commit
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Commit {
	int id;

	// Revision
	String rev;

	// Commit comment
	String comment;

	// Number of files touched by commit
	int filesTouched;

	// Number of modules touched by commit
	int modulesTouched;

	// Files
	private List<MinerFile> files = new ArrayList<MinerFile>();

	/**
	 * Creates a new empty log
	 */
	public Commit() {
	}

	/**
	 * Creates a new empty log with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public Commit(int id) {
		setId(id);
	}

	/**
	 * Returns the commit ID
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the commit ID
	 * 
	 * @param id
	 *            ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the revision
	 * 
	 * @return rev
	 */
	public String getRev() {
		return rev;
	}

	/**
	 * Sets the revision
	 * 
	 * @param rev
	 *            revision to set
	 */
	public void setRev(String rev) {
		this.rev = rev;
	}

	/**
	 * Returns the comment
	 * 
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment
	 * 
	 * @param comment
	 *            comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the number of files touched
	 * 
	 * @return filesTouched
	 */
	public int getFilesTouched() {
		return filesTouched;
	}

	/**
	 * Sets the number of files touched
	 * 
	 * @param filesTouched
	 *            number of files touched
	 */
	public void setFilesTouched(int filesTouched) {
		this.filesTouched = filesTouched;
	}

	/**
	 * Returns the number of modules touched
	 * 
	 * @return modulesTouched
	 */
	public int getModulesTouched() {
		return modulesTouched;
	}

	/**
	 * Sets the number of modules touched
	 * 
	 * @param modulesTouched
	 *            number of modules touched
	 */
	public void setModulesTouched(int modulesTouched) {
		this.modulesTouched = modulesTouched;
	}

	/**
	 * Returns the files belonging to the commit
	 * 
	 * @return files
	 */
	public List<MinerFile> getFiles() {
		return files;
	}

	/**
	 * Adds a new file to the commit
	 * 
	 * @param file
	 *            file to add
	 */
	public void addFile(MinerFile file) {
		this.files.add(file);
	}

	@Override
	public String toString() {
		String result = "";
		result += "ID:\t\t\t" + getId() + "\n";
		result += "Revision:\t\t" + getRev() + "\n";
		result += "Files touched:\t\t" + getFilesTouched() + "\n";
		result += "Modules touched:\t" + getModulesTouched() + "\n";
		result += "\n" + getComment() + "\n";

		result += "Files:\n-------------------------------------------------------------------------------";
		for (MinerFile file : getFiles()) {
			result += "\n"
					+ file
					+ "\n-------------------------------------------------------------------------------";
		}

		return result;
	}
}
