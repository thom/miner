package com.infosys.setlabs.miner.domain;

/**
 * Represents a miner file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerFile {
	private int id;

	// Newest file name
	private String fileName;
	private String path;
	private Type type;
	private int modifications;
	private Module module;

	/**
	 * File types
	 */
	public static enum Type {
		CODE, BUILD, UI, I18N, DOCUMENTATION, DEVEL_DOC, PACKAGE, IMAGE, MULTIMEDIA, DIRECTORY, UNKNOWN
	}

	/**
	 * Creates a new miner file
	 */
	public MinerFile() {
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
	 * @return filename
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
	 * Returns the directory
	 * 
	 * @return directory Path - Filename
	 */
	public String getDirectory() {
		return this.path.substring(0, path.length() - fileName.length());
	}

	/**
	 * Is the file of type code?
	 * 
	 * @return is file of type code?
	 */
	public boolean isCode() {
		return type == Type.CODE;
	}

	/**
	 * Return the file type
	 * 
	 * @return type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the file type
	 * 
	 * @param type
	 *            type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Set the file type
	 * 
	 * @param typeName
	 *            type to set
	 */
	public void setType(String typeName) {
		type = Type.valueOf(typeName.toUpperCase().replace("-", "_"));
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
		result += "ID:\t\t" + getId() + "\n";
		result += "File name:\t" + getFileName() + "\n";
		result += "Path:\t\t" + getPath() + "\n";
		result += "Directory:\t" + getDirectory() + "\n";
		result += "Type:\t\t" + getType().toString().toLowerCase() + "\n";
		result += "Modifications:\t" + getModifications() + "\n\n";
		result += "Module:\n";
		result += getModule();
		return result;
	}
}
