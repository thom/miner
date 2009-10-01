package com.infosys.setlabs.miner.domain;

public class MinerFile {
	private int id;
	private String fileName;
	private String path;
	private MinerModule module;
	
	public MinerFile(int id) {
		setId(id);
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

	public MinerModule getModule() {
		return module;
	}

	public void setModule(MinerModule module) {
		this.module = module;
	}
}
