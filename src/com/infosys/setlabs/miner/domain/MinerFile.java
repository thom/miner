package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile {
	private int id;
	private String fileName;
	private String path;
	private RepositoryFile repositoryFile;
	private MinerModule module;

	/**
	 * Creates a new miner file
	 */
	public MinerFile() {
	}

	/**
	 * Creates a new miner file with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public MinerFile(int id) {
		setId(id);
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
	 * Returns the filename
	 * 
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the filename
	 * 
	 * @param fileName
	 *            filename to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the path
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path
	 * 
	 * @param path
	 *            path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * Returns the repository file
	 * 
	 * @return repositoryFile
	 */
	public RepositoryFile getRepositoryFile() {
		return repositoryFile;
	}

	/**
	 * Sets the repository file
	 * 
	 * @param repositoryFile
	 *            repository file to set
	 */
	public void setRepositoryFile(RepositoryFile repositoryFile) {
		this.repositoryFile = repositoryFile;
	}
}
