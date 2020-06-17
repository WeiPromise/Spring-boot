package com.promise.demo.db.util;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


public class TypesUtil {

    private static final Map<Integer, String> TYPE_NAME_MAP = new HashMap<>(Types.class.getDeclaredFields().length, 1.0f);

    private TypesUtil() {
        super();
    }

    static {
        for(Field field :Types.class.getDeclaredFields()){
            Integer  tmp  = null;
            try{
                tmp = (Integer) field.get(Types.class);
            }catch (IllegalAccessException e){
                tmp = Integer.MAX_VALUE;
            }
            TYPE_NAME_MAP.put(tmp,field.getName());
        }
    }

    public static String getName(int type, String defaultValue) {
        String  name = TYPE_NAME_MAP.get(type);
        if(name != null)
            return name;
        else
            return defaultValue;
    }
}
