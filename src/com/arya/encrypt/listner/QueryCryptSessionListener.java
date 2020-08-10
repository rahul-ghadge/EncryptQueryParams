package com.arya.encrypt.listner;

import com.arya.encrypt.QueryCrypt;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class QueryCryptSessionListener implements HttpSessionListener {
  public void sessionDestroyed(HttpSessionEvent sessionEvt) {
    String sessionID = sessionEvt.getSession().getId();
    QueryCrypt.getInstance().clearUserInfo(sessionID);
  }
  
  public void sessionCreated(HttpSessionEvent sessionEvt) {}
}
