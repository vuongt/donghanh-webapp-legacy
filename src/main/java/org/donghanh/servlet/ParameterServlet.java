package org.donghanh.servlet;

import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;
import org.donghanh.service.ParameterService;
import org.donghanh.service.UniversityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.donghanh.db.DBCPDataSource.getConnection;

@WebServlet("/app/parameter")
public class ParameterServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    viewParams(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    switch (action) {
      case "updateCommonParams":
        updateCommonParams(request);
        doGet(request, response);
        break;
      case "updateUniversityParams":
        updateUniversityParams(request);
        doGet(request, response);
        break;
      default:
        doGet(request, response);
    }
  }

  private void viewParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Map<String, String> commonParams = ParameterService.getCommonParams();
    request.setAttribute("parameters", commonParams);
    Map<String, UniversityParams> universityParams = UniversityService.getAllUniversityParams();
    request.setAttribute("universityParams", universityParams.values());
    request.setAttribute("title", "Tham số");

    request.getRequestDispatcher("/jsp/parameters.jsp").forward(request, response);
  }

  private void updateCommonParams(HttpServletRequest request) {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      for (String paramKey : Constants.PARAM_DEFAULT.keySet()) {
        stmt.execute("UPDATE parameters SET Value = '"
            + request.getParameter(paramKey) + "' WHERE " + "Param = " + "'" + paramKey + "'");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateUniversityParams(HttpServletRequest request) {
    Map<String, UniversityParams> uniParams = UniversityService.getAllUniversityParams();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      for (String uniCode : uniParams.keySet()) {
        if (request.getParameter(uniCode) != null) {
          String sql = "UPDATE universities SET "
              + "UniversityName = '" + request.getParameter(uniCode + "_name") + "', "
              + "FoundationName = '" + request.getParameter(uniCode + "_foundation") + "', "
              + "StudentClass = '" + request.getParameter(uniCode + "_studentClass") + "', "
              + "EvaluatedBy = '" + request.getParameter(uniCode + "_evaluatedBy") + "', "
              + "Logo = '" + request.getParameter(uniCode + "_logo") + "', "
              + "VnCoefs = '" + request.getParameter(uniCode + "_vnCoef") + "', "
              + "NbJugesByCopy = '" + request.getParameter(uniCode + "_nbJuriesByCopy") + "', "
              + "MaxDocs = '" + request.getParameter(uniCode + "_nbJuries") + "' "
              + "WHERE Code = '" + uniCode + "'";
          stmt.execute(sql);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
