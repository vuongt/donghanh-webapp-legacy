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
import java.sql.*;
import java.util.*;

/**
 * Servlet implementation class Params
 */
@WebServlet("/Parameters")
public class Parameters extends HttpServlet {
  private static final long serialVersionUID = 1L;

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
  static final String DB_USER = "dbuser";
  static final String DB_PASSWORD = "dbuser789";
  static final String REGISTER_URL = "jdbc:mysql://localhost:3306/users?useSSL=false";
  static final String IP = "52.37.193.91";

  static Map<String, String> PARAMS;
  static Map<String, String> UNIVERSITY_NAMES;
  static Map<String, String> FOUNDATION_NAMES;
  static Map<String, String> EVALUATED_BY;
  static Map<String, String> STUDENT_CLASS;
  static Map<String, String> LOGO;
  static Map<String, Double> VN_COEFS;
  static Map<String, Integer> NB_JUGES_BY_COPY;
  static Map<String, Integer> MAX_DOCS;

  static Map<String, String> JURY;

  static String[] PARAMS_LIST = new String[]{
      "LOCAL_PATH", "LOCAL_SOURCE_FILE", "ONLINE_SOURCE_FILE", "FONT",
      "CANDIDATE_TABLE_NAME", "PARAM_TABLE_NAME",
      "CURRENT_SEMESTER", "HEADERS_LIST",
      "HEADERTYPES_LIST", "LIMITS_LIST", "SEPARATOR", "DELIMITOR",
      "PLACE_AND_DATE_FR", "SIGNATURE_TITLE_FR", "SIGNATURE_NAME_FR",
      "PLACE_AND_DATE_SG", "SIGNATURE_TITLE_SG", "SIGNATURE_NAME_SG",
      "PLACE_AND_DATE_TW", "SIGNATURE_TITLE_TW", "SIGNATURE_NAME_TW",
      "PLACE_AND_DATE_KR", "SIGNATURE_TITLE_KR", "SIGNATURE_NAME_KR",
      "SIGNATURE_IMAGE_FR", "SIGNATURE_IMAGE_SG", "SIGNATURE_IMAGE_TW", "SIGNATURE_IMAGE_KR",
      "LOGO_DH", "LOGO_DH_SING", "LOGO_DH_TAIWAN", "LOGO_DH_KOREA",
      "LOGO_CDP", "LOGO_XD", "LOGO_NT", "LOGO_KETTER"};

  /*
  static String[] DEFAULT_VALUES = new String[] {
      "D:/wamp64/www/", "Dulieu20_31.csv", "", "C:/Windows/Fonts/",
      "candidates", "parameters",
      "29", "Ma so,Ho dem,Ten,Gioi tinh,Ngay sinh,Nam thu,Lop nganh,Truong,Dia chi,Email,Ki,Ket qua",
      "INT NOT NULL,VARCHAR(100),VARCHAR(20),VARCHAR(10),VARCHAR(20),VARCHAR(1),VARCHAR(100),VARCHAR(20),VARCHAR(100),VARCHAR(100),INT NOT NULL,VARCHAR(5)",
      ",',',',',',',',',',,'", ",", "",
      "Paris, ngày 31 tháng 12 năm 2016", "Chủ tịch Quỹ học bổng Đồng Hành", "Lê Hồng Thái",
      "Singapore, ngày 31 tháng 12 năm 2016", "Chủ tịch Quỹ học bổng Đồng Hành Singapore", "Lê Hồng Thái",
      "Đài Bắc, ngày 31 tháng 12 năm 2016", "Chủ tịch Quỹ học bổng Đồng Hành Đài Loan", "Lê Hồng Thái",
      "D:/wamp64/www/logoDH.png", "D:/wamp64/www/logoDHSing.png",
      "D:/wamp64/www/logoCDP.jpg", "D:/wamp64/www/logoXD.gif", "D:/wamp64/www/logoNT.jpg", "D:/wamp64/www/logoKetter.jpg"};
  */
  static String[] DEFAULT_VALUES = new String[]{
      "/var/www/html/", "Data/Dulieu20_32.csv", "", "/usr/share/fonts",
      "candidates", "parameters",
      "32", "Ma so,Ho dem,Ten,Gioi tinh,Ngay sinh,Nam thu,Lop nganh,Truong,Dia chi,Email,Ki,Ket qua",
      "INT NOT NULL,VARCHAR(100),VARCHAR(20),VARCHAR(10),VARCHAR(20),VARCHAR(1),VARCHAR(100),VARCHAR(20),VARCHAR(100),VARCHAR(100),INT NOT NULL,VARCHAR(5)",
      ",',',',',',',',',',,'", ",", "",
      "Paris, ngày 10 tháng 5 năm 2017", "Chủ tịch Quỹ học bổng Đồng Hành", "Lê Hồng Thái",
      "Singapore, ngày 10 tháng 5 năm 2017", "Chủ tịch Quỹ học bổng Đồng Hành Singapore", "Nguyễn Duy Ánh",
      "Đài Bắc, ngày 10 tháng 5 năm 2017", "Chủ tịch Quỹ học bổng Đồng Hành Đài Loan", "Võ Trường Giang",
      "Seoul, ngày 10 tháng 5 năm 2017", "Chủ tịch Quỹ học bổng Đồng Hành Korea", "Nguyễn Dương Hoàn",
      "Image/SignatureFR.bmp", "Image/SignatureSG.bmp", "Image/SignatureTW.bmp", "Image/SignatureKR.bmp",
      "/var/www/html/Image/logoDH.png", "/var/www/html/Image/logoDHSing.png", "/var/www/html/Image/logoDHTaiwan.png", "/var/www/html/Image/logoDHKorea.png",
      "/var/www/html/Image/logoCDP.jpg", "/var/www/html/Image/logoXD.gif", "/var/www/html/Image/logoNT.jpg", "/var/www/html/Image/logoKetter.jpg"};


  static int[] SECURITY = new int[]{
      0, 0, 1, 1,
      1, 1,
      0, 1,
      1,
      1, 1, 1,
      0, 0, 0,
      0, 0, 0,
      0, 0, 0,
      0, 0, 0,
      0, 0, 0, 0,
      0, 0, 0, 0,
      0, 0, 0, 0};

  static void initParams() throws ServletException, IOException {
    PARAMS = new LinkedHashMap<String, String>();
    UNIVERSITY_NAMES = new LinkedHashMap<String, String>();
    FOUNDATION_NAMES = new LinkedHashMap<String, String>();
    EVALUATED_BY = new LinkedHashMap<String, String>();
    STUDENT_CLASS = new LinkedHashMap<String, String>();
    LOGO = new LinkedHashMap<String, String>();
    VN_COEFS = new LinkedHashMap<String, Double>();
    NB_JUGES_BY_COPY = new LinkedHashMap<String, Integer>();
    MAX_DOCS = new LinkedHashMap<String, Integer>();
    JURY = new LinkedHashMap<String, String>();
  }

  void setDefaultParams() throws ServletException, IOException {
    initParams();

    for (int index = 0; index < PARAMS_LIST.length; ++index) {
      PARAMS.put(PARAMS_LIST[index], DEFAULT_VALUES[index]);
    }

    UNIVERSITY_NAMES.put("BKHN", "Trường đại học Bách Khoa Hà Nội");
    UNIVERSITY_NAMES.put("TNHN", "Trường đại học Khoa học tự nhiên, ĐHQG Hà Nội");
    UNIVERSITY_NAMES.put("CNHN", "Trường đại học Công nghệ, ĐHQG Hà Nội");
    UNIVERSITY_NAMES.put("XD", "Trường đại học Xây dựng");
    UNIVERSITY_NAMES.put("GTVT1", "Trường đại học Giao thông vận tải, Cơ sở I tại Hà Nội");
    UNIVERSITY_NAMES.put("VINH", "Trường đại học Vinh");
    UNIVERSITY_NAMES.put("BKDN", "Trường đại học Bách khoa, Đại học Đà Nẵng");
    UNIVERSITY_NAMES.put("KTDN", "Trường đại học Kinh tế, Đại học Đà Nẵng");
    UNIVERSITY_NAMES.put("SPDN", "Trường đại học Sư phạm, Đại học Đà Nẵng");
    UNIVERSITY_NAMES.put("NNDN", "Trường đại học Ngoại ngữ, Đại học Đà Nẵng");
//    UNIVERSITY_NAMES.put("DALAT", "Trường đại học Đà Lạt");
    UNIVERSITY_NAMES.put("BKHCM", "Trường đại học Bách khoa, ĐHQG TP Hồ Chí Minh");
    UNIVERSITY_NAMES.put("TNHCM", "Trường đại học Khoa học tự nhiên, ĐHQG TP Hồ Chí Minh");
    UNIVERSITY_NAMES.put("KTLHCM", "Trường đại học Kinh tế - Luật, ĐHQG TP Hồ Chí Minh");
    UNIVERSITY_NAMES.put("GTVT2", "Trường đại học Giao thông vận tải, Cơ sở II tại TP Hồ Chí Minh");
    UNIVERSITY_NAMES.put("SPKTHCM", "Trường đại học Sư phạm kĩ thuật TP Hồ Chí Minh");
//    UNIVERSITY_NAMES.put("CTHO", "Trường đại học Cần Thơ");
    UNIVERSITY_NAMES.put("KHAC", "Các trường đại học khác");
    UNIVERSITY_NAMES.put("THPT_VINH", "Trường THPT Vinh");
//    UNIVERSITY_NAMES.put("KHUE", "Các trường THPT tại Thừa Thiên Huế");
//    UNIVERSITY_NAMES.put("KHHCM", "Các trường THPT tại TP Hồ Chí Minh");
//    UNIVERSITY_NAMES.put("KNA1", "Trường THPT chuyên Phan Bội Châu - Nghệ An");
//    UNIVERSITY_NAMES.put("KNA2", "Trường THPT chuyên Đại học Vinh");
//    UNIVERSITY_NAMES.put("KHT1", "Trường THPT Phan Đình Phùng - Hà Tĩnh");
//    UNIVERSITY_NAMES.put("KHT2", "Trường THPT Năng khiếu Hà Tĩnh");
    UNIVERSITY_NAMES.put("NNHN", "Trường Đại Học Ngoại Ngữ, ĐHQG Hà Nội");

    FOUNDATION_NAMES.put("BKHN", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("TNHN", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("CNHN", "học bổng Đồng Hành Singapore");
    FOUNDATION_NAMES.put("XD", "học bổng Đồng Hành - Xây Dựng");
    FOUNDATION_NAMES.put("GTVT1", "học bổng Đồng Hành - Cầu Đường Pháp");
    FOUNDATION_NAMES.put("VINH", "học bổng Đồng Hành - Nghệ Tĩnh");
    FOUNDATION_NAMES.put("BKDN", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("KTDN", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("SPDN", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("NNDN", "học bổng Đồng Hành");
//    FOUNDATION_NAMES.put("DALAT", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("BKHCM", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("TNHCM", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("KTLHCM", "học bổng Đồng Hành Singapore");
    FOUNDATION_NAMES.put("GTVT2", "học bổng Đồng Hành - Cầu Đường Pháp");
    FOUNDATION_NAMES.put("SPKTHCM", "học bổng Đồng Hành Korea");
//    FOUNDATION_NAMES.put("CTHO", "học bổng Đồng Hành Đài Loan");
    FOUNDATION_NAMES.put("KHAC", "học bổng Đồng Hành");
    FOUNDATION_NAMES.put("THPT_VINH", "học bổng Đồng Hành - Nghệ Tĩnh");
//    FOUNDATION_NAMES.put("KHUE", "học bổng Đồng Hành");
//    FOUNDATION_NAMES.put("KHHCM", "học bổng Đồng Hành");
//    FOUNDATION_NAMES.put("KNA1", "học bổng Đồng Hành - FEA Ketterlé");
//    FOUNDATION_NAMES.put("KNA2", "học bổng Đồng Hành - FEA Ketterlé");
//    FOUNDATION_NAMES.put("KHT1", "học bổng Đồng Hành - FEA Ketterlé");
//    FOUNDATION_NAMES.put("KHT2", "học bổng Đồng Hành - FEA Ketterlé");
    FOUNDATION_NAMES.put("NNHN", "học bổng Đồng Hành Singapore");

    STUDENT_CLASS.put("BKHN", "SINH VIÊN");
    STUDENT_CLASS.put("TNHN", "SINH VIÊN");
    STUDENT_CLASS.put("CNHN", "SINH VIÊN");
    STUDENT_CLASS.put("XD", "SINH VIÊN");
    STUDENT_CLASS.put("GTVT1", "SINH VIÊN");
    STUDENT_CLASS.put("VINH", "SINH VIÊN");
    STUDENT_CLASS.put("BKDN", "SINH VIÊN");
    STUDENT_CLASS.put("KTDN", "SINH VIÊN");
    STUDENT_CLASS.put("SPDN", "SINH VIÊN");
    STUDENT_CLASS.put("NNDN", "SINH VIÊN");
//    STUDENT_CLASS.put("DALAT", "SINH VIÊN");
    STUDENT_CLASS.put("BKHCM", "SINH VIÊN");
    STUDENT_CLASS.put("TNHCM", "SINH VIÊN");
    STUDENT_CLASS.put("KTLHCM", "SINH VIÊN");
    STUDENT_CLASS.put("GTVT2", "SINH VIÊN");
    STUDENT_CLASS.put("SPKTHCM", "SINH VIÊN");
//    STUDENT_CLASS.put("CTHO", "SINH VIÊN");
    STUDENT_CLASS.put("KHAC", "SINH VIÊN");
    STUDENT_CLASS.put("VINH", "HỌC SINH");
//    STUDENT_CLASS.put("KHUE", "HỌC SINH");
//    STUDENT_CLASS.put("KHHCM", "HỌC SINH");
//    STUDENT_CLASS.put("KNA1", "HỌC SINH");
//    STUDENT_CLASS.put("KNA2", "HỌC SINH");
//    STUDENT_CLASS.put("KHT1", "HỌC SINH");
//    STUDENT_CLASS.put("KHT2", "HỌC SINH");
    STUDENT_CLASS.put("NNHN", "SINH VIÊN");

    EVALUATED_BY.put("BKHN", "FR");
    EVALUATED_BY.put("TNHN", "FR");
    EVALUATED_BY.put("CNHN", "SG");
    EVALUATED_BY.put("XD", "FR");
    EVALUATED_BY.put("GTVT1", "FR");
    EVALUATED_BY.put("VINH", "FR");
    EVALUATED_BY.put("BKDN", "FR");
    EVALUATED_BY.put("KTDN", "FR");
    EVALUATED_BY.put("SPDN", "FR");
    EVALUATED_BY.put("NNDN", "FR");
//    EVALUATED_BY.put("DALAT", "FR");
    EVALUATED_BY.put("BKHCM", "FR");
    EVALUATED_BY.put("TNHCM", "FR");
    EVALUATED_BY.put("KTLHCM", "SG");
    EVALUATED_BY.put("GTVT2", "FR");
    EVALUATED_BY.put("SPKTHCM", "FR");
//    EVALUATED_BY.put("CTHO", "TW");
    EVALUATED_BY.put("KHAC", "FR");
    EVALUATED_BY.put("THPT_VINH", "FR");
//    EVALUATED_BY.put("KHUE", "FR");
//    EVALUATED_BY.put("KHHCM", "FR");
//    EVALUATED_BY.put("KNA1", "FR");
//    EVALUATED_BY.put("KNA2", "FR");
//    EVALUATED_BY.put("KHT1", "FR");
//    EVALUATED_BY.put("KHT2", "FR");
    EVALUATED_BY.put("NNHN", "SG");

    LOGO.put("BKHN", PARAMS.get("LOGO_DH"));
    LOGO.put("TNHN", PARAMS.get("LOGO_DH"));
    LOGO.put("CNHN", PARAMS.get("LOGO_DH_SING"));
    LOGO.put("XD", PARAMS.get("LOGO_XD"));
    LOGO.put("GTVT1", PARAMS.get("LOGO_CDP"));
    LOGO.put("VINH", PARAMS.get("LOGO_NT"));
    LOGO.put("BKDN", PARAMS.get("LOGO_DH"));
    LOGO.put("KTDN", PARAMS.get("LOGO_DH"));
    LOGO.put("SPDN", PARAMS.get("LOGO_DH"));
    LOGO.put("NNDN", PARAMS.get("LOGO_DH"));
//    LOGO.put("DALAT", PARAMS.get("LOGO_DH"));
    LOGO.put("BKHCM", PARAMS.get("LOGO_DH"));
    LOGO.put("TNHCM", PARAMS.get("LOGO_DH"));
    LOGO.put("KTLHCM", PARAMS.get("LOGO_DH_SING"));
    LOGO.put("GTVT2", PARAMS.get("LOGO_CDP"));
    LOGO.put("SPKTHCM", PARAMS.get("LOGO_DH"));
//    LOGO.put("CTHO", PARAMS.get("LOGO_DH"));
    LOGO.put("KHAC", PARAMS.get("LOGO_DH"));
    LOGO.put("THPT_VINH", PARAMS.get("LOGO_DH"));
//    LOGO.put("KHUE", PARAMS.get("LOGO_DH"));
//    LOGO.put("KHHCM", PARAMS.get("LOGO_DH"));
//    LOGO.put("KNA1", PARAMS.get("LOGO_KETTER"));
//    LOGO.put("KNA2", PARAMS.get("LOGO_KETTER"));
//    LOGO.put("KHT1", PARAMS.get("LOGO_KETTER"));
//    LOGO.put("KHT2", PARAMS.get("LOGO_KETTER"));
    LOGO.put("NNHN", PARAMS.get("LOGO_DH_SING"));

    VN_COEFS.put("BKHN", 0.0);
    VN_COEFS.put("TNHN", 0.0);
    VN_COEFS.put("CNHN", 0.0);
    VN_COEFS.put("XD", 0.0);
    VN_COEFS.put("GTVT1", 0.0);
    VN_COEFS.put("VINH", 0.0);
    VN_COEFS.put("BKDN", 0.0);
    VN_COEFS.put("KTDN", 0.0);
    VN_COEFS.put("SPDN", 0.0);
    VN_COEFS.put("NNDN", 0.0);
//    VN_COEFS.put("DALAT", 0.0);
    VN_COEFS.put("BKHCM", 0.0);
    VN_COEFS.put("TNHCM", 0.0);
    VN_COEFS.put("KTLHCM", 0.0);
    VN_COEFS.put("GTVT2", 0.0);
    VN_COEFS.put("SPKTHCM", 0.0);
//    VN_COEFS.put("CTHO", 0.0);
    VN_COEFS.put("KHAC", 0.0);
    VN_COEFS.put("THPT_VINH", 0.0);
//    VN_COEFS.put("KHUE", 0.0);
//    VN_COEFS.put("KHHCM", 0.0);
//    VN_COEFS.put("KNA1", 0.0);
//    VN_COEFS.put("KNA2", 0.0);
//    VN_COEFS.put("KHT1", 0.0);
//    VN_COEFS.put("KHT2", 0.0);
    VN_COEFS.put("NNHN", 0.0);

    NB_JUGES_BY_COPY.put("BKHN", 3);
    NB_JUGES_BY_COPY.put("TNHN", 3);
    NB_JUGES_BY_COPY.put("CNHN", 4);
    NB_JUGES_BY_COPY.put("XD", 3);
    NB_JUGES_BY_COPY.put("GTVT1", 3);
    NB_JUGES_BY_COPY.put("VINH", 3);
    NB_JUGES_BY_COPY.put("BKDN", 3);
    NB_JUGES_BY_COPY.put("KTDN", 3);
    NB_JUGES_BY_COPY.put("SPDN", 3);
    NB_JUGES_BY_COPY.put("NNDN", 3);
//    NB_JUGES_BY_COPY.put("DALAT", 3);
    NB_JUGES_BY_COPY.put("BKHCM", 3);
    NB_JUGES_BY_COPY.put("TNHCM", 3);
    NB_JUGES_BY_COPY.put("KTLHCM", 4);
    NB_JUGES_BY_COPY.put("GTVT2", 3);
    NB_JUGES_BY_COPY.put("SPKTHCM", 3);
//    NB_JUGES_BY_COPY.put("CTHO", 4);
    NB_JUGES_BY_COPY.put("KHAC", 3);
    NB_JUGES_BY_COPY.put("THPT_VINH", 3);
//    NB_JUGES_BY_COPY.put("KHUE", 3);
//    NB_JUGES_BY_COPY.put("KHHCM", 3);
//    NB_JUGES_BY_COPY.put("KNA1", 3);
//    NB_JUGES_BY_COPY.put("KNA2", 3);
//    NB_JUGES_BY_COPY.put("KHT1", 3);
//    NB_JUGES_BY_COPY.put("KHT2", 3);
    NB_JUGES_BY_COPY.put("NNHN", 4);

    MAX_DOCS.put("BKHN", 18);
    MAX_DOCS.put("TNHN", 18);
    MAX_DOCS.put("CNHN", 10);
    MAX_DOCS.put("XD", 18);
    MAX_DOCS.put("GTVT1", 18);
    MAX_DOCS.put("VINH", 18);
    MAX_DOCS.put("BKDN", 18);
    MAX_DOCS.put("KTDN", 18);
    MAX_DOCS.put("SPDN", 18);
    MAX_DOCS.put("NNDN", 18);
//    MAX_DOCS.put("DALAT", 18);
    MAX_DOCS.put("BKHCM", 18);
    MAX_DOCS.put("TNHCM", 18);
    MAX_DOCS.put("KTLHCM", 10);
    MAX_DOCS.put("GTVT2", 18);
    MAX_DOCS.put("SPKTHCM", 18);
//    MAX_DOCS.put("CTHO", 10);
    MAX_DOCS.put("KHAC", 18);
    MAX_DOCS.put("THPT_VINH", 18);
//    MAX_DOCS.put("KHUE", 18);
//    MAX_DOCS.put("KHHCM", 18);
//    MAX_DOCS.put("KNA1", 18);
//    MAX_DOCS.put("KNA2", 18);
//    MAX_DOCS.put("KHT1", 18);
//    MAX_DOCS.put("KHT2", 18);
    MAX_DOCS.put("NNHN", 10);
  }

  protected static void getParams() throws ServletException, IOException {
    if (Parameters.PARAMS == null) {
      initParams();
    }

    try {
      Class.forName(Parameters.JDBC_DRIVER);

      Connection conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
      Statement stmt = conn.createStatement();
      String sql = "SELECT * FROM parameters";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        Parameters.PARAMS.put(rs.getString("Param"), rs.getString("Value"));
      }

      sql = "SELECT * FROM universities";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        Parameters.UNIVERSITY_NAMES.put(rs.getString("Code"), rs.getString("UniversityName"));
        Parameters.FOUNDATION_NAMES.put(rs.getString("Code"), rs.getString("FoundationName"));
        Parameters.STUDENT_CLASS.put(rs.getString("Code"), rs.getString("StudentClass"));
        Parameters.EVALUATED_BY.put(rs.getString("Code"), rs.getString("EvaluatedBy"));
        Parameters.LOGO.put(rs.getString("Code"), rs.getString("Logo"));
        Parameters.VN_COEFS.put(rs.getString("Code"), rs.getDouble("VnCoefs"));
        Parameters.NB_JUGES_BY_COPY.put(rs.getString("Code"), rs.getInt("NbJugesByCopy"));
        Parameters.MAX_DOCS.put(rs.getString("Code"), rs.getInt("MaxDocs"));
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

  void createCandidateTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*
     * Đầu mỗi học kì, dữ liệu sẽ được cập nhật vào một bảng dữ liệu. Hàm này nhằm mục đích
     * - Xoá bảng candidates_main nếu bảng này đang tồn tại
     * - Tạo lại bảng candidates_main
     * - Upload dữ liệu vào bảng candidates_main. Đây là bảng dữ liệu hoàn chỉnh ban đầu của tất cả các kì.
     * - Sau đó, xoá bảng candidates nếu bảng này đang tồn tại
     * - Và tạo lại bảng dữ liệu hoàn chỉnh của kì.
     */
    getParams();

    File old_source_file = new File(Parameters.PARAMS.get("LOCAL_PATH") + Parameters.PARAMS.get("LOCAL_SOURCE_FILE"));
    if (!old_source_file.exists() || old_source_file.isDirectory()) {
      LocalFunctions.downloadFromUrl(new URL(Parameters.PARAMS.get("ONLINE_SOURCE_FILE")), Parameters.PARAMS.get("LOCAL_SOURCE_FILE"));
    }

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    PrintWriter out = response.getWriter();
    String title = "Tạo bảng dữ liệu sinh viên";


    String doctype =
        "<!doctype html public \" - //w3c//dtd html 4.0 " +
            "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" />");
    out.println("<link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h1 align=\"center\">" + title + "</h1>");

    Statement stmt = null;
    Connection conn = null;

    try {
      //Register JDBC Driver
      Class.forName(Parameters.JDBC_DRIVER);

      //Open a connection
      conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);

      //Execute SQL query
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

  void resetParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        "<!doctype html public \" - //w3c//dtd html 4.0 " +
            "transitional//en\">\n";
    out.println(doctype);
    out.println("<html>");
    out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" />");
    out.println("<link rel='stylesheet' type='text/css' href='main.css'></head>");
    out.println("<body>");
    out.println("<h1 align=\"center\">" + title + "</h1>");

    Statement stmt = null;
    Connection conn = null;

    try {
      //Register JDBC Driver
      Class.forName(Parameters.JDBC_DRIVER);

      //Open a connection
      conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);

      //Execute SQL query
      stmt = conn.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS parameters(Param VARCHAR(100) PRIMARY KEY, Value VARCHAR(400), Security INT) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8";
      System.out.println(sql);
      stmt.execute(sql);

      for (int index = 0; index < PARAMS_LIST.length; index++) {
        sql = "INSERT INTO parameters VALUES(\"" + PARAMS_LIST[index] + "\", \"" + DEFAULT_VALUES[index] + "\", " + SECURITY[index] + ") ";
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

  void viewParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*
     * Hiển thị các tham số
     */
    getParams();

    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

    request.setAttribute("title", "Tham số");

    List<Map<String, Object>> params = new ArrayList<>();
    for (int index = 0; index < PARAMS_LIST.length; ++index) {
      if (SECURITY[index] == 0) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", PARAMS_LIST[index]);
        param.put("value", PARAMS.get(PARAMS_LIST[index]));
        params.add(param);
        System.out.println(param.toString());
      }
    }
    request.setAttribute("parameters", params);

    List<Map<String, Object>> universities = new ArrayList<>();
    for (String key : FOUNDATION_NAMES.keySet()) {
      Map<String, Object> university = new HashMap<>();
      university.put("code", key);
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

  protected void setParam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String key = request.getParameter("key");
    String value = request.getParameter("value");
    System.out.println("Setting key: " + key + ": " + value);

    try {
      Class.forName(Parameters.JDBC_DRIVER);

      Connection conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
      Statement stmt = conn.createStatement();
      String sql = "UPDATE parameters SET Value = '" + value + "' WHERE Param = '" + key + "'";

      System.out.println(sql);
      stmt.execute(sql);

      viewParams(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void setUniversityParams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String key = request.getParameter("key");
    String values_string = request.getParameter("values");

    String[] values = values_string.split("XXX");

    try {
      Class.forName(Parameters.JDBC_DRIVER);

      Connection conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    System.out.println(cookies == null);
    if (cookies != null)
      System.out.println(cookies[0].getValue());

    if (cookies == null) {
      response.setContentType("text/html");
      response.setCharacterEncoding("utf-8");

      PrintWriter out = response.getWriter();

      String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
      out.println(doctype);
      out.println("<head><meta charset=\"UTF-8\" /></script></head>");
      out.println("<body>");
      out.println("<h4>Ấn Ctrl+F5 để xem nội dung trang.</h4>");
      out.println("<h4>Nếu bạn vẫn không thể xem được nội dung sau khi nhấn, vui lòng quay lại trang đăng nhập và điền chính xác các từ khóa.</h4>");
      out.println("<h6><a href='Login.html'>Trang đăng nhập</a></h6>");
      out.println("</body>");
      return;
    }

    if (cookies != null) {
      Cookie cookie = cookies[0];
      if (!(cookie.getName().equals("Validated") && cookie.getValue().equals("OKTechies"))) {
        RequestDispatcher rs = request.getRequestDispatcher("/Login.html");
        rs.forward(request, response);
        return;
      }
    }

    String my_action = request.getParameter("action");

    //Nếu lệnh là LOG_OUT, đưa về trang logIn và xoá cache
    if (my_action != null && my_action.equals("LOG_OUT")) {
      Login.logIn(request, response);
    }

    //Khi kĩ thuật viên cần thay đổi tham số
    if (my_action != null && my_action.equals("RESET_PARAMS")) {
      resetParams(request, response);
    }

    //Khi kĩ thuật viên cần lấy tham số
    if (my_action != null && my_action.equals("VIEW_PARAMS")) {
      viewParams(request, response);
    }

    if (my_action != null && my_action.equals("SET_PARAM")) {
      setParam(request, response);
    }

    if (my_action != null && my_action.equals("SET_UNIVERSITY_PARAMS")) {
      setUniversityParams(request, response);
    }

    //Đầu mỗi kì, khi kĩ thuật viên upload toàn bộ thông tin về dữ liệu sinh viên và tạo ra bảng rút gọn để làm việc.
    if (my_action != null && my_action.equals("CREATE_CANDIDATE_TABLE")) {
      createCandidateTable(request, response);
    }

  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response).
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  }
}
