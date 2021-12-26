package org.donghanh.service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.donghanh.db.MainDataSource.getConnection;

public class JuryService {

  public static void createAllEvaluationTableIfNotExist(String university, int nbJuries) {
    try (Connection conn = getConnection();
         Statement statement = conn.createStatement()) {
      for (int juryIndex = 0; juryIndex <= nbJuries; juryIndex++) {
        String query = "CREATE TABLE IF NOT EXISTS candidates_" + university + "_jury_"
            + juryIndex + " AS SELECT `Ma so`, `Ho dem`, `Ten`, "
            + "'000000' AS hoancanh, '000000' AS hoancanhnorm,"
            + " '000000' AS hoctap, '000000' AS hoctapnorm, "
            + "'000000' AS uocmo, '000000' AS uocmonorm, '000000'"
            + " AS diemcong, '000000' AS diemcongnorm, "
            + "'000000' AS tongket, '000000' AS tongketnorm"
            + " FROM all_candidates_"
            + university + " WHERE `Phan phoi giam khao` LIKE '%G" + juryIndex + "G%';";
        statement.execute(query);
      }
    } catch (SQLException e) {
      e.getMessage();
    }
  }

  public static Map<String, String> juriesCodeToName() {
    Map<String, String> juriesData = new HashMap<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "SELECT * FROM jury";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        juriesData.put(rs.getString("Code"), rs.getString("Name"));
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    return juriesData;
  }

  public static Map<String, Object> getJuryScoreDetails(int juryIndex, String university) {
    Map<String, Object> juryScoreDetails = new HashMap<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      juryScoreDetails.put("allCandidatesScores", getAllCandidatesScoresForJury(stmt, juryIndex, university));
      juryScoreDetails.put("average", getAverageScoreForJury(stmt, juryIndex, university));
      juryScoreDetails.put("stdev", getStdDevForJury(stmt, juryIndex, university));
    } catch (SQLException e) {
      e.getMessage();
    }
    return juryScoreDetails;
  }

  public static List<Map<String, Object>> getAllCandidatesForJury(int juryIndex, String university) {
    List<Map<String, Object>> allCandidatesScores = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String query = "SELECT * FROM candidates_" + university + "_jury_" + juryIndex;
      ResultSet resultSet = stmt.executeQuery(query);
      while (resultSet.next()) {
        Map<String, Object> candidateInfo = new HashMap<>();
        candidateInfo.put("code", resultSet.getString("Ma so"));
        candidateInfo.put("last_name", resultSet.getString("Ho dem"));
        candidateInfo.put("first_name", resultSet.getString("Ten"));
        allCandidatesScores.add(candidateInfo);
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    return allCandidatesScores;
  }

  private static List<Map<String, Object>> getAllCandidatesScoresForJury(
      Statement stmt, int juryIndex, String university) throws SQLException {
    List<Map<String, Object>> allCandidatesScores = new ArrayList<>();
    String query = "SELECT * FROM candidates_" + university + "_jury_" + juryIndex;
    ResultSet resultSet = stmt.executeQuery(query);
    while (resultSet.next()) {
      Map<String, Object> candidateInfo = new HashMap<>();
      candidateInfo.put("code", resultSet.getString("Ma so"));
      candidateInfo.put("last_name", resultSet.getString("Ho dem"));
      candidateInfo.put("first_name", resultSet.getString("Ten"));
      candidateInfo.put("hoancanh", resultSet.getDouble("hoancanh"));
      candidateInfo.put("hoctap", resultSet.getDouble("hoctap"));
      candidateInfo.put("uocmo", resultSet.getDouble("uocmo"));
      candidateInfo.put("diemcong", resultSet.getDouble("diemcong"));
      candidateInfo.put("hoancanhnorm", resultSet.getDouble("hoancanhnorm"));
      candidateInfo.put("hoctapnorm", resultSet.getDouble("hoctapnorm"));
      candidateInfo.put("uocmonorm", resultSet.getDouble("uocmonorm"));
      candidateInfo.put("diemcongnorm", resultSet.getDouble("diemcongnorm"));
      candidateInfo.put("tongket", resultSet.getDouble("tongket"));
      candidateInfo.put("tongketnorm", resultSet.getDouble("tongketnorm"));
      allCandidatesScores.add(candidateInfo);
    }
    return allCandidatesScores;
  }

  private static Map<String, Double> getAverageScoreForJury(
      Statement stmt, int juryIndex, String university) throws SQLException {
    Map<String, Double> average = new HashMap<>();
    ResultSet resultSet = stmt.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, "
        + "ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, "
        + "ROUND(AVG(diemcong),2) AS diemcong, "
        + "ROUND(AVG(hoancanhnorm),2) AS hoancanhnorm, "
        + "ROUND(AVG(hoctapnorm),2) AS hoctapnorm, ROUND(AVG(uocmonorm),2) AS uocmonorm, "
        + "ROUND(AVG(diemcongnorm),2) AS diemcongnorm, "
        + "ROUND(AVG(tongket),2) AS tongket, ROUND(AVG(tongketnorm),2) AS tongketnorm "
        + "FROM candidates_" + university + "_jury_" + juryIndex);
    resultSet.next();
    average.put("hoancanh", resultSet.getDouble("hoancanh"));
    average.put("hoctap", resultSet.getDouble("hoctap"));
    average.put("uocmo", resultSet.getDouble("uocmo"));
    average.put("diemcong", resultSet.getDouble("diemcong"));
    average.put("hoancanhnorm", resultSet.getDouble("hoancanhnorm"));
    average.put("hoctapnorm", resultSet.getDouble("hoctapnorm"));
    average.put("uocmonorm", resultSet.getDouble("uocmonorm"));
    average.put("diemcongnorm", resultSet.getDouble("diemcongnorm"));
    average.put("tongket", resultSet.getDouble("tongket"));
    average.put("tongketnorm", resultSet.getDouble("tongketnorm"));
    return average;
  }

  private static Map<String, Double> getStdDevForJury(
      Statement stmt, int juryIndex, String university) throws SQLException {
    Map<String, Double> stddev = new HashMap<>();
    ResultSet resultSet = stmt.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, "
        + "ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, "
        + "ROUND(STDDEV(diemcong),2) AS diemcong, "
        + "ROUND(STDDEV(hoancanhnorm),2) AS hoancanhnorm, "
        + "ROUND(STDDEV(hoctapnorm),2) AS hoctapnorm, ROUND(STDDEV(uocmonorm),2) AS uocmonorm, "
        + "ROUND(STDDEV(diemcongnorm),2) AS diemcongnorm, "
        + "ROUND(STDDEV(tongket),2) AS tongket, ROUND(STDDEV(tongketnorm),2) AS tongketnorm "
        + "FROM candidates_" + university + "_jury_" + juryIndex);
    resultSet.next();
    stddev.put("hoancanh", resultSet.getDouble("hoancanh"));
    stddev.put("hoctap", resultSet.getDouble("hoctap"));
    stddev.put("uocmo", resultSet.getDouble("uocmo"));
    stddev.put("diemcong", resultSet.getDouble("diemcong"));
    stddev.put("hoancanhnorm", resultSet.getDouble("hoancanhnorm"));
    stddev.put("hoctapnorm", resultSet.getDouble("hoctapnorm"));
    stddev.put("uocmonorm", resultSet.getDouble("uocmonorm"));
    stddev.put("diemcongnorm", resultSet.getDouble("diemcongnorm"));
    stddev.put("tongket", resultSet.getDouble("tongket"));
    stddev.put("tongketnorm", resultSet.getDouble("tongketnorm"));
    return stddev;
  }

  public static List<Map<String, Object>> recreateBilan(String university, int nbJuries, double vnCoef) {
    List<Map<String, Object>> allCandidatesScores = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      recreateBilanAndSelectedTable(stmt, university, nbJuries, vnCoef);
      allCandidatesScores = getFinalScores(stmt, university, nbJuries);
    } catch (SQLException e) {
      e.getMessage();
    }
    return allCandidatesScores;
  }

  private static void recreateBilanAndSelectedTable(Statement stmt, String university, int nbJuries, double vnCoef) throws SQLException {
    String sql = "DROP TABLE IF EXISTS bilan_" + university;
    stmt.execute(sql);

    sql = "CREATE TABLE bilan_" + university + " AS SELECT DISTINCT "
        + "all_candidates_" + university + ".`Ma so`, "
        + "all_candidates_" + university + ".`Ho dem`, "
        + "all_candidates_" + university + ".`Ten`, ";
    for (int count = 0; count <= nbJuries; ++count) {
      sql += "candidates_" + university + "_jury_" + count + ".tongketnorm AS gk" + count + ", ";
    }
    sql += "'000000' AS finalscore FROM all_candidates_" + university + " ";
    for (int count = 0; count <= nbJuries; ++count) {
      sql += "LEFT JOIN candidates_" + university + "_jury_" + count + " ";
      sql += "ON all_candidates_" + university + ".`Ma so` = candidates_" + university + "_jury_" + count + ".`Ma so`";
    }
    stmt.execute(sql);

    sql = "CREATE TABLE IF NOT EXISTS selected_" + university + " AS SELECT "
        + "all_candidates_" + university + ".`Ma so`, "
        + "'0' AS selected FROM all_candidates_" + university;
    stmt.execute(sql);

    if (nbJuries > 0) {
      sql = "UPDATE bilan_" + university + " SET finalscore=ROUND( COALESCE(gk0, 0) * "
          + vnCoef + " + (";
      for (int count = 1; count < nbJuries; ++count) {
        sql += "COALESCE(gk" + count + ", 0) + ";
      }

      sql += "COALESCE(gk" + nbJuries + ", 0))/GREATEST((";
      for (int count = 1; count < nbJuries; ++count) {
        sql += "COALESCE(CEIL(gk" + count + "/(gk" + count + "+ 1)), 0) + ";
      }
      sql += "COALESCE(CEIL(gk" + nbJuries + "/(gk" + nbJuries + "+ 1)), 0)), 1) * " + (1 - vnCoef) + ", 2)";
      stmt.execute(sql);
    }

    sql = "ALTER TABLE bilan_" + university + " MODIFY COLUMN finalscore DECIMAL(10,2)";
    stmt.execute(sql);
  }

  private static List<Map<String, Object>> getFinalScores(Statement stmt, String university, int nbJuries) throws SQLException {
    List<Map<String, Object>> allCandidatesScores = new ArrayList<>();
    String sql = "SELECT bilan_" + university + ".*, selected_" + university + ".selected FROM bilan_"
        + university + " INNER JOIN selected_" + university + " "
        + "ON bilan_" + university + ".`Ma so` = selected_" + university
        + ".`Ma so` ORDER BY finalscore DESC, selected_" + university + ".`Ma so` ASC";
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {
      Map<String, Object> oneCandidateScores = new HashMap<>();
      oneCandidateScores.put("code", rs.getString("Ma so"));
      oneCandidateScores.put("last_name", rs.getString("Ho dem"));
      oneCandidateScores.put("first_name", rs.getString("Ten"));
      List<String> scores = new ArrayList<>();
      for (int count = 0; count <= nbJuries; ++count) {
        if (rs.getString("gk" + count) == null) {
          scores.add("");
        } else {
          scores.add(String.valueOf(rs.getDouble("gk" + count)));
        }
      }
      oneCandidateScores.put("scores", scores);
      oneCandidateScores.put("finalScore", rs.getDouble("finalscore"));
      boolean selected = rs.getInt("selected") != 0;
      oneCandidateScores.put("selected", selected);
      allCandidatesScores.add(oneCandidateScores);
    }
    return allCandidatesScores;
  }

  public static void saveEvaluation(HttpServletRequest request) {
    try {
      request.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException ignored) {
      // ignored
    }
    String juryIndex = request.getParameter("juryIndex");
    String university = request.getParameter("university");
    String juryName = request.getParameter("juryName");

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      // Update jury name
      String sql = "INSERT INTO jury VALUES ('" + university + "_" + juryIndex + "', '" + juryName + "')";
      try {
        stmt.execute(sql);
      } catch (SQLException e) {
        sql = "UPDATE jury SET Name='" + juryName + "' WHERE Code='" + university + "_" + juryIndex + "'";
        try {
          stmt.execute(sql);
        } catch (Exception ignored) {
          // ignore
        }
      }

      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String currentParam = paramNames.nextElement();
        if (currentParam.contains("_")) {
          String competence = currentParam.split("_")[0];
          String candidateId = currentParam.split("_")[1];
          sql = "UPDATE candidates_" + university + "_jury_" + juryIndex + " SET " + competence
              + "=" + request.getParameter(currentParam) + " WHERE `Ma so`=" + candidateId;
          stmt.execute(sql);
        }
      }

      ResultSet rs = stmt.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, ROUND(AVG(diemcong),2) AS diemcong FROM candidates_" + university + "_jury_" + juryIndex);
      rs.next();

      double[] average = new double[]{rs.getDouble("hoancanh"), rs.getDouble("hoctap"), rs.getDouble("uocmo"), rs.getDouble("diemcong")};

      rs = stmt.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, ROUND(STDDEV(diemcong),2) AS diemcong FROM candidates_" + university + "_jury_" + juryIndex);
      rs.next();

      double[] stddev = new double[]{rs.getDouble("hoancanh"), rs.getDouble("hoctap"), rs.getDouble("uocmo"), rs.getDouble("diemcong")};
      for (int index = 0; index < 4; ++index) {
        if (Math.abs(stddev[index]) < 0.01) {
          stddev[index] = 1;
        }
      }

      sql = "UPDATE candidates_" + university + "_jury_" + juryIndex + " SET "
          + "hoancanhnorm = ROUND((hoancanh -" + average[0] + ")*2/" + stddev[0] + " + 10, 2), "
          + "hoctapnorm = ROUND((hoctap -" + average[1] + ")*2/" + stddev[1] + " + 10, 2), "
          + "uocmonorm = ROUND((uocmo -" + average[2] + ")*2/" + stddev[2] + " + 10, 2), "
          + "diemcongnorm = ROUND((diemcong -" + average[3] + ")*2/" + stddev[3] + " + 10, 2)";
      stmt.execute(sql);

      sql = "UPDATE candidates_" + university + "_jury_" + juryIndex + " SET "
          + "tongket = ROUND(hoancanhnorm * 0.5 + hoctapnorm * 0.3 + uocmonorm * 0.1 + diemcongnorm * 0.1, 2)";
      stmt.execute(sql);

      rs = stmt.executeQuery("SELECT ROUND(AVG(tongket),2) AS avg, ROUND(STDDEV(tongket),2) AS stddev FROM candidates_" + university + "_jury_" + juryIndex);
      rs.next();

      double totalavg = rs.getDouble("avg");
      double totalstddev = rs.getDouble("stddev");
      if (Math.abs(totalstddev) < 0.001) {
        totalstddev = 1;
        sql = "UPDATE candidates_" + university + "_jury_" + juryIndex + " SET "
            + "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + ", 2)";
      } else {
        sql = "UPDATE candidates_" + university + "_jury_" + juryIndex + " SET "
            + "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + " + 10, 2)";
      }
      stmt.execute(sql);
    } catch (SQLException e) {
      e.getMessage();
    }
  }

  public static void createJuryTable() {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS jury(Code VARCHAR(10) PRIMARY KEY, Name VARCHAR(100)) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      stmt.execute(sql);
    } catch (SQLException e) {
      e.getMessage();
    }
  }

}
