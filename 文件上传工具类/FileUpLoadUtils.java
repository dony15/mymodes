package com.dony15.shop.utils;

import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author DonY15
 * @description 文件上传工具类
 *
 * @method public static String fileUpLoad(HttpServletRequest req,MultipartFile file,Boolean b,String  rootDirectory)
 *    上传方法,返回字符串 图片src虚拟路径/error
 * @属性  IMAGE_PATH 保存磁盘路径
 *
 * @create 2017\12\20 0020
 */
public class FileUpLoadUtils {
    /**
     * 默认保存路径
     */
    public static final String IMAGE_PATH="e:"+ File.separator+"apache-tomcat-9.0.1"+File.separator+"images";

    /**
     *
     * @param req			固定HttpServletRequest请求
     * @param file			上传io文件
     * @param b	true		打开水印功能/false关闭水印功能(默认)
     * @param rootDirectory tomcat定义的逻辑路径,如 /imageswarehouse
     * @return 字符串		图片src虚拟路径/error
     * @throws IOException
     */
    public static String fileUpLoad(HttpServletRequest req,MultipartFile file,Boolean b,String  rootDirectory
    ) throws IOException {
        //获取文件名
        String reqFileName=file.getOriginalFilename();
        String endFileName=reqFileName.substring(reqFileName.lastIndexOf("."));
        /**
         * ----------------------------------------------------------------------------------------
         * 防止木马入侵
         * 可根据情况增减格式
         * ----------------------------------------------------------------------------------------
         */

        if (".png".equalsIgnoreCase(endFileName)||
                ".jpg".equalsIgnoreCase(endFileName)||
                ".jpeg".equalsIgnoreCase(endFileName)||
                ".bmp".equalsIgnoreCase(endFileName)
                ) {
            //UUID去重,防止覆盖
            String uuid=UUID.randomUUID().toString().replaceAll("-", "");
            //原图名和水印处理图
            String newFileName =  uuid+endFileName;

            //保存位置
            File fileAddress = new File(IMAGE_PATH+File.separator+newFileName);
            //保存文件
            try {
                file.transferTo(fileAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * 水印功能:默认false关闭
             */
            if (b==true){
                newFileName =  uuid+"wm"+endFileName;
                saveWaterMark(req, fileAddress, uuid,endFileName);
            }
            System.out.println("上传完毕");
            return rootDirectory+File.separator+newFileName;
        }
        return "error";
    }

    //添加水印
    private static String saveWaterMark(HttpServletRequest req,File file,String uuid,String endFileName) throws IOException {
        //获取waterMark
        String waterMark = req.getSession().getServletContext().getRealPath("WEB-INF" + File.separator + "watermark.png");
        //获取原图和水印图形

        BufferedImage waterImage = ImageIO.read(new FileInputStream(waterMark));
        BufferedImage fileImage = ImageIO.read(new FileInputStream(file));
        //画图
        Graphics graphics = fileImage.getGraphics();
        graphics.drawImage(waterImage,
                fileImage.getWidth()/2-waterImage.getWidth()/2,
                fileImage.getHeight()-waterImage.getHeight(),null);
        File waterFile = new File(IMAGE_PATH + File.separator +uuid+"wm"+endFileName);
        if (!waterFile.exists()){
            waterFile.createNewFile();
        }
        ImageIO.write(fileImage,"jpg",waterFile);
        return waterFile.getAbsolutePath();
    }
}
