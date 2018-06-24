package com.dony15.shop.utils;

/**
 * @author DonY15
 * @description
 * @create 2018\6\19 0019
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtils {
    public static void main(String[] args) {
        System.out.println(md5("123456").length());
        System.out.println(sha1("123456").length());
    }


    private static final char [] hexDigits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static String md5(String inStr){
        //字节数组
        byte [] inStrBytes=inStr.getBytes();

        try {
            //摘要算法对象，它代表了md5算法
            MessageDigest messageDigest= MessageDigest.getInstance("MD5");
            //我们给它一个被计算的值，它就给我们一个计算的结果
            messageDigest.update(inStrBytes);
            byte [] mdByte=messageDigest.digest();
            //FA1B36987ACD512E52A2B……
            //我们有0101，目的是十六进制数
            //二进制转化十六进制
            //0110 1001
            //6 9
            //设置一个字符数组接收转化的结果
            char [] str=new char[mdByte.length*2];
            int k=0;
            //转化
            for (int i=0;i<mdByte.length;i++){
                byte temp=mdByte[i];
                //0110 1001
                //xxxx 0110
                //0000 1111
                //0000 0110

                str[k++]=hexDigits[temp>>>4&0xf];
                str[k++]=hexDigits[temp&0xf];
            }
            return new String(str);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String sha1(String inStr){
        //字节数组，也就是0101
        byte [] inStrBytes=inStr.getBytes();

        try {
            //摘要算法对象，它代表了md5算法
            MessageDigest messageDigest= MessageDigest.getInstance("SHA-1");
            //我们给它一个被计算的值，它就给我们一个计算的结果
            messageDigest.update(inStrBytes);
            byte [] mdByte=messageDigest.digest();
            //FA1B36987ACD512E52A2B……
            //我们有0101，目的是十六进制数
            //二进制转化十六进制
            //0110 1001
            //6 9
            //设置一个字符数组接收转化的结果
            char [] str=new char[mdByte.length*2];
            int k=0;
            //转化
            for (int i=0;i<mdByte.length;i++){
                byte temp=mdByte[i];
                //0110 1001
                //xxxx 0110
                //0000 1111
                //0000 0110

                str[k++]=hexDigits[temp>>>4&0xf];
                str[k++]=hexDigits[temp&0xf];
            }
            return new String(str);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
