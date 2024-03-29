package org.donghanh.servlet;

import org.donghanh.service.ParameterService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.donghanh.common.Constants.CANDIDATE_TABLE_NAME;
import static org.donghanh.db.MainDataSource.getConnection;

@WebServlet("/app/candidate")
public class CandidateServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Map<String, String> commonParams = ParameterService.getCommonParams();
    String university = request.getParameter("university");
    if (university != null) {
      showCandidatesForUniversity(request, response, university);
    } else {
      showAllCandidates(request, response, commonParams);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private void showAllCandidates(HttpServletRequest request, HttpServletResponse response,
                                 Map<String, String> commonParams) throws IOException,
      ServletException {
    String[] headers = commonParams.get("HEADERS_LIST").split(",");
    request.setAttribute("headers", headers);
    request.setAttribute("title", "Danh sách sinh viên tất cả các trường");
    List<List<String>> candidates = new ArrayList<>();

    try {
      Connection conn = getConnection();
      Statement stmt = conn.createStatement();

      String sql = "SELECT * FROM " + CANDIDATE_TABLE_NAME + "_main "
          + "WHERE `Ki` = " + commonParams.get("CURRENT_SEMESTER") + ";";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        List<String> candidate = new ArrayList<>();
        for (int index = 0; index < headers.length; ++index) {
          String[] headerTypes = commonParams.get("HEADERTYPES_LIST").split(",");
          if (headerTypes[index].equals("INT NOT NULL")) {
            candidate.add(String.valueOf(rs.getInt(headers[index])));
          } else {
            candidate.add(rs.getString(headers[index]));
          }
        }
        candidates.add(candidate);
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    request.setAttribute("candidates", candidates);
    request.getRequestDispatcher("/jsp/candidates.jsp").forward(request, response);
  }

  private void showCandidatesForUniversity(HttpServletRequest request, HttpServletResponse response,
                                           String university) throws ServletException, IOException {
    List<Map<String, String>> candidates = new ArrayList<>();
    try {
      Connection conn = getConnection();
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM all_candidates_" + university;
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        Map<String, String> candidate = new HashMap<>();
        candidate.put("code", String.valueOf(rs.getInt("Ma so")));
        candidate.put("last_name", rs.getString("Ho dem"));
        candidate.put("first_name", rs.getString("Ten"));
        candidates.add(candidate);
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    request.setAttribute("candidates", candidates);
    request.getRequestDispatcher("/jsp/candidates-simple.jsp").forward(request, response);
  }


}
