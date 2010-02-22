package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * MySQL Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlRepositoryFileDAO extends JdbcDAO implements
		RepositoryFileDAO {

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */	
	public MysqlRepositoryFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public RepositoryFile find(int id) throws DataAccessException {
		// TODO: Implement
		return null;
	}

}
