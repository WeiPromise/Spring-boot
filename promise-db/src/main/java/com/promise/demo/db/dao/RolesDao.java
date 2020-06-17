package com.promise.demo.db.dao;

import com.promise.demo.db.model.Roles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Created by leiwei on 2019-6-18.
*/
@Mapper
@Repository
public interface RolesDao {

    int insert(Roles roles);

    int delete(Roles roles);

    int deleteById(@Param("id") Integer id);

    int deleteByIds(@Param("ids") Integer[] ids);

    int update(Roles roles);

    Roles getById(@Param("id") Integer id);

    List<Roles> list(Roles roles);
}