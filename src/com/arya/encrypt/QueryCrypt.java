package com.arya.encrypt;



import com.arya.encrypt.module.QueryCryptProperties;
import com.arya.encrypt.module.QueryCryptUser;
import com.arya.encrypt.module.RequestParameterObject;
import com.arya.encrypt.utils.HexConvert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

public final class QueryCrypt {
    private static String algorithm = "DESede";

    private static final QueryCrypt instance = new QueryCrypt();

    private static HashMap userSessionMap = new HashMap();

    private static QueryCryptProperties initQCP = null;

    public static QueryCrypt getInstance() {
        return instance;
    }

    public static void init(QueryCryptProperties qcp) {
        initQCP = qcp;
        if (initQCP.isDebugEnabled())
            System.out.println("Successfully initialized QueryCrypt by Aveda Technology, Inc.");
    }

    public static String encrypt(HttpServletRequest request, String message) {
        if (initQCP.isDebugEnabled()) {
            System.out.println("======== BEGIN ENCRYPTION ===============");
            System.out.println("encrypt() : Plain String : " + message);
        }
        String encodedOutput = "";
        String sessionID = request.getSession().getId();
        SecretKey key = null;
        QueryCryptUser qcu = grabUserFromKeyStore(sessionID);
        key = qcu.getSecretKey();
        if (key != null)
            encodedOutput = performEncrypt(message, qcu);
        encodedOutput = String.valueOf(initQCP.getQueryName()) + "=" + encodedOutput;
        if (initQCP.isDebugEnabled()) {
            System.out.println("encrypt() : Encrypted String : " + encodedOutput);
            System.out.println("======== END ENCRYPTION ===============");
        }
        return encodedOutput;
    }

    public static RequestParameterObject decrypt(HttpServletRequest request) {
        if (initQCP.isDebugEnabled())
            System.out.println("======== BEGIN DECRYPTION ===============");
        RequestParameterObject rpo = null;
        String sessionID = request.getSession().getId();
        if (request.getParameter(initQCP.getQueryName()) != null) {
            String encryptedString = request.getParameter(initQCP.getQueryName());
            if (initQCP.isDebugEnabled())
                System.out.println("decrypt() : Encrypted String : " + encryptedString);
            SecretKey key = null;
            QueryCryptUser qcu = grabUserFromKeyStore(sessionID);
            key = qcu.getSecretKey();
            if (key != null) {
                String decodedResult = performDecrypt(encryptedString, qcu);
                rpo = new RequestParameterObject(decodedResult);
            }
        }
        if (initQCP.isDebugEnabled()) {
            System.out.println("decrypt() : Plain String : " + rpo.toString());
            System.out.println("======== END DECRYPTION ===============");
        }
        return rpo;
    }

    private static String performEncrypt(String input, QueryCryptUser qcu) {
        String result = "";
        if (initQCP.isDebugEnabled())
            System.out.println("Input - Plain Message:" + input);
        try {
            SecretKey key = qcu.getSecretKey();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(1, key);
            byte[] inputBytes = input.getBytes();
            byte[] outputBytes = cipher.doFinal(inputBytes);
            if (initQCP.isDebugEnabled())
                System.out.println("Output - Encrypted Message (Before Encoding):" + outputBytes.toString());
            String md5result = putMD5ToHash(outputBytes, key.getEncoded(), qcu);
            if (initQCP.isDebugEnabled())
                System.out.println("Output - MD5 of Encrypted Message:" + md5result);
            return md5result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    private static String performDecrypt(String md5String, QueryCryptUser qcu) {
        String out = "";
        try {
            SecretKey key = qcu.getSecretKey();
            if (initQCP.isDebugEnabled())
                System.out.println("Input - MD5 String:" + md5String);
            byte[] encryptedString = getMD5ToHash(md5String, qcu);
            if (encryptedString != null) {
                if (initQCP.isDebugEnabled())
                    System.out.println("Input - Encrypted Message (Before Decode):" + encryptedString.toString());
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(2, key);
                byte[] recoveredBytes = cipher.doFinal(encryptedString);
                String recovered = new String(recoveredBytes);
                if (initQCP.isDebugEnabled())
                    System.out.println("Output - Decrypted Message:" + recovered);
                return recovered;
            }
            return md5String;
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
    }

    private static QueryCryptUser grabUserFromKeyStore(String sessionID) {
        QueryCryptUser qcu = null;
        SecretKey b = null;
        if (initQCP.isSessionKeyEnabled()) {
            if (userSessionMap.containsKey(sessionID)) {
                qcu = (QueryCryptUser)userSessionMap.get(sessionID);
            } else {
                try {
                    b = KeyGenerator.getInstance(algorithm).generateKey();
                    qcu = new QueryCryptUser(sessionID, b);
                    userSessionMap.put(sessionID, qcu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (initQCP.isDebugEnabled())
                System.out.println("Session ID:" + qcu.getSessionID());
        }
        return qcu;
    }

    private static final byte[] getMD5ToHash(String md5, QueryCryptUser qcu) {
        if (qcu.getByKey(md5) != null)
            return qcu.getByKey(md5);
        return null;
    }

    private static final String putMD5ToHash(byte[] encryptedString, byte[] key, QueryCryptUser qcu) {
        byte[] md5str = (byte[])null;
        String md5result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(encryptedString);
            md5str = md5.digest(key);
            md5result = HexConvert.encode(md5str);
            qcu.addToMD5Map(md5result, encryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 Algorithm is not available");
            e.printStackTrace();
        }
        return md5result;
    }

    public void clearUserInfo(String sessionID) {
        userSessionMap.remove(sessionID);
        if (initQCP.isDebugEnabled())
            System.out.println("User Session:" + sessionID + " and all related query string data has been successfully removed.");
    }
}

