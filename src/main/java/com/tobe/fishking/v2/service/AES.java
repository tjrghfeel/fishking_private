package com.tobe.fishking.v2.service;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES {
    public static String aesEncode(String str, String key)
            throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);//배열 복사. 인자는 (복사할배열,복사시작지점,붙여넣기배열,붙여넣기시작점,복사할길이)
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");//암호화를 해주는 Cipher객체를 설정과함께 생성.
        c.init(1, (Key)keySpec, new IvParameterSpec(iv.getBytes()));//어떤작업을 할지, 암호화키 등을 초기화.
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));//주어진 문자열을 암호화. (바이트로 변환해서)
        String enStr = new String(Base64.encodeBase64((byte[])encrypted));//암호화된 바이트를 String으로 변환.
        return enStr;
    }
    public static String aesDecode(String str, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(2, (Key)keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
        byte[] byteStr = Base64.decodeBase64((byte[])str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }


}
