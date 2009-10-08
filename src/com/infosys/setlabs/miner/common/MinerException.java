package com.infosys.setlabs.miner.common;

/**
 * Wraps exceptions
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Wraps underlying exception.
	 * 
	 * @param e
	 *            exception to wrap
	 */
	public MinerException(Exception e) {
		super(e.getMessage());
		// e.printStackTrace();
	}
}
