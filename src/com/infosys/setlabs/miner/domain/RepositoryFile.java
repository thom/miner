package com.infosys.setlabs.miner.domain;

/**
 * Represents a repository file
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class RepositoryFile {
	private int id;

	// Newest file name
	private String fileName;
	private String path;
	private Type type;

	/**
	 * File types
	 */
	public static enum Type {
		CODE, BUILD, UI, I18N, DOCUMENTATION, DEVEL_DOC, PACKAGE, IMAGE, MULTIMEDIA, UNKNOWN
	}

	/**
	 * Creates a new repository file with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public RepositoryFile(int id) {
		this.setId(id);
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
		if (typeName.equalsIgnoreCase("code")) {
			type = Type.CODE;
		} else if (typeName.equalsIgnoreCase("build")) {
			type = Type.BUILD;
		} else if (typeName.equalsIgnoreCase("ui")) {
			type = Type.UI;
		} else if (typeName.equalsIgnoreCase("i18n")) {
			type = Type.I18N;
		} else if (typeName.equalsIgnoreCase("documentation")) {
			type = Type.DOCUMENTATION;
		} else if (typeName.equalsIgnoreCase("devel-doc")) {
			type = Type.DEVEL_DOC;
		} else if (typeName.equalsIgnoreCase("package")) {
			type = Type.PACKAGE;
		} else if (typeName.equalsIgnoreCase("image")) {
			type = Type.IMAGE;
		} else if (typeName.equalsIgnoreCase("multimedia")) {
			type = Type.MULTIMEDIA;
		} else {
			type = Type.UNKNOWN;
		}
	}
}