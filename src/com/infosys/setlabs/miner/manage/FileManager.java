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

	public File getFile(int id) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).find(id);
	}

	public Collection<File> getAllFiles(File file) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).findAll();
	}

	public File getLatest(int id) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).findLatest(id);
	}

	public File getLatest(File file) throws DataAccessException {
		return this.getFactory().getFileDAO(this.getSession()).findLatest(file);
	}

	public String getPath(int id, boolean nameOnly) throws DataAccessException {
		if (nameOnly) {
			return this.getFactory().getFileDAO(this.getSession()).find(id)
					.getFileName();
		} else {
			return this.getFactory().getFileDAO(this.getSession()).findPath(id);
		}
	}

	public String getPath(File file, boolean nameOnly) throws DataAccessException {
		return getPath(file.getId(), nameOnly);
	}
}
