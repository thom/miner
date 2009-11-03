package com.infosys.setlabs.miner.domain;

public class CommitMetrics {
	private int id;
	private String start;
	private String stop;
	private IdType idType;
	private double modularization;
	private boolean csv;

	/**
	 * ID types
	 */
	public enum IdType {
		ID, REV, TAG
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
	 * Returns the start ID
	 * 
	 * @return start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Sets the start ID
	 * 
	 * @param start
	 *            ID to start with
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * Returns the stop ID
	 * 
	 * @return stop
	 */
	public String getStop() {
		return stop;
	}

	/**
	 * Sets the stop ID
	 * 
	 * @param stop
	 *            ID to stop with
	 */
	public void setStop(String stop) {
		this.stop = stop;
	}

	/**
	 * Returns the ID type
	 * 
	 * @return idType
	 */
	public IdType getIdType() {
		return idType;
	}

	/**
	 * Sets the ID type
	 * 
	 * @param idType
	 *            ID type to set
	 */
	public void setIdType(IdType idType) {
		this.idType = idType;
	}

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

	/**
	 * Should the output be comma separated values?
	 * 
	 * @return csv
	 */
	public boolean isCSV() {
		return csv;
	}

	/**
	 * Sets whether the output should be comma separated values
	 * 
	 * @param csv
	 *            should the output be comma separated values?
	 */
	public void setCSV(boolean csv) {
		this.csv = csv;
	}

	@Override
	public String toString() {
		String result = "";
		if (isCSV()) {
			result += getId() + "," + getModularization() + "," + "From "
					+ getStart() + " to " + getStop() + " (" + getIdType()
					+ "s)";
		} else {
			result += "ID:\t\t" + getId() + "\n";
			result += "Start ID:\t" + getStart() + "\n";
			result += "Stop ID:\t" + getStop() + "\n";
			result += "ID types:\t" + getIdType() + "s\n";
			result += "Modularization:\t" + getModularization();
		}
		return result;
	}
}
