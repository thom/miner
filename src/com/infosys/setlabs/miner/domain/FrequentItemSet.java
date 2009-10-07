package com.infosys.setlabs.miner.domain;

import java.util.ArrayList;
import java.util.List;

public class FrequentItemSet {
	private int id;
	private int absoluteItemSetSupport;
	private double relativeItemSetSupport;
	private List<MinerFile> items = new ArrayList<MinerFile>();
	
	public FrequentItemSet(int id) {
		setId(id);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAbsoluteItemSetSupport() {
		return absoluteItemSetSupport;
	}
	
	public void setAbsoluteItemSetSupport(int absoluteItemSetSupport) {
		this.absoluteItemSetSupport = absoluteItemSetSupport;
	}
	
	public double getRelativeItemSetSupport() {
		return relativeItemSetSupport;
	}
	
	public void setRelativeItemSetSupport(double relativeItemSetSupport) {
		this.relativeItemSetSupport = relativeItemSetSupport;
	}
	
	public List<MinerFile> getItems() {
		return items;
	}
	
	public void addItem(MinerFile item) {
		this.items.add(item);
	}
}
