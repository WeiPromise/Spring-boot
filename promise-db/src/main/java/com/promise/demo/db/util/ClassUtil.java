package com.promise.demo.db.util;


import com.promise.demo.db.annotations.FieldMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;


@Slf4j
public final class ClassUtil {
    
    private static final Pattern PATTERN = Pattern.compile("^([gs]et|is)[A-Z].*");

    private static final Map<Class, Map<Field, Pair<Method, Method>>> CLASS_MAP = new HashMap<>();

    private ClassUtil() {
        super();
    }

    /**
     * setNonNull 把不是NULL的值赋给target
     */
    public static <T> void setNonNull(T source, T target) {
        for (Map.Entry<Field, Pair<Method, Method>> entry : getFieldsMap(source.getClass()).entrySet()) {
            final Method getMethod = entry.getValue().getLeft();
            final Method setMethod = entry.getValue().getRight();
            try {
                Object getResult = getMethod.invoke(source);
                if (getResult != null) {
                    setMethod.invoke(target, getResult);
                }
            } catch (Exception e) {
                log.error(String.format("field%s赋值错误", entry.getKey().toGenericString()));
                log.error(String.format("get: %s", getMethod.toGenericString()));
                log.error(String.format("set: %s", setMethod.toGenericString()));
                log.error(e.getMessage());
            }
        }
    }

    /**
     * copy 类复制
     */
    public static <B, S extends B, T extends B> void copy(final Class<B> base, S source, T target) {
        for (Map.Entry<Field, Pair<Method, Method>> entry : getFieldsMap(base).entrySet()) {
            final Method getMethod = entry.getValue().getLeft();
            final Method setMethod = entry.getValue().getRight();
            try {
                setMethod.invoke(target, getMethod.invoke(source));
            } catch (Exception e) {
                log.error(String.format("field%s赋值错误", entry.getKey().toGenericString()));
                log.error(String.format("get: %s", getMethod.toGenericString()));
                log.error(String.format("set: %s", setMethod.toGenericString()));
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 根据FieldsMap进行赋值
     *
     * @param base   赋值源与目标的Class
     * @param source 赋值源
     * @param target 赋值目标
     */
    public static <B, S extends B, T extends B> void setBaseClassValue(final Class<? extends B> base, S source, T target) {

        for (Map.Entry<Field, Pair<Method, Method>> entry : getFieldsMap(base).entrySet()) {
            final Method getMethod = entry.getValue().getLeft();
            final Method setMethod = entry.getValue().getRight();
            try {
                Object getResult = getMethod.invoke(source);
                if (entry.getKey().getGenericType().getClass() == Class.class) {
                    setMethod.invoke(target, getResult);
                }
            } catch (Exception e) {
                log.error(String.format("field%s赋值错误", entry.getKey().toGenericString()));
                log.error(String.format("get: %s", getMethod.toGenericString()));
                log.error(String.format("set: %s", setMethod.toGenericString()));
                log.error(e.getMessage());
            }
        }
    }

    /**
     * getFields
     *
     * @param baseClass class
     * @return 非private或static或final并且FieldMeta声明了的field
     */
    public static Field[] getFields(Class<?> baseClass) {
        List<Field> list = new ArrayList<>();
        for (Field field : baseClass.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            // 非private或static或final并且FieldMeta声明了的field
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && field.isAnnotationPresent(FieldMeta.class)) {
                list.add(field);
            }
        }
        Field[] fields = list.toArray(new Field[0]);
        Class superClass = baseClass.getSuperclass();
        if (superClass != Object.class) {
            return ArrayUtils.addAll(getFields(superClass), fields);
        } else {
            return fields;
        }
    }

    /**
     * 获取每一个Field的get与set的Method
     *
     * @param cls class
     */
    public static Map<Field, Pair<Method, Method>> getFieldsMap(Class cls) {
        return getFieldsMap(cls, false);
    }

    /**
     * 获取每一个Field的get与set的Method
     *
     * @param cls class
     */
    public static Map<Field, Pair<Method, Method>> getFieldsMap(Class cls, boolean ignoreAnnotation) {

        Map<Field, Pair<Method, Method>> fieldMethodMap = CLASS_MAP.get(cls);

        if (fieldMethodMap == null) {

            Map<String, Method> methodMap = new HashMap<>();

            for (Method method : cls.getMethods()) {
                int modifiers = method.getModifiers();
                String name = method.getName();
                if (method.getDeclaringClass() != Object.class && PATTERN.matcher(name).matches() && Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                    Method oldMethod = methodMap.get(name);
                    if (oldMethod == null || oldMethod.getReturnType().isInstance(method.getReturnType())) {
                        methodMap.put(name, method);
                    }
                }
            }

            fieldMethodMap = getFieldsMap(cls, methodMap, ignoreAnnotation);

            CLASS_MAP.put(cls, fieldMethodMap);
        }
        return fieldMethodMap;
    }

    private static Map<Field, Pair<Method, Method>> getFieldsMap(Class<?> cls, Map<String, Method> methodMap, boolean ignoreAnnotation) {

        final Field[] fields = cls.getDeclaredFields();
        final List<Field> otherFieldList = new ArrayList<>();
        final List<Field> boolFieldList = new ArrayList<>();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // 非private或static或final并且FieldMeta声明了的field
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                    && (field.isAnnotationPresent(FieldMeta.class) || ignoreAnnotation))
                if (field.getType() == Boolean.class || field.getType() == Boolean.TYPE) {
                    boolFieldList.add(field);
                } else {
                    otherFieldList.add(field);
                }
        }
        Collections.sort(boolFieldList, new Comparator<Field>() {
            @Override
            public int compare(Field field1, Field field2) {
                return Integer.compare(field2.getName().length(), field1.getName().length());
            }
        });

        Map<Field, Pair<Method, Method>> fieldMethodMap = new HashMap<>();
        for (Field field : otherFieldList) {
            final String fieldName = getName(field.getName());
            final Class fieldType = field.getType();
            Method getMethod = methodMap.remove("get" + fieldName);
            Method setMethod = methodMap.remove("set" + fieldName);
            if (null != getMethod && null != setMethod && getMethod.getReturnType() == fieldType && fieldType == setMethod.getParameterTypes()[0]) {
                fieldMethodMap.put(field, Pair.of(getMethod, setMethod));
            } else {
                log.error(String.format("field %s 错误", String.valueOf(field)));
                log.error(String.format("get: %s", String.valueOf(getMethod)));
                log.error(String.format("set: %s", String.valueOf(setMethod)));
            }
        }

        final Map<Method, String> boolGetMethod = new HashMap<>();
        final Map<Method, String> boolSetMethod = new HashMap<>();
        for (Map.Entry<String, Method> methodEntry : methodMap.entrySet()) {
            Class returnType = methodEntry.getValue().getReturnType();
            Class[] parameterTypes = methodEntry.getValue().getParameterTypes();
            if (returnType == Boolean.class || returnType == Boolean.TYPE) {
                boolGetMethod.put(methodEntry.getValue(), methodEntry.getKey());
            } else if (parameterTypes.length == 1 && (parameterTypes[0] == Boolean.class || parameterTypes[0] == Boolean.TYPE)) {
                boolSetMethod.put(methodEntry.getValue(), methodEntry.getKey());
            }
        }
        for (Field field : boolFieldList) {
            String fieldNameRev = StringUtils.reverse(field.getName());
            int getMatchCount = 1;
            Method getMethod = null;
            for (Method method : boolGetMethod.keySet()) {
                int common = StringUtils.getCommonPrefix(StringUtils.reverse(method.getName()), fieldNameRev).length();
                if (getMatchCount < common) {
                    getMethod = method;
                    getMatchCount = common;
                }
            }
            int setMatchCount = 1;
            Method setMethod = null;
            for (Method method : boolSetMethod.keySet()) {
                int common = StringUtils.getCommonPrefix(StringUtils.reverse(method.getName()), fieldNameRev).length();
                if (setMatchCount < common) {
                    setMethod = method;
                    setMatchCount = common;
                }
            }
            if (getMethod != null && setMethod != null) {
                fieldMethodMap.put(field, Pair.of(methodMap.remove(boolGetMethod.remove(getMethod)), methodMap.remove(boolSetMethod.remove(setMethod))));
            } else {
                log.error(String.format("field %s 错误", String.valueOf(field)));
            }
        }

        if (cls.getSuperclass() != Object.class) {
            fieldMethodMap.putAll(getFieldsMap(cls.getSuperclass(), methodMap, ignoreAnnotation));
        }

        return fieldMethodMap;
    }

    public static List<List<String>> getFieldsValues(List<?> list, Field[] fields, String def) {
        List<List<String>> result = new ArrayList<>();
        for (Object targetAttr : list) {
            List<String> fieldsValue = getFieldsValue(targetAttr, fields, def);
            result.add(fieldsValue);
        }
        return result;
    }

    public static List<String> getFieldsValue(Object attr, Field[] fields, String def) {

        final Map<Field, Pair<Method, Method>> fieldMethodMap = getFieldsMap(attr.getClass());

        final List<String> fieldValueList = new ArrayList<>(fields.length);
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // 非private或static或final并且FieldMeta声明了的field
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && field.isAnnotationPresent(FieldMeta.class)) {
                try {
                    Object fieldValue = fieldMethodMap.get(field).getLeft().invoke(attr);
                    if (field.getName().equals("columnType") && fieldValue instanceof Integer) {
                        fieldValueList.add(TypesUtil.getName((Integer) fieldValue, def));
                    } else if (String.valueOf(fieldValue).isEmpty()) {
                        fieldValueList.add("empty");
                    } else {
                        fieldValueList.add(String.valueOf(fieldValue));
                    }
                } catch (Exception e) {
                    fieldValueList.add(def);
                }
            }
        }
        return fieldValueList;
    }

    public static List<String> getFieldNames(Field[] fields) {

        final List<String> fieldNameList = new ArrayList<>(fields.length);
        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldMeta.class)) {
                fieldNameList.add(field.getAnnotation(FieldMeta.class).fieldName());
            }
        }
        return fieldNameList;
    }

    private static String getName(String fieldName) {
        StringBuilder stringBuilder = new StringBuilder(fieldName);
        stringBuilder.setCharAt(0, Character.toUpperCase(fieldName.charAt(0)));
        return stringBuilder.toString();
    }

}
