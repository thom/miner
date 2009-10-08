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
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;
import com.infosys.setlabs.miner.domain.MinerFile;

public class MysqlFrequentItemSetDAO extends JdbcDAO
		implements
			FrequentItemSetDAO {

	protected static String CREATE_MINER_FISM_TABLE = ""
			+ "CREATE TABLE miner_frequent_item_sets ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "absolute_item_set_support INT, "
			+ "relative_item_set_support FLOAT(7,4) "
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
	protected static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
	protected static String SELECT_FREQUENT_ITEM_SET_SQL = ""
			+ "SELECT id, absolute_item_set_support, relative_item_set_support "
			+ "FROM miner_frequent_item_sets WHERE id=?";
	protected static String SELECT_FREQUENT_ITEMS_SQL = ""
			+ "SELECT id, miner_frequent_item_set_id, file_id FROM miner_frequent_items "
			+ "WHERE miner_frequent_item_set_id=?";
	protected static String CREATE_FREQUENT_ITEM_SET_SQL = ""
			+ "INSERT INTO miner_frequent_item_sets (id, absolute_item_set_support, relative_item_set_support) "
			+ "VALUES (?,?,?)";
	protected static String ADD_FREQUENT_ITEM_SQL = ""
			+ "INSERT INTO miner_frequent_items (miner_frequent_item_set_id, file_id) "
			+ "VALUES (?,?)";

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
				result.setAbsoluteItemSetSupport(rs
						.getInt("absolute_item_set_support"));
				result.setRelativeItemSetSupport(rs
						.getDouble("relative_item_set_support"));
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
					SELECT_FREQUENT_ITEM_SET_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				FrequentItemSet frequentItemSet = new FrequentItemSet(rs
						.getInt("id"));
				frequentItemSet.setAbsoluteItemSetSupport(rs
						.getInt("absolute_item_set_support"));
				frequentItemSet.setRelativeItemSetSupport(rs
						.getDouble("relative_item_set_support"));
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
	public int create(FrequentItemSet frequentItemSet)
			throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					CREATE_FREQUENT_ITEM_SET_SQL,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, frequentItemSet.getId());
			ps.setInt(2, frequentItemSet.getAbsoluteItemSetSupport());
			ps.setDouble(3, frequentItemSet.getRelativeItemSetSupport());
			ps.execute();

			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				result = rs.getInt(1);
				frequentItemSet.setId(result);
			}

			addFrequentItem(frequentItemSet);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
		return result;
	}
	
	@Override
	public int create(String frequentItemSetLine) throws DataAccessException {
		// TODO: Create item set from string
		return 0;
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(
					DROP_TABLE_IF_EXISTS + "miner_frequent_items");
			ps.executeUpdate();
			ps.executeUpdate(DROP_TABLE_IF_EXISTS + "miner_frequent_item_sets");
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

	private void addFrequentItem(FrequentItemSet frequentItemSet)
			throws SQLException {
		if (!frequentItemSet.getItems().isEmpty()) {
			// Prepare statement for connecting items, if there are any
			PreparedStatement ps = this.getConnection().prepareStatement(
					ADD_FREQUENT_ITEM_SQL);

			// Iterate through all items, connect them to the item set and
			// execute batch
			for (MinerFile file : frequentItemSet.getItems()) {
				ps.setInt(1, frequentItemSet.getId());
				ps.setInt(2, file.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		}
	}
}
