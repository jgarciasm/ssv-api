package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.MockMethods;
import com.bugsyteam.utils.Response;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * The Class NonBlockingImplementations handles all the non blocking endpoint
 * calls.
 */
public class NonBlockingImplementations {

	private static final Logger LOGGER = Logger.getLogger(NonBlockingImplementations.class);

	// -------------- endpoint: GET /nonblocking ----------------//

	/**
	 * Handles GET /nonblocking endpoint calls. This is recomended to handle very
	 * short operations that do not blocks the event loop.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long getNonBlocking(RoutingContext routingContext) {
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
