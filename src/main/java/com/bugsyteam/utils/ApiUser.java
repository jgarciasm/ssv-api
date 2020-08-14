package com.bugsyteam.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

public class ApiUser implements User {

	private final String name;

	public ApiUser(String username) {
		this.name = username;
	}

	@Override
	public User clearCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User isAuthorized(String arg0, Handler<AsyncResult<Boolean>> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject principal() {
		JsonObject response = new JsonObject();
		response.put("user", this.name);
		response.put("passsword", "******");
		return response;
	}

	@Override
	public void setAuthProvider(AuthProvider arg0) {
		// TODO Auto-generated method stub

	}

}
