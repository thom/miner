package com.infosys.setlabs.miner.domain;

public class CommitMetrics {
	private int id;
	// TODO: private String start;
	// TODO: private String stop;
	private double modularization;
	// TODO: private boolean human = true;

	/**
	 * Returns the modularization
	 * 
	 * @return modularization
	 */
	public double getModularization() {
		return modularization;
	}

	/**
	 * Sets ID
	 * 
	 * @param id
	 *            ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns ID
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the modularization
	 * 
	 * @param modularization
	 *            modularization to set
	 */
	public void setModularization(double modularization) {
		this.modularization = modularization;
	}

	@Override
	public String toString() {
		String result = "";
		result += "ID:\t\t" + getId() + "\n";
		result += "Modularization:\t" + getModularization();
		return result;
	}
}
