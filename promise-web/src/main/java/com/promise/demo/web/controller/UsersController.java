package com.promise.demo.web.controller;


import com.promise.demo.db.model.Users;
import com.promise.demo.util.PageBean;
import com.promise.demo.util.Request;
import com.promise.demo.util.Response;
import com.promise.demo.web.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
* Created by leiwei on 2019-6-18.
*/
@Slf4j
@RestController
@Api(tags = "用户管理")
@RequestMapping(value = "/users",produces = "application/json;charset=UTF-8")
public class UsersController{

    @Autowired
    UsersService usersService;

    @ApiOperation("新建用户")
    @PostMapping("/add")
    public Response addUser(@RequestBody Request<Users> request){
        Users data = request.getData();
        int insert = usersService.insert(data);
        boolean success=false;
        if(insert==1){
            success=true;
        }
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"插入成功":"插入失败");
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public Response updateUser(@RequestBody Request<Users> request){
        Users data = request.getData();
        int insert = usersService.update(data);
        boolean success=false;
        if(insert==1){
            success=true;
        }
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"更新成功":"更新失败");

    }

    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public Response getUsers(@RequestParam(value = "keyword", required = false) String keyword,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize){

        PageBean pageBean = usersService.users(keyword, pageNum, pageSize);
        return Response.of(pageBean, HttpStatus.OK.value(),"获取成功");

    }


    @ApiOperation("删除报警联系人")
    @DeleteMapping("/deleteWhiteList")
    public Response deleteUser(@RequestParam(name = "userId") Integer userId){
        boolean success=usersService.deleteUser(userId);
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"删除成功！":"删除失败！");
    }


}
