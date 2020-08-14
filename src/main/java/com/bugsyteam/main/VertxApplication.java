package com.bugsyteam.main;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;

import com.bugsyteam.verticles.MainVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxApplication {

	private VertxApplication() {
	}

	public static void main(String[] args) {

		BasicConfigurator.configure();

		VertxOptions options = new VertxOptions();

		// check for blocked threads every 5s
		options.setBlockedThreadCheckInterval(5);
		options.setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS);

		// warn if an event loop thread handler took more than 100ms to execute
		options.setMaxEventLoopExecuteTime(100);
		options.setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS);

		// warn if an worker thread handler took more than 10s to execute
		options.setMaxWorkerExecuteTime(10);
		options.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);

		// log the stack trace if an event loop or worker handler took more than 20s to
		// execute
		options.setWarningExceptionTime(20);
		options.setWarningExceptionTimeUnit(TimeUnit.SECONDS);

		Vertx vertx = Vertx.vertx(options);
		vertx.deployVerticle(MainVerticle.class.getName());
	}

}
