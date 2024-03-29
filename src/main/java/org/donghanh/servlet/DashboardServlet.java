package org.donghanh.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.donghanh.service.UniversityService.getUniversitiesByLocationAndColumns;
import static org.donghanh.utils.Utils.getUserProfile;

@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

  private static final int NB_COLUMNS = 2;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    showDashboardByProfile(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private void showDashboardByProfile(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setAttribute("locationToTitle", Constants.locationToTitle);

    Map<String, List<List<UniversityParams>>> locationToColumns =
        getUniversitiesByLocationAndColumns(NB_COLUMNS);
    request.setAttribute("locationToColumns", locationToColumns);
    Constants.UserProfile userProfile = getUserProfile(request);
    request.setAttribute("userProfile", userProfile.name());
    request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
  }


}
