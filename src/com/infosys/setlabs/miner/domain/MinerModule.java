package com.infosys.setlabs.miner.domain;

public class MinerModule {
	private int id;
	private String moduleName;
	
	public MinerModule(int id) {
		this.setId(id);
	}
	
	public MinerModule(String moduleName) {
		this.setModuleName(moduleName);
	}	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
