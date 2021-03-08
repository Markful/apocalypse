package com.vzoom.apocalypse.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wans
 */
public class Md5Util {

    public static String getMd5(String str){

        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            return new BigInteger(1, bytes).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
