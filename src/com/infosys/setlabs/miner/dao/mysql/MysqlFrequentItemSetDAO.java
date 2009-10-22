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
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * MySQL Frequent Item Set DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlFrequentItemSetDAO extends JdbcDAO
		implements
			FrequentItemSetDAO {

	public static String frequentItemSetsPrefix = "miner_fis_";
	public static String frequentItemsPrefix = "miner_fi_";
	private String name = MinerInfo.defaultName;

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlFrequentItemSetDAO(Connection conn) {
		super(conn);
	}

	private String frequentItemSetsTableName() {
		return frequentItemSetsPrefix + getName();
	}

	private String frequentItemsTableName() {
		return frequentItemsPrefix + getName();
	}

	private String createFrequentItemSetsTable() {
		return "CREATE TABLE " + frequentItemSetsTableName() + " ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " + "size INT, "
				+ "absolute_item_set_support INT, "
				+ "relative_item_set_support FLOAT(7,4) ,"
				+ "modules_touched INT"
				// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses
				// MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	}

	private String createFrequentItemsTable() {
		return "CREATE TABLE "
				+ frequentItemsTableName()
				+ " ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "miner_frequent_item_set_id INT, file_id INT, "
				+ "FOREIGN KEY(miner_frequent_item_set_id) REFERENCES miner_frequent_item_sets(id), "
				+ "FOREIGN KEY(file_id) REFERENCES miner_files(id), "
				+ "UNIQUE(miner_frequent_item_set_id, file_id)"
				// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses
				// MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	}

	private String dropFrequentItemSetsTable() {
		return "DROP TABLE IF EXISTS " + frequentItemSetsTableName();
	}

	private String dropFrequentItemsTable() {
		return "DROP TABLE IF EXISTS " + frequentItemsTableName();
	}

	private String selectFrequentItemSet() {
		return "SELECT id, size, absolute_item_set_support, relative_item_set_support, modules_touched "
				+ "FROM " + frequentItemSetsTableName() + " WHERE id=?";
	}

	private String selectFrequentItemSets() {
		return "SELECT id, size, absolute_item_set_support, relative_item_set_support, modules_touched "
				+ "FROM " + frequentItemSetsTableName();
	}

	private String selectFrequentItems() {
		return "SELECT id, miner_frequent_item_set_id, file_id FROM "
				+ frequentItemsTableName()
				+ " WHERE miner_frequent_item_set_id=?";
	}

	private String createFrequentItemSet() {
		return "INSERT INTO "
				+ frequentItemSetsTableName()
				+ " (id, size, absolute_item_set_support, relative_item_set_support, modules_touched) "
				+ "VALUES (?,?,?,?,?)";
	}

	private String updateModulesTouchedItemSet() {
		return "UPDATE " + frequentItemSetsTableName()
				+ " SET modules_touched=? WHERE id=?";
	}

	private String getModulesTouchedItemSet() {
		return "SELECT COUNT(*) count "
				+ "FROM (SELECT DISTINCT f.miner_module_id FROM "
				+ frequentItemsTableName() + " fi, miner_files f "
				+ "WHERE f.id = fi.file_id "
				+ "AND fi.miner_frequent_item_set_id = ?) AS modules_count";
	}

	private String addFrequentItem() {
		return "INSERT INTO " + frequentItemsTableName()
				+ " (miner_frequent_item_set_id, file_id) " + "VALUES (?,?)";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public FrequentItemSet find(int id) throws DataAccessException {
		FrequentItemSet result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectFrequentItemSet());
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
			ps = this.getConnection()
					.prepareStatement(selectFrequentItemSets());
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
					createFrequentItemSet(), Statement.RETURN_GENERATED_KEYS);
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

			psItem = this.getConnection().prepareStatement(addFrequentItem());

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
					getModulesTouchedItemSet());
			psModulesTouched.setInt(1, id);
			rs = psModulesTouched.executeQuery();
			int modulesTouched = 0;
			while (rs.next()) {
				modulesTouched = rs.getInt("count");
			}

			psUpdate = this.getConnection().prepareStatement(
					updateModulesTouchedItemSet());
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
			ps = this.getConnection()
					.prepareStatement(dropFrequentItemsTable());
			ps.executeUpdate();
			ps.executeUpdate(dropFrequentItemSetsTable());
			ps.executeUpdate(createFrequentItemSetsTable());
			ps.executeUpdate(createFrequentItemsTable());
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
			ps = this.getConnection().prepareStatement(selectFrequentItems());
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
