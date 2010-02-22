package com.infosys.setlabs.miner.dao.mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.BasketFormatDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

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

	protected String selectSQL(boolean allFiles) {
		return "SELECT a.commit_id, s.rev, "
				+ "GROUP_CONCAT(m.id ORDER BY m.id ASC SEPARATOR \" \") "
				+ "AS modified_files "
				+ "FROM miner_actions a, miner_files m, scmlog s "
				+ "WHERE m.id = a.file_id AND a.commit_id = s.id AND m.modifications >= ? "
				+ (allFiles ? "" : "AND m.type = '" + MinerFile.Type.CODE + "'")
				+ "GROUP BY a.commit_id ORDER BY a.commit_id ASC";
	}

	protected String setGroupConcatMaxLenSQL() {
		return "SET GLOBAL group_concat_max_len = 1000000000";
	}

	@Override
	public void format(File output, boolean allFiles, boolean revs,
			int modifications, int minSize, int maxSize)
			throws DataAccessException {
		// Minimum size has to be at least 2
		if (minSize < 2) {
			throw new DataAccessException(new Exception(
					"Minimum commit size has to be at least 2"));
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// Open output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			ps = this.getConnection().prepareStatement(
					setGroupConcatMaxLenSQL());
			ps.execute();
			ps = this.getConnection().prepareStatement(selectSQL(allFiles));
			ps.setInt(1, modifications);
			rs = ps.executeQuery();
			while (rs.next()) {
				String modifiedFiles = rs.getString("modified_files");

				int commitSize = modifiedFiles.split(" ").length;

				// 'commitSize' needs to be bigger than 1.
				// If 'maxSize' is smaller than 0, there's no maximum size
				// If 'maxSize' is bigger than -1, the maximum size is 'maxSize'
				if (commitSize >= minSize
						&& (maxSize < 0 || (maxSize > -1 && commitSize <= maxSize))) {
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
