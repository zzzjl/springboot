package com.zzz.o2o.util;

import com.zzz.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
//    Logger logger = (Logger) LoggerFactory.getLogger(ImageUtil.class);
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr ) {
        String realFileName = getRandomFileName();
        String extension =thumbnail.getImageName().substring(thumbnail.getImageName().lastIndexOf("."));

        String relativeAddr = targetAddr + realFileName + extension;
        makeDirPath(targetAddr);

        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200) .watermark(Positions.BOTTOM_RIGHT,
                    ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f).toFile(relativeAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("水印图片添加成功，保存路径："+relativeAddr);//targetAddr);
        //Logger.logMsg(Logger.INFO,"水印图片添加成功，保存路径："+targetAddr);
        return relativeAddr;
    }

    /*
    * 生成随机文件名  当前时间+5位随机数
    * */
    public static String getRandomFileName() {
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        String fileName = nowTimeStr + rannum;
        return fileName;
    }

    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = targetAddr;
        //System.out.println(realFileParentPath);
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /*获取拓展名
    * */
    private static String getFileExtension(File cFile) {
        String oriFileName = cFile.getName();
        return oriFileName.substring(oriFileName.lastIndexOf("."));
    }
    /*
    * 删除文件或路径
    * 如果storePath是文件路径则删除该文件
    * 如果storePath是目录路径则删除目录下所有文件
    * */
    public static void deleteFileOrPath(String storePath){
        //File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
        File fileOrPath=new File(storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

/*    public static void main(String[] args) throws IOException {

        File img = new File("I:/zzz/pic/haha/");
        img.delete();
       *//* File files[] = img.listFiles();
        for(int i=0;i<files.length;i++){
            System.out.println(files[i]);
            files[i].delete();
        }*//*
       // String aa= getFileExtension(img);
       // System.out.println(aa);
       // String dest = PathUtil.getShopImgPath(3);
       // String s =generateThumbnail(img,dest);
       // System.out.println(s);
    }*/
}
      // String fileExtension = getFileExtension(new File("I:/zzz/pic/starbucks.jpg"));
       // System.out.println(fileExtension);
      // Thumbnails.of(new File ("I:/zzz/pic/starbucks.jpg")).size(200,200).watermark(Positions.BOTTOM_RIGHT,
            //    ImageIO.read(new File(basePath+"/watermark.jpg")),0.5f).toFile("I:/zzz/pic/topic/starbucks2.jpg");


