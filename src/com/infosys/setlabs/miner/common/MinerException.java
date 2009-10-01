package com.infosys.setlabs.miner.common;

public class MinerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Wraps underlying exception.
	 * 
	 * @param e
	 */
	public MinerException(Exception e) {
		super(e.getMessage());
		e.printStackTrace();
	}
}
