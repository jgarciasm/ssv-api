package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.bugsyteam.utils.MockMethods;

import io.vertx.ext.web.RoutingContext;

public class NonBlockingImplementations {

	private static final Logger LOGGER = Logger.getLogger(NonBlockingImplementations.class);

	// -------------- endpoint: GET /nonblocking ----------------//
	public static long getNonBlocking(RoutingContext routingContext) {
		LOGGER.info("Request to GET /nonlocking");
		JSONObject response = new JSONObject();
		try {

			response.put("Response", MockMethods.nonBlockingMethod());

		} catch (Exception e) {

			response.put("Response", "ERROR");

			LOGGER.error("Error en la llamada a /nonBlocking1", e);

		}

		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(response.toJSONString());

		return Thread.currentThread().getId();
	}

}
