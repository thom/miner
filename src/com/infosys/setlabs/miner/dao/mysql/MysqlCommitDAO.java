package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.domain.Commit;

/**
 * MySQL Commit DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlCommitDAO extends JdbcDAO implements CommitDAO {
	public static String tableName = "miner_commits";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlCommitDAO(Connection conn) {
		super(conn);
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s "
				+ "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "UNIQUE(rev(255)))" + "SELECT l.id, l.rev, l.message, "
				+ "(SELECT COUNT(DISTINCT file_id) "
				+ "FROM actions a, miner_files f "
				+ "WHERE a.commit_id = l.id AND a.file_id = f.id) "
				+ "as miner_files_touched, "
				+ "(SELECT COUNT(DISTINCT f.miner_module_id) "
				+ "FROM actions a, miner_files f "
				+ "WHERE a.commit_id = l.id AND f.id = a.file_id) "
				+ "as modules_touched from scmlog l", tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", tableName);
	}

	protected String selectSQL() {
		return String.format("SELECT id, rev, miner_files_touched, "
				+ "modules_touched FROM %s WHERE id=?", tableName);
	}

	protected String selectByRev() {
		return String.format("SELECT id, rev, miner_files_touched, "
				+ "modules_touched FROM %s WHERE rev LIKE \"%?%\"", tableName);
	}
	protected String selectAllSQL() {
		return String.format("SELECT id, rev, miner_files_touched, "
				+ "modules_touched FROM %s", tableName);
	}

	@Override
	public Commit find(int id) throws DataAccessException {
		Commit result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new Commit(rs.getInt("id"));
				result.setRev(rs.getString("rev"));
				result.setComment(rs.getString("message"));
				result.setFilesTouched(rs.getInt("miner_files_touched"));
				result.setModulesTouched(rs.getInt("modules_touched"));
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
	public Commit findByRev(String rev) throws DataAccessException {
		Commit result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setString(1, rev);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new Commit(rs.getInt("id"));
				result.setRev(rs.getString("rev"));
				result.setComment(rs.getString("message"));
				result.setFilesTouched(rs.getInt("miner_files_touched"));
				result.setModulesTouched(rs.getInt("modules_touched"));
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
	public Collection<Commit> findAll() throws DataAccessException {
		ArrayList<Commit> result = new ArrayList<Commit>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectAllSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				Commit commit = new Commit(rs.getInt("id"));
				commit.setRev(rs.getString("rev"));
				commit.setComment(rs.getString("message"));
				commit.setFilesTouched(rs.getInt("miner_files_touched"));
				commit.setModulesTouched(rs.getInt("modules_touched"));
				result.add(commit);
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
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(dropTableSQL());
			ps.executeUpdate();
			ps.executeUpdate(createTableSQL());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
