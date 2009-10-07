package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

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

	// TODO: SQL strings for select!

	public MysqlFrequentItemSetDAO(Connection conn) {
		super(conn);
	}

	@Override
	public FrequentItemSet find(int id) throws DataAccessException {
		// TODO: Find specific frequent item set
		return null;
	}

	@Override
	public Collection<FrequentItemSet> findAll()
			throws DataAccessException {
		// TODO: Find all frequent item sets
		return null;
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
}
