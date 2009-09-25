package com.infosys.setlabs.miner.format;

/**
 * Formats revision history transactions extracted from databases created by
 * CVSAnaly2 into the market-basket format used by
 * http://www.borgelt.net/apriori.html
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.infosys.setlabs.miner.common.DatabaseUtil;

public class BasketFormat {

	// Database connection
	private Connection connection;

	// Should all files be included? Normally only code files are included in
	// the transactions generated
	private boolean allFiles;

	// Should the revision id be included in the transaction file as a comment?
	private boolean revs;

	/**
	 * Create a new formatter object
	 * 
	 * @param connection
	 *            Database connection
	 * @param allFiles
	 *            Should all files be included in the transaction file?
	 * @param revs
	 *            Should the revisions get added as comments to the transaction
	 *            file?
	 */
	public BasketFormat(Connection connection, boolean allFiles, boolean revs) {
		this.connection = connection;
		this.allFiles = allFiles;
		this.revs = revs;
	}

	/**
	 * Formats a database populated by CVSAnaly2 into market-basket format used
	 * by Borgelt's Association Rule Induction / Frequent Item Set Mining
	 * algorithm (http://www.borgelt.net/apriori.html)
	 */
	public void format() {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.createStatement();

			// Build the statement
			String sqlStatement = "SELECT a.commit_id, s.rev, f.id AS modified_files, f.file_name, ft.type "
					+ "FROM actions a, files f, file_types ft, scmlog s "
					+ "WHERE f.id = a.file_id AND f.id = ft.file_id AND s.id = a.commit_id ";
			String sqlOrder = "ORDER BY a.commit_id asc, modified_files asc;";
			if (allFiles) {
				sqlStatement += sqlOrder;
			} else {
				sqlStatement += "AND ft.type = 'code'" + sqlOrder;
			}

			// Execute the query
			rs = stmt.executeQuery(sqlStatement);

			int counter = -1;

			// Retrieve the data from the result set
			rs.beforeFirst();
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

			// Print newline at the end
			System.out.println();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
		}
	}
}