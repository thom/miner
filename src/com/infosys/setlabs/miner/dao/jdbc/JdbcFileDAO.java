package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FileDAO;
import com.infosys.setlabs.miner.domain.File;

public abstract class JdbcFileDAO extends JdbcDAO implements FileDAO {

	protected static String SELECT_FILE_SQL = ""
			+ "SELECT id, file_name, repository_id "
			+ "FROM files WHERE id = ?";
	protected static String SELECT_FILES_SQL = ""
			+ "SELECT id, file_name, repository_id FROM files";
	protected static String SELECT_PATH_SQL = ""
			+ "SELECT f.id, f.file_name, fl.parent_id "
			+ "FROM files f, file_links fl "
			+ "WHERE f.id = fl.file_id AND f.id = ?";
	protected static String SELECT_NEWEST_FILE_NAME = ""
			+ "SELECT * FROM file_copies "
			+ "WHERE new_file_name <> '' AND from_id = to_id AND from_id = ? "
			+ "ORDER BY to_id, from_commit_id";

	public JdbcFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public File find(int id) throws DataAccessException {
		File result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(SELECT_FILE_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				// Create a new file
				result = new File(rs.getInt("id"));

				// Get the newest file name
				result.setFileName(getNewestFileName(id, rs
						.getString("file_name")));
				
				// Get path
				result.setPath(getPath(id));
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
	public Collection<File> findAll() throws DataAccessException {
		ArrayList<File> result = new ArrayList<File>();
		File file = null;
		int id;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_FILES_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");

				// Create a new file
				file = new File(id);

				// Get the newest file name
				file.setFileName(getNewestFileName(id, rs
						.getString("file_name")));
				
				// Get path
				file.setPath(getPath(id));				

				result.add(file);
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
			ps = this.getConnection().prepareStatement(SELECT_PATH_SQL);
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
			ps = this.getConnection().prepareStatement(SELECT_NEWEST_FILE_NAME);
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