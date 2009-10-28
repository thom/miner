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
		return "SELECT a.commit_id, s.rev, m.id AS modified_files "
				+ "FROM actions a, miner_files m, scmlog s "
				+ "WHERE m.id = a.file_id AND a.commit_id = s.id "
				+ "ORDER BY a.commit_id ASC, modified_files ASC";
	}

	@Override
	public String format(File output, boolean revs) {
		String result = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		int counter = -1;

		try {
			// Open output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			ps = this.getConnection().prepareStatement(selectSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				int commitId = rs.getInt("commit_id");

				if (counter < commitId) {
					if (counter > -1) {
						out.write("\n");
					}

					counter = commitId;

					if (revs) {
						out.write("# " + rs.getString("rev") + "\n");
					}
				} else {
					out.write(" ");
				}

				out.write(rs.getString("modified_files"));
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
		return result;
	}
}
