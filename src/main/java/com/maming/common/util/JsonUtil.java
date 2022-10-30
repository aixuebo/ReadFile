package com.maming.common.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static <E> JSONObject mapToJson(Map<E, E> map) {
		try {
			return JSONObject.fromObject(map);
		} catch (Exception ex) {
			return null;
		}
	}

	public static <E> String mapToJsonString(Map<E, E> map) {
		try {
			return JSONObject.fromObject(map).toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static JSONObject strToJson(String jsonStr) {
		try {
			return JSONObject.fromObject(jsonStr);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static JSONArray strToJsonArray(String jsonStr) {
		try {
			return JSONArray.fromObject(jsonStr);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static <E> JSONArray listToJsonArray(List<Map<E, E>> list) {
		try {
			return JSONArray.fromObject(list);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String stringValue(JSONObject obj,String key) {
		try {
			return obj.getString(key);
		}catch (Exception ex){
			return null;
		}
	}
}
