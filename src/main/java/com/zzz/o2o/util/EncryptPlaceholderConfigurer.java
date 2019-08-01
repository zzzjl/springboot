package com.zzz.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //需要加密的数据
    private String[] encryptPropNames = {"jdbc.username","jdbc.password"};
    /*
    * 对关键的属性进行转换*/
    protected String convertProperty(String propertyName,String propertyValue){
        if(isEncryptProp(propertyName)){
            //对加密字段进行解密
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            return decryptValue ;
        }else{
            return propertyValue ;
        }
    }
    private boolean isEncryptProp(String propertyName){
        for(String encryptpropertyName:encryptPropNames) {
            if (encryptpropertyName.equals(propertyName)){
                return true;
            }
        }return false;
    }
}
