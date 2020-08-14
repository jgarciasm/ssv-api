package com.bugsyteam.utils;

import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

public class BasicHttpAuthentication implements AuthProvider {

	private String apiUsername;

	private String apiPassword;

	public BasicHttpAuthentication(String apiUsername, String apiPassword) {
		super();
		this.apiUsername = apiUsername;
		this.apiPassword = apiPassword;
	}

	private static final Logger LOGGER = Logger.getLogger(BasicHttpAuthentication.class);

	@Override
	public void authenticate(JsonObject arg0, Handler<AsyncResult<User>> arg1) {
		String username = arg0.getString("username");
		String passw = arg0.getString("password");
		User user = null;

		if (username.equalsIgnoreCase(apiUsername) && passw.equalsIgnoreCase(apiPassword))
			user = new ApiUser(username);

		if (user != null)
			arg1.handle(Future.succeededFuture(user));
		else
			arg1.handle(Future.failedFuture("Invalid User"));

	}

}
