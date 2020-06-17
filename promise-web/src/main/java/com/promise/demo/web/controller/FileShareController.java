package com.promise.demo.web.controller;


import com.promise.demo.util.Request;
import com.promise.demo.util.Response;
import com.promise.demo.web.service.FileShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
* Created by leiwei on 2019-6-18.
*/

@Slf4j
@RestController
@Api(tags = "文件管理")
@RequestMapping(value = "/fileShare",produces = "application/json;charset=UTF-8")
public class FileShareController {

    @Autowired
    FileShareService fileShareService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Response uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "fileInfoName") String fileInfoName){

        int insert = fileShareService.uploadFile(file,fileInfoName);
        boolean success=false;
        if(insert==1){
            success=true;
        }
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"插入成功":"插入失败");
    }


    @ApiOperation("下载文件")
    @GetMapping(value = "/down")
    public Response downFile(HttpServletResponse response, @RequestParam(value = "fileIds") String fileIds){

        boolean isdown = fileShareService.downFile(response,fileIds);
        return Response.of(isdown ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),isdown?"下载成功":"下载失败");

    }


    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public Response deleteFile(@RequestBody Request<List<Integer>> request){
        List<Integer> fileIds = request.getData();
        if (fileIds == null) {
            throw new IllegalArgumentException("不合法的请求格式");
        }
        boolean success=fileShareService.deleteFile(fileIds);
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"删除成功！":"删除失败！");
    }
}
