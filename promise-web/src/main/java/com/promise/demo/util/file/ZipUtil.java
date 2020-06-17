package com.promise.demo.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by leiwei on 2020/4/23 18:51
 */
public class ZipUtil {
    private static final int  BUFFER_SIZE = 2 * 1024;
    public static void fileListToZip(List<File> srcFiles , OutputStream out) throws IOException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;

        try {

            zos = new ZipOutputStream(out);

            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                // 防止文件重名导致压缩失败
                String fileName = srcFile.getName();
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                String newFileName = fileName.substring(0, fileName.length()-prefix.length()) + "_" + UUID.randomUUID().toString().replace("-", "") + prefix;
                zos.putNextEntry(new ZipEntry(newFileName));

                int len;

                FileInputStream in = new FileInputStream(srcFile);

                while ((len = in.read(buf)) != -1){

                    zos.write(buf, 0, len);

                }

                zos.closeEntry();

                in.close();

            }

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) +" ms");

        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            if(zos != null){

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }
    }

}

