package com.ezdi.audioplayer.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SimpleCORSFilter implements Filter {

   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

       HttpServletRequest request = (HttpServletRequest) req;

       HttpServletResponse response = (HttpServletResponse) res;
       response.setHeader("Access-Control-Allow-Origin", "*");
       response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
       response.setHeader("Access-Control-Max-Age", "20000");
       response.setHeader("Access-Control-Allow-Headers", "X-Accept-Charset,X-Accept,Content-Type, x-xsrf-token, Authorization");

       request.authenticate(response);
       chain.doFilter(request, response);
   }

   public void init(FilterConfig filterConfig) {}

   public void destroy() {}
}