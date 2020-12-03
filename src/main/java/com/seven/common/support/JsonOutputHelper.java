package com.seven.common.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.common.emun.StatusCode;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonOutput的辅助类
 * 
 *
 */
public class JsonOutputHelper {
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AAA", "BBB");
		System.out.println(JsonOutputHelper.toJson(StatusCode.SUCCESS, map));
	}
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static String toJson(StatusCode statusCode, Object data) {
		
		JsonOutput output = new JsonOutput(statusCode.getIndex(),statusCode.getMessage(),data);
		
		return toJson(output);
	}
	
	public static String toJson(StatusCode statusCode)  {
		
		JsonOutput output = new JsonOutput(statusCode.getIndex(),statusCode.getMessage());
						
		return toJson(output);
	}
	
	public static String toJson(int code, String msg, Object data) {
		
		JsonOutput output = new JsonOutput(code, msg, data);
						
		return toJson(output);
	}
	
	public static String toJson(int code, String msg) {
		
		JsonOutput output = new JsonOutput(code, msg);
						
		return toJson(output);
	}
	
	public static String toJson(int code, String msg, Object... data) {
		
		JsonOutput output = new JsonOutput(code, msg, data);
						
		return toJson(output);
	}
	
	public static String toJson(JsonOutput jsonOutput) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(jsonOutput);
		} catch (JsonProcessingException e) {
			
		}
		return json;
	}
	
	public static String toJson(Object data) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			
		}
		return json;
	}
	
}
