package org.tutorial.servletfilter.conn;

import java.sql.Connection;

public class ConnectionUtils {

	public static Connection getConnection() {
		Connection conn = null;

		return conn;
	}

	public static void closeQuietly(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void rollbackQuietly(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
        }
	}
}
