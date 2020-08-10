package com.arya.encrypt.utils;

public class HexConvert {
  public static final String encode(byte[] convertBytes) {
    StringBuffer sb = new StringBuffer();
    try {
      for (int i = 0; i < convertBytes.length; i++) {
        Byte by = new Byte(convertBytes[i]);
        int k = by.intValue();
        int lowfour = k & 0xF;
        int highfour = k & 0xF0;
        highfour >>>= 4;
        sb.append(Integer.toHexString(highfour));
        sb.append(Integer.toHexString(lowfour));
      } 
    } catch (Exception e) {
      sb = new StringBuffer();
    } 
    return sb.toString();
  }
  
  public static final byte[] decode(String encodedString) {
    byte[] b = new byte[encodedString.length() / 2];
    try {
      int iter = encodedString.length() / 2;
      for (int i = 0; i < iter; i++) {
        int thisCnt = i * 2;
        char c = encodedString.charAt(thisCnt);
        char cnext = encodedString.charAt(thisCnt + 1);
        int highfour = Character.digit(c, 16);
        int lowfour = Character.digit(cnext, 16);
        highfour <<= 4;
        int k = highfour ^ lowfour;
        b[i] = (new Integer(k)).byteValue();
      } 
    } catch (Exception e) {
      return null;
    } 
    return b;
  }
}
