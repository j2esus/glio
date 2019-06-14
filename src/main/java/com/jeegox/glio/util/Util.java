package com.jeegox.glio.util;

import com.jeegox.glio.enumerators.Status;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author j2esus
 */
public class Util {
    
    public static String encodeSha256(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (byte b : sha256(cadena.getBytes())) {
            sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
        }
        return sb.toString();
    }
    
    private static byte[] sha256(byte[] data) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
            sha.update(data);
            return sha.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Date getCurrentDate(){
        return new Date();
    }
    
    public static Timestamp getCurrentCompleteDate() {
        return new Timestamp(new Date().getTime());
    }
    
    public static String getRandom(int num){
        SecureRandom random = new SecureRandom();
        return new BigInteger(num, random).toString(32);
    }
    
    public static Date stringToDate(String fecha, String formato) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
        Date date = null;
        try {
            date = simpleDateFormat.parse(fecha);
            return date;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return date;
        }
    }
    
    public static String dateToString(Date date, String formato) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
        String fecha = simpleDateFormat.format(date);
        return fecha;
    }
    
    public static String statusToString(Status[] status, String character){
        String result = "";
        for(Status item: status){
            result += "'" + item.name() + "'" + character;
        }
        return result.substring(0, result.length() -1);
    }
}
