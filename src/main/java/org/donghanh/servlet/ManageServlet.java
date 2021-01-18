package org.donghanh.servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.donghanh.common.Constants;
import org.donghanh.common.UniversityParams;
import org.donghanh.service.ParameterService;
import org.donghanh.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.donghanh.common.Constants.FONT;
import static org.donghanh.db.DBCPDataSource.getConnection;
import static org.donghanh.service.JuryService.getAllCandidatesScoresForJury;
import static org.donghanh.service.ParameterService.getCommonParams;
import static org.donghanh.service.UniversityService.*;
import static org.donghanh.utils.Utils.*;

@WebServlet("/app/manage")
public class ManageServlet extends HttpServlet {
  String CSS_PDF = "css/pdf.css";

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String action = request.getParameter("action");
    String university = request.getParameter("university");
    switch (action) {
      case "activate":
        activateUniversityData(request, response);
        break;
      case "result":
        if (university == null) {
          downloadAllNotifs(response);
        } else {
          downloadNotifForUniversity(response, university);
        }
        break;
      case "form":
        if (university == null) {
          downloadAllEvalForms(response);
        } else {
          // todo handle error here
          String finalEvalFormRelativePath = downloadEvalFormForUniversity(university);
          String downloadLink = "http://" + Constants.IP + "/" + finalEvalFormRelativePath;
          response.sendRedirect(downloadLink);
        }
        break;
      default:
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    doGet(request, response);
  }

  private void activateUniversityData(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String university = request.getParameter("university");
    createCandidateTableFor(university);
    distributeJuryCandidate(university);
    request.getRequestDispatcher("candidate?university=" + university)
        .forward(request, response);
  }

  private static void distributeJuryCandidate(String university) {
    List<Integer> candidateCodes = getCandidateCodes(university);
    UniversityParams uniParams = getUniversityParams(university);

    for (int index = 0; index < candidateCodes.size(); index++) {
      String juryDistribution;
      if (uniParams.evaluatedBy().equals("SG")) {
        juryDistribution = getJuryDistributionSG(index, uniParams.nbJuriesByCopy(), uniParams.nbJuries(), candidateCodes.size());
      } else {
        juryDistribution = getJuryDistributionFR(index, uniParams.nbJuriesByCopy(), uniParams.nbJuries());
      }
      updateJuryDistribution(juryDistribution, candidateCodes.get(index), university);
    }

  }

  private void downloadNotifForUniversity(HttpServletResponse response, String university) throws IOException {
    // Create HTML version of the result document
    Map<String, String> commonParams = ParameterService.getCommonParams();
    UniversityParams universityParams = getUniversityParams(university);
    String inputHtml = createHtmlNotification(universityParams, commonParams);
    File f = new File(inputHtml);
    if (!f.exists() || f.isDirectory()) {
      return;
    }

    try {
      Document outputDocument = new Document();
      String outputPdf = commonParams.get("LOCAL_PATH") + "evalforms/"
          + university + "/final_notification_" + university + ".pdf";
      PdfWriter pdfWriter = PdfWriter.getInstance(outputDocument, new FileOutputStream(outputPdf));

      FileInputStream finput = new FileInputStream(inputHtml);
      FileInputStream cssFile = new FileInputStream(getServletContext().getRealPath(CSS_PDF));
      outputDocument.open();
      XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

      //convert to PDF
      worker.parseXHtml(pdfWriter, outputDocument, finput, cssFile, StandardCharsets.UTF_8,
          new XMLWorkerFontProvider(FONT));

      outputDocument.close();
      pdfWriter.close();

    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }

    String downloadLink = "http://" + Constants.IP + "/evalforms/" + university
        + "/final_notification_" + university + ".pdf";
    response.sendRedirect(downloadLink);

  }

  private String createHtmlNotification(UniversityParams universityParams, Map<String,
      String> commonParams) throws FileNotFoundException, UnsupportedEncodingException {
    String university = universityParams.code;
    int nbCandidates = 0;
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      // Get number of candidates from university
      ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
      rs.next();
      nbCandidates = rs.getInt("nb");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (nbCandidates == 0) {
      // return a directory will make downloadNotifForUniversity return nothing
      return commonParams.get("LOCAL_PATH") + "/evalforms";
    }

    File outDir = createEvalDirFor(university, commonParams.get("LOCAL_PATH"));

    String outputPath = outDir.getPath() + "/final_notification_" + university + ".html";
    PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
    writer.println("<html>");
    writer.println("<head>");
    writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
    writer.println("</head>");
    writer.println("<body>");

    // Header
    writer.println("<div style=\"overflow:hidden;\">");
    writer.println("<div style=\"float:left;\">");
    writer.println("<h4>Quỹ " + universityParams.foundation.getFullName() + "</h4>");
    if (universityParams.foundation.equals(Constants.Foundation.SG)) {
      writer.println("<h4>Email: contact-DHSing@donghanh.net</h4>");
    } else if (universityParams.foundation.equals(Constants.Foundation.KR)) {
      writer.println("<h4>Email: contact-DHKorea@donghanh.net</h4>");
    } else {
      writer.println("<h4>16 rue du Petit-Musc</h4>");
      writer.println("<h4>75004 Paris, Cộng hòa Pháp</h4>");
      writer.println("<h4>Email: contact@donghanh.net</h4>");
    }
    writer.println("<h4>Website: donghanh.net</h4>");
    writer.println("</div>");


    writer.println("<div style='float:right; text-align:right; color:white; filter: brightness(50%);'>");
    // This is the additional logo to add on the left of the main log go DH FR, SG or KR
    String logo = universityParams.logo;
    if (!logo.equals("LOGO_DH") && !logo.equals("LOGO_DH_SING") && !logo.equals("LOGO_DH_KOREA")) {
      writer.println("<img src='" + commonParams.get("LOCAL_PATH")
          + commonParams.get(logo) + "' alt='stamp' height='100' width='105'/>");
    }
    // Main logo
    if (logo.equals("LOGO_DH_SING") || logo.equals("LOGO_DH_KOREA")) {
      writer.println("<img src='" + commonParams.get("LOCAL_PATH")
          + commonParams.get(logo) + "' alt='stamp' height='100' width='105'/>");
    } else {
      writer.println("<img src='" + commonParams.get("LOCAL_PATH")
          + commonParams.get("LOGO_DH") + "' alt='stamp' height='100' width='105'></img>");
    }
    writer.println("</div>");
    writer.println("</div>");

    // Body
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<h1>DANH SÁCH SINH VIÊN ĐƯỢC NHẬN "
        + universityParams.foundation.getFullName().toUpperCase() + " KÌ "
        + commonParams.get("CURRENT_SEMESTER") + "</h1>");
    writer.println("<h1>" + universityParams.name + "</h1>");
    writer.println("<br/>");
    writer.println("<br/>");

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      int counter = 1;
      String sql = "(SELECT `Ho dem`, `Ten`, `Gioi tinh`, `Ngay sinh`, `Lop nganh` FROM candidates_main "
          + "INNER JOIN selected_" + university + " ON candidates_main.`Ma so` = selected_"
          + university + ".`Ma so` WHERE selected=1) " + "ORDER BY `Ten`, `Ho dem`";
      ResultSet rs = stmt.executeQuery(sql);

      writer.println("<table width=\"100%\" border=\"1\" align=\"center\">");
      writer.println("<tr>");
      writer.println("<th>STT</th>");
      writer.println("<th colspan=2 >Họ và tên</th>");
      writer.println("<th>Giới tính</th>");
      writer.println("<th>Ngày sinh</th>");
      writer.println("<th>Lớp, ngành</th>");
      writer.println("</tr>");

      while (rs.next()) {
        writer.println("<tr>");
        writer.println("<td style=\"text-align:center;\">" + counter + "</td>");
        writer.println("<td>" + rs.getString("Ho dem") + "</td>");
        writer.println("<td>" + rs.getString("Ten") + "</td>");
        writer.println("<td style=\"text-align:center;\">" + rs.getString("Gioi tinh") + "</td>");
        writer.println("<td style=\"text-align:center;\">" + rs.getString("Ngay sinh") + "</td>");
        writer.println("<td>" + rs.getString("Lop nganh") + "</td>");
        writer.println("</tr>");
        counter++;
      }

      writer.println("</table>");
      writer.println("Danh sách này gồm " + (counter - 1) + " "
          + universityParams.studentClass.toLowerCase() + ".");

      writer.println("<br/>");
      writer.println("<br/>");

      // Signature and stamp
      String foundation = universityParams.foundation.name();
      writer.println("<h5>" + commonParams.get("PLACE_AND_DATE_" + foundation)
          + tabs("&nbsp;", 2) + "</h5>");
      writer.println("<h5>" + commonParams.get("SIGNATURE_TITLE_" + foundation) + "</h5>");
      writer.println("<p style='text-align:right; color:white; filter: brightness(50%);'><img src='"
          + commonParams.get("LOCAL_PATH") + commonParams.get("SIGNATURE_IMAGE_" + foundation)
          + "' alt='stamp' height='140' width='210'></img>--</p>");
      writer.println("<h5>" + commonParams.get("SIGNATURE_NAME_" + foundation)
          + tabs("&nbsp;", 11) + "</h5>");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    writer.println("</body>");
    writer.println("</html>");
    writer.close();
    return outputPath;
  }

  private static void downloadAllNotifs(HttpServletResponse response) throws IOException {

    Map<String, String> commonParams = ParameterService.getCommonParams();
    List<String> universities = getUniversityCodes();
    PDFMergerUtility ut = new PDFMergerUtility();

    for (String university : universities) {
      File f = new File(commonParams.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".pdf");
      if (f.exists() && !f.isDirectory()) {
        ut.addSource(commonParams.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".pdf");
      }
    }
    ut.setDestinationFileName(commonParams.get("LOCAL_PATH") + "evalforms/FinalNotification.pdf");
    try {
      ut.mergeDocuments();
    } catch (COSVisitorException e) {
      e.printStackTrace();
    }

    String downloadLink = "http://" + Constants.IP + "/evalforms/FinalNotification.pdf";
    response.sendRedirect(downloadLink);
  }

  private String downloadEvalFormForUniversity(String university) throws IOException {
    Map<String, String> commonParams = getCommonParams();
    UniversityParams uniParams = getUniversityParams(university);
    int nbCandidates = getNbCandidates(university);
    int nbJuries = Utils.nbJuries(nbCandidates, uniParams);
    List<String> outputFiles = new ArrayList<>();

    createEvalDirFor(university, commonParams.get("LOCAL_PATH"));

    for (int juryIndex = 1; juryIndex <= nbJuries; juryIndex++) {
      PdfWriter pdfWriter;
      Document document = new Document(PageSize.A4.rotate());

      try {
        String sourceHtml = createHtmlEvalForm(uniParams, juryIndex, nbJuries, commonParams);
        // path for the PDF file to be generated
        String destination = commonParams.get("LOCAL_PATH") + "evalforms/" + university + "/eval_jury_" + juryIndex + ".pdf";
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destination));
        document.open();

        //document header attributes
        FileInputStream fis = new FileInputStream(sourceHtml);
        FileInputStream fis2 = new FileInputStream(getServletContext().getRealPath(CSS_PDF));

        //get the XMLWorkerHelper Instance
        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
        //convert to PDF
        worker.parseXHtml(pdfWriter, document, fis, fis2, StandardCharsets.UTF_8,
            new XMLWorkerFontProvider(FONT));

        document.close();
        pdfWriter.close();

        outputFiles.add(destination);

      } catch (IOException | DocumentException e) {
        e.printStackTrace();
      }
    }
    String finalEvalFormRelativePath = "evalforms/" + university + "/eval_form.pdf";
    try {
      mergePdfs(outputFiles, commonParams.get("LOCAL_PATH") + finalEvalFormRelativePath);
      return finalEvalFormRelativePath;
    } catch (COSVisitorException e) {
      // todo handle error here
      e.printStackTrace();
      return null;
    }
  }

  private String createHtmlEvalForm(UniversityParams uniParams, int juryIndex, int nbJuries, Map<String, String> commonParams)
      throws IOException {
    String university = uniParams.code();

    String outputPath = commonParams.get("LOCAL_PATH") + "evalforms/" + university + "/eval_jury_" + juryIndex + ".html";
    File outFile = new File(outputPath);
    outFile.createNewFile();

    PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
    writer.println("<html>");
    writer.println("<head>");
    writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
    writer.println("</head>");
    writer.println("<body>");
    writer.println("<h1>PHIẾU CHẤM HỒ SƠ HỌC BỔNG ĐỒNG HÀNH KÌ " + commonParams.get("CURRENT_SEMESTER") + "</h1>");
    writer.println("<h1>" + uniParams.name() + " - Người chấm " + juryIndex + "/" + nbJuries + "</h1>");
    writer.println("<h1>Họ và tên người chấm: " + Utils.tabs(".", 50) + "</h1>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_" + juryIndex + "'>");
    writer.println("<tr>");
    writer.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");
    writer.println("<th>Hoàn cảnh</th>");
    writer.println("<th>Học tập</th>");
    writer.println("<th>Ước mơ</th>");
    writer.println("<th>Điểm cộng</th>");
    writer.println("<th>Ghi chú" + Utils.tabs("&nbsp;", 50) + "</th>");
    writer.println("</tr>");


    List<Map<String, Object>> candidates = getAllCandidatesScoresForJury(juryIndex, university);

    for (Map<String, Object> candidate : candidates) {
      writer.println("<tr>");
      writer.println("<td>" + candidate.get("code") + "</td>");
      writer.println("<td>" + candidate.get("last_name") + "</td>");
      writer.println("<td>" + candidate.get("first_name") + "</td>");
      writer.println("<td></td>");
      writer.println("<td></td>");
      writer.println("<td></td>");
      writer.println("<td></td>");
      writer.println("<td></td>");
      writer.println("</tr>");
    }

    writer.println("</table>");
    writer.println("<br/>");
    writer.println("<h5>Chữ kí người chấm" + Utils.tabs("&nbsp;", 20) + "</h5>");
    writer.close();

    return outputPath;
  }

  private void downloadAllEvalForms(HttpServletResponse response) throws IOException {
    Map<String, String> commonParams = ParameterService.getCommonParams();
    List<String> universities = getUniversityCodes();
    List<String> sourceFiles = new ArrayList<>();

    for (String university : universities) {
      createEvalDirFor(university, commonParams.get("LOCAL_PATH"));
      String finalEvalFormRelativePath = downloadEvalFormForUniversity(university);
      if (finalEvalFormRelativePath != null) {
        String sourceFile = commonParams.get("LOCAL_PATH") + finalEvalFormRelativePath;
        sourceFiles.add(sourceFile);
      }
    }
    String destinationFileName = commonParams.get("LOCAL_PATH") + "evalforms/AllEvalForms.pdf";
    try {
      mergePdfs(sourceFiles, destinationFileName);
    } catch (COSVisitorException e) {
      e.printStackTrace();
    }

    // todo put this in a string format constant
    String downloadLink = "http://" + Constants.IP + "/evalforms/AllEvalForms.pdf";
    response.sendRedirect(downloadLink);
  }

  private void mergePdfs(List<String> sourceFiles, String destinationFileName)
      throws IOException, COSVisitorException {
    PDFMergerUtility ut = new PDFMergerUtility();
    for (String sourceFile : sourceFiles) {
      File f = new File(sourceFile);
      if (f.exists() && !f.isDirectory()) {
        ut.addSource(sourceFile);
      }
    }
    ut.setDestinationFileName(destinationFileName);
    ut.mergeDocuments();
  }

  private File createEvalDirFor(String university, String localPath) {
    File outDir = new File(localPath
        + File.separator + "evalforms"
        + File.separator + university);
    if (!outDir.exists()) {
      outDir.mkdirs();
    }
    return outDir;
  }

}
