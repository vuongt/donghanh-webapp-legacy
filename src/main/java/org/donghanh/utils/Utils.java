package org.donghanh.utils;

import org.donghanh.common.Constants;
import org.donghanh.common.Constants.Foundation;
import org.donghanh.common.UniversityParams;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.donghanh.common.Constants.COOKIE_NAME;

public class Utils {

  public static Constants.UserProfile getUserProfile(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(COOKIE_NAME)) {
          switch (cookie.getValue()) {
            case "OKJury":
              return Constants.UserProfile.JURY;
            case "OKAdmin":
              return Constants.UserProfile.ADMIN;
            case "OKSU":
              return Constants.UserProfile.SUPER_USER;
            default:
              return Constants.UserProfile.UNK;
          }
        }
      }
    }
    return Constants.UserProfile.UNK;
  }

  private boolean validateCookie(HttpServletRequest request, String acceptedCookieValue) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        return cookie.getName().equals(COOKIE_NAME)
            && cookie.getValue().equals(acceptedCookieValue);
      }
    }
    return false;
  }

  public static int nbJuries(int nbCandidates, UniversityParams universityParams) {
    if (universityParams.evaluatedBy.equals(Foundation.FR.name())
        || universityParams.evaluatedBy.equals(Foundation.KR.name())) {
      return universityParams.nbJuries;
    } else if (universityParams.evaluatedBy.equals(Foundation.SG.name())) {
      //Công thức cho cách chia nhóm tách rời, áp dụng tại ĐH Taiwan và Singapore
      // Input Nb of juries by copy = nb of juries per group
      // Input Nb of Juries = nb of group
      if (nbCandidates <= 0) {
        return 0;
      } else {
        return universityParams.nbJuries * universityParams.nbJuriesByCopy;
      }
    } else {
      return 0;
    }
  }

  public static String getJuryDistributionSG(int candidateIndex, int nbJuriesByCopy, int nbGroup, int nbCandidates) {

    int numCandidatesPerGroup = nbCandidates / nbGroup; // except last first group
    // get index of the group this candidate belongs to
    int groupIndex = candidateIndex / numCandidatesPerGroup;

    // Get distribution for all candidate in the current group
    StringBuilder sb = new StringBuilder("G0G"); // gk viet nam
    // numJuriesPerCopy = num of juries per group
    for (int juryIndexInGroup = 0; juryIndexInGroup < nbJuriesByCopy; juryIndexInGroup++) {
      int currentJury = groupIndex * nbJuriesByCopy + juryIndexInGroup + 1;
      sb.append(currentJury).append("G");
    }
    return sb.toString();
  }

  public static String getJuryDistributionFR(int candidateIndex, int nbJuriesByCopy, int nbJuries) {
    StringBuilder sb = new StringBuilder("G0G"); // gk viet nam
    for (int i = 0; i < nbJuriesByCopy; i++) {
      int juryIndex = (candidateIndex + i) % nbJuries;
      sb.append(juryIndex).append("G");
    }
    return sb.toString();
  }

  public static String tabs(String base, int nb_tabs) {
    if (nb_tabs == 0)
      return "";
    return
        base + tabs(base, nb_tabs - 1);
  }

  public static void downloadFromUrl(URL url, String localFilename) throws IOException {
    InputStream is = null;
    FileOutputStream fos = null;

    try {
      URLConnection urlConn = url.openConnection(); //connect

      is = urlConn.getInputStream();               //get connection inputstream
      fos = new FileOutputStream(localFilename);   //open outputstream to local file

      byte[] buffer = new byte[4096];              //declare 4KB buffer
      int len;

      //while we have available data, continue downloading and storing to local file
      while ((len = is.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }
    } finally {
      try {
        if (is != null) {
          is.close();
        }
      } finally {
        if (fos != null) {
          fos.close();
        }
      }
    }
  }

}





