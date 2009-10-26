package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.miner.dao.BasketFormatDAO.CodeFiles;

/**
 * Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface MetricsDAO {
	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            name to set
	 */
	public void setName(String name);

	/**
	 * Returns code files
	 * 
	 * @return codeFiles
	 */
	public CodeFiles getCodeFiles();

	/**
	 * Sets code files
	 * 
	 * @param codeFiles
	 *            code files to set
	 */
	public void setCodeFiles(CodeFiles codeFiles);

	/**
	 * Returns modularization metrics
	 * 
	 * @return modularization
	 */
	public double modularization();
}
