package com.promise.demo.db.dao;

import com.promise.demo.db.model.FileShare;
import com.promise.demo.db.model.FileSharePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by leiwei on 2020/6/1 15:29
 */
@Mapper
@Repository
public interface FileShareDao {

    int insert(FileShare fileShare);

    int deleteByIds(@Param("ids") Integer[] ids);

    FileSharePO findAttachment(@Param("id") Integer id);


}
