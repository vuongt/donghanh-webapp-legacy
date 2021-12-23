package org.donghanh.servlet;

import org.donghanh.common.UniversityParams;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.donghanh.service.CandidateService.saveSelectedCandidates;
import static org.donghanh.service.JuryService.juriesCodeToName;
import static org.donghanh.service.JuryService.recreateBilan;
import static org.donghanh.service.UniversityService.getNbCandidates;
import static org.donghanh.service.UniversityService.getUniversityParams;
import static org.donghanh.utils.Utils.nbJuries;

@WebServlet("/app/bilan")
public class BilanServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    showBilan(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    saveSelectedCandidates(request);
    showBilan(request, response);
  }

  private void showBilan(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String university = request.getParameter("university");
    if (university == null) {
      request.getRequestDispatcher("dashboard").forward(request, response);
      return;
    }

    UniversityParams uniParams = getUniversityParams(university);
    request.setAttribute("title", "Bảng tổng kết trường" + uniParams.name);

    int nbCandidates = getNbCandidates(university);
    int nbJuries = nbJuries(nbCandidates, uniParams);
    request.setAttribute("nbJudges", nbJuries);
    request.setAttribute("university", university);
    Map<String, Object> bilan = getBilan(nbJuries, uniParams);
    request.setAttribute("bilan", bilan);
    request.getRequestDispatcher("/jsp/bilan.jsp").forward(request, response);
  }

  private static Map<String, Object> getBilan(int nbJuries, UniversityParams universityParams) {
    Map<String, Object> bilan = new HashMap<>();
    String university = universityParams.code;
    bilan.put("vnCoef", universityParams.vnCoef);
    bilan.put("evaluatedBy", universityParams.evaluatedBy);

    List<Map<String, Object>> candidateFinalScores = recreateBilan(universityParams.code, nbJuries, universityParams.vnCoef);
    bilan.put("candidates", candidateFinalScores);

    Map<String, String> juries = juriesCodeToName();
    List<String> juryNames = new ArrayList<>();
    for (int count = 0; count <= nbJuries; count++) {
      String defaultName = "GK " + count;
      if (juries.containsKey(university + "_" + count)) {
        String juryName = juries.get(university + "_" + count).equals("")
            ? defaultName : juries.get(university + "_" + count);
        juryNames.add(juryName);
      } else {
        juryNames.add(defaultName);
      }
    }
    bilan.put("juryNames", juryNames);
    return bilan;
  }

}
