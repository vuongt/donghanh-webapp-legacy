package org.donghanh.servlet;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

import static org.donghanh.common.Constants.COOKIE_NAME;
import static org.donghanh.service.UserService.isValidUser;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

  public LoginServlet() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Login.html");
    requestDispatcher.forward(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    logIn(request, response);
  }

  private void logIn(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("username");
    String pass = request.getParameter("password");
    String successPage = request.getContextPath() + "/app/dashboard";

    if (isValidUser(name, pass, "admin")) {
      setCookieAndRedirect(request, response, "OKAdmin", successPage);
    } else if (isValidUser(name, pass, "jury")) {
      setCookieAndRedirect(request, response, "OKJury", successPage);
    } else if (isValidUser(name, pass, "techies")) {
      setCookieAndRedirect(request, response, "OKSU", successPage);
    } else {
      PrintWriter out = response.getWriter();
      out.println("<font color=red>Either username or password is wrong.</font>");
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Login.html");
      requestDispatcher.forward(request, response);
    }
  }

  private void setCookieAndRedirect(HttpServletRequest request, HttpServletResponse response,
                                    String cookieValue, String redirectPage) throws IOException {
    HttpSession oldSession = request.getSession(false);
    if (oldSession != null) {
      oldSession.invalidate();
    }
    //generate a new session
    HttpSession newSession = request.getSession(true);

    //setting session to expiry in 5 mins
    newSession.setMaxInactiveInterval(COOKIE_MAX_AGE_SECONDS);

    Cookie message = new Cookie(COOKIE_NAME, cookieValue);
    response.addCookie(message);
    response.sendRedirect(redirectPage);
  }
}
