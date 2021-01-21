package org.donghanh.db;

import org.apache.commons.dbcp.PoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.donghanh.db.DbUtils.setupDataSource;

public class UserDataSource {

  private static final String DB_USER = "dbuser";
  private static final String DB_PASSWORD = "dbuser789";
  private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/users?useSSL=false";

  private static PoolingDataSource dataSource = setupDataSource(USER_DB_URL, DB_USER, DB_PASSWORD);

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  private UserDataSource() {
  }
}
