package org.donghanh;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.donghanh.Utils.getDbConnection;

/**
 * Servlet implementation class Params
 */
@WebServlet("/Parameters")
public class Parameters extends HttpServlet {
  private static final long serialVersionUID = 1L;
  static final String IP = "52.37.193.91";

  static Map<String, String> PARAMS = new HashMap<>();
  static Map<String, String> UNIVERSITY_NAMES = new HashMap<>();
  static Map<String, String> FOUNDATION_NAMES = new HashMap<>();
  static Map<String, String> EVALUATED_BY = new HashMap<>();
  static Map<String, String> STUDENT_CLASS = new HashMap<>();
  static Map<String, String> LOGO = new HashMap<>();
  static Map<String, Double> VN_COEFS = new HashMap<>();
  static Map<String, Integer> NB_JUGES_BY_COPY = new HashMap<>();
  static Map<String, Integer> MAX_DOCS = new HashMap<>();
  static Map<String, String> JURY = new HashMap<>();

  static Map<String, String> PARAM_DEFAULT;

  static {
    PARAM_DEFAULT = new HashMap<>();
    PARAM_DEFAULT.put("LOCAL_PATH", "/var/www/html/");
    PARAM_DEFAULT.put("LOCAL_SOURCE_FILE", "Data/Dulieu20_32.csv");
    PARAM_DEFAULT.put("ONLINE_SOURCE_FILE", "");
    PARAM_DEFAULT.put("FONT", "/usr/share/fonts");
    PARAM_DEFAULT.put("CANDIDATE_TABLE_NAME", "candidates");
    PARAM_DEFAULT.put("PARAM_TABLE_NAME", "parameters");
    PARAM_DEFAULT.put("CURRENT_SEMESTER", "38");
    PARAM_DEFAULT.put("HEADERS_LIST", "Ma so,Ho dem,Ten,Gioi tinh,Ngay sinh,Nam thu,Lop nganh,Truong,Dia chi,Email,Ki,Ket qua");
    PARAM_DEFAULT.put("HEADERTYPES_LIST", "INT NOT NULL,VARCHAR(100),VARCHAR(20),VARCHAR(10),VARCHAR(20),VARCHAR(1),VARCHAR(100),VARCHAR(20),VARCHAR(100),VARCHAR(100),INT NOT NULL,VARCHAR(5)");
    PARAM_DEFAULT.put("LIMITS_LIST", ",',',',',',',',',',,'");
    PARAM_DEFAULT.put("SEPARATOR", ",");
    PARAM_DEFAULT.put("DELIMITOR", "");
    PARAM_DEFAULT.put("PLACE_AND_DATE_FR", "Paris, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_FR", "Chủ tịch Quỹ học bổng Đồng Hành");
    PARAM_DEFAULT.put("SIGNATURE_NAME_FR", "Lê Hồng Thái");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_FR", "Image/SignatureFR.bmp");
    PARAM_DEFAULT.put("PLACE_AND_DATE_SG", "Singapore, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_SG", "Chủ tịch Quỹ học bổng Đồng Hành Singapore");
    PARAM_DEFAULT.put("SIGNATURE_NAME_SG", "Nguyễn Duy Ánh");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_SG", "Image/SignatureSG.bmp");
    PARAM_DEFAULT.put("PLACE_AND_DATE_KR", "Seoul, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_KR", "Chủ tịch Quỹ học bổng Đồng Hành Korea");
    PARAM_DEFAULT.put("SIGNATURE_NAME_KR", "Cao Quyên");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_KR", "Image/SignatureKR.bmp");
    PARAM_DEFAULT.put("LOGO_DH", "/var/www/html/Image/logoDH.png");
    PARAM_DEFAULT.put("LOGO_DH_SING", "/var/www/html/Image/logoDH.png");
    PARAM_DEFAULT.put("LOGO_DH_TAIWAN", "/var/www/html/Image/logoDHSing.png");
    PARAM_DEFAULT.put("LOGO_DH_KOREA", "/var/www/html/Image/logoDHKorea.png");
    PARAM_DEFAULT.put("LOGO_CDP", "/var/www/html/Image/logoCDP.jpg");
    PARAM_DEFAULT.put("LOGO_XD", "/var/www/html/Image/logoXD.gif");
    PARAM_DEFAULT.put("LOGO_NT", "/var/www/html/Image/logoNT.jpg");
    PARAM_DEFAULT.put("LOGO_KETTER", "/var/www/html/Image/logoKetter.jpg");
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      response.setContentType("text/html");
      response.setCharacterEncoding("utf-8");
      request.getRequestDispatcher("/jsp/unauthorized.jsp").forward(request, response);
    }

    if (cookies != null) {
      Cookie cookie = cookies[0];
      if (!(cookie.getName().equals("Validated") && cookie.getValue().equals("OKTechies"))) {
        RequestDispatcher rs = request.getRequestDispatcher("/Login.html");
        rs.forward(request, response);
        return;
      }
    }

    RequestDispatcher rs;
    String action = request.getParameter("action");
    if (action == null) {
      rs = request.getRequestDispatcher("Center.html");
      rs.forward(request, response);
      return;
    }

    switch (action) {
      case "VIEW_PARAMS":
        viewParams(request, response);
        break;
      case "SET_PARAM":
        setParam(request, response);
        break;
      case "SET_UNIVERSITY_PARAMS":
        setUniversityParams(request, response);
        break;
      case "CREATE_CANDIDATE_TABLE":
        createCandidateTable(request, response);
        break;
      case "RESET_PARAMS":
        resetParams(request, response);
        break;
      default:
        rs = request.getRequestDispatcher("Center.html");
        rs.forward(request, response);
    }

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
  }

  private void viewParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*
     * Hiển thị các tham số
     */
    getParamsFromDb();

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    request.setAttribute("title", "Tham số");

    List<Map<String, Object>> params = new ArrayList<>();
    for (String paramName : PARAM_DEFAULT.keySet()) {
      Map<String, Object> param = new HashMap<>();
      param.put("name", paramName);
      param.put("value", PARAMS.get(paramName));
      params.add(param);
    }
    request.setAttribute("parameters", params);

    List<Map<String, Object>> universities = new ArrayList<>();
    for (String key : FOUNDATION_NAMES.keySet()) {
      Map<String, Object> university = new HashMap<>();
      university.put("code", key);
      university.put("foundation", FOUNDATION_NAMES.get(key));
      university.put("name", UNIVERSITY_NAMES.get(key));
      university.put("studentClass", STUDENT_CLASS.get(key));
      university.put("evaluatedBy", EVALUATED_BY.get(key));
      university.put("logo", LOGO.get(key));
      university.put("vnCoefs", VN_COEFS.get(key));
      university.put("nbJuriesByCopy", NB_JUGES_BY_COPY.get(key));
      university.put("maxDocs", MAX_DOCS.get(key));
      universities.add(university);
    }
    request.setAttribute("universities", universities);
    request.getRequestDispatcher("/jsp/parameters.jsp").forward(request, response);
  }

  private void setParam(HttpServletRequest request, HttpServletResponse response) {
    String key = request.getParameter("key");
    String value = request.getParameter("value");
    System.out.println("Setting key: " + key + ": " + value);

    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();
      String sql = "UPDATE parameters SET Value = '" + value + "' WHERE Param = '" + key + "'";

      System.out.println(sql);
      stmt.execute(sql);

      viewParams(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setUniversityParams(HttpServletRequest request, HttpServletResponse response) {
    String key = request.getParameter("key");
    String values_string = request.getParameter("values");

    String[] values = values_string.split("XXX");

    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();
      String sql = "UPDATE universities SET UniversityName = '" + values[0] + "', "
          + "FoundationName = '" + values[1] + "', "
          + "StudentClass = '" + values[2] + "', "
          + "EvaluatedBy = '" + values[3] + "', "
          + "Logo = '" + values[4] + "', "
          + "VnCoefs = '" + values[5] + "', "
          + "NbJugesByCopy = '" + values[6] + "', "
          + "MaxDocs = '" + values[7] + "' "
          + "WHERE Code = '" + key + "'";

      System.out.println(sql);
      stmt.execute(sql);

      viewParams(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createCandidateTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /*
     * Đầu mỗi học kì, dữ liệu sẽ được cập nhật vào một bảng dữ liệu. Hàm này nhằm mục đích
     * - Xoá bảng candidates_main nếu bảng này đang tồn tại
     * - Tạo lại bảng candidates_main
     * - Upload dữ liệu vào bảng candidates_main.
     *   Đây là bảng dữ liệu hoàn chỉnh ban đầu của tất cả các kì.
     * - Sau đó, xoá bảng candidates nếu bảng này đang tồn tại
     * - Và tạo lại bảng dữ liệu hoàn chỉnh của kì.
     */
    getParamsFromDb();

    File old_source_file = new File(Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("LOCAL_SOURCE_FILE"));
    if (!old_source_file.exists() || old_source_file.isDirectory()) {
      LocalFunctions.downloadFromUrl(new URL(Parameters.PARAMS.get("ONLINE_SOURCE_FILE")), Parameters.PARAMS.get("LOCAL_SOURCE_FILE"));
    }

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Tạo bảng dữ liệu sinh viên";


    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" />");
    out.println("<link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h1 align=\"center\">" + title + "</h1>");

    Statement stmt = null;
    Connection conn = null;

    try {
      conn = getDbConnection();
      stmt = conn.createStatement();

      String[] HEADERS = Parameters.PARAMS.get("HEADERS_LIST").split(",");
      String[] HEADERTYPES = Parameters.PARAMS.get("HEADERTYPES_LIST").split(",");

      String sql = "CREATE TABLE IF NOT EXISTS " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + "_main(`Ma so` VARCHAR(10) PRIMARY KEY, ";
      for (int index = 1; index < HEADERS.length; ++index) {
        sql += "`" + HEADERS[index] + "` " + HEADERTYPES[index] + ", ";
      }
      sql = sql.substring(0, sql.length() - 2);
      sql += ")  ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;";

      System.out.println(sql);

      stmt.execute(sql);

      sql = "LOAD DATA LOCAL INFILE '" + Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("LOCAL_SOURCE_FILE") + "' INTO TABLE " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME")
          + "_main FIELDS TERMINATED BY '" + Parameters.PARAMS.get("SEPARATOR") + "' ENCLOSED BY '" + Parameters.PARAMS.get("DELIMITOR") + "'"
          + " LINES TERMINATED BY '\\n' IGNORE 1 LINES";
      System.out.println(sql);

      stmt.execute(sql);

      sql = "DROP TABLE IF EXISTS " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME");
      stmt.execute(sql);

      sql = "CREATE TABLE " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + " AS SELECT DISTINCT `Ma so`, `Ho dem`, `Ten`, `Nam thu`, `Truong`, `Ki` FROM "
          + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + "_main"
          + " WHERE `Ki`=" + Parameters.PARAMS.get("CURRENT_SEMESTER");
      System.out.println(sql);
      stmt.execute(sql);

      out.println("<h4><a href='Techies.html'>Quay lại trang chủ</a> </h4>");
      out.println("<p>Thành công</p>");

    } catch (Exception e) {
      out.println("<h4><a href='Techies.html'>Quay lại trang chủ</a> </h4>");
      out.println("<p>Thất bại</p>");
      e.printStackTrace();
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException sqle2) {
        sqle2.printStackTrace();
      }
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException sqle3) {
        sqle3.printStackTrace();
      }
    }

    out.println("</table>");
    out.println("</html>");
  }

  private void resetParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*
     * Đầu mỗi học kì, dữ liệu sẽ được cập nhật vào một bảng dữ liệu. Hàm này nhằm mục đích
     * - Xoá bảng candidates_main nếu bảng này đang tồn tại
     * - Tạo lại bảng candidates_main
     * - Upload dữ liệu vào bảng candidates_main. Đây là bảng dữ liệu hoàn chỉnh ban đầu của tất cả các kì.
     * - Sau đó, xoá bảng candidates nếu bảng này đang tồn tại
     * - Và tạo lại bảng dữ liệu hoàn chỉnh của kì.
     */
    init();
    setDefaultParams();

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Tạo bảng tham số";


    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" />");
    out.println("<link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h1 align=\"center\">" + title + "</h1>");

    Statement stmt = null;
    Connection conn = null;

    try {
      //Execute SQL query
      conn = getDbConnection();
      stmt = conn.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS parameters(Param VARCHAR(100) PRIMARY KEY, Value VARCHAR(400), Security INT) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      System.out.println(sql);
      stmt.execute(sql);

      for (Map.Entry param : PARAM_DEFAULT.entrySet()) {
        sql =
            "INSERT INTO parameters VALUES(\"" + param.getKey() + "\", \"" + param.getValue() + "\", 0) ";
        System.out.println(sql);
        try {
          stmt.execute(sql);
        } catch (SQLException sqle) {
          System.out.println("Can not execute sql");
        }
      }

      sql = "CREATE TABLE IF NOT EXISTS universities(Code VARCHAR(10) PRIMARY KEY, UniversityName VARCHAR(255), "
          + "FoundationName VARCHAR(255), StudentClass VARCHAR(50), EvaluatedBy VARCHAR(50), Logo VARCHAR(255),  VnCoefs VARCHAR(10), NbJugesByCopy VARCHAR(10), MaxDocs VARCHAR(10))"
          + " ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      System.out.println(sql);
      stmt.execute(sql);

      for (String key : FOUNDATION_NAMES.keySet()) {
        sql = "INSERT INTO universities VALUES(\"" + key + "\", \""
            + UNIVERSITY_NAMES.get(key) + "\", \""
            + FOUNDATION_NAMES.get(key) + "\", \""
            + STUDENT_CLASS.get(key) + "\", \""
            + EVALUATED_BY.get(key) + "\", \""
            + LOGO.get(key) + "\", \""
            + VN_COEFS.get(key) + "\", \""
            + NB_JUGES_BY_COPY.get(key) + "\", \""
            + MAX_DOCS.get(key) + "\")";
        System.out.println(sql);
        try {
          stmt.execute(sql);
        } catch (SQLException sqle) {
          System.out.println("Can not execute sql");
        }
        ;
      }

      sql = "CREATE TABLE IF NOT EXISTS jury(Code VARCHAR(10) PRIMARY KEY, Name VARCHAR(100)) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      System.out.println(sql);
      stmt.execute(sql);

      out.println("<h4><a href='Techies.html'>Quay lại trang chủ</a> </h4>");
      out.println("<p>Thành công</p>");

    } catch (Exception e) {
      out.println("<h4><a href='Techies.html'>Quay lại trang chủ</a> </h4>");
      out.println("<p>Thất bại</p>");
      e.printStackTrace();
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException sqle2) {
        sqle2.printStackTrace();
      }
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException sqle3) {
        sqle3.printStackTrace();
      }
    }

    out.println("</table>");
    out.println("</html>");
  }

  private void setDefaultParams() {
    PARAMS.putAll(PARAM_DEFAULT);

    for (Constants.University uni : Constants.University.values()) {
      UNIVERSITY_NAMES.put(uni.name(), uni.getFullName());
      FOUNDATION_NAMES.put(uni.name(), uni.getFoundation().getFullName());
      STUDENT_CLASS.put(uni.name(), uni.getStudentClass().getFullName());
      EVALUATED_BY.put(uni.name(), uni.getFoundation().getLocation().name());
      LOGO.put(uni.name(), uni.getFoundation().getLogo());
      VN_COEFS.put(uni.name(), 0.0);
      NB_JUGES_BY_COPY.put(uni.name(), 3);
      MAX_DOCS.put(uni.name(), 10);
    }
  }

  static void getParamsFromDb() {
    try {
      Connection conn = getDbConnection();
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM parameters";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        Parameters.PARAMS.put(rs.getString("Param"), rs.getString("Value"));
      }

      sql = "SELECT * FROM universities";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        UNIVERSITY_NAMES.put(rs.getString("Code"), rs.getString("UniversityName"));
        FOUNDATION_NAMES.put(rs.getString("Code"), rs.getString("FoundationName"));
        STUDENT_CLASS.put(rs.getString("Code"), rs.getString("StudentClass"));
        EVALUATED_BY.put(rs.getString("Code"), rs.getString("EvaluatedBy"));
        LOGO.put(rs.getString("Code"), rs.getString("Logo"));
        VN_COEFS.put(rs.getString("Code"), rs.getDouble("VnCoefs"));
        NB_JUGES_BY_COPY.put(rs.getString("Code"), rs.getInt("NbJugesByCopy"));
        MAX_DOCS.put(rs.getString("Code"), rs.getInt("MaxDocs"));
      }

      sql = "SELECT * FROM jury";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        Parameters.JURY.put(rs.getString("Code"), rs.getString("Name"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
