package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.BasketFormatDAO;

public class JdbcBasketFormatDOA extends JdbcDAO
		implements
			BasketFormatDAO {

	protected static String SELECT_SQL = ""
			+ "SELECT a.commit_id, s.rev, f.id AS modified_files, f.file_name, ft.type "
			+ "FROM actions a, files f, file_types ft, scmlog s "
			+ "WHERE f.id = a.file_id AND f.id = ft.file_id AND s.id = a.commit_id ";
	protected static String ORDER_SQL = ""
			+ "ORDER BY a.commit_id asc, modified_files ASC";
	protected static String FILTER_SQL = "AND ft.type = 'code'";

	public JdbcBasketFormatDOA(Connection conn) {
		super(conn);
	}

	@Override
	public String format(boolean allFiles, boolean revs) {
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
			ps = this.getConnection().prepareStatement(sqlStatement);
			rs = ps.executeQuery();
			while (rs.next()) {
				int commitId = rs.getInt("commit_id");

				if (counter < commitId) {
					if (counter > -1)
						System.out.print("\n");

					counter = commitId;

					if (revs) {
						System.out.println("# " + rs.getString("rev"));
					}
				} else {
					System.out.print(" ");
				}

				System.out.print(rs.getString("modified_files"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// Do nothing!
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}
}