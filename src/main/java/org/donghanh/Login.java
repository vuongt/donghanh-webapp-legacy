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
  private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

  public Login() {
    super();
  }

  static void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    String name = request.getParameter("email");
    String pass = request.getParameter("pass");

    Cookie cookie = new Cookie("Validated", "KO");
    cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
    String redirectPage = "Login.html";

    if (Validate.checkUser(name, pass, "admin")) {
      cookie.setValue("OK");
      redirectPage = "Center.html";
    } else if (Validate.checkUser(name, pass, "jury")) {
      cookie.setValue("OKJury");
      redirectPage = "Jury.html";
    } else if (Validate.checkUser(name, pass, "techies")) {
      cookie.setValue("OKTechies");
      redirectPage = "Techies.html";
    } else {
      out.println("Username or Password incorrect");

    }
    response.addCookie(cookie);
    RequestDispatcher rs = request.getRequestDispatcher(redirectPage);
    rs.forward(request, response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Parameters.getParams();
    logIn(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}
