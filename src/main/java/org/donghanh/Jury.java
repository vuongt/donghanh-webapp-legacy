package org.donghanh;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

/**
 * Servlet implementation class Jury
 */
@WebServlet("/Jury")
public class Jury extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Jury() {
        super();
    }
    
    void evaluate(HttpServletRequest request, HttpServletResponse response, String university) throws ServletException, IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("utf-8");
    	
		PrintWriter out = response.getWriter();
		String title = "Danh sách sinh viên " + Parameters.UNIVERSITY_NAMES.get(university) + " phân chia theo giám khảo";
		
		String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out.println(doctype);
		out.println("<head><title>" + title + "</title><meta charset=\"UTF-8\" /><script type=\"text/javascript\" src=\"normalize.js\" ></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
		out.println("<body>");
		out.println("<h4><a href='Jury.html'>Quay lại trang chủ</a></h4>");
		out.println("<h4><font color='red'>Lưu ý: điểm phải là một số nguyên, hoặc số thập phân với dấu CHẤM. Dấu phẩy không được chấp nhận.</font></h4>");
		
		Statement stmt = null;
		Connection conn = null;
		int nb_juges=0;
		int nb_candidates=0;
		
		try {
			//Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Open a connection
			conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			
			stmt = conn.createStatement();
			String sql;
			ResultSet rs = stmt.executeQuery("SELECT count(*) AS nb FROM all_candidates_" + university);
			rs.next();
			
			nb_candidates = rs.getInt("nb");
			nb_juges = LocalFunctions.nb_Juges(nb_candidates, Parameters.NB_JUGES_BY_COPY.get(university), Parameters.MAX_DOCS.get(university), Parameters.EVALUATED_BY.get(university));
			
			for (int index = 0; index <= nb_juges; ++index){
				sql = "CREATE TABLE IF NOT EXISTS candidates_" + university + "_jury_" + index + " AS SELECT `Ma so`, `Ho dem`, `Ten`, "
						+ "'000000' AS hoancanh, '000000' AS hoancanhnorm, '000000' AS hoctap, '000000' AS hoctapnorm, "
						+ "'000000' AS uocmo, '000000' AS uocmonorm, '000000' AS diemcong, '000000' AS diemcongnorm, "
						+ "'000000' AS tongket, '000000' AS tongketnorm"
						+ " FROM all_candidates_"
						+ university + " WHERE `Phan phoi giam khao` LIKE '%" +  index + "%';";
				stmt.execute(sql);
				
				sql = "SELECT * FROM jury WHERE Code='" + university + "_" + index + "'";
				rs = stmt.executeQuery(sql);
				out.println("<h1 align=\"center\">Danh sách sinh viên được chấm bởi giám khảo " + index + "</h1>");
				if (rs.next()){
					out.println("<h3 align=\"center\">Họ và tên giám khảo: <input size='60' style='text-align : center;' type='text' id='jugename" + index +"' value='" + rs.getString("Name") + "'></h3>");
				}
				
				else{
					out.println("<h3 align=\"center\">Họ và tên giám khảo: <input size='60' style='text-align : center;' type='text' id='jugename" + index +"' value='Chưa có người chấm'></h3>");
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
				
				while (rs.next()){
					out.println("<tr>");
					out.println("<td>" + rs.getInt("Ma so") + "</td>");
					out.println("<td>" + rs.getString("Ho dem") + "</td>");
					out.println("<td>" + rs.getString("Ten") + "</td>");
					out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='hoancanh_" + rs.getInt("Ma so") + "_gk" + index +"' value=" + rs.getDouble("hoancanh") + "></td>");
					out.println("<td align='right'>" + rs.getDouble("hoancanhnorm") + "</td>");
					out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='hoctap_" + rs.getInt("Ma so") + "_gk" + index +"' value=" + rs.getDouble("hoctap") + "></td>");
					out.println("<td align='right'>" + rs.getDouble("hoctapnorm") + "</td>");
					out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='uocmo_" + rs.getInt("Ma so") + "_gk" + index +"' value=" + rs.getDouble("uocmo") + "></td>");
					out.println("<td align='right'>" + rs.getDouble("uocmonorm") + "</td>");
					out.println("<td><input size='10' style='text-align : right;' class='raw_mark_" + index + "' type='text' id='diemcong_" + rs.getInt("Ma so") + "_gk" + index +"' value=" + rs.getDouble("diemcong") + "></td>");
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
				out.println("<button type='button' onclick='jury_normalizer(" + index +", \""+ university + "\")'>Lưu để chuẩn hóa</button>");
				out.println("<a id='confirm_link_" + index + "' href=''></a>");
			}
			
		} catch (Exception e) {
			System.out.println(e);
			out.println("</body></html>");
			return;
		}
		
		out.println("<h4><a href='Jury.html'>Quay lại trang chủ</a></h4>");
		out.println("</body>");
		out.println("</html>");
    }
    
    void juryNormalize(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String jury = request.getParameter("jury");
    	String university = request.getParameter("university");
    	String jugename = request.getParameter("jugename");
    	
    	try {
			//Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			
			Statement stmt = null;
			Connection conn = null;
			
			//Open a connection
			conn = DriverManager.getConnection(Parameters.DB_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			
			//Execute SQL query
			stmt = conn.createStatement();
			
			String sql = "INSERT INTO jury VALUES ('" + university + "_" + jury + "', '" + jugename + "')" ;
			try {
				stmt.execute(sql);
			} catch (Exception e) {
				sql = "UPDATE jury SET Name='" + jugename + "' WHERE Code='" + university + "_" + jury + "'"; 
				try {
					stmt.execute(sql);
				} catch (Exception e2){
					
				}
			}
			
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()){
				String current_param = (String)paramNames.nextElement();
				if (current_param.contains("_")){
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
			for (int index = 0; index < 4; ++index){
				if (Math.abs(stddev[index]) < 0.01){
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
			if (Math.abs(totalstddev) < 0.01){
				totalstddev = 1;
			}
			
			sql = "UPDATE candidates_" + university + "_jury_" + jury + " SET "
					+ "tongketnorm = ROUND((tongket -" + totalavg + ")*2/" + totalstddev + " + 10, 2)" ;
			stmt.execute(sql);
			
	    	conn.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	evaluate(request, response, university);
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies==null);
		if (cookies!=null)
			System.out.println(cookies[0].getValue());
		
		if (cookies==null){
			response.setContentType("text/html");
	    	response.setCharacterEncoding("utf-8");
	    	
			PrintWriter out = response.getWriter();
			
			String doctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
			out.println(doctype);
			out.println("<head><meta charset=\"UTF-8\" /></script><link rel='stylesheet' type='text/css' href='main.css'></head>");
			out.println("<body>");
			out.println("<h4>Ấn Ctrl+F5 để xem nội dung trang.</h4>");
			out.println("<h4>Nếu bạn vẫn không thể xem được nội dung sau khi nhấn, vui lòng quay lại trang đăng nhập và điền chính xác các từ khóa.</h4>");
			out.println("<h6><a href='Login.html'>Trang đăng nhập</a></h6>");
			out.println("</body>");
            return;
		}
		System.out.println(cookies.length);
		if (cookies!=null){
			Cookie cookie = cookies[0];
			if (!(cookie.getName().equals("Validated") && cookie.getValue().equals("OKJury"))){
				RequestDispatcher rs = request.getRequestDispatcher("/Login.html");
	            rs.forward(request, response);
				return;
			}
		}
		
		String my_action = request.getParameter("action");
		
		Parameters.getParams();
		
		if (my_action != null && my_action.equals("EVALUATE")){
			evaluate(request, response, request.getParameter("university"));
		}
		
		if (my_action != null && my_action.equals("JURY_NORMALIZE")){
			juryNormalize(request, response);
		}
		
		if (request.getParameter("action") != null && request.getParameter("action").equals("LOG_OUT")){
        	Login.logIn(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
