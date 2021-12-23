package org.donghanh.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import static org.donghanh.common.Constants.COOKIE_NAME;


public class AuthenticationFilter implements Filter {

  private static final String[] JURY_ALLOWED_PATH = {"Login", "/app/jury"};

  private ServletContext context;

  public void init(FilterConfig fConfig) {
    this.context = fConfig.getServletContext();
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    HttpSession session = req.getSession(false);

    if (session == null) {   //checking whether the session exists
      String path = req.getServletPath();
      this.context.log("Unauthorized access request to " + path);
      res.sendRedirect(req.getContextPath() + "/Login.html");
    } else {
      // pass the request along the filter chain
      chain.doFilter(request, response);
    }
  }

  public void destroy() {
    //close any resources here
  }

//  private boolean validateAccess(HttpServletRequest request, String path) {
//    if ()
//  }

  private boolean validateCookie(HttpServletRequest request, String acceptedCookieValue) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        return cookie.getName().equals(COOKIE_NAME)
            && cookie.getValue().equals(acceptedCookieValue);
      }
    }
    return false;
  }

}
