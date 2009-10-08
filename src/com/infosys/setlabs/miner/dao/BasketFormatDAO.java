package com.infosys.setlabs.miner.dao;

/**
 * Basket format DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface BasketFormatDAO {
	/**
	 * Formats transactions to basket format
	 * 
	 * @param allFiles
	 *            should all files be added to transactions?
	 * @param revs
	 *            should revisions be written in comments?
	 * @return transactions in basket format
	 */
	public String format(boolean allFiles, boolean revs);
}
