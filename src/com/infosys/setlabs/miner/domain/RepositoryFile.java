package com.infosys.setlabs.miner.domain;

public class RepositoryFile {
	private int id;

	// Newest file name
	private String fileName;
	private String path;
	private Type type;

	public enum Type {
		CODE, BUILD, UI, I18N, DOCUMENTATION, DEVEL_DOC, PACKAGE, IMAGE, MULTIMEDIA, UNKNOWN
	}

	public RepositoryFile(int id) {
		this.setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDirectory() {
		return this.path.substring(0, path.length() - fileName.length());
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
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