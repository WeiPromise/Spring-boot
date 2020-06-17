package com.promise.demo.web.service.impl;


import com.promise.demo.db.dao.RolesDao;
import com.promise.demo.db.model.Roles;
import com.promise.demo.util.ClassUtil;
import com.promise.demo.util.PageBean;
import com.promise.demo.web.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Created by leiwei on 2019-6-18.
*/
@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    RolesDao rolesDao;


    @Override
    public int insert(Roles roles) {
        return 0;
    }

    @Override
    public int update(Roles roles) {
        return 0;
    }

    @Override
    public PageBean roles(String keyword, Integer pageNum, Integer pageSize) {

        List<Roles> list = rolesDao.list(new Roles());

        if(keyword!=null){
            list=ClassUtil.of().search(keyword,list);
        }
        return PageBean.ofFull(pageNum, pageSize, list);
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        return false;
    }
}