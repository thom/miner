package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;
import com.infosys.setlabs.miner.domain.RepositoryFile;

/**
 * MySQL Miner File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMinerFileDAO extends JdbcDAO implements MinerFileDAO {
	
	// TODO: use string to save table names

	protected static String CREATE_MINER_FILES_TABLE = ""
			+ "CREATE TABLE miner_files (" + "id INT NOT NULL PRIMARY KEY, "
			+ "file_name VARCHAR(255), " + "path MEDIUMTEXT, "
			+ "miner_module_id INT NOT NULL, " + "INDEX(file_name), "
			+ "FOREIGN KEY(id) REFERENCES files(id), "
			+ "FOREIGN KEY(miner_module_id) REFERENCES miner_modules(id)"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_MINER_FILES_TABLE = ""
			+ "DROP TABLE IF EXISTS miner_files";
	protected static String SELECT_MINER_FILE_SQL = ""
			+ "SELECT id, file_name, path, miner_module_id "
			+ "FROM miner_files WHERE id=?";
	protected static String SELECT_MINER_FILES_SQL = ""
			+ "SELECT id, file_name, path, miner_module_id "
			+ "FROM miner_files";
	protected static String CREATE_MINER_FILE_SQL = ""
			+ "INSERT INTO miner_files (id, file_name, path, miner_module_id) "
			+ "VALUES (?,?,?,?)";
	protected static String DELETE_MINER_FILE_SQL = ""
			+ "DELETE FROM miner_files WHERE id=?";
	protected static String UPDATE_MINER_FILE_SQL = ""
			+ "UPDATE miner_files SET file_name=?, path=?, miner_module_id=? "
			+ "WHERE id=?";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerFile find(int id) throws DataAccessException {
		MinerFile result = null;
		RepositoryFile repositoryFile = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_FILE_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				repositoryFile = new MysqlRepositoryFileDAO(this
						.getConnection()).find(rs.getInt("id"));
				result = new MinerFile(repositoryFile);
				result.setId(rs.getInt("id"));
				result.setFileName(rs.getString("file_name"));
				result.setPath(rs.getString("path"));
				result.setModule(new MysqlMinerModuleDAO(this.getConnection())
						.find(rs.getInt("miner_module_id")));
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
	public Collection<MinerFile> findAll() throws DataAccessException {
		ArrayList<MinerFile> result = new ArrayList<MinerFile>();
		RepositoryFile repositoryFile = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_FILES_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				repositoryFile = new MysqlRepositoryFileDAO(this
						.getConnection()).find(rs.getInt("id"));
				repositoryFile = new MysqlRepositoryFileDAO(this
						.getConnection()).find(rs.getInt("id"));
				MinerFile minerFile = new MinerFile(repositoryFile);
				minerFile.setId(rs.getInt("id"));
				minerFile.setFileName(rs.getString("file_name"));
				minerFile.setPath(rs.getString("path"));
				minerFile.setModule(new MysqlMinerModuleDAO(this.getConnection())
						.find(rs.getInt("miner_module_id")));
				result.add(minerFile);
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
	public int create(MinerFile minerFile) throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		try {
			ps = this.getConnection().prepareStatement(CREATE_MINER_FILE_SQL,
					Statement.RETURN_GENERATED_KEYS);

			if (minerFile.getId() != 0) {
				id = minerFile.getId();
			} else {
				id = minerFile.getRepositoryFile().getId();
			}

			ps.setInt(1, id);
			ps.setString(2, minerFile.getFileName());
			ps.setString(3, minerFile.getPath());
			ps.setInt(4, minerFile.getModule().getId());
			ps.execute();

			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public void delete(MinerFile minerFile) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DELETE_MINER_FILE_SQL);
			ps.setInt(1, minerFile.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void update(MinerFile minerFile) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(UPDATE_MINER_FILE_SQL);
			ps.setString(1, minerFile.getFileName());
			ps.setString(2, minerFile.getPath());
			ps.setInt(3, minerFile.getModule().getId());
			ps.setInt(4, minerFile.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DROP_MINER_FILES_TABLE);
			ps.executeUpdate();
			ps.executeUpdate(CREATE_MINER_FILES_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
