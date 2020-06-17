package com.promise.demo.web.service;

import com.promise.demo.db.model.Users;
import com.promise.demo.util.PageBean;

/**
* Created by leiwei on 2019-6-18.
*/
public interface UsersService {

    int insert(Users users);

    int update(Users users);

    PageBean users(String keyword, Integer pageNum, Integer pageSize);

    boolean deleteUser(Integer userId);
}