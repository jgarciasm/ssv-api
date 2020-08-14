package com.bugsyteam.utils;

import org.json.simple.JSONObject;

public class MockMethods {

	public static String blockingMethod() throws InterruptedException {
		Thread.sleep(TimeWork.getMillisFromSeconds(10));
		JSONObject response = new JSONObject();
		response.put("result", "OK");
		return response.toJSONString();
	}

	public static String nonBlockingMethod() {
		return "OK";
	}

	public static String nonBlockingMethod(String param) {
		return "OK";
	}

}
