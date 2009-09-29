package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;

public class BasketFormatManager extends Manager {
	/**
	 * Creates a new basket format manager
	 * 
	 * @throws DataAccessException
	 */
	public BasketFormatManager(HashMap<String, String> connectionArgs)
			throws DataAccessException {
		super(connectionArgs);
	}

	public String format(boolean allFiles, boolean revs) throws DataAccessException {
		return this.getFactory().getBasketFormatDAO(this.getSession()).format(allFiles, revs);
	}
}
