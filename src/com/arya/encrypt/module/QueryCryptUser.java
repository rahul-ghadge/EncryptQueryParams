package com.arya.encrypt.module;

import java.util.HashMap;
import javax.crypto.SecretKey;

public class QueryCryptUser {
  private String sessionID = "";
  
  private HashMap md5Keys = new HashMap();
  
  private SecretKey secretKey = null;
  
  public HashMap getMd5Keys() {
    return this.md5Keys;
  }
  
  public void setMd5Keys(HashMap md5Keys) {
    this.md5Keys = md5Keys;
  }
  
  public String getSessionID() {
    return this.sessionID;
  }
  
  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }
  
  public void addToMD5Map(String md5, byte[] encryptedStr) {
    this.md5Keys.put(md5, encryptedStr);
  }
  
  public byte[] getByKey(String md5key) {
    return (byte[])this.md5Keys.get(md5key);
  }
  
  public SecretKey getSecretKey() {
    return this.secretKey;
  }
  
  public void setSecretKey(SecretKey secretKey) {
    this.secretKey = secretKey;
  }
  
  public QueryCryptUser(String sessionid, SecretKey key) {
    this.secretKey = key;
    this.sessionID = sessionid;
  }
}
