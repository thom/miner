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
			+ "SELECT a.commit_id, s.rev, f.id AS modified_files "
			+ "FROM actions a, files f, file_types ft, scmlog s "
			+ "WHERE f.id = a.file_id AND f.id = ft.file_id AND s.id = a.commit_id ";
	protected static String ORDER_SQL = ""
			+ "ORDER BY a.commit_id ASC, modified_files ASC";
	protected static String FILTER_CODE = " AND ft.type = 'code' ";
	protected static String FILTER_RENAMED = " "
			+ " AND f.id IN (SELECT DISTINCT a.file_id "
			+ "FROM actions a, file_copies fc "
			+ "WHERE a.type = 'V' AND a.file_id = fc.to_id) ";

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
	public String format(File output, IncludedFiles includedFiles, boolean revs) {
		String result = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Build the statement
		String sqlStatement = SELECT_SQL;

		switch (includedFiles) {
			case ALL :
				sqlStatement += ORDER_SQL;
				break;
			case ALL_RENAMED :
				sqlStatement += FILTER_RENAMED + ORDER_SQL;
				break;
			case CODE :
				sqlStatement += FILTER_CODE + ORDER_SQL;
				break;
			case CODE_RENAMED :
				sqlStatement += FILTER_CODE + FILTER_RENAMED + ORDER_SQL;
				break;
		}

		int counter = -1;

		try {
			// Open output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			ps = this.getConnection().prepareStatement(sqlStatement);
			rs = ps.executeQuery();
			while (rs.next()) {
				int commitId = rs.getInt("commit_id");

				// TODO: option to only write files that have been renamed
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
