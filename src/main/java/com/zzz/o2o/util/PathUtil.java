package com.zzz.o2o.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath="";
        if(os.toLowerCase().startsWith("win")){
            basePath="I:/zzz/pic/";
        }else{
            basePath="home/wowowo/img/";
        }
        basePath =basePath.replace("/",separator);
        return basePath;
    }
    public static String getShopImgPath(long shopId){
        String imgPath ="I:/zzz/pic/topic/"+shopId+"/";
        return imgPath;//imgPath.replace("/",separator);
    }
}
