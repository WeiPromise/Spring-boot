package com.promise.jdbc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;

/**
 * Created by leiwei on 2019-6-18.
 */
public class JsonUtil {

    /**
     * JSON格式化
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson SIMPLE_GSON = new GsonBuilder().create();

    /**
     * 非实例化
     */
    private JsonUtil() {
        super();
    }

    public static <T> T getObjFromStr(String jsonContent, Class<T> clz) {
        return GSON.fromJson(jsonContent, clz);
    }

    public static <T> T getObjFromFileReader(Reader jsonContent, Class<T> clz) {
        return GSON.fromJson(jsonContent, clz);
    }

    public static <T> T getTypeObjFromStr(String jsonContent, Type type) {
        return GSON.fromJson(jsonContent, type);
    }

    /**
     * getStrFromObj
     *
     * @param obj Object
     */
    public static String getStrFromObj(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * getStrFromObj
     *
     * @param obj Object
     */
    public static String getSimpleStrFromObj(Object obj) {
        return SIMPLE_GSON.toJson(obj);
    }

    /**
     * saveToFile
     *
     * @param path 路径
     * @param json JSON字符串
     * @throws IOException IOException
     */
    public static void saveToFile(String path, String json) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("Mkdirs failed.");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(json);
            bufferedWriter.write("\n");
        }
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            GSON.fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}

