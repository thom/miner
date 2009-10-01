package com.infosys.setlabs.miner.domain;

public class File {
	private int id;

	// Newest file name
	private String fileName;
	private String path;

	public File(int id) {
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
}