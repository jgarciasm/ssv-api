package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.anf.infoweb.utils.TimeWork;
import com.bugsyteam.utils.MockMethods;

import io.vertx.core.Vertx;

public class PeriodicTaskImplementations {

	private static final Logger LOGGER = Logger.getLogger(PeriodicTaskImplementations.class);

	public static void programPeriodicBlockingTask(Vertx vertx, long interval) {
		vertx.setPeriodic(TimeWork.getMillisFromMinutes(interval), id -> {
			new Thread(() -> {
				try {
					MockMethods.blockingMethod();
				} catch (Exception e) {
					LOGGER.error("Execution of automatic retry of RankingElEconomista Scanner aborted", e);
				}
			}).start();
		});
	}

	public static void programPeriodicNonBlockingTask(Vertx vertx, long interval) {
		vertx.setPeriodic(TimeWork.getMillisFromMinutes(interval), id -> {
			MockMethods.nonBlockingMethod();
		});
	}

}
