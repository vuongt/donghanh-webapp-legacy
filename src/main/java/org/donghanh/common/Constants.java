package org.donghanh.common;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

  public static final String COOKIE_NAME = "donghanh";
  public static final String IP = "52.37.193.91";

  public static final double DEFAULT_VN_COEF = 0.0;
  public static final double DEFAULT_NB_JURIES_BY_COPY = 4;
  public static final double DEFAULT_NB_JURIES = 4;
  public static final double DEFAULT_NB_GROUP = 2;

  public static final String CANDIDATE_TABLE_NAME = "candidates";
  public static final String FONT = "/usr/share/fonts";


  public static Map<String, String> PARAM_DEFAULT;

  static {
    PARAM_DEFAULT = new HashMap<>();
    PARAM_DEFAULT.put("LOCAL_PATH", "/var/www/html/");
    PARAM_DEFAULT.put("LOCAL_SOURCE_FILE", "Data/Dulieu38.csv");
    PARAM_DEFAULT.put("CURRENT_SEMESTER", "38");
    PARAM_DEFAULT.put("HEADERS_LIST", "Ma so,Ho dem,Ten,Gioi tinh,Ngay sinh,Nam thu,Lop nganh,Truong,Dia chi,Email,Ki,Ket qua");
    PARAM_DEFAULT.put("HEADERTYPES_LIST", "INT NOT NULL,VARCHAR(100),VARCHAR(20),VARCHAR(10),VARCHAR(20),VARCHAR(1),VARCHAR(100),VARCHAR(20),VARCHAR(100),VARCHAR(100),INT NOT NULL,VARCHAR(5)");
    PARAM_DEFAULT.put("PLACE_AND_DATE_FR", "Paris, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_FR", "Chủ tịch Quỹ học bổng Đồng Hành");
    PARAM_DEFAULT.put("SIGNATURE_NAME_FR", "Nguyễn Đức Vinh");
    PARAM_DEFAULT.put("PLACE_AND_DATE_SG", "Singapore, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_SG", "Chủ tịch Quỹ học bổng Đồng Hành Singapore");
    PARAM_DEFAULT.put("SIGNATURE_NAME_SG", "Nguyễn Duy Ánh");
    PARAM_DEFAULT.put("PLACE_AND_DATE_KR", "Seoul, ngày 10 tháng 5 năm 2017");
    PARAM_DEFAULT.put("SIGNATURE_TITLE_KR", "Chủ tịch Quỹ học bổng Đồng Hành Korea");
    PARAM_DEFAULT.put("SIGNATURE_NAME_KR", "Cao Quyên");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_KR", "Image/SignatureKR.bmp");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_FR", "Image/SignatureFR.bmp");
    PARAM_DEFAULT.put("SIGNATURE_IMAGE_SG", "Image/SignatureSG.bmp");
    PARAM_DEFAULT.put("LOGO_DH", "Image/logoFR.png");
    PARAM_DEFAULT.put("LOGO_DH_SING", "Image/logoDH.png");
    PARAM_DEFAULT.put("LOGO_DH_TAIWAN", "Image/logoSG.png");
    PARAM_DEFAULT.put("LOGO_DH_KOREA", "Image/logoKR.png");
    PARAM_DEFAULT.put("LOGO_CDP", "Image/logoCDP.jpg");
    PARAM_DEFAULT.put("LOGO_XD", "Image/logoXD.gif");
    PARAM_DEFAULT.put("LOGO_NT", "Image/logoNT.jpg");
    PARAM_DEFAULT.put("LOGO_KETTER", "Image/logoKetter.jpg");
  }

  public enum UserProfile {
    JURY,
    ADMIN,
    SUPER_USER,
    UNK
  }

  public enum University {
    //HN
    BKHN("Trường đại học Bách Khoa Hà Nội", Foundation.FR, StudentClass.SV),
    TNHN("Trường đại học Khoa học tự nhiên, ĐHQG Hà Nội", Foundation.FR, StudentClass.SV),
    GTVT1("Trường đại học Giao thông vận tải, Cơ sở I tại Hà Nội", Foundation.GTVT, StudentClass.SV),
    XD("Trường đại học Xây dựng", Foundation.XD, StudentClass.SV),
    //HCM
    BKHCM("Trường đại học Bách khoa, ĐHQG TP Hồ Chí Minh", Foundation.FR, StudentClass.SV),
    TNHCM("Trường đại học Khoa học tự nhiên, ĐHQG TP Hồ Chí Minh", Foundation.FR, StudentClass.SV),
    GTVT2("Trường đại học Giao thông vận tải, Cơ sở II tại TP Hồ Chí Minh", Foundation.GTVT, StudentClass.SV),
    // DN
    BKDN("Trường đại học Bách khoa, Đại học Đà Nẵng", Foundation.FR, StudentClass.SV),
    SPDN("Trường đại học Sư phạm, Đại học Đà Nẵng", Foundation.FR, StudentClass.SV),
    KTDN("Trường đại học Kinh tế, Đại học Đà Nẵng", Foundation.FR, StudentClass.SV),
    NNDN("Trường đại học Ngoại ngữ, Đại học Đà Nẵng", Foundation.FR, StudentClass.SV),
    // VINH
    VINH("Trường đại học Vinh", Foundation.NT, StudentClass.SV),
    THPTVINH("Trường THPT Vinh", Foundation.NT, StudentClass.HS),
    // SINGAPORE
    CNHN("Trường đại học Công nghệ, ĐHQG Hà Nội", Foundation.SG, StudentClass.SV),
    NNHN("Trường Đại Học Ngoại Ngữ, ĐHQG Hà Nội", Foundation.SG, StudentClass.SV),
    KTLHCM("Trường đại học Kinh tế - Luật, ĐHQG TP Hồ Chí Minh", Foundation.SG, StudentClass.SV),
    //KOKEA
    SPKTHCM("Trường đại học Sư phạm kĩ thuật TP Hồ Chí Minh", Foundation.KR, StudentClass.SV),
    KHAC("Các trường đại học khác", Foundation.FR, StudentClass.SV);

    private final String name;
    private final Foundation foundation;
    private final StudentClass studentClass;

    University(String name, Foundation foundation, StudentClass studentClass) {
      this.name = name;
      this.foundation = foundation;
      this.studentClass = studentClass;
    }

    public String getFullName() {
      return name;
    }

    public Foundation getFoundation() {
      return foundation;
    }

    public StudentClass getStudentClass() {
      return studentClass;
    }

  }

  public enum Foundation {
    FR("học bổng Đồng Hành", "LOGO_DH", EvaluatedBy.FR),
    SG("học bổng Đồng Hành Singapore", "LOGO_DH_SING", EvaluatedBy.SG),
    KR("học bổng Đồng Hành Korea", "LOGO_DH", EvaluatedBy.KR),
    GTVT("học bổng Đồng Hành - Cầu Đường Pháp", "LOGO_CDP", EvaluatedBy.FR),
    XD("học bổng Đồng Hành - Xây Dựng", "LOGO_XD", EvaluatedBy.FR),
    NT("học bổng Đồng Hành - Nghệ Tĩnh", "LOGO_NT", EvaluatedBy.FR);

    private final String name;
    private final String logo;
    private final EvaluatedBy evaluatedBy;

    Foundation(String name, String logo, EvaluatedBy evaluatedBy) {
      this.name = name;
      this.logo = logo;
      this.evaluatedBy = evaluatedBy;
    }

    public String getFullName() {
      return name;
    }

    public String getLogo() {
      return logo;
    }

    public EvaluatedBy getEvaluatedBy() {
      return evaluatedBy;
    }

  }

  public enum StudentClass {
    SV("SINH VIÊN"),
    HS("HỌC SINH");
    private final String name;

    StudentClass(String name) {
      this.name = name;
    }

    public String getFullName() {
      return name;
    }
  }

  public enum EvaluatedBy {
    FR,
    SG,
    KR
  }
}
