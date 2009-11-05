package com.infosys.setlabs.miner.dao;

import java.io.File;

/**
 * Basket format DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface BasketFormatDAO {
	/**
	 * Formats transactions to basket format
	 * 
	 * @param output
	 *            file to write the output to
	 * @param revs
	 *            should revisions be written in comments?
	 * @return transactions in basket format
	 */
	public void format(File output, boolean revs);
}
