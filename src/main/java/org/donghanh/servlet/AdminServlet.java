package org.donghanh.servlet;

import org.donghanh.service.ParameterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Map;

import static org.donghanh.service.CandidateService.reloadAllCandidatesData;
import static org.donghanh.service.JuryService.createJuryTable;
import static org.donghanh.service.ParameterService.*;
import static org.donghanh.service.UniversityService.createCandidateTableFor;

@WebServlet("/app/admin")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class AdminServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String action = request.getParameter("action");
    if (action == null) {
      request.getRequestDispatcher("/jsp/admin.jsp").forward(request, response);
    } else {
      switch (action) {
        case "reset-params":
          resetParams();
          request.getRequestDispatcher("parameter").forward(request, response);
          break;
        case "load-data":
          String university = request.getParameter("university");
          reloadAllCandidatesData();
          if (university == null) {
            request.getRequestDispatcher("candidate").forward(request, response);
          } else {
            createCandidateTableFor(university);
            request.getRequestDispatcher("candidate?university=" + university).forward(request, response);
          }
          break;
        default:
          request.getRequestDispatcher("/jsp/admin.jsp").forward(request, response);
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Map<String, String> commonParams = getCommonParams();
    // constructs path of the directory to save uploaded file
    String path = commonParams.get("LOCAL_PATH") + "Data";

    // Create path components to save the file
//    final String path = request.getParameter("destination");
    final Part filePart = request.getPart("file");
    final String fileName = getFileName(filePart);

    OutputStream out = null;
    InputStream filecontent = null;
    String message;
    String alertStatus;

    try {
      out = new FileOutputStream(new File(path + File.separator + fileName));
      filecontent = filePart.getInputStream();

      int read = 0;
      final byte[] bytes = new byte[1024];

      while ((read = filecontent.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      message = "New data " + fileName + " uploaded. "
          + "This file will be used when creating candidate info table";
      ParameterService.updateParam("LOCAL_SOURCE_FILE", "Data/" + fileName);
      alertStatus = "alert-success";

    } catch (FileNotFoundException fne) {
      message = "You either did not specify a file to upload or are "
          + "trying to upload a file to a protected or nonexistent "
          + "location. ERROR: " + fne.getMessage();
      alertStatus = "alert-danger";
    } finally {
      if (out != null) {
        out.close();
      }
      if (filecontent != null) {
        filecontent.close();
      }
    }
    request.setAttribute("alertMessage", message);
    request.setAttribute("alertStatus", alertStatus);
    doGet(request, response);
  }

  private void resetParams() {
    setDefaultCommonParams();
    setDefaultAllUniversitiesParams();
    createJuryTable();
  }

  private String getFileName(final Part part) {
    final String partHeader = part.getHeader("content-disposition");
    for (String content : partHeader.split(";")) {
      if (content.trim().startsWith("filename")) {
        return content.substring(
            content.indexOf('=') + 1).trim().replace("\"", "");
      }
    }
    return null;
  }

}
