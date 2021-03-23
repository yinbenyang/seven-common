package com.seven.common.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.seven.common.emun.StatusCode;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 简单JSON解析工具
 * 
 * @author ligang
 *
 */
public class JSONUtils {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, Boolean.TRUE);
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, Boolean.TRUE);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, Boolean.TRUE);
    }

    /**
     * 解析JSON数组，T1 必须有default constructor
     * 
     * @param text JSON数组字符串
     * @param clazz
     * @return 指定类型的对象集合
     * @throws Exception 非法JSON字符串或者不符合JSON数组要求的text可能会抛出异常
     * 
     * <h1>20180412修改   By WangHuijie</h1>
     * <p>constructParametricType()方法已被弃用</p>
     * <p>constructParametricType()改为constructParametrizedType()</p>
     */
    public static <T> List<T> parseJSONArray(String text, Class<T> clazz) throws Exception {

        List<T> results = null;
        if (StringUtils.isNotEmpty(text)) {
            JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, clazz);
            results = mapper.readValue(text, javaType);
        }
        return results;
    }

    /**
     * 解析JSON对象
     * 
     * @param text
     * @param clazz
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws Exception
     *             非法JSON字符串或者不符合JSON对象要求的text可能会抛出异常
     */
    public static <T> T parseJSONObject(String text, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException  {

        if (StringUtils.isEmpty(text)) {
            return null;
        }
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        return mapper.readValue(text, clazz);
    }

    public static <T> T parse(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            return mapper.readValue(text, clazz);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> List<T> parseList(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, clazz);
        try {
            return mapper.readValue(text, javaType);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> T parse(String text, TypeReference<T> reference) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            return mapper.readerFor(reference).readValue(text);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 把对象转换成JSON字符串
     * 
     * @param t
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String toJSONString(T t) {

        String jsonString = null;
        if (t == null) {
            jsonString = "{\"code\" : " + StatusCode.ERROR.getIndex() + ", \"msg\" : \"" + StatusCode.ERROR.getMessage() + "\"}";
            return jsonString;
        }
        try {
            jsonString = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            jsonString = "{\"code\" : " + StatusCode.ERROR.getIndex() + ", \"msg\" : \"" + StatusCode.ERROR.getMessage() + "\"}";
        }
        return jsonString;
    }

    /**
     * 慎重使用，从map构造出对象,,主要用来解析半成品的json对象，未解析完全的对象，其数据结构为{@link LinkedHashMap}
     * 对数组支持不够友好，谨慎使用，因为数组json解析出来为list，无法set赋值
     * 
     * @param map
     * @param clazz
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws Exception
     */
    public static <T> T buildObject(Map<String, Object> map, Class<T> clazz) throws InstantiationException, IllegalAccessException {

        if (map == null) {
            return null;
        }
        T t = null;
        t = clazz.newInstance();
        Map<String, Field> fieldMap = getFieldMap(clazz);
        if (fieldMap != null) {
            for (String key : map.keySet()) {
                Object o = map.get(key);
                Field field = fieldMap.get(key);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(t, o);
                }
            }
        }
        return t;
    }

    /**
     * 获取字段，如果子类父类都有相同的field，以子类为准
     * 
     * @param clazz
     * @return
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz) {

        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                if (field == null) {
                    continue;
                }
                String fieldName = field.getName();
                // 以子类为主
                if (!fieldMap.containsKey(fieldName)) {
                    fieldMap.put(fieldName, field);
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null) {
            fields = superClass.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (field == null) {
                        continue;
                    }
                    String fieldName = field.getName();
                    // 以子类为主
                    if (!fieldMap.containsKey(fieldName)) {
                        fieldMap.put(fieldName, field);
                    }
                }
            }
            superClass = superClass.getSuperclass();
        }
        return fieldMap;
    }

    /**
     * 慎重使用，批量构造对象,主要用来解析半成品的json对象，未解析完全的对象，其数据结构为{@link LinkedHashMap}
     * 
     * @param mapList
     * @param clazz
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws Exception
     */
    public static <T> List<T> buildObjectList(List<Map<String, Object>> mapList, Class<T> clazz) throws InstantiationException, IllegalAccessException {

        if (mapList == null) {
            return null;
        }
        List<T> ts = new ArrayList<T>();
        for (Map<String, Object> map : mapList) {
            T t = buildObject(map, clazz);
            ts.add(t);
        }
        return ts;
    }
}
