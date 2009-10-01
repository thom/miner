package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.domain.MinerModule;

public class MinerModuleManager extends Manager {
	/**
	 * Creates a new miner module manager
	 * 
	 * @param connectionArgs
	 * @throws DataAccessException
	 */
	public MinerModuleManager(HashMap<String, String> connectionArgs)
			throws DataAccessException {
		super(connectionArgs);
	}

	public MinerModule find(int id) throws DataAccessException {
		return this.getFactory().getMinerModuleDAO(this.getSession()).find(id);
	}

	public Collection<MinerModule> findAll() throws DataAccessException {
		return this.getFactory().getMinerModuleDAO(this.getSession()).findAll();		
	}

	public void create(MinerModule object) throws DataAccessException {
		// TODO: transaction!
		this.getFactory().getMinerModuleDAO(this.getSession()).create(object);		
	}

	public void delete(MinerModule object) throws DataAccessException {
		// TODO: transaction!		
		this.getFactory().getMinerModuleDAO(this.getSession()).delete(object);		
	}

	public void update(MinerModule object) throws DataAccessException {
		// TODO: transaction!		
		this.getFactory().getMinerModuleDAO(this.getSession()).update(object);		
	}	
}
