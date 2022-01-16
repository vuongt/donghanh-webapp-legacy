package org.donghanh.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDataSource {
  private static final HikariConfig config = new HikariConfig();
  private static final HikariDataSource ds;
  private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/users?useSSL=false";
  private static final String DB_USER = "dbuser";
  private static final String DB_PASSWORD = "dbuser789";

  static {
    config.setJdbcUrl( USER_DB_URL );
    config.setUsername( DB_USER );
    config.setPassword( DB_PASSWORD );
    config.addDataSourceProperty( "cachePrepStmts" , "true" );
    config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
    config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    config.addDataSourceProperty( "maximumPoolSize" , "1" );
    config.addDataSourceProperty( "readOnly" , "true" );
    ds = new HikariDataSource( config );
  }

  private UserDataSource() {}

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
