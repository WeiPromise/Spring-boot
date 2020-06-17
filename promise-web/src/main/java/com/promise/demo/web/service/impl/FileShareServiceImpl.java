package com.promise.demo.web.service.impl;

import com.promise.demo.db.dao.FileShareDao;
import com.promise.demo.db.model.FileShare;
import com.promise.demo.db.model.FileSharePO;
import com.promise.demo.web.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by leiwei on 2020/6/1 15:56
 */
@Service
@Slf4j
public class FileShareServiceImpl implements FileShareService {

    @Autowired
    private FileShareDao fileShareDao;

    @Override
    public int uploadFile(MultipartFile file, String fileInfoName) {
        int insert=0;
        try {
            String filename = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            FileShare fileShare = new FileShare();
            fileShare.setFile(bytes);
            fileShare.setFileName(filename);
            insert=fileShareDao.insert(fileShare);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    public boolean downFile(HttpServletResponse response, String fileIds) {

        String[] fileIdList = fileIds.split(",");

        for (String id : fileIdList) {
            FileSharePO attachment = fileShareDao.findAttachment(Integer.parseInt(id));

            try {
                byte[] files = (byte[]) attachment.getFile();

                log.info(attachment.getFileName());

                String fileName = java.net.URLEncoder.encode(attachment.getFileName(), "UTF-8");

                fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);

                log.info("fileName: "+fileName);

                response.setHeader("Content-Disposition", "attachment;fileName=" +fileName);

                byte[] buffer = new byte[1024];

                InputStream fis = new ByteArrayInputStream(files);

                OutputStream os = response.getOutputStream();

                BufferedInputStream bis;

                bis = new BufferedInputStream(fis);

                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
                bis.close();
                fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean deleteFile(List<Integer> fileIds) {
        int delete = fileShareDao.deleteByIds(fileIds.toArray(new Integer[0]));
        return delete==fileIds.size();
    }
}
