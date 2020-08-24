package org.donghanh;

public class Constants {

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
    FR("học bổng Đồng Hành", "LOGO_DH", Location.FR),
    SG("học bổng Đồng Hành Singapore", "LOGO_DH_SING", Location.SG),
    KR("học bổng Đồng Hành Korea", "LOGO_DH", Location.FR),
    GTVT("học bổng Đồng Hành - Cầu Đường Pháp", "LOGO_CDP", Location.FR),
    XD("học bổng Đồng Hành - Xây Dựng", "LOGO_XD", Location.FR),
    NT("học bổng Đồng Hành - Nghệ Tĩnh", "LOGO_NT", Location.FR);

    private final String name;
    private final String logo;
    private final Location location;

    Foundation(String name, String logo, Location location) {
      this.name = name;
      this.logo = logo;
      this.location = location;
    }

    public String getFullName() {
      return name;
    }

    public String getLogo() {
      return logo;
    }

    public Location getLocation() {
      return location;
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

  public enum Location {
    FR,
    SG,
    KR
  }
}
