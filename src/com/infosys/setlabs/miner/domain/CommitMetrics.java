package com.infosys.setlabs.miner.domain;

public class CommitMetrics {
	private int id;
	private String start;
	private String stop;
	private IdType idType;
	private double localization;
	// TODO: Add number of files moves	
	private int commits;

	private boolean csv;

	/**
	 * ID types
	 */
	public enum IdType {
		ID {
			public String toString() {
				return "commit ID";
			}
		},

		REV {
			public String toString() {
				return "revision";
			}
		},

		TAG {
			public String toString() {
				return "tag";
			}
		}
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
	 * Returns the localization
	 * 
	 * @return localization
	 */
	public double getLocalization() {
		return localization;
	}

	/**
	 * Sets the localization
	 * 
	 * @param localization
	 *            localization to set
	 */
	public void setLocalization(double localization) {
		this.localization = localization;
	}

	/**
	 * Returns the number of commits with more than one code file
	 * 
	 * @return commits
	 */
	public int getCommits() {
		return commits;
	}

	/**
	 * Set the number of commits with more than one code file
	 * 
	 * @param commits
	 *            number of commits with more than one code file to set
	 */
	public void setCommits(int commits) {
		this.commits = commits;
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
			if (getId() == 1) {
				result += "ID,Localization,Commits,Start,Stop,Type\n";
			}
			result += getId() + "," + getLocalization() + "," + getCommits()
					+ "," + getStart() + "," + getStop() + "," + getIdType()
					+ "s";
		} else {
			result += "ID:\t\t" + getId() + "\n";
			result += "Start ID:\t" + getStart() + "\n";
			result += "Stop ID:\t" + getStop() + "\n";
			result += "ID types:\t" + getIdType() + "s\n";
			result += "Localization:\t" + getLocalization() + "\n";
			result += "Commits:\t" + getCommits();
		}
		return result;
	}
}
