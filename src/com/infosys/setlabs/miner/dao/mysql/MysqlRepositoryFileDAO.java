package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * MySQL Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlRepositoryFileDAO extends MysqlFileDAO implements
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

	protected String selectSQL() {
		return String.format("SELECT f.id, f.file_name, "
				+ "COALESCE(ft.type, 'directory') as type "
				+ "FROM %s f LEFT JOIN file_types ft ON f.id = ft.file_id "
				+ "WHERE  f.id = ?", getName());
	}

	protected String isDeletedSQL() {
		return String.format("SELECT * FROM %s f, miner_actions a "
				+ "WHERE f.id = a.file_id AND a.type = 'D' AND f.id = ?",
				getName());
	}

	@Override
	public RepositoryFile find(int id) throws DataAccessException {
		RepositoryFile result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new RepositoryFile();
				result.setId(rs.getInt("id"));
				result.setDeleted(isDeleted(id));
				result.setFileName(getNewestFileName(id, rs
						.getString("file_name")));
				result.setPath(getPath(id));
				result.setType(rs.getString("type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	private boolean isDeleted(int id) {
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(isDeletedSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			result = rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

}
