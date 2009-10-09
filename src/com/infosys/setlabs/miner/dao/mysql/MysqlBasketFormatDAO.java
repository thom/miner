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

	protected static String SELECT_SQL = ""
			+ "SELECT a.commit_id, s.rev, f.id AS modified_files, f.file_name, ft.type "
			+ "FROM actions a, files f, file_types ft, scmlog s "
			+ "WHERE f.id = a.file_id AND f.id = ft.file_id AND s.id = a.commit_id ";
	protected static String ORDER_SQL = ""
			+ "ORDER BY a.commit_id asc, modified_files ASC";
	protected static String FILTER_SQL = "AND ft.type = 'code'";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlBasketFormatDAO(Connection conn) {
		super(conn);
	}

	@Override
	public String format(File output, boolean allFiles, boolean revs) {
		String result = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Build the statement
		String sqlStatement = SELECT_SQL;
		if (allFiles) {
			sqlStatement += ORDER_SQL;
		} else {
			sqlStatement += FILTER_SQL + ORDER_SQL;
		}

		int counter = -1;

		try {
			// Open output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			ps = this.getConnection().prepareStatement(sqlStatement);
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
