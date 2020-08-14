package com.bugsyteam.endpoints;

import java.util.function.Function;

import com.bugsyteam.utils.HttpServerUtils;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;

public class RequestHandlerSelector {

	private Vertx vertx;

	private AuthProvider authProvider;

	private JsonObject config;

	public RequestHandlerSelector(Vertx vertx, AuthProvider authProvider, JsonObject config) {
		super();
		this.vertx = vertx;
		this.authProvider = authProvider;
		this.config = config;
	}

	public void version(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> AdminImplementations.version(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void getLogs(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> AdminImplementations.getLogs(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void cleanLogs(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> AdminImplementations.cleanLogs(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void getBlocking(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> BlockingImplementations.getBlocking(vertx, e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void getBlockingWithAsyncResponse(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> BlockingImplementations.getBlockingWithAsyncResponse(vertx, e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void getNonBlocking(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> NonBlockingImplementations.getNonBlocking(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void postWithQueryParams(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> ParamsImplementations.postWithQueryParams(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void postWithJsonBodyParams(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> ParamsImplementations.postWithJsonBodyParams(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

	public void postWithMultipartFiles(RoutingContext rc) {
		Function<RoutingContext, Long> func = e -> ParamsImplementations.postWithMultipartFiles(e);
		HttpServerUtils.authenticatedFunction(authProvider, rc, func);
	}

}
