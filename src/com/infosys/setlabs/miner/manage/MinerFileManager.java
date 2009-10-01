package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.domain.MinerFile;

public class MinerFileManager extends Manager {
	/**
	 * Creates a new miner file manager
	 * 
	 * @param connectionArgs
	 * @throws DataAccessException
	 */	
	public MinerFileManager(HashMap<String, String> connectionArgs)
			throws DataAccessException {
		super(connectionArgs);
	}
	

	public MinerFile find(int id) throws DataAccessException {
		return this.getFactory().getMinerFileDAO(this.getSession()).find(id);
	}

	public Collection<MinerFile> findAll() throws DataAccessException {
		return this.getFactory().getMinerFileDAO(this.getSession()).findAll();		
	}

	public void create(MinerFile object) throws DataAccessException {
		// TODO: transaction!
		this.getFactory().getMinerFileDAO(this.getSession()).create(object);		
	}

	public void delete(MinerFile object) throws DataAccessException {
		// TODO: transaction!		
		this.getFactory().getMinerFileDAO(this.getSession()).delete(object);		
	}

	public void update(MinerFile object) throws DataAccessException {
		// TODO: transaction!		
		this.getFactory().getMinerFileDAO(this.getSession()).update(object);		
	}		
}
