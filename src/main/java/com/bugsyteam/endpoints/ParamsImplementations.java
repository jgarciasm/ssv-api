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

/**
 * The Class ParamsImplementations handles all the endpoint calls with
 * parameters.
 */
public class ParamsImplementations {

	private static final Logger LOGGER = Logger.getLogger(ParamsImplementations.class);

	/**
	 * Handles POST /with-query-params endpoint calls. This is intended to
	 * demonstrate how to obtain query parameters from POST request.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long postWithQueryParams(RoutingContext routingContext) {
		String param1 = routingContext.request().getParam("param1");
		LOGGER.info("param1: " + param1);

		JsonObject response = new JsonObject();
		try {
			response.put("Response", MockMethods.nonBlockingMethod(param1));
		} catch (Exception e) {
			response.put("Response", "ERROR");

			LOGGER.error("Error en la llamada a /with-query-params", e);
		}

		Response.sendSimpleResponse(200, response.encodePrettily(), "application/json; charset=utf-8", routingContext);

		return Thread.currentThread().getId();
	}

	/**
	 * Handles PUT /with-json-body-params endpoint calls. This is intended to
	 * demonstrate how to obtain parameters from a json received in the body of
	 * POST/PUT/etc request.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long putWithJsonBodyParams(RoutingContext routingContext) {

		routingContext.request().bodyHandler(b -> {

			JsonObject body = b.toJsonObject();
			String param1 = body.getString("param1");
			LOGGER.info("param1: " + param1);

			JsonArray paramArray = body.getJsonArray("array-param");
			paramArray.forEach(p -> {
				LOGGER.info("array param: " + p);
			});

			JsonObject response = new JsonObject();
			try {

				response.put("Response", MockMethods.nonBlockingMethod(param1));

			} catch (Exception e) {

				response.put("Response", "ERROR");

				LOGGER.error("Error en la llamada a /with-json-body-params", e);

			}

			Response.sendSimpleResponse(200, response.encodePrettily(), "application/json; charset=utf-8",
					routingContext);
		});

		return Thread.currentThread().getId();
	}

	/**
	 * Handles POST /with-json-multipart-files endpoint calls. This is intended to
	 * demonstrate how to obtain files from a multipart POST request.
	 *
	 * @param routingContext the routing context of the HTTP call.
	 * @return the id of the thread in execution.
	 */
	public static long postWithMultipartFiles(RoutingContext rc) {

		if (rc.fileUploads().size() < 1) {
			LOGGER.error("Bad Request: Any file received.");
			Response.sendSimpleResponse(400, "Bad Request: Any file received.", "text/plain", rc);
		}

		// It depends on the number of expected files. Only 1 in this case.
		if (rc.fileUploads().size() > 1) {
			LOGGER.error("Bad Request: The amount of recieved files is wrong.");
			Response.sendSimpleResponse(400, "Bad Request: The amount of recieved files is wrong.", "text/plain", rc);
		}

		FileUpload file = rc.fileUploads().iterator().next();

		// Expected json file
		if (file != null && file.fileName().toLowerCase().endsWith(".json")) {

			// TODO: revisar todo esto para eliminar pasos innecesarios
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
					LOGGER.error("Internal Server Error: Unknown error working with the received file.");
					Response.sendSimpleResponse(500,
							"Internal Server Error: Unknown error working with the received file.", "text/plain", rc);
				}

				uploadFile.delete();
			} else {
				LOGGER.error("Bad Request: Unknown error working with the received file.");
				Response.sendSimpleResponse(400, "Bad Request: Unknown error working with the received file.",
						"text/plain", rc);
			}
		} else {
			LOGGER.error("Bad Request: Invalid format of the received file.");
			Response.sendSimpleResponse(400, "Bad Request: Invalid format of the received file.", "text/plain", rc);
		}

		Response.sendSimpleResponse(200, "OK", "text/plain", rc);
		return Thread.currentThread().getId();
	}

}
