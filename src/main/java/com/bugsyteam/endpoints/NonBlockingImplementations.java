package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.MockMethods;
import com.bugsyteam.utils.Response;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class NonBlockingImplementations {

	private static final Logger LOGGER = Logger.getLogger(NonBlockingImplementations.class);

	// -------------- endpoint: GET /nonblocking ----------------//
	public static long getNonBlocking(RoutingContext routingContext) {
		LOGGER.info("Request to GET /nonblocking");
		JsonObject response = new JsonObject();
		try {

			response.put("Response", MockMethods.nonBlockingMethod());

		} catch (Exception e) {

			response.put("Response", "ERROR");

			LOGGER.error("Error en la llamada a /nonBlocking1", e);

		}

		Response.sendSimpleResponse(200, response.encodePrettily(), "application/json; charset=utf-8", routingContext);

		return Thread.currentThread().getId();
	}

}
