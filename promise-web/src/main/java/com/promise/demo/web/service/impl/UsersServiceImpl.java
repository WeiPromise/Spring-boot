package com.promise.demo.web.service.impl;


import com.promise.demo.db.dao.UsersDao;
import com.promise.demo.db.model.Users;
import com.promise.demo.util.ClassUtil;
import com.promise.demo.util.PageBean;
import com.promise.demo.web.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Created by leiwei on 2019-6-18.
*/
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersDao usersDao;


    @Override
    public int insert(Users users) {
        return 0;
    }

    @Override
    public int update(Users users) {
        return 0;
    }

    @Override
    public PageBean users(String keyword, Integer pageNum, Integer pageSize) {
        List<Users> list = usersDao.list(new Users());
        if(keyword!=null){
            list= ClassUtil.of().search(keyword,list);
        }
        return PageBean.ofFull(pageNum, pageSize, list);
    }
    @Override
    public boolean deleteUser(Integer userId) {
        return false;
    }

}