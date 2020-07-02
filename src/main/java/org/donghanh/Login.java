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

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }
    
    static void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Cookie[] cookies = request.getCookies();
		
		System.out.println(cookies==null);
		if (cookies!=null)
			System.out.println(cookies[0].getValue());
		
		response.setContentType("text/html;charset=UTF-8");
		
        PrintWriter out = response.getWriter();
        
        Cookie cookie;
        
		if (cookies==null){
			Cookie userCookie = new Cookie("Validated", "KO");
    		userCookie.setMaxAge(0);
    		response.addCookie(userCookie);
		}
		
		else {
			cookie = cookies[0];
			cookie.setValue("KO");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
        
        String name = request.getParameter("email");
        String pass = request.getParameter("pass");
        
        if(Validate.checkUser(name, pass, "admin"))
        {
        	cookie = new Cookie("Validated", "OK");
    		cookie.setMaxAge(60*60*24*365);
    		response.addCookie(cookie);
    		RequestDispatcher rs = request.getRequestDispatcher("Center.html");
            rs.forward(request, response);
        }
        else if (Validate.checkUser(name, pass, "jury")){
        	cookie = new Cookie("Validated", "OKJury");
    		cookie.setMaxAge(60*60*24*365);
    		response.addCookie(cookie);
    		RequestDispatcher rs = request.getRequestDispatcher("Jury.html");
            rs.forward(request, response);
        }
        else if (Validate.checkUser(name, pass, "techies")){
        	cookie = new Cookie("Validated", "OKTechies");
    		cookie.setMaxAge(60*60*24*365);
    		response.addCookie(cookie);
    		RequestDispatcher rs = request.getRequestDispatcher("Techies.html");
            rs.forward(request, response);
        }
        else
        {
           out.println("Username or Password incorrect");
           RequestDispatcher rs = request.getRequestDispatcher("Login.html");
           rs.forward(request, response);
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Parameters.getParams();
        logIn(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
