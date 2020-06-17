package com.promise.demo.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leiwei on 2019/6/18 19:39
 * 通用工具
 */
public class ClassUtil {


    public  <T> List<T> search(String keyword,List<T> list){
        return list.stream().filter(bean -> {
            boolean flag = false;
            try {
                for (Field field : bean.getClass().getDeclaredFields()) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
                    Method readMethod = pd.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    if((""+result).contains(keyword)){
                        flag=true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }).collect(Collectors.toList());
    }

    public static ClassUtil of(){
        return new ClassUtil();
    }
}
