package com.promise.demo.util.file;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Created by leiwei on 2020/4/10 11:01
 */
@Slf4j
public class FTPManager {


    public static void main(String[] args) throws Exception {
      /*  FTPUtil ftpUtil = FTPUtil.createFtpCli("ws03.mlamp.cn", "program1", "123456", null);
        ftpUtil.connect();

        log.info(ftpUtil.printWorkingDirectory());

        log.info(ftpUtil.isConnected()+"");


        Map<String, String> map = ftpUtil.listFileName("/CONA");

        map.forEach((path,name)-> log.info(path+"============="+name));*/

        /*SSHUtil sshUtil=new SSHUtil("172.21.1.148",22,"root","mlamp123456");

        List<String> list = sshUtil.listFiles("/data/cona/config");
        for (String s : list) {
            log.info(s);
        }
        sshUtil.openResource();
        sshUtil.getFileFromSftpServer("/data/cona/config","C:\\Users\\Promise\\Desktop\\test");
        Map<String, String> map = sshUtil.listFileAndPath("/data/cona/config");
        map.forEach((name,path)-> log.info(name+"============="+path));
        sshUtil.putFileToSftpServer("/data/cona/test","C:\\Users\\Promise\\Desktop\\test");

        sshUtil.closeResource();*/


        HdfsUtil instance = HdfsUtil.getInstance();
        instance.init();

        //FileSystem fs = instance.getFs();

        /*FileStatus[] fileStatuses = new FileStatus[0];
        try {
            fileStatuses =fs.listStatus(new Path("/cona/"));
        } catch (AccessControlException e) {
            log.error("==========权限不足===================");
        }
        System.out.println("==========================================================");
        for (FileStatus f : fileStatuses) {
            log.info( f.getPath().getName()+"===="+ f.getGroup()+"==="+f.getPath().toUri().getPath());
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(), f.isDirectory(), f.getLen());
        }*/

        Pair<Map<String, String>, List<HdfsDir>> pair = instance.listFileAndPath("/cona3_lw", true);
        pair.getKey().forEach((name,path)-> log.info(name+"============="+path));

        pair.getValue().forEach(hdfsDir-> log.info(hdfsDir.toString()));



        instance.closeResource();




    }
}
