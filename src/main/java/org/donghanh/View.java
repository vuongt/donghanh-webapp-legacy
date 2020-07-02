package org.donghanh;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class View
 */
@WebServlet("/View")
public class View extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
  
    
    void showCandidateTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	
    	response.setContentType("text/html");
    	response.setCharacterEncoding("utf-8");
    	
		PrintWriter out = response.getWriter();
		String title = "Danh sách sinh viên tất cả các trường";
		
		String doctype = 
				"<!doctype html public \" - //w3c//dtd html 4.0 " +
				"transitional//en\">\n";
		out.println(doctype);
		out.println("<html>");
		out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><link rel='stylesheet' type='text/css' href='main.css'></head>");
		out.println("<body>");
		
		out.println("<h1 align=\"center\">" + title + "</h1>");
		out.println("<table width=\"100%\" border=\"1\" align=\"center\">");
		out.println("<tr>");
		
		String[] HEADERS = Parameters.PARAMS.get("HEADERS_LIST").split(",");
		for (int index = 0; index < HEADERS.length; ++index){
			out.println("<th>" + HEADERS[index] + "</th>");
		}
		
		Statement stmt = null;
		Connection conn = null;
		
		try {
			//Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Open a connection
			conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			
			//Execute SQL query
			stmt = conn.createStatement();
			String sql;
			
			//sql = "SELECT `Ma so`, `Ho dem`, `Ten` FROM " + CANDIDATE_TABLE_NAME + " WHERE `Ki` = 29;";
			sql = "SELECT * FROM " + Parameters.PARAMS.get("CANDIDATE_TABLE_NAME") + "_main WHERE `Ki` = " + Parameters.PARAMS.get("CURRENT_SEMESTER") + ";";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()){
				out.println("<tr>");
				for (int index = 0; index < HEADERS.length; ++index){
					String[] LIMITS = Parameters.PARAMS.get("LIMITS_LIST").split(",");
					if (LIMITS[index] == ""){
						out.println("<td>" + rs.getInt(HEADERS[index]) + "</td>");
					} else
						out.println("<td>" + rs.getString(HEADERS[index]) + "</td>");
				}
				out.println("</tr>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
    }

    void viewDistributions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String university = request.getParameter("university");
    	
    	response.setContentType("text/html");
    	response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String title = "Danh sách sinh viên " + Parameters.UNIVERSITY_NAMES.get(university);
		
		String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out.println(doctype);
		out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><script type=\"text/javascript\" src=\"normalize.js\" ></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
		out.println("<body>");
		
		Statement stmt = null;
		Connection conn = null;
		int nb_juges = 0;
		try {
			//Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Open a connection
			conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
			rs.next();
			
			int nb_candidates = rs.getInt("nb");
			if (nb_candidates == 0){
				out.println("</body></html>");
				return;
			}
			
			nb_juges = LocalFunctions.nb_Juges(nb_candidates, Parameters.NB_JUGES_BY_COPY.get(university), Parameters.MAX_DOCS.get(university), Parameters.EVALUATED_BY.get(university));
			
			String sql = "SELECT bilan_" + university + ".*, selected_" + university + ".selected FROM bilan_" + university + " INNER JOIN selected_" + university + " "
					+ "ON bilan_" + university + ".`Ma so` = selected_" + university + ".`Ma so` ORDER BY finalscore DESC, bilan_" + university + ".`Ma so` ASC";
			rs = stmt.executeQuery(sql);
			
			out.println("<h1 align=\"center\">Bảng tổng kết</h1>");
			out.println("<h3 align=\"center\">Hệ số giám khảo 0 (VN): " + Parameters.VN_COEFS.get(university) 
			 + " - Tổng hệ số các giám khảo "+ Parameters.EVALUATED_BY.get(university) + ": " + (1 - Parameters.VN_COEFS.get(university)) +"</h1>");
		
			out.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_bilan'>");
			out.println("<tr>");
			out.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");
			
			for (int count = 0; count <= nb_juges; ++count){
				if (Parameters.JURY.containsKey(university + "_" + count))
					out.println("<th>Gk" + count + ":<br>" + Parameters.JURY.get(university + "_" + count) + "</th>");
				else
					out.println("<th>Giám khảo " + count + "</th>");
			};
			
			out.println("<th>Điểm tổng kết</th>\n<th>Chọn</th>");
			out.println("</tr>");
			while(rs.next()){
				String selected = (rs.getInt("selected")==0)?"":"checked";
				String color = (selected.equals("checked"))?"bgcolor=#ffff66":"";
				out.println("<tr " + color + ">");
				out.println("<td>" + rs.getString("Ma so") + "</td>");
				out.println("<td>" + rs.getString("Ho dem") + "</td>");
				out.println("<td>" + rs.getString("Ten") + "</td>");
				for (int count = 0; count <= nb_juges; ++count){
					if (rs.getString("gk" + count)==null){
						out.println("<td></td>");
					}
					else
						out.println("<td>" + rs.getDouble("gk" + count) + "</td>");
				}; 
				out.println("<td><b>" + rs.getDouble("finalscore") + "</b></td>");
				
				out.println("<td><input type='checkbox' disabled='disabled'" + selected + "></td>");
				out.println("</tr>");
			}
			
			out.println("</table>");
		} catch (Exception e) {
			out.println("</body></html>");
			return;
		}
		try {
			//Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Open a connection
			conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			
			stmt = conn.createStatement();
			String sql;
			ResultSet rs;
			
			for (int index = 0; index <= nb_juges; ++index){
				sql = "SELECT * FROM jury WHERE Code='" + university + "_" + index + "'";
				rs = stmt.executeQuery(sql);
				out.println("<h1 align=\"center\">Danh sách sinh viên được chấm bởi giám khảo " + index + "</h1>");
				if (rs.next()){
					out.println("<h3 align=\"center\">Họ và tên giám khảo: " + rs.getString("Name") + "</h3>");
				}
				
				else{
					out.println("<h3 align=\"center\">Chưa có người chấm</h3>");
				}
				
				out.println("<table width=\"100%\" border=\"1\" align=\"center\" id='table_" + university + "_" + index + "'>");
				out.println("<tr>");
				out.println("<th>Mã số</th>\n<th>Họ</th>\n<th>Tên</th>");
				out.println("<th>Hoàn cảnh</th>\n<th>Chuẩn hóa</th>");
				out.println("<th>Học tập</th>\n<th>Chuẩn hóa</th>");
				out.println("<th>Ước mơ</th>\n<th>Chuẩn hóa</th>");
				out.println("<th>Điểm cộng</th>\n<th>Chuẩn hóa</th>");
				out.println("<th>Tổng</th>\n<th>Chuẩn hóa</th>");
				out.println("</tr>");
				
				sql = "SELECT * FROM candidates_" + university + "_jury_" + index;
				rs = stmt.executeQuery(sql);
				
				while (rs.next()){
					out.println("<tr>");
					out.println("<td>" + rs.getInt("Ma so") + "</td>");
					out.println("<td>" + rs.getString("Ho dem") + "</td>");
					out.println("<td>" + rs.getString("Ten") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("hoancanh") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("hoancanhnorm") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("hoctap") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("hoctapnorm") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("uocmo") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("uocmonorm") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("diemcong") + "</td>");
					out.println("<td align='right'>" + rs.getDouble("diemcongnorm") + "</td>");
					out.println("<td align='right'><b>" + rs.getDouble("tongket") + "</b></td>");
					out.println("<td align='right'><b>" + rs.getDouble("tongketnorm") + "</b></td>");
					out.println("</tr>");
				}
				
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//out.println("<button type='button' onclick='normalizer(1, \""+ university + "\")'>Lưu để chuẩn hóa</button>");
		//out.println("<a id='confirm_link_1' href=''></a>");
		out.println("</body>");
		out.println("</html>");
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String my_action = request.getParameter("action");
		
		Parameters.getParams();

		if (my_action != null && my_action.equals("VIEW_DISTRIBUTIONS")){
			viewDistributions(request, response);
		}
		
		if (my_action!= null && my_action.equals("SHOW_CANDIDATE_TABLE")){
			showCandidateTable(request, response);
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
