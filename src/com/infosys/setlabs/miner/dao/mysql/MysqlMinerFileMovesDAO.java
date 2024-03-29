package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileMovesDAO;

public class MysqlMinerFileMovesDAO extends JdbcDAO implements
		MinerFileMovesDAO {
	public static String tableName = "miner_actions";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerFileMovesDAO(Connection conn) {
		super(conn);
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s LIKE actions;", tableName);
	}

	protected String initializeTableSQL() {
		return String.format("INSERT INTO %s SELECT * FROM actions", tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", tableName);
	}

	protected String findMovesSQL() {
		return String.format("SELECT f1.file_name, a1.id AS d_action_id, "
				+ "a2.id AS a_action_id, a1.file_id AS old_file_id, "
				+ "a2.file_id AS new_file_id, a1.commit_id "
				+ "FROM %s a1, %s a2, files f1, files f2 "
				+ "WHERE f1.id = a1.file_id AND f2.id = a2.file_id "
				+ "AND f1.file_name = f2.file_name AND a1.type = 'D' "
				+ "AND a2.type = 'A' AND a1.commit_id = a2.commit_id "
				+ "ORDER BY a1.commit_id", tableName, tableName);
	}

	protected String modifyAddActionSQL() {
		return String.format("UPDATE %s SET type='X' WHERE id=?", tableName);
	}

	protected String deleteDeleteActionSQL() {
		return String.format("DELETE a FROM %s a WHERE id=?", tableName);
	}

	protected String replaceFileIdsSQL() {
		return String.format("UPDATE %s SET file_id=? WHERE file_id=?",
				tableName);
	}

	@Override
	public void initialize() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// Find moves (D-A pattern with same commit ID)
			ps = this.getConnection().prepareStatement(findMovesSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				// Modify action "A" to "X"
				ps = this.getConnection()
						.prepareStatement(modifyAddActionSQL());
				ps.setInt(1, rs.getInt("a_action_id"));
				ps.execute();

				// Deleted action "D"
				ps = this.getConnection().prepareStatement(
						deleteDeleteActionSQL());
				ps.setInt(1, rs.getInt("d_action_id"));
				ps.execute();

				// Replace all occurrences of old_file_id with new_file_id
				// in miner_actions table
				ps = this.getConnection().prepareStatement(replaceFileIdsSQL());
				ps.setInt(1, rs.getInt("new_file_id"));
				ps.setInt(2, rs.getInt("old_file_id"));
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(dropTableSQL());
			ps.executeUpdate();
			ps.executeUpdate(createTableSQL());
			ps.executeUpdate(initializeTableSQL());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

}
