package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

/**
 * MySQL Frequent Item Set DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlFrequentItemSetDAO extends JdbcDAO
		implements
			FrequentItemSetDAO {

	protected static String CREATE_MINER_FISM_TABLE = ""
			+ "CREATE TABLE miner_frequent_item_sets ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " + "size INT, "
			+ "absolute_item_set_support INT, "
			+ "relative_item_set_support FLOAT(7,4) ," + "modules_touched INT"
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String CREATE_MINER_FIS_TABLE = ""
			+ "CREATE TABLE miner_frequent_items ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "miner_frequent_item_set_id INT, file_id INT, "
			+ "FOREIGN KEY(miner_frequent_item_set_id) REFERENCES miner_frequent_item_sets(id), "
			+ "FOREIGN KEY(file_id) REFERENCES miner_files(id), "
			+ "UNIQUE(miner_frequent_item_set_id, file_id)"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_MINER_FISM_TABLE = "DROP TABLE IF EXISTS miner_frequent_item_sets";
	protected static String DROP_MINER_FIS_TABLE = "DROP TABLE IF EXISTS miner_frequent_items";
	protected static String SELECT_FREQUENT_ITEM_SET_SQL = ""
			+ "SELECT id, size, absolute_item_set_support, relative_item_set_support, modules_touched "
			+ "FROM miner_frequent_item_sets WHERE id=?";
	protected static String SELECT_FREQUENT_ITEM_SETS_SQL = ""
			+ "SELECT id, size, absolute_item_set_support, relative_item_set_support, modules_touched "
			+ "FROM miner_frequent_item_sets";
	protected static String SELECT_FREQUENT_ITEMS_SQL = ""
			+ "SELECT id, miner_frequent_item_set_id, file_id FROM miner_frequent_items "
			+ "WHERE miner_frequent_item_set_id=?";
	protected static String CREATE_FREQUENT_ITEM_SET_SQL = ""
			+ "INSERT INTO miner_frequent_item_sets (id, size, absolute_item_set_support, relative_item_set_support, modules_touched) "
			+ "VALUES (?,?,?,?,?)";
	protected static String SET_MODULES_TOUCHED_ITEM_SET_SQL = ""
			+ "UPDATE miner_frequent_item_sets SET modules_touched=? WHERE id=?";
	protected static String GET_MODULES_TOUCHED_ITEM_SET_SQL = ""
			+ "SELECT COUNT(*) count "
			+ "FROM (SELECT DISTINCT f.miner_module_id FROM miner_frequent_items fi, miner_files f "
			+ "WHERE f.id = fi.file_id AND fi.miner_frequent_item_set_id = ?) as modules_count";
	protected static String ADD_FREQUENT_ITEM_SQL = ""
			+ "INSERT INTO miner_frequent_items (miner_frequent_item_set_id, file_id) "
			+ "VALUES (?,?)";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlFrequentItemSetDAO(Connection conn) {
		super(conn);
	}

	@Override
	public FrequentItemSet find(int id) throws DataAccessException {
		FrequentItemSet result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					SELECT_FREQUENT_ITEM_SET_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new FrequentItemSet(rs.getInt("id"));
				result.setSize(rs.getInt("size"));
				result.setAbsoluteItemSetSupport(rs
						.getInt("absolute_item_set_support"));
				result.setRelativeItemSetSupport(rs
						.getDouble("relative_item_set_support"));
				result.setModulesTouched(rs.getInt("modules_touched"));
				addItems(result);
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
	public Collection<FrequentItemSet> findAll() throws DataAccessException {
		ArrayList<FrequentItemSet> result = new ArrayList<FrequentItemSet>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					SELECT_FREQUENT_ITEM_SETS_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				FrequentItemSet frequentItemSet = new FrequentItemSet(rs
						.getInt("id"));
				frequentItemSet.setSize(rs.getInt("size"));
				frequentItemSet.setAbsoluteItemSetSupport(rs
						.getInt("absolute_item_set_support"));
				frequentItemSet.setRelativeItemSetSupport(rs
						.getDouble("relative_item_set_support"));
				frequentItemSet.setModulesTouched(rs.getInt("modules_touched"));
				addItems(frequentItemSet);
				result.add(frequentItemSet);
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
	public int create(String frequentItemSetLine) throws DataAccessException {
		Pattern p = Pattern.compile(":");
		String[] parts = p.split(frequentItemSetLine);
		p = Pattern.compile(" ");

		// First part: File IDs
		String[] files = p.split(parts[0]);
		LinkedList<Integer> fileIds = new LinkedList<Integer>();
		for (String file : files) {
			fileIds.add(Integer.parseInt(file));
		}

		// Seconds part: absolute and relative item support
		String[] supports = p.split(parts[1]);

		// Create new frequent item set
		int result = 0;
		PreparedStatement psItemSet = null;
		PreparedStatement psItem = null;
		ResultSet rs = null;
		try {
			psItemSet = this.getConnection().prepareStatement(
					CREATE_FREQUENT_ITEM_SET_SQL,
					Statement.RETURN_GENERATED_KEYS);
			psItemSet.setInt(1, 0);
			psItemSet.setInt(2, fileIds.size());
			psItemSet.setInt(3, Integer.parseInt(supports[0]));
			psItemSet.setDouble(4, Double.parseDouble(supports[1]));
			psItemSet.setInt(5, 0);
			psItemSet.execute();

			rs = psItemSet.getGeneratedKeys();
			if (rs != null && rs.next()) {
				result = rs.getInt(1);
			}

			psItem = this.getConnection().prepareStatement(
					ADD_FREQUENT_ITEM_SQL);

			// Add items to frequent item set
			for (Integer fileId : fileIds) {
				psItem.setInt(1, result);
				psItem.setInt(2, fileId);
				psItem.addBatch();
			}
			psItem.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(psItemSet);
			this.closeStatement(psItem);
		}

		return result;
	}

	@Override
	public void setNumberOfModulesTouched(int id) {
		PreparedStatement psUpdate = null;
		PreparedStatement psModulesTouched = null;
		ResultSet rs = null;
		try {
			psModulesTouched = this.getConnection().prepareStatement(
					GET_MODULES_TOUCHED_ITEM_SET_SQL);
			psModulesTouched.setInt(1, id);
			rs = psModulesTouched.executeQuery();
			int modulesTouched = 0;
			while (rs.next()) {
				modulesTouched = rs.getInt("count");
			}

			psUpdate = this.getConnection().prepareStatement(
					SET_MODULES_TOUCHED_ITEM_SET_SQL);
			psUpdate.setInt(1, modulesTouched);
			psUpdate.setInt(2, id);
			psUpdate.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(psUpdate);
			this.closeStatement(psModulesTouched);
		}
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DROP_MINER_FIS_TABLE);
			ps.executeUpdate();
			ps.executeUpdate(DROP_MINER_FISM_TABLE);
			ps.executeUpdate(CREATE_MINER_FISM_TABLE);
			ps.executeUpdate(CREATE_MINER_FIS_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	private void addItems(FrequentItemSet frequentItemSet)
			throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					SELECT_FREQUENT_ITEMS_SQL);
			ps.setInt(1, frequentItemSet.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				frequentItemSet.addItem(new MysqlMinerFileDAO(this
						.getConnection()).find(rs.getInt("file_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
	}
}
