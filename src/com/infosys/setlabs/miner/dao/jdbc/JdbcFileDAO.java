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

	public JdbcFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public File find(int id) throws DataAccessException {
		File file = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(SELECT_FILE_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				file = new File(rs.getInt("id"));
				file.setFileName(rs.getString("file_name"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// Do nothing!
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return file;
	}

	@Override
	public Collection<File> findAll() throws DataAccessException {
		ArrayList<File> result = new ArrayList<File>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_FILES_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				File file = new File(rs.getInt("id"));
				file.setFileName(rs.getString("file_name"));
				result.add(file);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// Do nothing!
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public File findLatest(int id) throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public File findLatest(File file) throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public String findPath(int id) throws DataAccessException {
		return findPathRecursive(id);
	}

	@Override
	public String findPath(File file) throws DataAccessException {
		return findPath(file.getId());
	}

	private String findPathRecursive(int id) throws DataAccessException {
		// Base case
		if (id == -1) {
			return "";
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
					return findPathRecursive(rs.getInt("parent_id"))
							+ rs.getString("file_name");
				} else {
					return findPathRecursive(rs.getInt("parent_id")) + "/"
							+ rs.getString("file_name");
				}
			} else {
				throw new DataAccessException("Couldn't find file with ID '"
						+ id + "'");
			}
		} catch (SQLException e) {
			// Do nothing!
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		// This is never reached!
		return "";
	}
}