package org.donghanh.service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Map;

import static org.donghanh.common.Constants.CANDIDATE_TABLE_NAME;
import static org.donghanh.db.MainDataSource.getConnection;

public class CandidateService {

  /**
   * Đầu mỗi học kì, dữ liệu sẽ được cập nhật vào một bảng dữ liệu. Hàm này nhằm mục đích
   * - Xoá bảng candidates_main nếu bảng này đang tồn tại
   * - Tạo lại bảng candidates_main
   * - Upload dữ liệu vào bảng candidates_main.
   * Đây là bảng dữ liệu hoàn chỉnh ban đầu của tất cả các kì.
   * - Sau đó, xoá bảng candidates nếu bảng này đang tồn tại
   * - Và tạo lại bảng dữ liệu hoàn chỉnh của kì.
   */
  public static void reloadAllCandidatesData() {
    Map<String, String> commonParams = ParameterService.getCommonParams();
    File f = new File(commonParams.get("LOCAL_PATH") + commonParams.get("LOCAL_SOURCE_FILE"));
    if (!f.exists() || f.isDirectory()) {
      return;
    }

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {

      String sql = "DROP TABLE IF EXISTS " + CANDIDATE_TABLE_NAME + "_main";
      stmt.execute(sql);

      String[] headers = commonParams.get("HEADERS_LIST").split(",");
      String[] headerTypes = commonParams.get("HEADERTYPES_LIST").split(",");

      sql = "CREATE TABLE IF NOT EXISTS " + CANDIDATE_TABLE_NAME + "_main(`Ma so` VARCHAR(10) PRIMARY KEY, ";
      for (int index = 1; index < headers.length; ++index) {
        sql += "`" + headers[index] + "` " + headerTypes[index] + ", ";
      }
      sql = sql.substring(0, sql.length() - 2);
      sql += ")  ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;";

      stmt.execute(sql);

      sql = "LOAD DATA LOCAL INFILE '" + commonParams.get("LOCAL_PATH") + commonParams.get("LOCAL_SOURCE_FILE") + "' INTO TABLE " + CANDIDATE_TABLE_NAME
          + "_main FIELDS TERMINATED BY ',' ENCLOSED BY '' LINES TERMINATED BY '\\n' IGNORE 1 LINES";

      stmt.execute(sql);

      sql = "DROP TABLE IF EXISTS " + CANDIDATE_TABLE_NAME;
      stmt.execute(sql);

      sql = "CREATE TABLE " + CANDIDATE_TABLE_NAME + " AS SELECT DISTINCT `Ma so`, `Ho dem`, `Ten`, `Nam thu`, `Truong`, `Ki` FROM "
          + CANDIDATE_TABLE_NAME + "_main"
          + " WHERE `Ki`=" + commonParams.get("CURRENT_SEMESTER");
      stmt.execute(sql);

    } catch (SQLException e) {
      e.getMessage();
    }
  }

  public static void saveSelectedCandidates(HttpServletRequest request) {
    String university = request.getParameter("university");
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {

      Enumeration<String> paramNames = request.getParameterNames();
      String sql = "UPDATE selected_" + university + " SET selected=0";
      stmt.execute(sql);
      while (paramNames.hasMoreElements()) {
        String currentParam = paramNames.nextElement();
        if (currentParam.contains("_")) {
          String candidateId = currentParam.split("_")[1];
          String selected = request.getParameter(currentParam);
          sql = "UPDATE selected_" + university + " SET selected=" + selected + " WHERE `Ma so`=" + candidateId;
          stmt.execute(sql);
        }
      }
    } catch (SQLException e) {
      e.getMessage();
    }
  }

}
