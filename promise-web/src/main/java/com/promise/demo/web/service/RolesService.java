package com.promise.demo.web.service;

import com.promise.demo.db.model.Roles;
import com.promise.demo.util.PageBean;

/**
* Created by leiwei on 2019-6-18.
*/
public interface RolesService {

    int insert(Roles roles);

    int update(Roles roles);

    PageBean roles(String keyword, Integer pageNum, Integer pageSize);

    boolean deleteRole(Integer roleId);
}