package com.promise.demo.util.file;


import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leiwei on 2020/4/17 11:41
 */
@Slf4j
public class HdfsUtil {

    public static final String USER = "hdfs";
    public static final String PRINCIPAL = "";
    //public static final String USER_KEY = "cona@MLAMP.CN";
    public static final String KEY_TAB_PATH = "C:\\Users\\Promise\\Desktop\\cona.keytab";
    public static final String KRB5_PATH = "C:\\Users\\Promise\\Desktop\\krb5.conf";
    public static final String HDFS_SITE_PATH  = "C:\\Users\\Promise\\Desktop\\test\\hdfs-site.xml";
    public static final String CORE_SITE_PATH  = "C:\\Users\\Promise\\Desktop\\test\\core-site.xml";

    //private final static ConfigurationBean config= DBeans.getConfigurationBean();
    //public static final String USER = config.getProperty("hdfs.user");
    //public static final String PRINCIPAL = config.getProperty("hdfs.principal");
    ////public static final String USER_KEY = "cona@MLAMP.CN";
    //public static final String KEY_TAB_PATH = config.getProperty("hdfs.keytab");//"C:\\Users\\Promise\\Desktop\\cona.keytab";
    //public static final String KRB5_PATH = config.getProperty("hdfs.krbFile");//"C:\\Users\\Promise\\Desktop\\krb5.conf";
    ////public static final String CORE_SITE_PATH  = "C:\\Users\\Promise\\Desktop\\core-site.xml";*/
    //public static final String CORE_SITE_PATH  = config.getProperty("core.site.path");//"C:\\Users\\Promise\\Desktop\\test\\core-site.xml";
    //public static final String HDFS_SITE_PATH  = config.getProperty("hdfs.site.path");

    public static Configuration conf = new Configuration();

    private static HdfsUtil hdfsUtil;

    private String basePath;

    private  FileSystem fs;

    public  void init() {
        conf.addResource(new Path( HDFS_SITE_PATH));
        conf.addResource(new Path( CORE_SITE_PATH));
        if(!StringUtils.isEmpty(PRINCIPAL)){
            System.setProperty("java.security.krb5.conf", KRB5_PATH);
            conf.set("hadoop.security.authentication", "kerberos");
            try {

                UserGroupInformation.setConfiguration(conf);
                UserGroupInformation.loginUserFromKeytab(PRINCIPAL, KEY_TAB_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.setProperty("HADOOP_USER_NAME",USER);
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static HdfsUtil getInstance(){
        if(hdfsUtil==null){
            hdfsUtil=new HdfsUtil();
        }
        return hdfsUtil;
    }

    private synchronized FileSystem getFs(){
        return this.fs;
    }

    public synchronized void closeResource(){
        if (fs != null) {
            try {
                fs.close();
            } catch (IOException e) {
                log.error("关闭hdfs连接失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description:  获取文件名及路径
     * @param directory:源文件夹
     * @param isRecursive:是否迭代，默认true
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public Pair<Map<String,String>,List<HdfsDir>> listFileAndPath(String directory, Boolean isRecursive){
        Map<String,String> mapFile=new HashMap<>();
        List<HdfsDir> hdfsDirList=new ArrayList<>();
        if(isRecursive==null)isRecursive=true;
        try {
            if(directory.lastIndexOf("/")!=directory.length()-1){
                directory=directory+"/";
                if(basePath==null){
                    basePath=directory;
                }
            }
            listFiles(directory,mapFile,hdfsDirList,isRecursive);
            //清除没有文件的文件夹
            hdfsDirList=checkDir(hdfsDirList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(mapFile,hdfsDirList);
    }

    private List<HdfsDir> checkDir(List<HdfsDir> hdfsDirList) throws IOException {
        Map<Integer, List<HdfsDir>> collect = hdfsDirList.stream().collect(Collectors.groupingBy(HdfsDir::getRank));
        //按目录层级倒序排：4,3,2,1,0
        List<Integer> rankSort = collect.keySet().stream().sorted((o1, o2) -> o2-o1).collect(Collectors.toList());

        Map<Integer, List<HdfsDir>> result=new HashMap<>();
        for (Integer integer : rankSort) {
            //获取相应的目录
            List<HdfsDir> hdfsDirs = collect.get(integer);
            List<HdfsDir> resultList=new ArrayList<>();
            //遍历，检查是不是有文件
            for (HdfsDir hdfsDir : hdfsDirs) {
                FileStatus[] fileStatuses= fs.listStatus(new Path(hdfsDir.getPath()));
                if(fileStatuses.length>0){
                    //先检查是不是有子文件夹
                    List<HdfsDir> dirs = result.get(integer + 1);
                    //没子文件夹，但是文件数目大于0，说明有文件
                    if(dirs==null){
                        //添加到有效文件夹集合（fileStatuses.length>0）
                        resultList.add(hdfsDir);
                    }else {
                        //排除同级目录的子文件夹
                        List<HdfsDir> dirs1 = dirs.stream().filter(hdfsDir1 -> hdfsDir.getPDirName().equals(hdfsDir.getDirName())).collect(Collectors.toList());
                        if(dirs1.size()==0){
                            resultList.add(hdfsDir);
                            continue;
                        }
                        //有子文件夹，遍历看一下子文件夹是否被移除
                        for (FileStatus fileStatus : fileStatuses) {
                            //文件夹下有子文件夹，检查
                            if(fileStatus.isDirectory()){
                                String dirName = fileStatus.getPath().getName();
                                String pDirName = fileStatus.getPath().getParent().getName();
                                //比对已添加的低层级目录，检查是否有效
                                for (HdfsDir dir : dirs) {
                                    if(dir.getDirName().equals(dirName)&&dir.getPDirName().equals(pDirName)){
                                        resultList.add(hdfsDir);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            result.put(integer,resultList);
        }

        return result.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

    }

    /**
     * @Description:  上传文件到hdsf
     * @param file: 文件
     * @param targetPath: 目标路径
     * @return void
     */
    public void uploadHdfsFile(File file, String targetPath) throws Exception {
        fs.copyFromLocalFile(new Path(file.getAbsolutePath()),new Path(targetPath));
    }

    /**
     * @Description:  下载文件
     * @param filePath: 文件
     * @return void
     */
    public void downHdfsFile(String filePath,  HttpServletResponse response) throws Exception {
        FSDataInputStream open = fs.open(new Path(filePath));
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copyBytes(open,outputStream,1024,true);
    }

    /**
     * @Description:  测试是否有当前目录/文件权限
     * @param filePath: 文件/目录
     * @return boolean
     */
    public Boolean testHdfsAuth(String filePath) {
        boolean hasAuth=true;
        try {
            fs.open(new Path(filePath) );
        } catch (IOException e) {
           hasAuth=false;
        }
        return hasAuth;
    }

    private void listFiles(String directory,Map<String,String> mapFile,List<HdfsDir> hdfsDirList,Boolean isRecursive) throws IOException {

        FileStatus[] fileStatuses;
        try {
            fileStatuses= fs.listStatus(new Path(directory));
        }catch (AccessControlException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("输入路径权限不足，请检查：PATH："+directory);
        }
        //添加目录信息(有子文件或者子文件夹的才添加)
        getDir(hdfsDirList, fileStatuses);
        //文件
        for (FileStatus file : fileStatuses) {
            if(!(file.getGroup().equals(USER)||file.getOwner().equals(USER))){
                log.warn("当前用户无此文件权限：USER:"+USER+";dir:"+file.getPath().getName());
                continue;
            }
            if(file.isDirectory()&&isRecursive){
                log.info(file.getPath().getName()+"Is a folder, iterate to fetch files");
                listFiles(file.getPath().toUri().getPath(),mapFile,hdfsDirList,isRecursive);
            }
            if(file.isFile()){
                Date date = new Date(file.getModificationTime());
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                log.info(file.getPath().getName()+"====="+sd.format(date));
                mapFile.put(file.getPath().getName()+"##"+file.getPath().toUri().getPath(),file.getPath().toUri().getPath());
            }
        }

    }

    private void getDir(List<HdfsDir> hdfsDirList, FileStatus[] fileStatuses) {

        if(fileStatuses.length>=1){
            FileStatus fileStatus = fileStatuses[0];
            HdfsDir hdfsDir = new HdfsDir();
            hdfsDir.setDirName(fileStatus.getPath().getParent().getName());
            hdfsDir.setPath(fileStatus.getPath().getParent().toUri().getPath());
            hdfsDir.setPDirName(fileStatus.getPath().getParent().getParent().getName());
            String path = fileStatus.getPath().getParent().toUri().getPath();
            if(hdfsDirList.size()==0){
                //根目录需要加一个“/”
                path=path+"/";
            }
            String subPath= path.replace(basePath, "").trim();
            log.info("subPath:"+subPath);
            int rank = subPath.split("/").length;
            if(StringUtils.isEmpty(subPath)){
                rank=0;
            }
            log.info("rank:"+rank);
            hdfsDir.setRank(rank);
            hdfsDirList.add(hdfsDir);
        }
    }

}
