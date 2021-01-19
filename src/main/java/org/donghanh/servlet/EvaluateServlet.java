package org.donghanh.servlet;

import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.donghanh.service.JuryService.*;
import static org.donghanh.service.UniversityService.getNbCandidates;
import static org.donghanh.service.UniversityService.getUniversityParams;
import static org.donghanh.utils.Utils.getUserProfile;
import static org.donghanh.utils.Utils.nbJuries;

@WebServlet("/app/evaluate")
public class EvaluateServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    showEvaluationByProfile(request, response, false);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    saveEvaluation(request);
    doGet(request, response);
  }

  static void showEvaluationByProfile(HttpServletRequest request, HttpServletResponse response,
                                      boolean readOnly) throws IOException, ServletException {
    String university = request.getParameter("university");
    if (university == null) {
      request.getRequestDispatcher("dashboard").forward(request, response);
      return;
    }

    UniversityParams uniParams = getUniversityParams(university);
    request.setAttribute("title", "Danh sách sinh viên " + uniParams.name
        + " phân chia theo giám khảo");
    Constants.UserProfile userProfile = getUserProfile(request);
    request.setAttribute("userProfile", userProfile.name());

    int nbCandidates = getNbCandidates(university);
    int nbJuries = nbJuries(nbCandidates, uniParams);
    request.setAttribute("nbJudges", nbJuries);
    request.setAttribute("university", university);

    createAllEvaluationTableIfNotExist(university, nbJuries);

    Map<String, String> juriesCodeToName = juriesCodeToName();

    List<Map<String, Object>> juries = new ArrayList<>();
    for (int juryIndex = 0; juryIndex <= nbJuries; ++juryIndex) {
      Map<String, Object> juryData = new HashMap<>();
      juryData.put("index", juryIndex);
      juryData.put("name", juriesCodeToName.get(university + "_" + juryIndex));
      juryData.put("candidates", getAllCandidatesScoresForJury(juryIndex, university));
      juryData.put("average", getAverageScoreForJury(juryIndex, university));
      juryData.put("stddev", getStdDevForJury(juryIndex, university));

      juries.add(juryData);
    }
    request.setAttribute("juries", juries);

    if (userProfile == Constants.UserProfile.ADMIN
        || userProfile == Constants.UserProfile.SUPER_USER) {
      Map<String, Object> bilan = getBilan(nbJuries, uniParams);
      request.setAttribute("bilan", bilan);
    }

    if (readOnly) {
      request.getRequestDispatcher("/jsp/visitor-result.jsp").forward(request, response);
    } else {
      request.getRequestDispatcher("/jsp/evaluate.jsp").forward(request, response);
    }
  }

  private static Map<String, Object> getBilan(int nbJuries, UniversityParams universityParams) {
    Map<String, Object> bilan = new HashMap<>();
    String university = universityParams.code;
    bilan.put("vnCoef", universityParams.vnCoef);
    bilan.put("evaluatedBy", universityParams.evaluatedBy);

    recreateBilanAndSelectedTable(universityParams.code, nbJuries, universityParams.vnCoef);

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
    bilan.put("candidates", getFinalScores(university, nbJuries));
    return bilan;
  }

}
