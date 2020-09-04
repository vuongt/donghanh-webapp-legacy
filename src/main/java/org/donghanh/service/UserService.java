package org.donghanh.service;

import org.donghanh.db.UserDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
  public static boolean isValidUser(String name, String pass, String type) {
    boolean isValid = false;
    try (Connection conn = UserDataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement
             ("select * from register where name=? and pass=? and type=?")) {
      ps.setString(1, name);
      ps.setString(2, pass);
      ps.setString(3, type);
      ResultSet rs = ps.executeQuery();
      isValid = rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isValid;
  }
}
