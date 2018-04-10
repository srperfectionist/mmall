package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author SR
 * @date 2017/11/23
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getPropertyToString("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getPropertyToString("ftp.user");
    private static String ftpPass = PropertiesUtil.getPropertyToString("ftp.pass");
    private static String ftpPort = PropertiesUtil.getPropertyToString("ftp.port");

    private String ip;
    private String port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil() {
    }

    public FTPUtil(String ip, String port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 上传附件
     *
     * @param fileList
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, ftpPort, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器，上传结束，上传结果{}",result);
        return result;
    }

    /**
     * 上传附件
     *
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean upload = connectFtp(this.ip, this.user, this.pwd);
        FileInputStream fileInputStream = null;
        if (upload) {
            try {
                //更改路径
                ftpClient.changeWorkingDirectory(remotePath);
                //设置缓冲区
                ftpClient.setBufferSize(1024);
                //设置字符集
                ftpClient.setControlEncoding("UTF-8");
                //设置编码格式
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //开启被动模式
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fileInputStream = new FileInputStream(file);
                    //存储图片
                    ftpClient.storeFile(file.getName(), fileInputStream);
                }
            } catch (IOException e) {
                upload = false;
                logger.error("文件上传异常", e);
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return upload;
    }

    /**
     * 连接FTP
     *
     * @param ip
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectFtp(String ip, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("FTP连接异常", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
