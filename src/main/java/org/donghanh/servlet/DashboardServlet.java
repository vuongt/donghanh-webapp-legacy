package org.donghanh.servlet;

import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.donghanh.service.UniversityService.getUniversitiesByLocationAndColumns;
import static org.donghanh.utils.Utils.getUserProfile;

@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

  private static final int NB_COLUMNS = 2;
  static Map<String, String> locationToTitle;

  static {
    locationToTitle = new HashMap<>();
    locationToTitle.put("FR", "Đồng Hành France");
    locationToTitle.put("SG", "Đồng Hành Singapore");
    locationToTitle.put("KR", "Đồng Hành Korea");
  }

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
    request.setAttribute("locationToTitle", locationToTitle);

    Map<String, List<List<UniversityParams>>> locationToColumns =
        getUniversitiesByLocationAndColumns(NB_COLUMNS);
    request.setAttribute("locationToColumns", locationToColumns);
    Constants.UserProfile userProfile = getUserProfile(request);
    request.setAttribute("userProfile", userProfile.name());
    request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
  }


}
