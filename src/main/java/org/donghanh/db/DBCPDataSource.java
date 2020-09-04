package org.donghanh.db;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true"
      + "&characterEncoding=utf-8&useSSL=false";
  private static final String DB_USER = "dbuser";
  private static final String DB_PASSWORD = "dbuser789";

  private static BasicDataSource ds = new BasicDataSource();

  static {
    ds.setUrl(DB_URL);
    ds.setUsername(DB_USER);
    ds.setPassword(DB_PASSWORD);
    ds.setMinIdle(5);
    ds.setMaxIdle(10);
    ds.setMaxOpenPreparedStatements(100);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  private DBCPDataSource() {
  }
}
