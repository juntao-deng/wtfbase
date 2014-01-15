package net.juniper.jmp.persist.jdbc;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import net.juniper.jmp.persist.SQLParameter;
import net.juniper.jmp.persist.impl.PersistenceHelper;
import net.juniper.jmp.persist.jdbc.trans.SqlTranslator;


public class SQLHelper {

	public static String getInsertSQL(String table, String names[]) {
		StringBuffer buffer = new StringBuffer("INSERT INTO " + table + " (");
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase("ts"))
				continue;
			buffer.append(names[i] + ",");
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append(") VALUES (");
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase("ts"))
				continue;
			buffer.append("?,");
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append(")");
		return buffer.toString();
	}

	public static String getUpdateSQL(String tableName, String[] names,
			String pkName) {
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET  ");
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase("ts"))
				continue;
			sql.append(names[i] + "=?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" WHERE ").append(pkName).append("=?");
		return sql.toString();
	}

	public static String getUpdateSQL(String tableName, String[] names) {
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET  ");
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase("ts"))
				continue;
			sql.append(names[i] + "=?,");
		}
		sql.setLength(sql.length() - 1);
		return sql.toString();
	}

	public static String getDeleteByPKSQL(String tableName, String pkName) {
		return "DELETE FROM " + tableName + " WHERE " + pkName + "=?";
	}

	public static String getDeleteSQL(String tableName, String[] names) {
		StringBuffer sql = new StringBuffer("DELETE FROM " + tableName
				+ " WHERE ");
		for (int i = 0; i < names.length; i++) {
			sql.append(names[i] + "=? AND ");
		}
		sql.setLength(sql.length() - 4);
		return sql.toString();
	}

	public static String getSelectSQL(String tableName, String[] names,
			boolean isAnd, String[] fields) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}
		String append = "AND ";
		if (!isAnd)
			append = "OR ";
		if (names == null || names.length == 0)
			return sql.toString();
		sql.append(" WHERE ");
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			sql.append(name + "=? ");
			if (i != names.length - 1)
				sql.append(append);
		}
		return sql.toString();

	}

	public static String getSelectSQL(String tableName, String[] fields) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}

		return sql.toString();

	}

	public static String getSelectSQL(String tableName, String[] fields,
			String[] names) {
		StringBuffer sql = new StringBuffer();
		if (fields == null)
			sql.append("SELECT * FROM " + tableName);
		else {

			sql.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i] + ",");

			}
			sql.setLength(sql.length() - 1);
			sql.append(" FROM " + tableName);
		}
		String append = "AND ";

		if (names == null || names.length == 0)
			return sql.toString();
		sql.append(" WHERE ");
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			sql.append(name + "=? ");
			if (i != names.length - 1)
				sql.append(append);
		}
		return sql.toString();

	}
	
	public static SQLParameter getSQLParam(Object entity, String names[]) {
		Map<String, Integer> types = PersistenceHelper.getColumnTypes(entity);
		SQLParameter params = new SQLParameter();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			int type = types.get(name.toUpperCase());
			Object value = PersistenceHelper.getEntityValue(entity, name);
			if (value == null) {
				params.addNullParam(type);
				continue;
			}
			if (type == Types.BLOB || type == Types.LONGVARBINARY || type == Types.VARBINARY || type == Types.BINARY) {
				params.addBlobParam(value);
				continue;
			}
			if (type == Types.CLOB || type == Types.LONGVARCHAR) {
				params.addClobParam(String.valueOf(value));
				continue;
			}
			params.addParam(value);

		}
		return params;
	}
	
	public static String buildSql(Class<?> clazz, String condition, String[] fields) {
		String pkField = PersistenceHelper.getPkField(clazz);
		String tableName = PersistenceHelper.getTableName(clazz);
		boolean hasPKField = false;
		StringBuffer buffer = new StringBuffer();
		if (fields == null)
			buffer.append("SELECT * FROM ").append(tableName);
		else {
			buffer.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				buffer.append(fields[i]).append(",");
				if (fields[i].equalsIgnoreCase(pkField))
					hasPKField = true;
			}
			if (!hasPKField)
				buffer.append(pkField).append(",");
			buffer.setLength(buffer.length() - 1);
			buffer.append(" FROM ").append(tableName);
		}
		if (condition != null && condition.length() != 0) {
			if (condition.toUpperCase().trim().startsWith("ORDER "))
				buffer.append(" ").append(condition);
			else
				buffer.append(" WHERE ").append(condition);
		}

		return buffer.toString();
	}
	
	public static String translate(String sql, CrossDBConnection conn) throws SQLException {
		if (sql == null || sql.equals(""))
			return sql;
		
		if (!conn.isSQLTranslatorEnabled()) {
			return sql;
		}
		
		SQLLRUCache cache = SQLCache.getInstance().getCache(conn.getDsName());
		String trans = (String) cache.getPreparedSQL(sql);
		if (trans == null) {
			trans = SqlTranslator.getInstance(conn.getDbType()).getSql(sql);
			cache.putPreparedSQL(sql, trans);
		}
		return trans;
	}
}