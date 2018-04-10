package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author SR
 * @date 2017/11/23
 */
public interface IFileService {
    /**
     * 上传附件
     *
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);
}
