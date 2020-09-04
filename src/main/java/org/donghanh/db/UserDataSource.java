package org.donghanh.db;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDataSource {

  private static final String DB_USER = "dbuser";
  private static final String DB_PASSWORD = "dh789";
  private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/users?useSSL=false";

  private static BasicDataSource ds = new BasicDataSource();

  static {
    ds.setUrl(USER_DB_URL);
    ds.setUsername(DB_USER);
    ds.setPassword(DB_PASSWORD);
    ds.setMinIdle(5);
    ds.setMaxIdle(10);
    ds.setMaxOpenPreparedStatements(100);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  private UserDataSource() {
  }
}
