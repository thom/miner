package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.ShiatsuDAO;

public class MysqlShiatsuDAO extends JdbcDAO implements ShiatsuDAO {

	protected static String CREATE_MINER_FILES_TABLE = ""
			+ "CREATE TABLE miner_files ("
			+ "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "file_name VARCHAR(255), " + "path MEDIUMTEXT, "
			+ "file_id INT NOT NULL, " + "miner_module_id INT NOT NULL, "
			+ "INDEX(file_name), " + "INDEX(file_id), "
			+ "FOREIGN KEY(file_id) REFERENCES files(id), "
			+ "FOREIGN KEY(miner_module_id) REFERENCES miner_modules(id)"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String CREATE_MINER_MODULES_TABLE = ""
			+ "CREATE TABLE miner_modules ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
			+ "module_name MEDIUMTEXT NOT NULL,"
			+ "UNIQUE(module_name(255))"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

	public MysqlShiatsuDAO(Connection conn) {
		super(conn);
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(
					DROP_TABLE_IF_EXISTS + "miner_files");
			ps.executeUpdate();
			ps.executeUpdate(DROP_TABLE_IF_EXISTS + "miner_modules");
			ps.executeUpdate(CREATE_MINER_MODULES_TABLE);
			ps.executeUpdate(CREATE_MINER_FILES_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
