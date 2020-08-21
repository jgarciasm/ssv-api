package com.bugsyteam.utils;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

public class Response {

	public static void sendSimpleResponse(int statusCode, String response, String contentType, RoutingContext rc) {
		rc.response().setStatusCode(statusCode).putHeader(HttpHeaders.CONTENT_TYPE, contentType).end(response);
	}

	public static void sendFileResponse(int statusCode, String fileName, String downloadFileName, String contentType,
			RoutingContext rc) {
		rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
				.putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
				.putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").sendFile(fileName);
	}

}
