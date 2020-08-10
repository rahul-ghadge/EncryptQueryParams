package com.arya.encrypt.servlet;

import com.arya.encrypt.QueryCrypt;
import com.arya.encrypt.module.QueryCryptProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class QueryCryptInitServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  public void init() throws ServletException {
    super.init();
    QueryCryptProperties qcp = new QueryCryptProperties();
    if (getInitParameter("debug") != null)
      try {
        qcp.setDebug((new Boolean(getInitParameter("debug"))).booleanValue());
      } catch (Exception e) {
        e.printStackTrace();
      }  
    if (getInitParameter("queryName") != null)
      try {
        qcp.setQueryName(getInitParameter("queryName"));
      } catch (Exception e) {
        e.printStackTrace();
      }  
    QueryCrypt.init(qcp);
  }
}
