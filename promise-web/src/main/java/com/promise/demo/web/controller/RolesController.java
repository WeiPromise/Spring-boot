package com.promise.demo.web.controller;


import com.promise.demo.db.model.Roles;
import com.promise.demo.util.PageBean;
import com.promise.demo.util.Request;
import com.promise.demo.util.Response;
import com.promise.demo.web.service.RolesService;
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
@Api(tags = "角色管理")
@RequestMapping(value = "/roles",produces = "application/json;charset=UTF-8")
public class RolesController{

    @Autowired
    RolesService rolesService;

    @ApiOperation("新建角色")
    @PostMapping("/add")
    public Response addRole(@RequestBody Request<Roles> request){
        Roles data = request.getData();
        int insert = rolesService.insert(data);
        boolean success=false;
        if(insert==1){
            success=true;
        }
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"插入成功":"插入失败");
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public Response updateRole(@RequestBody Request<Roles> request){
        Roles data = request.getData();
        int insert = rolesService.update(data);
        boolean success=false;
        if(insert==1){
            success=true;
        }
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"更新成功":"更新失败");

    }

    @ApiOperation("获取用户列表")
    @GetMapping("/roles")
    public Response getRole(@RequestParam(value = "keyword", required = false) String keyword,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize){

        PageBean pageBean = rolesService.roles(keyword, pageNum, pageSize);
        return Response.of(pageBean, HttpStatus.OK.value(),"获取成功");

    }


    @ApiOperation("删除报警联系人")
    @DeleteMapping("/deleteRole")
    public Response deleteRole(@RequestParam(name = "roleId") Integer roleId){
        boolean success=rolesService.deleteRole(roleId);
        return Response.of(success ? HttpStatus.OK.value() : HttpStatus.NOT_MODIFIED.value(),success?"删除成功！":"删除失败！");
    }
}
