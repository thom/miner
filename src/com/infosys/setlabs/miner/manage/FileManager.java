package com.infosys.setlabs.miner.manage;

import java.util.Collection;
import java.util.HashMap;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.domain.File;

public class FileManager extends Manager {
	/**
	 * Creates a new file manager
	 * 
	 * @throws DataAccessException
	 */
	public FileManager(HashMap<String, String> connectionArgs)
			throws DataAccessException {
		super(connectionArgs);
	}

	public File find(int id) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).find(id);
	}

	public Collection<File> findAll(File file) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).findAll();
	}

	public String findPath(int id, boolean nameOnly) throws DataAccessException {
		if (nameOnly) {
			return this.getFactory().getFileDAO(this.getSession()).find(id)
					.getFileName();
		} else {
			return this.getFactory().getFileDAO(this.getSession()).findPath(id);
		}
	}

	public String findPath(File file, boolean nameOnly) throws DataAccessException {
		return findPath(file.getId(), nameOnly);
	}
}
