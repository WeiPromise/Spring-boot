package com.promise.demo.web.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by leiwei on 2020/6/1 15:56
 */
public interface FileShareService {

    int uploadFile(MultipartFile file, String fileInfoName);

    boolean downFile(HttpServletResponse response, String fileIds);

    boolean deleteFile(List<Integer> fileIds);
}
