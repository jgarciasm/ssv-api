package com.bugsyteam.endpoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.IOUtil;

import com.bugsyteam.utils.MockMethods;
import com.bugsyteam.utils.Response;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

public class ParamsImplementations {

	private static final Logger LOGGER = Logger.getLogger(ParamsImplementations.class);

	// -------------- endpoint: POST /with-query-params ----------------//
	public static long postWithQueryParams(RoutingContext routingContext) {
		LOGGER.info("Request to POST /with-query-params");
		String param1 = routingContext.request().getParam("param1");
		LOGGER.info("param1: " + param1);

		JsonObject response = new JsonObject();
		try {

			response.put("Response", MockMethods.nonBlockingMethod(param1));

		} catch (Exception e) {

			response.put("Response", "ERROR");

			LOGGER.error("Error en la llamada a /with-query-params", e);

		}

		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(response.encodePrettily());

		return Thread.currentThread().getId();
	}

	// -------------- endpoint: POST /with-json-body-params ----------------//
	public static long postWithJsonBodyParams(RoutingContext routingContext) {
		LOGGER.info("Request to POST /with-json-body-params");

		routingContext.request().bodyHandler(b -> {

			JsonObject body = b.toJsonObject();
			String param1 = body.getString("param1");
			LOGGER.info("param1: " + param1);

			JsonArray paramArray = body.getJsonArray("files");
			paramArray.forEach(p -> {
				LOGGER.info("param1: " + p);
			});

			JsonObject response = new JsonObject();
			try {

				response.put("Response", MockMethods.nonBlockingMethod(param1));

			} catch (Exception e) {

				response.put("Response", "ERROR");

				LOGGER.error("Error en la llamada a /with-json-body-params", e);

			}

			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(response.encodePrettily());
		});

		return Thread.currentThread().getId();
	}

	// -------------- endpoint: POST /with-json-multipart-files ----------------//
	public static long postWithMultipartFiles(RoutingContext rc) {
		LOGGER.info("Request to POST /with-json-multipart-files");

		if (rc.fileUploads().size() < 1) {
			LOGGER.error("Bad Request: Ningún fichero recibido.");
			Response.sendSimpleResponse(400, "Bad Request: Ningún fichero recibido.", "text/plain", rc);
		}

		// Depende del numero de ficheros que se esperan recibir, en este caso solo 1
		if (rc.fileUploads().size() > 1) {
			LOGGER.error("Bad Request: Ningún fichero recibido.");
			Response.sendSimpleResponse(400, "Bad Request: El número de ficheros recibidos no es correcto.",
					"text/plain", rc);
		}

		FileUpload file = rc.fileUploads().iterator().next();

		// Se espera recibir un json
		if (file != null && file.fileName().toLowerCase().endsWith(".json")) {

			// revisar todo esto para eliminar pasos innecesarios
			File uploadFile = new File(file.uploadedFileName());

			if (uploadFile.exists()) {

				try (FileReader reader = new FileReader(uploadFile);
						FileInputStream fileInputStream = new FileInputStream(uploadFile)) {

					// Read JSON file
					String string = IOUtil.toString(reader);
					JsonObject json = new JsonObject(string);

					byte[] bFile = new byte[(int) uploadFile.length()];
					fileInputStream.read(bFile);

					String param1 = json.getString("param1");
					LOGGER.info("param1: " + param1);

					MockMethods.nonBlockingMethod(param1);

				} catch (Exception e1) {
					LOGGER.error("Internal Server Error: Error desconocido en el manejo del fichero recibido.");
					Response.sendSimpleResponse(500,
							"Internal Server Error: Error desconocido en el manejo del fichero recibido.", "text/plain",
							rc);
				}

				uploadFile.delete();
			} else {
				LOGGER.error("Bad Request: Error desconocido con el fichero recibido.");
				Response.sendSimpleResponse(400, "Bad Request: Error desconocido con el fichero recibido.",
						"text/plain", rc);
			}
		} else

		{
			LOGGER.error("Bad Request: El fichero recibido tiene un formato no soportado.");
			Response.sendSimpleResponse(400, "Bad Request: El fichero recibido tiene un formato no soportado.",
					"text/plain", rc);
		}

		return Thread.currentThread().getId();
	}

}
