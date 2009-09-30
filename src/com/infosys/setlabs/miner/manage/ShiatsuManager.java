package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;

public class ShiatsuManager extends Manager {
	/**
	 * Creates a new shiatsu manager
	 * 
	 * @param connectionArgs
	 * @throws DataAccessException
	 */
	public ShiatsuManager(HashMap<String, String> connectionArgs)
			throws DataAccessException {
		super(connectionArgs);
	}
	
	public void massage() throws DataAccessException {
		this.getFactory().getShiatsuDAO(this.getSession()).massage();		
	}
}
