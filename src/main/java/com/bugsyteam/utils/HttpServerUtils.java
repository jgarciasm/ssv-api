package com.bugsyteam.utils;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.apache.log4j.Logger;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;

public class HttpServerUtils {

	private static final Logger LOGGER = Logger.getLogger(HttpServerUtils.class);

	public static void initCors(final Router router) {
		final Set<String> allowedHeaders = new HashSet<>();
		allowedHeaders.add("x-requested-with");
		allowedHeaders.add("Access-Control-Allow-Origin");
		allowedHeaders.add("Access-Control-Allow-Methods");
		allowedHeaders.add("Access-Control-Allow-Headers");
		allowedHeaders.add("Access-Control-Allow-Credentials");
		allowedHeaders.add("origin");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("accept");
		allowedHeaders.add("X-PINGARUNER");
		allowedHeaders.add("Authorization");

		final Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.OPTIONS);
		allowedMethods.add(HttpMethod.DELETE);
		allowedMethods.add(HttpMethod.PATCH);
		allowedMethods.add(HttpMethod.PUT);

		router.route().handler(CorsHandler.create(".*.").allowCredentials(true).allowedMethods(allowedMethods)
				.allowedHeaders(allowedHeaders));
	}

	public static void initApiLogging(Router router) {
		router.route().handler(event -> {
			String headers = "";
			if (event.request().headers() != null && !event.request().headers().isEmpty()) {
				headers = event.request().headers().entries().toString();
			}
			LOGGER.debug(
					event.request().method() + " request to " + event.request().absoluteURI() + " headers: " + headers);
//	        LOGGER.debug("{} request to {}, headers: {}",
//	                event.request().method(),
//	                event.request().absoluteURI(),
//	                headers);
			event.next();
		});
	}

	public static void authenticatedFunction(AuthProvider authProvider, RoutingContext routingContext,
			Function<RoutingContext, Long> func) {
		String authorization = routingContext.request().headers().get("authorization");

		if (authorization == null) {
			routingContext.response().setStatusCode(401).putHeader("content-type", "application/json; charset=utf-8")
					.end("No authorization token");
		} else {
			String user = "";
			String pass = "";
			String scheme = "";

			try {
				String[] parts = authorization.split(" ");
				scheme = parts[0];
				String[] credentials = new String(Base64.getDecoder().decode(parts[1])).split(":");
				user = credentials[0];
				// when the header is: "user:"
				pass = credentials.length > 1 ? credentials[1] : null;
			} catch (ArrayIndexOutOfBoundsException e) {
				routingContext.response().setStatusCode(401)
						.putHeader("content-type", "application/json; charset=utf-8").end("No authorization token");
			} catch (IllegalArgumentException | NullPointerException e) {
				// IllegalArgumentException includes PatternSyntaxException
				routingContext.response().setStatusCode(400)
						.putHeader("content-type", "application/json; charset=utf-8").end("Authentication Error");
			}

			if (!"Basic".equals(scheme)) {
				routingContext.response().setStatusCode(401)
						.putHeader("content-type", "application/json; charset=utf-8")
						.end("Error on Authorization Header");
			} else {
				JsonObject authInfo = new JsonObject().put("username", user).put("password", pass);
				authProvider.authenticate(authInfo, res -> {
					if (res.succeeded()) {

						User userLogged = res.result();

						System.out.println("User " + userLogged.principal().encodePrettily() + " is now authenticated");

						// test(routingContext);
						if (func != null) {
							func.apply(routingContext);
						} else {
							routingContext.next();
						}

					} else {
						LOGGER.error("Invalid user/password", res.cause());
						routingContext.response().setStatusCode(400)
								.putHeader("content-type", "application/json; charset=utf-8")
								.end("Invalid user/password");
					}
				});

			}
		}
	}

}
