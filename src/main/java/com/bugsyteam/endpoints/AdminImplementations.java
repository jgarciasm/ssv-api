package com.bugsyteam.endpoints;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.bugsyteam.utils.CompilationDataUtil;
import com.bugsyteam.utils.FilesWork;
import com.bugsyteam.utils.Response;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * The Class AdminImplementations handles all the administration endpoint calls.
 */
public class AdminImplementations {

	private static final Logger LOGGER = Logger.getLogger(AdminImplementations.class);

	/**
	 * Handles GET /admin/version endpoint calls.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return a JSON with the API version.
	 * 
	 *         { "Api Version": "0.0.1" }
	 *
	 */
	public static long version(RoutingContext routingContext) {
		JsonObject response = new JsonObject();
		try {
			response.put("Api Version", CompilationDataUtil.getVersion());

		} catch (Exception e) {
			response.put("Api Version", "UNKNOWN");

			LOGGER.error("Error en la llamada a /destinatarios/test", e);
		}

		Response.sendSimpleResponse(200, response.encodePrettily(), "application/json; charset=utf-8", routingContext);

		return Thread.currentThread().getId();
	}

	/**
	 * Handles GET /admin/logs endpoint calls.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the specified logs file.
	 */
	public static long getLogs(RoutingContext rc) {

		String userDir = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		int backward_days = Integer.parseInt(rc.request().getParam("backward_days"));

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String fileName = "";
		if (backward_days > 0) {
			cal.add(Calendar.DATE, -backward_days);
			fileName = userDir + separator + "logs" + separator + "ssv-api-log-" + dateFormat.format(cal.getTime())
					+ ".log";
		} else {
			fileName = userDir + separator + "logs" + separator + "ssv-api-log-" + dateFormat.format(cal.getTime())
					+ ".log";
		}

		Path p = Paths.get(fileName);

		if (Files.exists(p)) {
			Response.sendFileResponse(200, fileName, "logs.log", "text/plain; charset=utf-8", rc);
		} else {
			Response.sendSimpleResponse(400, "No existe " + fileName, "text/plain; charset=utf-8", rc);
		}

		return Thread.currentThread().getId();

	}

	/**
	 * Handles DELETE /admin/logs endpoint calls.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long cleanLogs(RoutingContext rc) {

		String userDir = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		String filePath = userDir + separator + "logs";

		Path p = Paths.get(filePath);

		if (Files.exists(p)) {
			FilesWork.emptyLogRoot(filePath);
			LOGGER.info("All ancient logs will be removed.");
			Response.sendSimpleResponse(200, "Se borraron correctamente todos los logs antiguos.",
					"text/plain; charset=utf-8", rc);
		} else {
			LOGGER.info("Specified logs dir does not exist.");
			Response.sendSimpleResponse(400, "No existe el directorio " + filePath, "text/plain; charset=utf-8", rc);
		}

		return Thread.currentThread().getId();
	}

}
