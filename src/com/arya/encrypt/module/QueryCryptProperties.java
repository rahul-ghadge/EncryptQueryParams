package com.arya.encrypt.module;

public class QueryCryptProperties {
  boolean sessionKeyEnabled = true;
  
  String queryName = "_qc";
  
  boolean debug = false;
  
  public boolean isSessionKeyEnabled() {
    return this.sessionKeyEnabled;
  }
  
  public String getQueryName() {
    return this.queryName;
  }
  
  public void setQueryName(String queryName) {
    this.queryName = queryName;
  }
  
  public boolean isDebugEnabled() {
    return this.debug;
  }
  
  public void setDebug(boolean debug) {
    this.debug = debug;
  }
}
