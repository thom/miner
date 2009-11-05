package com.infosys.setlabs.miner.dao.mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.BasketFormatDAO;

/**
 * MySQL Basket Format DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlBasketFormatDAO extends JdbcDAO implements BasketFormatDAO {
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlBasketFormatDAO(Connection conn) {
		super(conn);
	}

	protected String selectSQL() {
		return "SELECT a.commit_id, s.rev, "
				+ "GROUP_CONCAT(m.id ORDER BY m.id ASC SEPARATOR \" \") "
				+ "AS modified_files "
				+ "FROM actions a, miner_files m, scmlog s "
				+ "WHERE m.id = a.file_id AND a.commit_id = s.id AND m.modifications >= ? "
				+ "GROUP BY a.commit_id ORDER BY a.commit_id ASC";
	}

	protected String setGroupConcatMaxLenSQL() {
		return "SET GLOBAL group_concat_max_len = 1000000000";
	}

	@Override
	public void format(File output, boolean revs, int modifications) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// Open output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			ps = this.getConnection().prepareStatement(
					setGroupConcatMaxLenSQL());
			ps.execute();
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, modifications);
			rs = ps.executeQuery();
			while (rs.next()) {
				String modifiedFiles = rs.getString("modified_files");
				if (modifiedFiles.split(" ").length > 1) {
					if (revs) {
						out.write("# " + rs.getString("rev") + "\n");
					}
					out.write(modifiedFiles + "\n");
				}
			}

			// Close output file
			out.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
	}
}
