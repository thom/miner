package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;

/**
 * MySQL File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public abstract class MysqlFileDAO extends JdbcDAO {
	/**
	 * Table name
	 */
	public static String tableName = "files";	
	
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */	
	public MysqlFileDAO(Connection conn) {
		super(conn);
	}
	
	protected String getName() {
		return tableName;
	}	
	
	protected String setNewestFileNameSQL() {
		return String.format("UPDATE %s AS f1, "
				+ "(SELECT afn.file_id as id, afn.new_file_name "
				+ "FROM actions_file_names afn WHERE afn.type = 'V' "
				+ "ORDER BY afn.commit_id DESC) AS f2 "
				+ "SET f1.file_name = f2.new_file_name WHERE f1.id = f2.id",
				getName());
	}

	protected String selectNewestFileNameSQL() {
		return String.format("SELECT afn.file_id as id, afn.new_file_name "
				+ "FROM actions_file_names afn WHERE afn.type = 'V' "
				+ "AND afn.file_id = ? ORDER BY afn.commit_id", "files");
	}

	protected String selectPathSQL() {
		return String.format("SELECT f.id, f.file_name, fl.parent_id "
				+ "FROM %s f, file_links fl "
				+ "WHERE f.id = fl.file_id AND f.id = ? ORDER BY fl.commit_id",
				"files");
	}	

	public String getPath(int id) throws DataAccessException {
		return getPathRecursive(id);
	}

	private String getPathRecursive(int id) throws DataAccessException {
		// Base case: "./"
		if (id == -1) {
			return "." + System.getProperty("file.separator");
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(selectPathSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();

			// Retrieve the data from the result set
			if (rs.last()) {
				int parentId = rs.getInt("parent_id");
				if (parentId == -1) {
					return getPathRecursive(parentId)
							+ getNewestFileName(id, rs.getString("file_name"));
				} else {
					return getPathRecursive(parentId)
							+ System.getProperty("file.separator")
							+ getNewestFileName(id, rs.getString("file_name"));
				}
			} else {
				throw new DataAccessException("Couldn't find file with ID '"
						+ id + "'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		// This is never reached!
		return "";
	}

	protected String getNewestFileName(int id, String oldFileName)
			throws DataAccessException {
		String newFileName = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(
					selectNewestFileNameSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				newFileName = rs.getString("new_file_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		if (newFileName != null) {
			return newFileName;
		} else {
			return oldFileName;
		}
	}	
}
