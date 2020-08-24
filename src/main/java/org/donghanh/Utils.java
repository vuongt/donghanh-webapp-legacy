package org.donghanh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
  static final String DB_USER = "dbuser";
  static final String DB_PASSWORD = "dbuser789";

  public static Connection getDbConnection() throws SQLException, ClassNotFoundException {
    //Register JDBC Driver
    Class.forName(JDBC_DRIVER);
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }

}
