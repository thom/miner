package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * MySQL Repository File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlRepositoryFileDAO extends JdbcDAO
		implements
			RepositoryFileDAO {
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
	public MysqlRepositoryFileDAO(Connection conn) {
		super(conn);
	}

	protected String selectSQL() {
		return String.format(
				"SELECT f.id, f.file_name, f.repository_id, ft.type "
						+ "FROM %s f LEFT JOIN file_types ft "
						+ "ON f.id = ft.file_id WHERE f.id = ?", tableName);
	}

	protected String selectAllSQL() {
		return String.format(
				"SELECT f.id, f.file_name, f.repository_id, ft.type "
						+ "FROM %s f LEFT JOIN file_types ft "
						+ "ON f.id = ft.file_id", tableName);
	}

	protected String selectDeletedSQL() {
		return String.format("SELECT f.id, 1 AS deleted "
				+ "FROM files f, actions a WHERE f.id = a.file_id "
				+ "AND a.type = 'D' AND f.id = ?", tableName);
	}
	protected String selectModificationsSQL() {
		return "SELECT f.id, COUNT(f.id) AS modifications "
				+ "FROM files f, actions a "
				+ "WHERE a.file_id = f.id AND f.id = ? GROUP BY f.id";
	}

	protected String selectPathSQL() {
		return String.format("SELECT f.id, f.file_name, fl.parent_id "
				+ "FROM %s f, file_links fl "
				+ "WHERE f.id = fl.file_id AND f.id = ?", tableName);
	}

	protected String selectNewestFileNameSQL() {
		return "SELECT new_file_name FROM file_copies "
				+ "WHERE new_file_name <> '' AND from_id = to_id AND from_id = ? "
				+ "ORDER BY to_id, from_commit_id";
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
				// Create a new file
				result = new RepositoryFile(rs.getInt("id"));

				// Set type
				String type = rs.getString("type");
				result.setType(type == null ? "directory" : type);

				// Get the newest file name
				result.setFileName(getNewestFileName(id, rs
						.getString("file_name")));

				// Get path
				result.setPath(getPath(id));

				// Set deleted
				result.setDeleted(isDeleted(id));

				// Set number of modifications
				result.setModifications(getModifications(id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public Collection<RepositoryFile> findAll() throws DataAccessException {
		ArrayList<RepositoryFile> result = new ArrayList<RepositoryFile>();
		RepositoryFile repositoryFile = null;
		int id;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectAllSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");

				// Create a new file
				repositoryFile = new RepositoryFile(id);

				// Get the newest file name
				repositoryFile.setFileName(getNewestFileName(id, rs
						.getString("file_name")));

				// Get path
				repositoryFile.setPath(getPath(id));

				// Set type
				String type = rs.getString("type");
				repositoryFile.setType(type == null ? "directory" : type);

				// Set deleted
				repositoryFile.setDeleted(isDeleted(id));

				// Set number of modifications
				repositoryFile.setModifications(getModifications(id));

				result.add(repositoryFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	private boolean isDeleted(int id) throws DataAccessException {
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(selectDeletedSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getBoolean("deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		return result;
	}

	private int getModifications(int id) throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection()
					.prepareStatement(selectModificationsSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("modifications");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		return result;
	}

	private String getPath(int id) throws DataAccessException {
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
			rs.beforeFirst();
			if (rs.next()) {
				if (rs.getInt("parent_id") == -1) {
					return getPathRecursive(rs.getInt("parent_id"))
							+ getNewestFileName(id, rs.getString("file_name"));
				} else {
					return getPathRecursive(rs.getInt("parent_id"))
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

	private String getNewestFileName(int id, String oldFileName)
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