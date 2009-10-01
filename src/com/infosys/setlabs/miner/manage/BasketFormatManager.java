package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;

public class BasketFormatManager extends Manager {
	/**
	 * Creates a new basket format manager
	 * 
	 * @throws MinerException
	 */
	public BasketFormatManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	public String format(boolean allFiles, boolean revs) throws MinerException {
		try {
			return this.getFactory().getBasketFormatDAO(this.getSession())
					.format(allFiles, revs);
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}
}
