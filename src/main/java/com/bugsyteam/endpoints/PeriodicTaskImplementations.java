package com.bugsyteam.endpoints;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.MockMethods;
import com.bugsyteam.utils.TimeWork;

import io.vertx.core.Vertx;

public class PeriodicTaskImplementations {

	private static final Logger LOGGER = Logger.getLogger(PeriodicTaskImplementations.class);

	/**
	 * Programs a blocking task to execute every x minutes.
	 *
	 * @param vertx          the vertx instance.
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
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

	/**
	 * Programs a non blocking task to execute every x minutes.
	 *
	 * @param vertx          the vertx instance.
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static void programPeriodicNonBlockingTask(Vertx vertx, long interval) {
		vertx.setPeriodic(TimeWork.getMillisFromMinutes(interval), id -> {
			MockMethods.nonBlockingMethod();
		});
	}

}
