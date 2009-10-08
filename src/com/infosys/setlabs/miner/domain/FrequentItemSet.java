package com.infosys.setlabs.miner.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a frequent item set
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSet {
	private int id;
	private int absoluteItemSetSupport;
	private double relativeItemSetSupport;
	private List<MinerFile> items = new ArrayList<MinerFile>();

	/**
	 * Creates a new empty frequent item set
	 */
	public FrequentItemSet() {
	}

	/**
	 * Creates a new empty frequent item set with id <code>id</code>
	 * 
	 * @param id
	 *            ID to create
	 */
	public FrequentItemSet(int id) {
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
	 * Returns the absolute item set support
	 * 
	 * @return absoluteItemSetSupport
	 */
	public int getAbsoluteItemSetSupport() {
		return absoluteItemSetSupport;
	}

	/**
	 * Sets the absolute item set support
	 * 
	 * @param absoluteItemSetSupport
	 *            absolute item set support to set
	 */
	public void setAbsoluteItemSetSupport(int absoluteItemSetSupport) {
		this.absoluteItemSetSupport = absoluteItemSetSupport;
	}

	/**
	 * Returns the relative item set support
	 * 
	 * @return realitveItemSetSupport
	 */
	public double getRelativeItemSetSupport() {
		return relativeItemSetSupport;
	}

	/**
	 * Sets the relative item set support
	 * 
	 * @param relativeItemSetSupport
	 *            relative item set support to set
	 */
	public void setRelativeItemSetSupport(double relativeItemSetSupport) {
		this.relativeItemSetSupport = relativeItemSetSupport;
	}

	/**
	 * Returns the items of the frequent item set
	 * 
	 * @return items
	 */
	public List<MinerFile> getItems() {
		return items;
	}

	/**
	 * Adds a new item to the frequent item set
	 * 
	 * @param item
	 *            item to add
	 */
	public void addItem(MinerFile item) {
		this.items.add(item);
	}
}
