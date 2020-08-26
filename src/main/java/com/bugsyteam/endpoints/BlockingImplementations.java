package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.MockMethods;
import com.bugsyteam.utils.Response;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * The Class BlockingImplementations handles all the blocking endpoint calls.
 * Vert.x APIs are non blocking but we can block the event loop in a handler.
 * For example with: Thread.sleep() or doing a long lived database operation and
 * waiting for a result. To handle this issue we call executeBlocking specifying
 * both the blocking code to execute and a result handler to be called back
 * asynchronous when the blocking code has been executed.
 */
public class BlockingImplementations {

	private static final Logger LOGGER = Logger.getLogger(BlockingImplementations.class);

	/**
	 * Handles GET /blocking endpoint calls. The event loop is free to receive other
	 * calls but when finish the execution of this code a response is returned to
	 * the routing context. This is recomended to handle short blocking operations.
	 *
	 * @param vertx          the vertx instance.
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long getBlocking(Vertx vertx, RoutingContext routingContext) {

		vertx.<String>executeBlocking(future -> {
			String result = "";
			try {
				result = MockMethods.blockingMethod();
				future.complete(result);
			} catch (Exception ex) {
				future.fail(ex);
			}
		}, res -> {

			if (res.succeeded()) {
				Response.sendSimpleResponse(200, res.result(), "application/json; charset=utf-8", routingContext);
				LOGGER.info("Response status code 200");

			} else {

				JsonObject response = new JsonObject();
				String responseMessage = "ERROR 500 - Ocurrio un error desconocido en la consulta a la base datos. Exception: "
						+ res.cause().toString();
				response.put("response", responseMessage);
				Response.sendSimpleResponse(500, response.encodePrettily(), "application/json; charset=utf-8",
						routingContext);
				LOGGER.error("Response status code 500. Cause: " + res.cause().toString());

			}
		});

		return Thread.currentThread().getId();
	}

	/**
	 * Gets the blocking with async response.
	 *
	 * @param vertx          the vertx
	 * @param routingContext the routing context
	 * @return the blocking with async response
	 */
	// -------------- endpoint: GET /blocking-async ----------//

	/**
	 * Handles GET /blocking-async endpoint calls. A response is returned to the
	 * routing context before the execution of blocking code. This is recomended to
	 * handle long blocking operations.
	 *
	 * @param vertx          the vertx instance.
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long getBlockingWithAsyncResponse(Vertx vertx, RoutingContext routingContext) {

		LOGGER.info("Request to GET /blocking-async");

		Response.sendSimpleResponse(200, "Operation in execution", "text/plain", routingContext);
		LOGGER.info("Response status code 200");

		new Thread(() -> {
			try {
				MockMethods.blockingMethod();
			} catch (Exception e) {
				LOGGER.error("Unknown error in execution of blocking code. Cause: " + e.getCause().toString());
			}
		}).start();

		return Thread.currentThread().getId();
	}

}
