package com.mmall.service.impl;

import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author SR
 * @date 2017/11/23
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    /**
     * 上传附件
     *
     * @param file
     * @param path
     * @return
     */
    @Override
    public String upload(MultipartFile file, String path) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //获取新文件名
        String uploadName = UUID.randomUUID() + "." + fileExtensionName;

        log.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadName);

        //根据路径创建文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            //设置写的权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        //目标文件路径
        File targetFile = new File(path, uploadName);

        try {
            //开始上传文件
            file.transferTo(targetFile);
            //上传附件到ftp
            FTPUtil.uploadFile(Arrays.asList(targetFile));
            //删除文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常", e);
        }

        return targetFile.getName();
    }
}
