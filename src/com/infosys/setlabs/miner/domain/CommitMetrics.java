package com.infosys.setlabs.miner.domain;

public class CommitMetrics {
	private double modularization;

	/**
	 * Returns the modularization
	 * 
	 * @return modularization
	 */
	public double getModularization() {
		return modularization;
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
		result += "Modularization:\t" + getModularization();
		return result;
	}
}
