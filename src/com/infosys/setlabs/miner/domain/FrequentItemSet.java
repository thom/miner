package com.infosys.setlabs.miner.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a frequent item set
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSet {
	private int id;

	// Cached items count to speed up calculations
	private int size;

	// Absolute item set support
	private int absoluteItemSetSupport;

	// Relative item set support
	private double relativeItemSetSupport;

	// Number of modules touched by item set
	private int modulesTouched;

	// Items
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
	 * Returns the number of items of this item set
	 * 
	 * @return size
	 */
	public int getSize() {
		return size == 0 ? items.size() : size;
	}

	/**
	 * Set the number of items of this item set
	 * 
	 * @param size
	 *            number of items in the item set
	 */
	public void setSize(int size) {
		this.size = size;
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
	 * Returns the number of modules that were touched in an item set
	 * 
	 * @return modulesTouched
	 */
	public int getModulesTouched() {
		if (modulesTouched == 0) {
			HashSet<Integer> modules = new HashSet<Integer>();
			for (MinerFile file : items) {
				modules.add(file.getModule().getId());
			}
			return modules.size();
		} else {
			return modulesTouched;
		}
	}

	/**
	 * Sets the number of modules that were touched in an item set
	 * 
	 * @param modulesTouched
	 *            number of modules touched
	 */
	public void setModulesTouched(int modulesTouched) {
		this.modulesTouched = modulesTouched;
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

	@Override
	public String toString() {		
		String result = "";
		result += "ID:\t\t\t\t" + getId() + "\n";
		result += "Size:\t\t\t\t" + getSize() + "\n";
		result += "Absolute item set support:\t" + getAbsoluteItemSetSupport()
				+ "\n";
		result += "Relative item set support:\t" + getRelativeItemSetSupport()
				+ "\n";
		result += "Modules touched:\t\t" + getModulesTouched() + "\n\n";

		result += "Files:\n-------------------------------------------------------------------------------";
		for (MinerFile file : getItems()) {
			result += "\n"
					+ file
					+ "\n-------------------------------------------------------------------------------";
		}

		return result;
	}
}
