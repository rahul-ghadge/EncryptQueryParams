package com.arya.encrypt.module;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestParameterObject {
  private HashMap hm = new HashMap();
  
  private String fullInitString = "";
  
  public RequestParameterObject(String initString) {
    this.fullInitString = initString;
    StringTokenizer st = new StringTokenizer(initString, "&");
    while (st.hasMoreElements()) {
      String element = st.nextToken();
      try {
        String name = element.substring(0, element.indexOf('='));
        String value = element.substring(element.indexOf('=') + 1, element.length());
        this.hm.put(name, value);
      } catch (Exception e) {
        System.out.println("Element:" + element + " was not in the format name=value or name=value1,value2,value3.");
      } 
    } 
  }
  
  public Map getParameterMap() {
    return this.hm;
  }
  
  public String getParameter(String name) {
    return (String)this.hm.get(name);
  }
  
  public Enumeration getParameterNames() {
    return (Enumeration)this.hm.keySet();
  }
  
  public String[] getParameterValues(String name) {
    ArrayList al = new ArrayList();
    StringTokenizer st = new StringTokenizer((String)this.hm.get(name), ",");
    while (st.hasMoreTokens())
      al.add((String)st.nextElement()); 
    //return al.<String>toArray(new String[1]);
    return (String[]) al.toArray(new String[1]);
  }
  
  public String toString() {
    return this.fullInitString;
  }
}
