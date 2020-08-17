package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.MockMethods;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BlockingImplementations {

	private static final Logger LOGGER = Logger.getLogger(BlockingImplementations.class);

	// -------------- endpoint: GET /blocking ----------//
	public static long getBlocking(Vertx vertx, RoutingContext routingContext) {

		LOGGER.info("Request to GET /blocking");

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
				routingContext.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(res.result());
				LOGGER.info("Response status code 200");

			} else {

				JsonObject response = new JsonObject();
				String responseMessage = "ERROR 500 - Ocurrio un error desconocido en la consulta a la base datos. Exception: "
						+ res.cause().toString();
				response.put("response", responseMessage);
				routingContext.response().setStatusCode(500)
						.putHeader("content-type", "application/json; charset=utf-8").end(response.encodePrettily());
				LOGGER.error("Response status code 500. Cause: " + res.cause().toString());

			}
		});

		return Thread.currentThread().getId();
	}

	// -------------- endpoint: GET /blocking-async ----------//
	public static long getBlockingWithAsyncResponse(Vertx vertx, RoutingContext routingContext) {

		LOGGER.info("Request to GET /blocking-async");

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
				routingContext.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(res.result());
				LOGGER.info("Response status code 200");

			} else {

				JsonObject response = new JsonObject();
				String responseMessage = "ERROR 500 - Ocurrio un error desconocido en la consulta a la base datos. Exception: "
						+ res.cause().toString();
				response.put("response", responseMessage);
				routingContext.response().setStatusCode(500)
						.putHeader("content-type", "application/json; charset=utf-8").end(response.encodePrettily());
				LOGGER.error("Response status code 500. Cause: " + res.cause().toString());

			}
		});

		return Thread.currentThread().getId();
	}

}
