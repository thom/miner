package com.infosys.setlabs.miner.domain;

import java.util.ArrayList;
import java.util.List;

public class MinerFrequentItemSet {
	private int id;
	private int absoluteItemSetSupport;
	private float relativeItemSetSupport;
	private List<MinerFile> items = new ArrayList<MinerFile>();
	
	public MinerFrequentItemSet(int id) {
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
	
	public float getRelativeItemSetSupport() {
		return relativeItemSetSupport;
	}
	
	public void setRelativeItemSetSupport(float relativeItemSetSupport) {
		this.relativeItemSetSupport = relativeItemSetSupport;
	}
	
	public List<MinerFile> getItems() {
		return items;
	}
	
	public void addItem(MinerFile item) {
		this.items.add(item);
	}
}
