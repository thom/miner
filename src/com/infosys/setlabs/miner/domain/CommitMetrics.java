package com.infosys.setlabs.miner.domain;

public class CommitMetrics {
	private int id;
	private String database;
	private double localization;
	private int filesMoved;
	private int commits;
	private int minimumCommitSize;
	private int maximumCommitSize;

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
	 * Sets the database
	 * 
	 * @param database
	 *            database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Returns the database
	 * 
	 * @return database
	 */
	public String getDatabase() {
		return database;
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
	 * Returns the number of distinct code files that have been moved into
	 * another directory
	 * 
	 * @return filesMoved
	 */
	public int getFilesMoved() {
		return filesMoved;
	}

	/**
	 * Sets the number of distinct code files that have been moved into another
	 * directory
	 * 
	 * @param filesMoved
	 *            number of distinct code files moved into another directory
	 */
	public void setFilesMoved(int filesMoved) {
		this.filesMoved = filesMoved;
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
	 * Returns the minimum commit size
	 * 
	 * @return minimumCommitSize
	 */
	public int getMinimumCommitSize() {
		return minimumCommitSize;
	}

	/**
	 * Sets the minimum commit size
	 * 
	 * @param minimumCommitSize
	 *            minimum commit size to set
	 */
	public void setMinimumCommitSize(int minimumCommitSize) {
		this.minimumCommitSize = minimumCommitSize;
	}

	/**
	 * Returns the maximum commit size
	 * 
	 * @return maximumCommitSize
	 */
	public int getMaximumCommitSize() {
		return maximumCommitSize;
	}

	/**
	 * Sets the maximum commit size
	 * 
	 * @param maximumCommitSize
	 *            maximum commit size to set
	 */
	public void setMaximumCommitSize(int maximumCommitSize) {
		this.maximumCommitSize = maximumCommitSize;
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
				result += "ID,Database,Localization,Moves,Commits,"
						+ "Minimum commit size,Maximum commit size\n";
			}
			result += getId() + "," + getDatabase() + "," + getLocalization()
					+ "," + getFilesMoved() + "," + getCommits() + ","
					+ getMinimumCommitSize() + "," + getMaximumCommitSize();
		} else {
			result += "Database:\t\t" + getDatabase() + "\n";
			result += "Localization:\t\t" + getLocalization() + "\n";
			result += "Moves to other dir:\t" + getFilesMoved() + "\n";
			result += "Commits:\t\t" + getCommits() + "\n";
			result += "Minimum commit size:\t" + getMinimumCommitSize() + "\n";
			result += "Maximum commit size:\t" + getMaximumCommitSize();
		}
		return result;
	}
}
