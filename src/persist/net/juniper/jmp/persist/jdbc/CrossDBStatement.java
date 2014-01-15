package net.juniper.jmp.persist.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.juniper.jmp.persist.utils.DbExceptionHelper;
import net.juniper.jmp.persist.utils.SqlLogger;
/**
 * Statement decorator
 * @author juntaod
 *
 */
public class CrossDBStatement implements Statement{
	protected Statement passthru;
	protected CrossDBConnection conn;
	protected List<CrossDBResultSet> resultSets = Collections.synchronizedList(new ArrayList<CrossDBResultSet>());

	public CrossDBStatement() {
		super();
	}

	public CrossDBStatement(Statement passthru, CrossDBConnection conn) {
		this();
		this.conn = conn;
		this.passthru = passthru;
	}


	public void addBatch(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		passthru.addBatch(sqlFixed);
	}

	@Override
	public void cancel() throws SQLException {
		passthru.cancel();
	}
	
	public void clearBatch() throws SQLException {
		passthru.clearBatch();
	}

	public void clearWarnings() throws SQLException {
		passthru.clearWarnings();
	}

	public void close() throws SQLException {
		closeResultSets();
		passthru.close();
		conn.deregisterStatement(this);
	}

	public void closeResultSets() {
		ResultSet[] rs = resultSets.toArray(new ResultSet[0]);
		for (int i = 0; i < rs.length; i ++) {
			if ((rs[i] instanceof CrossDBResultSet)) {
				try{
					((CrossDBResultSet) rs[i]).close();
				}
				catch(Throwable e){
					SqlLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	protected void deregisterResultSet(CrossDBResultSet rs) {
		resultSets.remove(rs);
	}

	public boolean execute(String sql) throws SQLException {
		String fixedSQL = SQLHelper.translate(sql, conn);
		return passthru.execute(fixedSQL);
	}

	public int[] executeBatch() throws SQLException {
		int[] result = passthru.executeBatch();
		return result;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		CrossDBResultSet rs = new CrossDBResultSet(passthru.executeQuery(sqlFixed), this);
		registerResultSet(rs);
		return rs;
	}

	public int executeUpdate(String sql) throws SQLException {
		String sqlFixed = SQLHelper.translate(sql, conn);
		int result = passthru.executeUpdate(sqlFixed);
		return result;
	}

	public Connection getConnection() throws SQLException {
		return conn;
	}

	public int getFetchDirection() throws SQLException {
		return passthru.getFetchDirection();
	}

	public int getFetchSize() throws SQLException {
		return passthru.getFetchSize();
	}

	public int getMaxFieldSize() throws SQLException {
		return passthru.getMaxFieldSize();
	}

	public int getMaxRows() throws SQLException {
		return passthru.getMaxRows();
	}

	public boolean getMoreResults() throws SQLException {
		return passthru.getMoreResults();
	}

	public int getQueryTimeout() throws SQLException {
		return passthru.getQueryTimeout();
	}

	public ResultSet getResultSet() throws SQLException {
		CrossDBResultSet rs = new CrossDBResultSet(passthru.getResultSet(), this);
		return rs;
	}

	public int getResultSetConcurrency() throws SQLException {
		return passthru.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return passthru.getResultSetType();
	}


	public int getUpdateCount() throws SQLException {
		return passthru.getUpdateCount();
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		return passthru.getWarnings();
	}

	protected void registerResultSet(CrossDBResultSet rs) {
		resultSets.add(rs);
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public void setEscapeProcessing(boolean b) throws SQLException {
		passthru.setEscapeProcessing(b);
	}

	public void setFetchDirection(int direction) throws SQLException {
		passthru.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException {
		passthru.setFetchSize(rows);
	}

	public void setMaxFieldSize(int size) throws SQLException {
		passthru.setMaxFieldSize(size);
	}

	public void setMaxRows(int maxRows) throws SQLException {
		passthru.setMaxRows(maxRows);
	}

	public void setQueryTimeout(int time) throws SQLException {
		passthru.setQueryTimeout(time);
	}

	public int getResultSetHoldability() throws SQLException {
		return passthru.getResultSetHoldability();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return passthru.getMoreResults(current);
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();

	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw DbExceptionHelper.getUnsupportedException();
	}

	public Statement getRealStatement() {
		return passthru;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return passthru.isClosed();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return passthru.isPoolable();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		passthru.setPoolable(poolable);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return passthru.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return passthru.unwrap(iface);
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		this.passthru.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return passthru.isCloseOnCompletion();
	}
}