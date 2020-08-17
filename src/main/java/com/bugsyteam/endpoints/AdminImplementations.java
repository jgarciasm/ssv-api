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

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AdminImplementations {

	private static final Logger LOGGER = Logger.getLogger(AdminImplementations.class);

	// TODO: Arreglar las respuestas

	// -------------- endpoint: GET /admin/version ----------------//
	public static long version(RoutingContext routingContext) {
		LOGGER.info("Request to GET /admin/version");
		JsonObject response = new JsonObject();
		try {
			response.put("Api Version", CompilationDataUtil.getVersion());

			response.put("ANFInfoWeb Version", CompilationDataUtil.getANFInfoWebVersion());

		} catch (Exception e) {
			response.put("Api Version", "UNKNOWN");

			response.put("ANFInfoWeb Version", "UNKNOWN");

			LOGGER.error("Error en la llamada a /destinatarios/test", e);
		}

		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(response.encodePrettily());

		return Thread.currentThread().getId();
		// return 1;
	}

	// ------------- endpoint: GET /admin/logs
	public static long getLogs(RoutingContext rc) {

		String userDir = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		int backward_days = Integer.parseInt(rc.request().getParam("backward_days"));

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String fileName = "";
		if (backward_days > 0) {
			cal.add(Calendar.DATE, -backward_days);
			fileName = userDir + separator + "logs" + separator + "apidestinatarios-log-"
					+ dateFormat.format(cal.getTime()) + ".log";
		} else {
			fileName = userDir + separator + "logs" + separator + "apidestinatarios-log-"
					+ dateFormat.format(cal.getTime()) + ".log";
		}

		Path p = Paths.get(fileName);

		if (Files.exists(p)) {
			rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
					.putHeader("Content-Disposition", "attachment; filename=\"logs.log\"")
					.putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").sendFile(fileName);
		} else {
			rc.response().setStatusCode(400).putHeader("content-type", "text/plain; charset=utf-8")
					.end("No existe " + fileName);
		}

		return Thread.currentThread().getId();
		// return 1;

	}

	// ------------- endpoint: DELETE /admin/logs
	public static long cleanLogs(RoutingContext rc) {

		LOGGER.info("Ejecutando llamada a endpoint /clean_logs.");
		String userDir = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		String filePath = userDir + separator + "logs";

		Path p = Paths.get(filePath);

		if (Files.exists(p)) {
			FilesWork.emptyLogRoot(filePath);
			LOGGER.info("Se borraron correctamente todos los logs antiguos.");
			rc.response().setStatusCode(200).putHeader("content-type", "text/plain; charset=utf-8")
					.end("Se borraron correctamente todos los logs antiguos.");
		} else {
			LOGGER.info("No existe el directorio de logs especificado.");
			rc.response().setStatusCode(200).putHeader("content-type", "text/plain; charset=utf-8")
					.end("No existe el directorio " + filePath);
		}

		return Thread.currentThread().getId();
		// return 1;

	}

}
