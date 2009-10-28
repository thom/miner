package com.infosys.setlabs.miner.dao;

import java.io.File;

/**
 * Basket format DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface BasketFormatDAO {
	/**
	 * Type of the basket format to generate
	 */
	public static enum CodeFiles {
		ALL, RENAMED, NONE
	}

	/**
	 * Formats transactions to basket format
	 * 
	 * @param output
	 *            file to write the output to
	 * @param revs
	 *            should revisions be written in comments?
	 * @return transactions in basket format
	 */
	public String format(File output, boolean revs);
}
