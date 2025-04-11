package com.mmu.services.utils;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorDiscriptions {

	public static HashMap<String, String> erHashMap = new HashMap<String, String>();

	public static String AUTH101 = "AUTH101";

	static {
		erHashMap.put("AUTH101", "invalid credentials !");
		erHashMap.put("loginname", "invalid LogiName !");
		erHashMap.put("pwd", "invalid Password !");

	}

	public static String getErrorDiscription(String errorCode) {
		if (erHashMap.get(errorCode) == null) {
			return "no error mssg found !";
		} else {
			return erHashMap.get(errorCode);
		}
	}

	public static JSONObject getReturnMsg(String msg, String status) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("status", status);
			jsonObject.put("msg", msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

}