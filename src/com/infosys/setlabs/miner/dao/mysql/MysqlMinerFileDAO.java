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

/**
 * MySQL Miner File DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMinerFileDAO extends JdbcDAO implements MinerFileDAO {
	/**
	 * Table name
	 */
	public static String tableName = "miner_files";

	/**
	 * Table name for files with randomized modules
	 */
	public static String tableNameRandomized = "miner_files_randomized";

	private boolean randomizedModules;

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
	public boolean hasRandomizedModules() {
		return randomizedModules;
	}

	@Override
	public void setRandomizedModules(boolean randomizedModules) {
		this.randomizedModules = randomizedModules;
	}

	protected String getName() {
		return randomizedModules ? tableNameRandomized : tableName;
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s ("
				+ "id INT NOT NULL PRIMARY KEY, file_name VARCHAR(255), "
				+ "path MEDIUMTEXT, type VARCHAR(255), modifications INT, "
				+ "miner_module_id INT NOT NULL DEFAULT 0, "
				+ "INDEX(file_name), FOREIGN KEY(id) REFERENCES files(id), "
				+ "FOREIGN KEY(miner_module_id) REFERENCES %s(id)"
				// MyISAM doesn't support foreign keys, but as
				// CVSAnaly2 uses MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8", getName(),
				MysqlModuleDAO.tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", getName());
	}

	protected String selectSQL() {
		return String.format("SELECT id, file_name, path, type, "
				+ "modifications, miner_module_id FROM %s WHERE id=?",
				getName());
	}

	protected String selectAllSQL() {
		return String.format("SELECT id, file_name, path, type, "
				+ "modifications, miner_module_id FROM %s", getName());
	}

	protected String createSQL() {
		return String.format("INSERT INTO %s (id, file_name, path, type, "
				+ "modifications, miner_module_id) VALUES (?,?,?,?,?,?)",
				getName());
	}

	protected String deleteSQL() {
		return String.format("DELETE FROM %s WHERE id=?", getName());
	}

	protected String updateSQL() {
		return String.format("UPDATE %s SET file_name=?, path=?, type=?, "
				+ "modifications=?, miner_module_id=? WHERE id=?", getName());
	}

	protected String countSQL(boolean allFiles) {
		return String.format(
				"SELECT COUNT(id) AS count FROM %s "
						+ (allFiles ? "" : "WHERE type = '"
								+ MinerFile.Type.CODE + "'"), getName());
	}

	protected String countModifiedSQL(boolean allFiles) {
		return String.format("SELECT COUNT(id) AS count FROM %s "
				+ "WHERE modifications >= ? "
				+ (allFiles ? "" : "AND type = '" + MinerFile.Type.CODE + "'"),
				getName());
	}

	protected String initializeSQL() {
		return String.format("INSERT INTO %s (id, file_name, type) "
				+ "SELECT f.id, f.file_name, ft.type "
				+ "FROM files f, file_types ft WHERE f.id = ft.file_id",
				getName());
	}

	protected String initializeRandomSQL() {
		return String.format("INSERT %s "
				+ "SELECT id, file_name, path, type, modifications, "
				+ "FLOOR(1 + (RAND() * (SELECT MAX(id) "
				+ "FROM miner_modules) -1)) as miner_module_id FROM %s",
				tableNameRandomized, tableName);
	}

	protected String deleteDeletedSQL() {
		return String.format("DELETE f FROM %s f, actions a "
				+ "WHERE f.id = a.file_id AND a.type = 'D'", getName());
	}

	protected String setModificationsSQL() {
		return String.format("UPDATE %s AS f1, "
				+ "(SELECT f.id, COUNT(f.id) AS modifications "
				+ "FROM %s f, actions a "
				+ "WHERE a.file_id = f.id GROUP BY f.id) AS f2 "
				+ "SET f1.modifications = f2.modifications "
				+ "WHERE f1.id = f2.id", getName(), getName());
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

	@Override
	public void initialize() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			if (!hasRandomizedModules()) {
				ps = this.getConnection().prepareStatement(initializeSQL());
				ps.executeUpdate();
				ps.executeUpdate(deleteDeletedSQL());
				ps.executeUpdate(setModificationsSQL());
				ps.executeUpdate(setNewestFileNameSQL());
			} else {
				ps = this.getConnection().prepareStatement(
						initializeRandomSQL());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public MinerFile find(int id) throws DataAccessException {
		MinerFile result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerFile();
				result.setId(rs.getInt("id"));
				result.setFileName(rs.getString("file_name"));
				result.setPath(rs.getString("path"));
				result.setType(rs.getString("type"));
				result.setModifications(rs.getInt("modifications"));
				result.setModule(new MysqlModuleDAO(this.getConnection())
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectAllSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				MinerFile minerFile = new MinerFile();
				minerFile.setId(rs.getInt("id"));
				minerFile.setFileName(rs.getString("file_name"));
				minerFile.setPath(rs.getString("path"));
				minerFile.setType(rs.getString("type"));
				minerFile.setModifications(rs.getInt("modifications"));
				minerFile.setModule(new MysqlModuleDAO(this.getConnection())
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
	public MinerFile create(MinerFile minerFile) throws DataAccessException {
		MinerFile result = minerFile;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(createSQL(),
					Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, minerFile.getId());
			ps.setString(2, minerFile.getFileName());
			ps.setString(3, minerFile.getPath());
			ps.setString(4, minerFile.getType().toString());
			ps.setInt(5, minerFile.getModifications());
			ps.setInt(6, minerFile.getModule().getId());
			ps.execute();

			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				result.setId(rs.getInt(1));
			}
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
			ps = this.getConnection().prepareStatement(deleteSQL());
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
			ps = this.getConnection().prepareStatement(updateSQL());
			ps.setString(1, minerFile.getFileName());
			ps.setString(2, minerFile.getPath());
			ps.setString(3, minerFile.getType().toString());
			ps.setInt(4, minerFile.getModifications());
			ps.setInt(5, minerFile.getModule().getId());
			ps.setInt(6, minerFile.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public int count(boolean allFiles) throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(countSQL(allFiles));
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
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
	public int count(boolean allFiles, int minimumModifications)
			throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					countModifiedSQL(allFiles));
			ps.setInt(1, minimumModifications);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
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
