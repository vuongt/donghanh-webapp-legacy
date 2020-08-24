package org.donghanh;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Set;

import static org.donghanh.Utils.getDbConnection;


@WebServlet("/Edit")
public class Edit extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public Edit() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      response.setContentType("text/html");
      response.setCharacterEncoding("utf-8");

      PrintWriter out = response.getWriter();

      String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
      out.println(doctype);
      out.println("<head><meta charset=\"UTF-8\" /></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
      out.println("<body>");
      out.println("<h4>Ấn Ctrl+F5 để xem nội dung trang.</h4>");
      out.println("<h4>Nếu bạn vẫn không thể xem được nội dung sau khi nhấn Ctrl+F5, vui lòng quay lại trang đăng nhập và điền chính xác các từ khóa.</h4>");
      out.println("<h4><a href='Login.html'>Trang đăng nhập</a></h4>");
      out.println("</body>");
      return;
    }
    if (cookies != null) {
      Cookie cookie = cookies[0];
      if (!(cookie.getName().equals("Validated")
          && (cookie.getValue().equals("OK") || cookie.getValue().equals("OKTechies")))) {
        RequestDispatcher rs = request.getRequestDispatcher("/Login.html");
        rs.forward(request, response);
        return;
      }
    }

    String action = request.getParameter("action");
    Parameters.getParamsFromDb();
    RequestDispatcher rs;

    if (action == null) {
      rs = request.getRequestDispatcher("Center.html");
      rs.forward(request, response);
      return;
    }

    switch (action) {
      case "NORMALIZE":
        normalize(request, response);
        break;
      case "UPDATE_FINAL_RESULTS":
        updateFinalResults(request, response);
        break;
      case "GET_ALL_CANDIDATES_FROM_UNIVERSITY":
        getAllCandidatesFromUniversity(request, response);
        break;
      case "EVALUATE":
        createEvaluationPage(request, response, request.getParameter("university"));
        break;
      case "DOWNLOAD_FORMS":
        try {
          mergeAll(request, response);
        } catch (COSVisitorException e) {
          e.printStackTrace();
        }
        break;
      case "DOWNLOAD_RESULTS_HTML":
        downloadResultsHtml(request, response);
        break;
      case "DOWNLOAD_NOTIF_FOR_UNIVERSITY":
        downloadNotifForUniversity(request, response);
        break;
      case "DOWNLOAD_ALL_NOTIFS":
        try {
          downloadAllNotifs(request, response);
        } catch (COSVisitorException e) {
          e.printStackTrace();
        }
        break;
      case "LOG_OUT":
        rs = request.getRequestDispatcher("Login");
        rs.forward(request, response);
        break;
      default:
        rs = request.getRequestDispatcher("Center.html");
        rs.forward(request, response);
    }

    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0); // Proxies
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private void normalize(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // HÀM CHUẨN HOÁ
    String jury = request.getParameter("jury");
    String university = request.getParameter("university");
    String jugename = request.getParameter("jugename");

    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();

      String sql = "INSERT INTO jury VALUES ('" + university + "_" + jury + "', '" + jugename + "')";
      try {
        stmt.execute(sql);
      } catch (Exception e) {
        sql = "UPDATE jury SET Name='" + jugename + "' WHERE Code='" + university + "_" + jury + "'";
        try {
          stmt.execute(sql);
        } catch (Exception ignored) {
          // ignore
        }
      }

      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String current_param = paramNames.nextElement();
        if (current_param.contains("_")) {
          String candidate_id = current_param.split("_")[1];
          String competence = current_param.split("_")[0];

          sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET " + competence + "=" + request.getParameter(current_param) + " WHERE `Ma so`=" + candidate_id;
          stmt.execute(sql);
        }
      }

      ResultSet rs = stmt.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, ROUND(AVG(diemcong),2) AS diemcong FROM candidates_" + university + "_jury_" + jury);
      rs.next();

      double[] average = new double[]{rs.getDouble("hoancanh"), rs.getDouble("hoctap"), rs.getDouble("uocmo"), rs.getDouble("diemcong")};

      rs = stmt.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, ROUND(STDDEV(diemcong),2) AS diemcong FROM candidates_" + university + "_jury_" + jury);
      rs.next();

      double[] stddev = new double[]{rs.getDouble("hoancanh"), rs.getDouble("hoctap"), rs.getDouble("uocmo"), rs.getDouble("diemcong")};
      for (int index = 0; index < 4; ++index) {
        if (Math.abs(stddev[index]) < 0.01) {
          stddev[index] = 1;
        }
      }

      sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
          + "hoancanhnorm = ROUND((hoancanh -" + average[0] + ")*2/" + stddev[0] + " + 10, 2), "
          + "hoctapnorm = ROUND((hoctap -" + average[1] + ")*2/" + stddev[1] + " + 10, 2), "
          + "uocmonorm = ROUND((uocmo -" + average[2] + ")*2/" + stddev[2] + " + 10, 2), "
          + "diemcongnorm = ROUND((diemcong -" + average[3] + ")*2/" + stddev[3] + " + 10, 2)";
      stmt.execute(sql);

      sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
          + "tongket = ROUND(hoancanhnorm * 0.5 + hoctapnorm * 0.3 + uocmonorm * 0.1 + diemcongnorm * 0.1, 2)";
      stmt.execute(sql);

      rs = stmt.executeQuery("SELECT ROUND(AVG(tongket),2) AS avg, ROUND(STDDEV(tongket),2) AS stddev FROM candidates_" + university + "_jury_" + jury);
      rs.next();

      double totalavg = rs.getDouble("avg");
      double totalstddev = rs.getDouble("stddev");
      if (Math.abs(totalstddev) < 0.001) {
        totalstddev = 1;
        sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
            + "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + ", 2)";
      } else {
        sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
            + "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + " + 10, 2)";
      }
      stmt.execute(sql);

      conn.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    createEvaluationPage(request, response, university);
  }

  private void updateFinalResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // VIẾT KẾT QUẢ HỌC BỔNG VÀO BẢNG DỮ LIỆU LỚN NHẤT
    String university = request.getParameter("university");

    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();
      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String current_param = paramNames.nextElement();
        if (current_param.contains("_")) {
          String candidate_id = current_param.split("_")[1];

          String sql;
          sql = "UPDATE selected_" + university + " SET selected=" + request.getParameter(current_param) + " WHERE `Ma so`=" + candidate_id;
          stmt.execute(sql);

          if (request.getParameter(current_param).equals("1"))
            sql = "UPDATE " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + "_main SET `Ket qua`='TRUE' WHERE `Ma so`=" + candidate_id;
          else
            sql = "UPDATE " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + "_main SET `Ket qua`='FALSE' WHERE `Ma so`=" + candidate_id;
          stmt.execute(sql);
        }
      }
      conn.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    createEvaluationPage(request, response, university);
    createFinalNotification(university);
  }

  private static void createFinalNotification(String university) throws FileNotFoundException, UnsupportedEncodingException {
    // TẠO RA FILE THÔNG BÁO KẾT QUẢ, TUỲ THEO TRƯỜNG

    int nb_candidates = 0;
    Statement stmt = null;
    Connection conn = null;

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
      rs.next();

      nb_candidates = rs.getInt("nb");
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (nb_candidates == 0) {
      return;
    }

    PrintWriter writer = new PrintWriter(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".html", "UTF-8");
    writer.println("<html>");
    writer.println("<head>");
    writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
    writer.println("</head>");
    writer.println("<body>");
    writer.println("<h4>Quỹ " + Parameters.FOUNDATION_NAMES.get(university) + "</h4>");
    if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Singapore")) {
      writer.println("<h4>Email: contact-DHSing@donghanh.net</h4>");
      writer.println("<h4>Website: donghanh.net</h4>");
    } else if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Đài Loan")) {
      writer.println("<h4>Email: contact-DHTaiwan@donghanh.net</h4>");
      writer.println("<h4>Website: donghanh.net</h4>");
    } else if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Korea")) {
      writer.println("<h4>Email: contact-DHKorea@donghanh.net</h4>");
      writer.println("<h4>Website: donghanh.net</h4>");
    } else {
      writer.println("<h4>16 rue du Petit-Musc</h4>");
      writer.println("<h4>75004 Paris, Cộng hòa Pháp</h4>");
      writer.println("<h4>contact@donghanh.net</h4>");
    }

    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    writer.println("<br/>");
    System.out.println(Parameters.FOUNDATION_NAMES.get(university));
    writer.println("<h1>DANH SÁCH SINH VIÊN ĐƯỢC NHẬN " + Parameters.FOUNDATION_NAMES.get(university).toUpperCase() + " KÌ " + Parameters.PARAMS.get("CURRENT_SEMESTER") + "</h1>");
    writer.println("<h1>" + Parameters.UNIVERSITY_NAMES.get(university) + "</h1>");
    writer.println("<br/>");
    writer.println("<br/>");

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();
      int counter = 1;
      String sql = "(SELECT `Ho dem`, `Ten`, `Gioi tinh`, `Ngay sinh`, `Lop nganh` FROM candidates_main "
          + "INNER JOIN selected_" + university + " ON candidates_main.`Ma so` = selected_" + university + ".`Ma so` WHERE selected=1) "
          + "ORDER BY `Ten`, `Ho dem`";
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

      if (Parameters.STUDENT_CLASS.get(university).equals("HỌC SINH"))
        writer.println("Danh sách này gồm " + (counter - 1) + " học sinh.");
      else
        writer.println("Danh sách này gồm " + (counter - 1) + " sinh viên.");

      writer.println("<br/>");
      writer.println("<br/>");

      if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Singapore")) {
        writer.println("<h5>" + Parameters.PARAMS.get("PLACE_AND_DATE_SG") + LocalFunctions.tabs("&nbsp;", 2) + "</h5>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_TITLE_SG") + "</h5>");
        writer.println("<p style='text-align:right; color:white; filter: brightness(50%);'><img src='" + Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("SIGNATURE_IMAGE_SG") + "' alt='Smiley face' height='140' width='210'></img>--</p>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_NAME_SG") + LocalFunctions.tabs("&nbsp;", 11) + "</h5>");
      } else if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Đài Loan")) {
        writer.println("<h5>" + Parameters.PARAMS.get("PLACE_AND_DATE_TW") + LocalFunctions.tabs("&nbsp;", 2) + "</h5>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_TITLE_TW") + "</h5>");
        writer.println("<p style='text-align:right; color:white; filter: brightness(50%);'><img src='" + Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("SIGNATURE_IMAGE_TW") + "' alt='Smiley face' height='140' width='210'></img>--</p>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_NAME_TW") + LocalFunctions.tabs("&nbsp;", 11) + "</h5>");
      } else if (Parameters.FOUNDATION_NAMES.get(university).equals("học bổng Đồng Hành Korea")) {
        writer.println("<h5>" + Parameters.PARAMS.get("PLACE_AND_DATE_KR") + LocalFunctions.tabs("&nbsp;", 2) + "</h5>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_TITLE_KR") + "</h5>");
        writer.println("<p style='text-align:right; color:white; filter: brightness(50%);'><img src='" + Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("SIGNATURE_IMAGE_KR") + "' alt='Smiley face' height='140' width='210'></img>--</p>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_NAME_KR") + LocalFunctions.tabs("&nbsp;", 11) + "</h5>");
      } else {
        writer.println("<h5>" + Parameters.PARAMS.get("PLACE_AND_DATE_FR") + LocalFunctions.tabs("&nbsp;", 2) + "</h5>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_TITLE_FR") + "</h5>");
        writer.println("<p style='text-align:right; color:white; filter: brightness(50%);'><img src='" + Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("SIGNATURE_IMAGE_FR") + "' alt='Smiley face' height='140' width='210'></img>--</p>");
        writer.println("<h5>" + Parameters.PARAMS.get("SIGNATURE_NAME_FR") + LocalFunctions.tabs("&nbsp;", 11) + "</h5>");
      }

      conn.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //http://" + Parameters.IP + ":8080/DH/Evaluation?action=UPDATE_FINAL_RESULTS&university=BKHN
    writer.println("</body>");
    writer.println("</html>");
    writer.close();
  }

  private void getAllCandidatesFromUniversity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String university = request.getParameter("university");

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    String title = "Danh sách sinh viên " + Parameters.UNIVERSITY_NAMES.get(university);

    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");

    out.println("<h1 align=\"center\">" + title + "</h1>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("<table width=\"100%\" border=\"1\" align=\"center\">");
    out.println("<tr>");
    out.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");

    Statement stmt;
    Connection conn;

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();
      String sql;
      sql = "CREATE TABLE IF NOT EXISTS all_candidates_" + university + " AS (SELECT DISTINCT `Ma so`, `Ho dem`, `Ten`, '00000000000000000000' AS `Phan phoi giam khao` FROM " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + " WHERE `Ki` =" + Parameters.PARAMS.get("CURRENT_SEMESTER") + " AND `Truong` = '" + university + "');";
      System.out.print(sql);
      stmt.execute(sql);

      // Distribute candidate to jury
      distributeJuryCandidate(university, stmt);

      sql = "SELECT * FROM all_candidates_" + university;
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        out.println("<tr>");
        out.println("<td>" + rs.getInt("Ma so") + "</td>");
        out.println("<td>" + rs.getString("Ho dem") + "</td>");
        out.println("<td>" + rs.getString("Ten") + "</td>");
        out.println("</tr>");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    out.println("</table>");
    out.println("</body>");
    out.println("</html>");
  }

  private static void distributeJuryCandidate(String university, Statement stmt) throws SQLException {
    ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
    rs.next();
    int nb_candidates = rs.getInt("nb");

    String sql = "SELECT * FROM all_candidates_" + university;
    rs = stmt.executeQuery(sql);

    int[] candidate_code_list = new int[nb_candidates];

    int counter = 0;
    while (rs.next()) {
      candidate_code_list[counter] = rs.getInt("Ma so");
      counter++;
    }

    String[] Circular_Distribution = LocalFunctions.Distribution(nb_candidates, Parameters.NB_JUGES_BY_COPY.get(university), Parameters.MAX_DOCS.get(university), Parameters.EVALUATED_BY.get(university));
    int period = Circular_Distribution.length;
    for (int index = 0; index < nb_candidates; ++index) {
      stmt.execute("UPDATE all_candidates_" + university + " SET `Phan phoi giam khao`='" + Circular_Distribution[index % period] + "' WHERE `Ma so`='" + candidate_code_list[index] + "';");
    }
  }

  private static void createEvaluationPage(HttpServletRequest request, HttpServletResponse response, String university) throws ServletException, IOException {

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Danh sách sinh viên " + Parameters.UNIVERSITY_NAMES.get(university) + " phân chia theo giám khảo";

    String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
    out.println(doctype);
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><script type=\"text/javascript\" src=\"normalize.js\" ></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a></h4>");
    out.println("<h4><font color='red'>Lưu ý: điểm phải là một số nguyên, hoặc số thập phân với dấu CHẤM. Dấu phẩy không được chấp nhận.</font></h4>");

    Statement stmt;
    Connection conn;
    int nb_candidates = 0;
    int nb_juges = 0;

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();
      String sql;
      ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
      rs.next();

      nb_candidates = rs.getInt("nb");
      nb_juges = LocalFunctions.nbJuges(nb_candidates, Parameters.NB_JUGES_BY_COPY.get(university), Parameters.MAX_DOCS.get(university), Parameters.EVALUATED_BY.get(university));

      for (int index = 0; index <= nb_juges; ++index) {
        sql = "CREATE TABLE IF NOT EXISTS candidates_" + university + "_jury_" + index + " AS SELECT DISTINCT `Ma so`, `Ho dem`, `Ten`, "
            + "'000000' AS hoancanh, '000000' AS hoancanhnorm, '000000' AS hoctap, '000000' AS hoctapnorm, "
            + "'000000' AS uocmo, '000000' AS uocmonorm, '000000' AS diemcong, '000000' AS diemcongnorm, "
            + "'000000' AS tongket, '000000' AS tongketnorm"
            + " FROM all_candidates_"
            + university + " WHERE `Phan phoi giam khao` LIKE '%" + 'G' + index + 'G' + "%';";
        //TO BE MODIFIED
        stmt.execute(sql);

        sql = "SELECT * FROM jury WHERE Code='" + university + "_" + index + "'";
        rs = stmt.executeQuery(sql);

        out.println("<h1 align=\"center\">Danh sách sinh viên được chấm bởi giám khảo " + index + "</h1>");
        if (rs.next()) {
          out.println("<h3 align=\"center\">Họ và tên giám khảo: <input size='60' style='text-align : center;' type='text' id='jugename" + index + "' value='" + rs.getString("Name") + "'></h3>");
        } else {
          out.println("<h3 align=\"center\">Họ và tên giám khảo: <input size='60' style='text-align : center;' type='text' id='jugename" + index + "' value='Chưa có người chấm'></h3>");
        }

        sql = "SELECT * FROM candidates_" + university + "_jury_" + index;
        rs = stmt.executeQuery(sql);

        out.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_" + index + "'>");
        out.println("<tr>");
        out.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");
        out.println("<th>Hoàn cảnh</th>\n<th>Chuẩn hóa</th>");
        out.println("<th>Học tập</th>\n<th>Chuẩn hóa</th>");
        out.println("<th>Ước mơ</th>\n<th>Chuẩn hóa</th>");
        out.println("<th>Điểm cộng</th>\n<th>Chuẩn hóa</th>");
        out.println("<th>Tổng</th>\n<th>Chuẩn hóa</th>");
        out.println("</tr>");


        PrintWriter writer = new PrintWriter(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + "_jury_" + index + ".html", "UTF-8");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h1>PHIẾU CHẤM HỒ SƠ HỌC BỔNG ĐỒNG HÀNH KÌ " + Parameters.PARAMS.get("CURRENT_SEMESTER") + "</h1>");
        writer.println("<h1>" + Parameters.UNIVERSITY_NAMES.get(university) + " - Người chấm " + index + "/" + nb_juges + "</h1>");
        writer.println("<h1>Họ và tên người chấm: " + LocalFunctions.tabs(".", 50) + "</h1>");
        writer.println("<br/>");
        writer.println("<br/>");
        writer.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_" + index + "'>");
        writer.println("<tr>");
        writer.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");
        writer.println("<th>Hoàn cảnh</th>");
        writer.println("<th>Học tập</th>");
        writer.println("<th>Ước mơ</th>");
        writer.println("<th>Điểm cộng</th>");
        writer.println("<th>Ghi chú" + LocalFunctions.tabs("&nbsp;", 50) + "</th>");
        writer.println("</tr>");

        while (rs.next()) {
          out.println("<tr>");
          out.println("<td>" + rs.getInt("Ma so") + "</td>");
          out.println("<td>" + rs.getString("Ho dem") + "</td>");
          out.println("<td>" + rs.getString("Ten") + "</td>");
          out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='hoancanh_" + rs.getInt("Ma so") + "_gk" + index + "' value=" + rs.getDouble("hoancanh") + "></td>");
          out.println("<td align='right'>" + rs.getDouble("hoancanhnorm") + "</td>");
          out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='hoctap_" + rs.getInt("Ma so") + "_gk" + index + "' value=" + rs.getDouble("hoctap") + "></td>");
          out.println("<td align='right'>" + rs.getDouble("hoctapnorm") + "</td>");
          out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='uocmo_" + rs.getInt("Ma so") + "_gk" + index + "' value=" + rs.getDouble("uocmo") + "></td>");
          out.println("<td align='right'>" + rs.getDouble("uocmonorm") + "</td>");
          out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='diemcong_" + rs.getInt("Ma so") + "_gk" + index + "' value=" + rs.getDouble("diemcong") + "></td>");
          out.println("<td align='right'>" + rs.getDouble("diemcongnorm") + "</td>");
          out.println("<td align='right'><b>" + rs.getDouble("tongket") + "</b></td>");
          out.println("<td align='right'><b>" + rs.getDouble("tongketnorm") + "</b></td>");
          out.println("</tr>");

          writer.println("<tr>");
          writer.println("<td>" + rs.getInt("Ma so") + "</td>");
          writer.println("<td>" + rs.getString("Ho dem") + "</td>");
          writer.println("<td>" + rs.getString("Ten") + "</td>");
          writer.println("<td></td>");
          writer.println("<td></td>");
          writer.println("<td></td>");
          writer.println("<td></td>");
          writer.println("<td></td>");
          writer.println("</tr>");

        }

        writer.println("</table>");
        writer.println("<br/>");
        writer.println("<h5>Chữ kí người chấm" + LocalFunctions.tabs("&nbsp;", 20) + "</h5>");
        writer.close();
        makePdfEvalForms(university, index);

        rs = stmt.executeQuery("SELECT ROUND(AVG(hoancanh),2) AS hoancanh, ROUND(AVG(hoctap),2) AS hoctap, ROUND(AVG(uocmo),2) AS uocmo, ROUND(AVG(diemcong),2) AS diemcong, "
            + "ROUND(AVG(hoancanhnorm),2) AS hoancanhnorm, ROUND(AVG(hoctapnorm),2) AS hoctapnorm, ROUND(AVG(uocmonorm),2) AS uocmonorm, ROUND(AVG(diemcongnorm),2) AS diemcongnorm, "
            + "ROUND(AVG(tongket),2) AS tongket, ROUND(AVG(tongketnorm),2) AS tongketnorm "
            + "FROM candidates_" + university + "_jury_" + index);
        rs.next();

        out.println("<tr align='right'>");
        out.println("<td>Trung bình</td>");
        out.println("<td></td>");
        out.println("<td></td>");
        out.println("<td>" + rs.getDouble("hoancanh") + "</td>");
        out.println("<td>" + rs.getDouble("hoancanhnorm") + "</td>");
        out.println("<td>" + rs.getDouble("hoctap") + "</td>");
        out.println("<td>" + rs.getDouble("hoctapnorm") + "</td>");
        out.println("<td>" + rs.getDouble("uocmo") + "</td>");
        out.println("<td>" + rs.getDouble("uocmonorm") + "</td>");
        out.println("<td>" + rs.getDouble("diemcong") + "</td>");
        out.println("<td>" + rs.getDouble("diemcongnorm") + "</td>");
        out.println("<td><b>" + rs.getDouble("tongket") + "</b></td>");
        out.println("<td><b>" + rs.getDouble("tongketnorm") + "</b></td>");
        out.println("</tr>");

        rs = stmt.executeQuery("SELECT ROUND(STDDEV(hoancanh),2) AS hoancanh, ROUND(STDDEV(hoctap),2) AS hoctap, ROUND(STDDEV(uocmo),2) AS uocmo, ROUND(STDDEV(diemcong),2) AS diemcong, "
            + "ROUND(STDDEV(hoancanhnorm),2) AS hoancanhnorm, ROUND(STDDEV(hoctapnorm),2) AS hoctapnorm, ROUND(STDDEV(uocmonorm),2) AS uocmonorm, ROUND(STDDEV(diemcongnorm),2) AS diemcongnorm, "
            + "ROUND(STDDEV(tongket),2) AS tongket, ROUND(STDDEV(tongketnorm),2) AS tongketnorm "
            + "FROM candidates_" + university + "_jury_" + index);
        rs.next();

        out.println("<tr align='right'>");
        out.println("<td>Độ lệch chuẩn</td>");
        out.println("<td></td>");
        out.println("<td></td>");
        out.println("<td>" + rs.getDouble("hoancanh") + "</td>");
        out.println("<td>" + rs.getDouble("hoancanhnorm") + "</td>");
        out.println("<td>" + rs.getDouble("hoctap") + "</td>");
        out.println("<td>" + rs.getDouble("hoctapnorm") + "</td>");
        out.println("<td>" + rs.getDouble("uocmo") + "</td>");
        out.println("<td>" + rs.getDouble("uocmonorm") + "</td>");
        out.println("<td>" + rs.getDouble("diemcong") + "</td>");
        out.println("<td>" + rs.getDouble("diemcongnorm") + "</td>");
        out.println("<td><b>" + rs.getDouble("tongket") + "</b></td>");
        out.println("<td><b>" + rs.getDouble("tongketnorm") + "</b></td>");
        out.println("</tr>");

        out.println("</table>");
        out.println("<button type='button' onclick='normalizer(" + index + ", \"" + university + "\")'>Lưu để chuẩn hóa</button>");
        out.println("<a id='confirm_link_" + index + "' href=''></a>");
      }


      mergePdfs(university, nb_juges);


    } catch (Exception e) {
      e.printStackTrace();
      out.println("</body></html>");
      return;
    }

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();

      String sql = "DROP TABLE IF EXISTS bilan_" + university;
      stmt.execute(sql);

      sql = "CREATE TABLE bilan_" + university + " AS SELECT DISTINCT "
          + "all_candidates_" + university + ".`Ma so`, "
          + "all_candidates_" + university + ".`Ho dem`, "
          + "all_candidates_" + university + ".`Ten`, ";
      for (int count = 0; count <= nb_juges; ++count) {
        sql += "candidates_" + university + "_jury_" + count + ".tongketnorm AS gk" + count + ", ";
      }
      sql += "'000000' AS finalscore FROM all_candidates_" + university + " ";
      for (int count = 0; count <= nb_juges; ++count) {
        sql += "LEFT JOIN candidates_" + university + "_jury_" + count + " ";
        sql += "ON all_candidates_" + university + ".`Ma so` = candidates_" + university + "_jury_" + count + ".`Ma so`";
      }
      ;
      stmt.execute(sql);

      sql = "CREATE TABLE IF NOT EXISTS selected_" + university + " AS SELECT "
          + "all_candidates_" + university + ".`Ma so`, "
          + "'0' AS selected FROM all_candidates_" + university;
      stmt.execute(sql);

      if (nb_juges > 0) {
        sql = "UPDATE bilan_" + university + " SET finalscore=ROUND( COALESCE(gk0, 0) * " + Parameters.VN_COEFS.get(university) + " + (";
        for (int count = 1; count < nb_juges; ++count) {
          sql += "COALESCE(gk" + count + ", 0) + ";
        }

        sql += "COALESCE(gk" + nb_juges + ", 0))/GREATEST((";
        for (int count = 1; count < nb_juges; ++count) {
          sql += "COALESCE(CEIL(gk" + count + "/(gk" + count + "+ 1)), 0) + ";
        }
        sql += "COALESCE(CEIL(gk" + nb_juges + "/(gk" + nb_juges + "+ 1)), 0)), 1) * " + (1 - Parameters.VN_COEFS.get(university)) + ", 2)";
        System.out.println("Attention!!!\n");
        System.out.println(sql);
        stmt.execute(sql);
      }

      sql = "ALTER TABLE bilan_" + university + " MODIFY COLUMN finalscore DECIMAL(10,2)";
      stmt.execute(sql);

      sql = "SELECT bilan_" + university + ".*, selected_" + university + ".selected FROM bilan_" + university + " INNER JOIN selected_" + university + " "
          + "ON bilan_" + university + ".`Ma so` = selected_" + university + ".`Ma so` ORDER BY finalscore DESC, selected_" + university + ".`Ma so` ASC";
      ResultSet rs = stmt.executeQuery(sql);

      out.println("<h1 align=\"center\">Bảng tổng kết</h1>");
      out.println("<h3 align=\"center\">Hệ số giám khảo 0 (VN): " + Parameters.VN_COEFS.get(university)
          + " - Tổng hệ số các giám khảo " + Parameters.EVALUATED_BY.get(university) + ": " + (1 - Parameters.VN_COEFS.get(university)) + "</h1>");
      out.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_bilan'>");
      out.println("<tr>");
      out.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");

      for (int count = 0; count <= nb_juges; ++count) {
        if (Parameters.JURY.containsKey(university + "_" + count)) {
          out.println("<th>Gk" + count + "<br>" + Parameters.JURY.get(university + "_" + count) + "</th>");
        } else {
          out.println("<th>Giám khảo " + count + "</th>");
        }
      }

      out.println("<th>Điểm tổng kết</th>\n<th>Chọn</th>");
      out.println("</tr>");
      while (rs.next()) {
        out.println("<tr>");
        out.println("<td>" + rs.getString("Ma so") + "</td>");
        out.println("<td>" + rs.getString("Ho dem") + "</td>");
        out.println("<td>" + rs.getString("Ten") + "</td>");
        for (int count = 0; count <= nb_juges; ++count) {
          if (rs.getString("gk" + count) == null) {
            out.println("<td></td>");
          } else {
            out.println("<td>" + rs.getDouble("gk" + count) + "</td>");
          }
        }
        out.println("<td><b>" + rs.getDouble("finalscore") + "</b></td>");
        String selected = (rs.getInt("selected") == 0) ? "" : "checked";
        out.println("<td><input type='checkbox' class='selected' id='selected_" + rs.getString("Ma so") + "' " + selected + "></td>");
        out.println("</tr>");
      }

      out.println("</table>");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    if (nb_candidates > 0) {
      out.println("<button type='button' onclick='update_selection_results(\"" + university + "\")'>Duyệt kết quả</button>");
      out.println("<a id='confirm_link_final' href=''></a>");
    }
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("</body>");
    out.println("</html>");
  }

  private static void makePdfEvalForms(String university, int index) throws IOException {
    //path for the PDF file to be generated
    if (university == null) {
      return;
    }

    String destination = Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + "_jury_" + index + ".pdf";
    String source = Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + "_jury_" + index + ".html";

    PdfWriter pdfWriter = null;

    //create a new document
    Document document = new Document(PageSize.A4.rotate());

    try {

      //get Instance of the PDFWriter
      pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destination));

      //document header attributes

      //open document
      document.open();

      //FileInputStream fis = new FileInputStream("final_notice_" + request.getParameter("university") + "html");
      FileInputStream fis = new FileInputStream(source);
      FileInputStream fis2 = new FileInputStream(Parameters.PARAMS.get("LOCAL_PATH") + "CSS/sample.css");

      //get the XMLWorkerHelper Instance
      XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
      //convert to PDF
      worker.parseXHtml(pdfWriter, document, fis, fis2, Charset.forName("UTF-8"), new XMLWorkerFontProvider(Parameters.PARAMS.get("FONT")));

      //close the document
      document.close();
      //close the writer
      pdfWriter.close();

    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }
  }

  private static void mergePdfs(String university, int nb_files) throws COSVisitorException, IOException {
    PDFMergerUtility ut = new PDFMergerUtility();
    for (int index = 1; index <= nb_files; ++index) {
      ut.addSource(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + "_jury_" + index + ".pdf");
    }
    ut.setDestinationFileName(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + ".pdf");
    ut.mergeDocuments();
  }

  private static void mergeAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, COSVisitorException {
    Set<String> universities = Parameters.UNIVERSITY_NAMES.keySet();
    PDFMergerUtility ut = new PDFMergerUtility();

    for (String university : universities) {
      File f = new File(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + ".pdf");
      if (f.exists() && !f.isDirectory()) {
        ut.addSource(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/note_" + university + ".pdf");
      }
    }
    ut.setDestinationFileName(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/AllForms.pdf");
    ut.mergeDocuments();

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Update candidate table";

    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("<p><a href=http://" + Parameters.IP + "/evalforms/AllForms.pdf>Tải phiếu chấm</a></p>");
    out.println("</body>");
    out.println("</html>");
  }

  private void downloadResultsHtml(HttpServletRequest request, HttpServletResponse response) throws ServletException, MalformedURLException, IOException {
    for (String university : Parameters.UNIVERSITY_NAMES.keySet()) {
      LocalFunctions.downloadFromUrl(new URL("http://" + Parameters.IP + ":8080/DH/View?action=VIEW_DISTRIBUTIONS&university=" + university),
          Parameters.PARAMS.get("LOCAL_PATH") + "KQ/" + university + ".html");
    }

    try {
      FolderZipper.zipFolder(Parameters.PARAMS.get("LOCAL_PATH") + "KQ/", Parameters.PARAMS.get("LOCAL_PATH") + "KQ.zip");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    String title = "Update candidate table";

    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /></head>");
    out.println("<body>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("<p><a href=http://" + Parameters.IP + "/KQ.zip>Tải tất cả các file kết quả</a></p>");
    out.println("</body>");
    out.println("</html>");

  }

  private void downloadNotifForUniversity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Parameters.getParamsFromDb();

    //path for the PDF file to be generated
    String university = request.getParameter("university");
    createFinalNotification(university);
    if (university == null) {
      return;
    }

    String destination = Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".pdf";
    String source = Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".html";

    File f = new File(source);
    if (!f.exists() || f.isDirectory()) {
      return;
    }

    PdfWriter pdfWriter = null;

    //create a new document
    Document document = new Document();

    try {

      //get Instance of the PDFWriter
      pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destination));

      //document header attributes

      //open document
      document.open();

      String anotherLogo = Parameters.LOGO.get(university);
      if (!anotherLogo.equals(Parameters.PARAMS.get("LOGO_DH")) && !anotherLogo.equals(Parameters.PARAMS.get("LOGO_DH_SING"))) {
        document.add(new Paragraph(""));
        Image logo = Image.getInstance(anotherLogo);
        if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_NT"))) {
          logo.scalePercent(30f);
          logo.setAbsolutePosition(document.getPageSize().getWidth() - 220f,
              document.getPageSize().getHeight() - 120f);
        } else if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_CDP"))) {
          logo.scalePercent(80f);
          logo.setAbsolutePosition(document.getPageSize().getWidth() - 250f,
              document.getPageSize().getHeight() - 110f);
        } else if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_XD"))) {
          logo.scalePercent(80f);
          logo.setAbsolutePosition(document.getPageSize().getWidth() - 190f,
              document.getPageSize().getHeight() - 110f);
        } else if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_KETTER"))) {
          logo.scalePercent(80f);
          logo.setAbsolutePosition(document.getPageSize().getWidth() - 190f,
              document.getPageSize().getHeight() - 110f);
        }
        document.add(logo);
      }

      if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_DH_SING"))) {
        document.add(new Paragraph(""));
        Image logo = Image.getInstance(Parameters.PARAMS.get("LOGO_DH_SING"));
        logo.scalePercent(15f);
        logo.setAbsolutePosition(document.getPageSize().getWidth() - 110f,
            document.getPageSize().getHeight() - 110f);
        document.add(logo);
      } else if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_DH_KOREA"))) {
        document.add(new Paragraph(""));
        Image logo = Image.getInstance(Parameters.PARAMS.get("LOGO_DH_KOREA"));
        logo.scalePercent(15f);
        logo.setAbsolutePosition(document.getPageSize().getWidth() - 110f,
            document.getPageSize().getHeight() - 110f);
        document.add(logo);
      } else if (anotherLogo.equals(Parameters.PARAMS.get("LOGO_DH_TAIWAN"))) {
        document.add(new Paragraph(""));
        Image logo = Image.getInstance(Parameters.PARAMS.get("LOGO_DH_TAIWAN"));
        logo.scalePercent(15f);
        logo.setAbsolutePosition(document.getPageSize().getWidth() - 110f,
            document.getPageSize().getHeight() - 110f);
        document.add(logo);
      } else {
        document.add(new Paragraph(""));
        Image logo = Image.getInstance(Parameters.PARAMS.get("LOGO_DH"));
        logo.scalePercent(15f);
        logo.setAbsolutePosition(document.getPageSize().getWidth() - 110f,
            document.getPageSize().getHeight() - 110f);
        document.add(logo);
      }


      FileInputStream fis = new FileInputStream(source);
      FileInputStream fis2 = new FileInputStream(Parameters.PARAMS.get("LOCAL_PATH") + "CSS/sample.css");

      //get the XMLWorkerHelper Instance
      XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
      //convert to PDF
      worker.parseXHtml(pdfWriter, document, fis, fis2, Charset.forName("UTF-8"), new XMLWorkerFontProvider(Parameters.PARAMS.get("FONT")));

      //close the document
      document.close();
      //close the writer
      pdfWriter.close();

    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    String title = "Update candidate table";

    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";

    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /></head>");
    out.println("<body>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("<p><a href=http://" + Parameters.IP + "/evalforms/" + university + "/final_notification_" + university + ".pdf>Tải kết quả</a></p>");
    out.println("</body>");
    out.println("</html>");

  }

  private static void downloadAllNotifs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, COSVisitorException {
    Set<String> universities = Parameters.UNIVERSITY_NAMES.keySet();
    PDFMergerUtility ut = new PDFMergerUtility();

    for (String university : universities) {
      File f = new File(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".pdf");
      if (f.exists() && !f.isDirectory()) {
        ut.addSource(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/" + university + "/final_notification_" + university + ".pdf");
      }
    }
    ut.setDestinationFileName(Parameters.PARAMS.get("LOCAL_PATH") + "evalforms/FinalNotification.pdf");
    ut.mergeDocuments();

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Update candidate table";

    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 " + "transitional//en\">\n";

    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h4><a href='Center.html'>Quay lại trang chủ</a> </h4>");
    out.println("<p><a href=http://" + Parameters.IP + "/evalforms/FinalNotification.pdf>Tải thông báo kết quả</a></p>");
    out.println("</body>");
    out.println("</html>");
  }


}
