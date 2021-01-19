package org.donghanh.service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;
import static org.donghanh.common.Constants.CANDIDATE_TABLE_NAME;
import static org.donghanh.db.DBCPDataSource.getConnection;

public class UniversityService {

  public static int getNbCandidates(String university) {
    try (Connection conn = getConnection();
         Statement statement = conn.createStatement()) {
      ResultSet resultSet = statement.executeQuery(
          "SELECT count(*) AS nb FROM all_candidates_" + university);
      resultSet.next();
      return resultSet.getInt("nb");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static void createCandidateTableFor(String university) {
    Map<String, String> commonParams = ParameterService.getCommonParams();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql;
      sql = "CREATE TABLE IF NOT EXISTS all_candidates_" + university + " AS (SELECT DISTINCT "
          + "`Ma so`, `Ho dem`, `Ten`, '00000000000000000000' AS `Phan phoi giam khao` FROM "
          + CANDIDATE_TABLE_NAME + " WHERE `Ki` ="
          + commonParams.get("CURRENT_SEMESTER") + " AND `Truong` = '" + university + "');";
      stmt.execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static List<Integer> getCandidateCodes(String university) {
    List<Integer> candidateCodes = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT * FROM all_candidates_" + university;
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        candidateCodes.add(rs.getInt("Ma so"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return candidateCodes;
  }

  public static void updateJuryDistribution(String juryDistribution, int candidateCode, String university) {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      stmt.execute("UPDATE all_candidates_" + university + " SET `Phan phoi giam khao`='"
          + juryDistribution + "' WHERE `Ma so`='" + candidateCode + "';");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public static Map<String, UniversityParams> getAllUniversityParams() {
    Map<String, UniversityParams> universityParamsMap = new HashMap<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT * FROM universities";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        UniversityParams uniParams = new UniversityParams.Builder()
            .code(rs.getString("Code"))
            .name(rs.getString("UniversityName"))
            .evaluatedBy(rs.getString("EvaluatedBy"))
            .studentClass(rs.getString("StudentClass"))
            .nbJuries(rs.getInt("MaxDocs"))
            .vnCoef(rs.getDouble("VnCoefs"))
            .nbJuriesByCopy(rs.getInt("NbJugesByCopy"))
            .logo(rs.getString("Logo"))
            .foundation(Constants.Foundation.valueOf(rs.getString("EvaluatedBy")))
            .build();
        universityParamsMap.put(rs.getString("Code"), uniParams);
      }
    } catch (MySQLSyntaxErrorException e) {
      System.out.println("University table hasn't been created yet");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return universityParamsMap;
  }

  public static UniversityParams getUniversityParams(String universityCode) {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT * FROM universities WHERE Code='" + universityCode + "';";
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      return new UniversityParams.Builder()
          .code(rs.getString("Code"))
          .name(rs.getString("UniversityName"))
          .evaluatedBy(rs.getString("EvaluatedBy"))
          .studentClass(rs.getString("StudentClass"))
          .nbJuries(rs.getInt("MaxDocs"))
          .vnCoef(rs.getDouble("VnCoefs"))
          .nbJuriesByCopy(rs.getInt("NbJugesByCopy"))
          .logo(rs.getString("Logo"))
          .foundation(Constants.Foundation.valueOf(rs.getString("EvaluatedBy")))
          .build();
    } catch (SQLException e) {
      e.printStackTrace();
      return new UniversityParams.Builder().build();
    }
  }

  public static List<String> getUniversityCodes() {
    List<String> codes = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT code FROM universities;";
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      while (rs.next()) {
        codes.add(rs.getString("Code"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return codes;
  }

  public static Map<String, List<List<UniversityParams>>> getUniversitiesByLocationAndColumns(int nbColumns) {
    Map<String, UniversityParams> universityParams = getAllUniversityParams();
    Map<String, List<UniversityParams>> locationToUniversities = new HashMap<>();
    for (UniversityParams params : universityParams.values()) {
      locationToUniversities.putIfAbsent(params.evaluatedBy, new ArrayList<>());
      locationToUniversities.get(params.evaluatedBy).add(params);
    }
    Map<String, List<List<UniversityParams>>> locationToColumns = new HashMap<>();
    for (String location : locationToUniversities.keySet()) {
      List<UniversityParams> universities = locationToUniversities.get(location);
      locationToColumns.put(location, new ArrayList<>());
      int minItemsPerColumn = max(universities.size() / nbColumns, 1);
      for (int i = 0; i < nbColumns; i++) {
        int fromIndex = i * minItemsPerColumn;
        int toIndex = i == nbColumns - 1 ? universities.size() : (i + 1) * minItemsPerColumn;
        if (fromIndex < toIndex) {
          locationToColumns.get(location)
              .add(0, new ArrayList<>(universities.subList(fromIndex, toIndex)));
        }
      }
    }
    return locationToColumns;
  }

  public static void resetUniversity(String university) {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SHOW TABLES";
      ResultSet rs = stmt.executeQuery(sql);
      List<String> allTables = new ArrayList<>();
      while (rs.next()) {
        allTables.add(rs.getString("Tables_in_test"));
      }
      for (String table : allTables) {
        if (table.contains(university)) {
          stmt.executeUpdate("DROP TABLE " + table);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
