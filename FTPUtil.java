package com.travel.utils;

/**
 * @author DonY15
 * @description
 * FTP上传服务器工具封装
 * IMAGE_SERVER_URL=http://www.dony15.com:8888/
 *
 * FTP_SERVER=www.dony15.com
 * FTP_PATH=/home/ftpuser/
 * FTP_USER=ftpuser
 * FTP_PASSWORD=ftpuser
 *
 * TEMP_FILE_DIR=F:/page/
 * 配合配置文件使用更方便
 *
 * @create 2018\7\11 0011
 */
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FTPUtil {
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = "UTF-8";

    public static boolean uploadFile(String url, int port, String username,
                                     String password, String path, String filename, InputStream input) {
        boolean result = false;
        try {
            int reply;
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            //ftpClient.connect(url);
            ftpClient.connect(url, port);// 连接FTP服务器
            // 登录
            ftpClient.login(username, password);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("连接失败");
                ftpClient.disconnect();
                return result;
            }

            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding), "iso-8859-1"), input);
                if (result) {
                    System.out.println("上传成功!");
                }
            }
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
