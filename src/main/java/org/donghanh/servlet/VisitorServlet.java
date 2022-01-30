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
import static org.donghanh.servlet.BilanServlet.showBilan;
import static org.donghanh.utils.Utils.getUserProfile;

@WebServlet("/visitor")
public class VisitorServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String university = request.getParameter("university");
    if (university != null) {
      showBilan(request, response, true);
    } else {
      showDashboard(request, response);
    }
  }

  private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("locationToTitle", Constants.locationToTitle);

    Map<String, List<List<UniversityParams>>> locationToColumns =
        getUniversitiesByLocationAndColumns(2);
    request.setAttribute("locationToColumns", locationToColumns);
    Constants.UserProfile userProfile = getUserProfile(request);
    request.getRequestDispatcher("/jsp/visitor-dashboard.jsp").forward(request, response);
  }
}
