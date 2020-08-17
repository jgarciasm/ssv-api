package com.bugsyteam.utils;

import io.vertx.core.json.JsonObject;

public class MockMethods {

	public static String blockingMethod() throws InterruptedException {
		Thread.sleep(TimeWork.getMillisFromSeconds(10));
		JsonObject response = new JsonObject();
		response.put("result", "OK");
		return response.encodePrettily();
	}

	public static String nonBlockingMethod() {
		return "OK";
	}

	public static String nonBlockingMethod(String param) {
		return "OK";
	}

}
