package org.donghanh.service;

import org.donghanh.common.Constants;
import org.donghanh.common.Constants.University;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.donghanh.common.Constants.PARAM_DEFAULT;
import static org.donghanh.db.DBCPDataSource.getConnection;

public class ParameterService {

  public static Map<String, String> getCommonParams() {
    Map<String, String> params = new HashMap<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT * FROM parameters";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        params.put(rs.getString("Param"), rs.getString("Value"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return params;
  }

  public static void setDefaultCommonParams() {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS parameters(Param VARCHAR(100) PRIMARY KEY, Value VARCHAR(400), Security INT) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      stmt.execute(sql);

      for (Entry param : PARAM_DEFAULT.entrySet()) {
        sql =
            "INSERT INTO parameters VALUES(\"" + param.getKey() + "\", \"" + param.getValue() + "\", 0) ";
        stmt.execute(sql);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void setDefaultAllUniversitiesParams() {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS universities(Code VARCHAR(10) PRIMARY KEY, UniversityName VARCHAR(255), "
          + "FoundationName VARCHAR(255), StudentClass VARCHAR(50), EvaluatedBy VARCHAR(50), Logo VARCHAR(255),  VnCoefs VARCHAR(10), NbJugesByCopy VARCHAR(10), MaxDocs VARCHAR(10))"
          + " ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      stmt.execute(sql);

      for (University uni : University.values()) {
        sql = "INSERT INTO universities VALUES(\"" + uni.name() + "\", \""
            + uni.getFullName() + "\", \""
            + uni.getFoundation().getFullName() + "\", \""
            + uni.getStudentClass() + "\", \""
            + uni.getFoundation().getEvaluatedBy() + "\", \""
            + uni.getFoundation().getLogo() + "\", \""
            + Constants.DEFAULT_VN_COEF + "\", \""
            + Constants.DEFAULT_NB_JURIES_BY_COPY + "\", \""
            + Constants.DEFAULT_NB_JURIES + "\")";
        stmt.execute(sql);
      }

      sql = "CREATE TABLE IF NOT EXISTS jury(Code VARCHAR(10) PRIMARY KEY, Name VARCHAR(100)) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      stmt.execute(sql);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void updateParam(String paramName, String paramValue) {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      for (String paramKey : Constants.PARAM_DEFAULT.keySet()) {
        stmt.execute("UPDATE parameters SET Value = '"
            + paramValue + "' WHERE " + "Param = " + "'" + paramName + "'");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
