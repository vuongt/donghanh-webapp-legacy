package org.donghanh;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static org.donghanh.Utils.getDbConnection;


/**
 * Servlet implementation class Jury.
 */
@WebServlet("/Jury")
public class Jury extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public Jury() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      response.setContentType("text/html");
      response.setCharacterEncoding("utf-8");

      PrintWriter out = response.getWriter();

      String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
      out.println(doctype);
      out.println("<head><meta charset=\"UTF-8\" /></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
      out.println("<body>");
      out.println("<h4>Ấn Ctrl+F5 để xem nội dung trang.</h4>");
      out.println("<h4>Nếu bạn vẫn không thể xem được nội dung sau khi nhấn, vui lòng quay lại trang đăng nhập và điền chính xác các từ khóa.</h4>");
      out.println("<h6><a href='Login.html'>Trang đăng nhập</a></h6>");
      out.println("</body>");
      return;
    }
    Cookie cookie = cookies[0];
    if (!(cookie.getName().equals("Validated"))) {
      RequestDispatcher rs = request.getRequestDispatcher("/Login.html");
      rs.forward(request, response);
      return;
    }

    String action = request.getParameter("action");
    Parameters.getParamsFromDb();

    if ("EVALUATE".equals(action)) {
      evaluate(request, response, request.getParameter("university"));
    } else if ("JURY_NORMALIZE".equals(action)) {
      juryNormalize(request, response);
    } else if ("LOG_OUT".equals(action)) {
      Login.logIn(request, response);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private void evaluate(HttpServletRequest request, HttpServletResponse response,
                        String university) throws IOException, ServletException {
    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");
    String title = "Danh sách sinh viên " + Parameters.UNIVERSITY_NAMES.get(university)
        + " phân chia theo giám khảo";
    request.setAttribute("title", title);

    Statement statement;
    Connection conn;

    try {
      conn = getDbConnection();
      statement = conn.createStatement();
      String query;
      ResultSet resultSet = statement.executeQuery(
          "SELECT count(*) AS nb FROM all_candidates_" + university);
      resultSet.next();
      int nbCandidates = resultSet.getInt("nb");

      int nbJudges = LocalFunctions.nbJuges(
          nbCandidates, Parameters.NB_JUGES_BY_COPY.get(university),
          Parameters.MAX_DOCS.get(university), Parameters.EVALUATED_BY.get(university));

      request.setAttribute("nbJudges", nbJudges);
      request.setAttribute("university", university);
      List<Map<String, Object>> juries = new ArrayList<>();

      for (int juryIndex = 0; juryIndex <= nbJudges; ++juryIndex) {
        query = "CREATE TABLE IF NOT EXISTS candidates_" + university + "_jury_"
            + juryIndex + " AS SELECT `Ma so`, `Ho dem`, `Ten`, "
            + "'000000' AS hoancanh, '000000' AS hoancanhnorm,"
            + " '000000' AS hoctap, '000000' AS hoctapnorm, "
            + "'000000' AS uocmo, '000000' AS uocmonorm, '000000'"
            + " AS diemcong, '000000' AS diemcongnorm, "
            + "'000000' AS tongket, '000000' AS tongketnorm"
            + " FROM all_candidates_"
            + university + " WHERE `Phan phoi giam khao` LIKE '%" + juryIndex + "%';";
        statement.execute(query);
        Map<String, Object> juryData = new HashMap<>();
        juryData.put("index", juryIndex);

        query = "SELECT * FROM jury WHERE Code='" + university + "_" + juryIndex + "'";
        resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
          juryData.put("name", resultSet.getString("Name"));
        }

        query = "SELECT * FROM candidates_" + university + "_jury_" + juryIndex;
        resultSet = statement.executeQuery(query);
        List<Map<String, Object>> candidates = new ArrayList<>();
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
          candidates.add(candidateInfo);
        }
        juryData.put("candidates", candidates);

        resultSet = statement.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, "
            + "ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, "
            + "ROUND(AVG(diemcong),2) AS diemcong, "
            + "ROUND(AVG(hoancanhnorm),2) AS hoancanhnorm, "
            + "ROUND(AVG(hoctapnorm),2) AS hoctapnorm, ROUND(AVG(uocmonorm),2) AS uocmonorm, "
            + "ROUND(AVG(diemcongnorm),2) AS diemcongnorm, "
            + "ROUND(AVG(tongket),2) AS tongket, ROUND(AVG(tongketnorm),2) AS tongketnorm "
            + "FROM candidates_" + university + "_jury_" + juryIndex);
        resultSet.next();
        Map<String, Double> average = new HashMap<>();
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
        juryData.put("average", average);

        resultSet = statement.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, "
            + "ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, "
            + "ROUND(STDDEV(diemcong),2) AS diemcong, "
            + "ROUND(STDDEV(hoancanhnorm),2) AS hoancanhnorm, "
            + "ROUND(STDDEV(hoctapnorm),2) AS hoctapnorm, ROUND(STDDEV(uocmonorm),2) AS uocmonorm, "
            + "ROUND(STDDEV(diemcongnorm),2) AS diemcongnorm, "
            + "ROUND(STDDEV(tongket),2) AS tongket, ROUND(STDDEV(tongketnorm),2) AS tongketnorm "
            + "FROM candidates_" + university + "_jury_" + juryIndex);
        resultSet.next();
        Map<String, Double> stddev = new HashMap<>();
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
        juryData.put("stddev", stddev);

        juries.add(juryData);
      }
      request.setAttribute("juries", juries);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    request.getRequestDispatcher("/jsp/evaluate.jsp").forward(request, response);
  }

  private void juryNormalize(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String jury = request.getParameter("jury");
    String university = request.getParameter("university");
    String jugename = request.getParameter("jugename");

    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();

      String sql =
          "INSERT INTO jury VALUES ('" + university + "_" + jury + "', '" + jugename + "')";
      try {
        stmt.execute(sql);
      } catch (Exception e) {
        sql =
            "UPDATE jury SET Name='" + jugename + "' WHERE Code='" + university + "_" + jury + "'";
        try {
          stmt.execute(sql);
        } catch (Exception ignored) {
          // ignored
        }
      }

      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String currentParam = paramNames.nextElement();
        if (currentParam.contains("_")) {
          String candidateId = currentParam.split("_")[1];
          String competence = currentParam.split("_")[0];

          sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET " + competence + "="
              + request.getParameter(currentParam) + " WHERE `Ma so`=" + candidateId;
          stmt.execute(sql);
        }
      }

      ResultSet rs = stmt.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, "
          + "ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, "
          + "ROUND(AVG(diemcong),2) AS diemcong FROM candidates_" + university + "_jury_" + jury);
      rs.next();

      double[] average = new double[]{
          rs.getDouble("hoancanh"),
          rs.getDouble("hoctap"),
          rs.getDouble("uocmo"),
          rs.getDouble("diemcong")};

      rs = stmt.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, "
          + "ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, "
          + "ROUND(STDDEV(diemcong),2) AS diemcong FROM candidates_"
          + university + "_jury_" + jury);
      rs.next();

      double[] stddev = new double[]{
          rs.getDouble("hoancanh"),
          rs.getDouble("hoctap"),
          rs.getDouble("uocmo"),
          rs.getDouble("diemcong")};
      for (int index = 0; index < 4; ++index) {
        if (Math.abs(stddev[index]) < 0.01) {
          stddev[index] = 1;
        }
      }

      sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
          + "hoancanhnorm = ROUND((hoancanh -" + average[0] + ")*2/" + stddev[0] + " + 10, 2), "
          + "hoctapnorm = ROUND((hoctap -" + average[1] + ")*2/" + stddev[1] + " + 10, 2), "
          + "uocmonorm = ROUND((uocmo -" + average[2] + ")*2/" + stddev[2] + " + 10, 2), "
          + "diemcongnorm = ROUND((diemcong -" + average[3] + ")*2/" + stddev[3] + " + 10, 2)";
      stmt.execute(sql);

      sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
          + "tongket = ROUND(hoancanhnorm * 0.5 + hoctapnorm * 0.3 + "
          + "uocmonorm * 0.1 + diemcongnorm * 0.1, 2)";
      stmt.execute(sql);

      rs = stmt.executeQuery("SELECT ROUND(AVG(tongket),2) AS avg, ROUND(STDDEV(tongket),2) "
          + "AS stddev FROM candidates_" + university + "_jury_" + jury);
      rs.next();

      double totalavg = rs.getDouble("avg");
      double totalstddev = rs.getDouble("stddev");
      if (Math.abs(totalstddev) < 0.01) {
        totalstddev = 1;
      }

      sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
          + "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + " + 10, 2)";
      stmt.execute(sql);

      conn.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    evaluate(request, response, university);
  }

}
