package com.bugsyteam.utils;

import io.vertx.ext.web.RoutingContext;

public class Response {

	public static void sendSimpleResponse(int statusCode, String response, String contentType, RoutingContext rc) {
		rc.response().setStatusCode(statusCode).putHeader("content-type", contentType).end(response);
	}

}
