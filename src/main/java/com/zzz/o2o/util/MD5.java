package com.zzz.o2o.util;

import com.zzz.o2o.exceptions.LocalAuthOperationException;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

public class MD5 {
    public static String getEncoderByMd5(String str)  {
        //确定计算方法
        MessageDigest md5 =null;
        String newstr =null;
        try {
             md5 = MessageDigest.getInstance("MD5");

        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
            newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        }catch(Exception e){
            throw new LocalAuthOperationException("密码加密失败："+e.getMessage());
        }
        return newstr;
    }

    public static void main(String[] args) {
        String password = "123";
        String encode;

        encode = getEncoderByMd5(password);

        System.out.println(encode);

    }
}
