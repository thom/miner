package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

public class MysqlMinerFileDAO extends JdbcDAO implements MinerFileDAO {

	protected static String SELECT_MINER_FILE_SQL = ""
			+ "SELECT id, file_name, path, file_id, module_id "
			+ "FROM \"miner_files\" WHERE id=?";
	protected static String SELECT_MINER_FILES_SQL = ""
			+ "SELECT id, file_name, path, file_id, module_id "
			+ "FROM \"miner_files\"";
	protected static String CREATE_MINER_FILE_SQL = ""
			+ "INSERT INTO miner_files (file_name, path, file_id, module_id) "
			+ "VALUES (?)";
	protected static String DELETE_MINER_FILE_SQL = ""
			+ "DELETE FROM \"miner_files\" WHERE id=?";
	protected static String UPDATE_MINER_FILE_SQL = ""
			+ "UPDATE \"miner_files\" SET file_name=?, path=?, file_id=?, module_id=? "
			+ "WHERE id=?)";

	public MysqlMinerFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerFile find(int id) throws DataAccessException {
		MinerFile result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_FILE_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerFile(rs.getInt("id"));
				result.setFileName(rs.getString("file_name"));
				result.setPath(rs.getString("path"));
				result.setFile(new MysqlFileDAO(this.getConnection()).find(rs
						.getInt("file_id")));
				result.setModule(new MysqlMinerModuleDAO(this.getConnection())
						.find(rs.getInt("module_id")));
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
	public Collection<MinerFile> findAll() throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public int create(MinerFile object) throws DataAccessException {
		// TODO
		int result = 0;

		// TODO: create corresponding MinerModule!
		// -> module is 'path'!

		return result;
	}

	@Override
	public void delete(MinerFile object) throws DataAccessException {
		// TODO
	}

	@Override
	public void update(MinerFile object) throws DataAccessException {
		// TODO
	}
}
