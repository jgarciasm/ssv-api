package com.bugsyteam.verticles;

import org.apache.log4j.Logger;

import com.bugsyteam.endpoints.PeriodicTaskImplementations;
import com.bugsyteam.endpoints.RequestHandlerSelector;
import com.bugsyteam.utils.BasicHttpAuthentication;
import com.bugsyteam.utils.HttpServerUtils;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(MainVerticle.class);

	@Override
	public void start(Future fut) {

		/**
		 * Obtains the configuration parameters stored in conf/config.json
		 */
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
				.setConfig(new JsonObject().put("path", "conf/config.json"));
		ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

		retriever.getConfig(json -> {
			JsonObject config = json.result();
			LOGGER.info("config JSON: " + config.encodePrettily());

			int apiPort = config.getInteger("http.port", 8080);
			String apiUsername = config.getString("api.username");
			String apiPassword = config.getString("api.password");
			boolean enableCORS = config.getBoolean("cors.enable");
			boolean enableAdmin = config.getBoolean("endpoint.admin.enable");
			boolean enableSwagger = config.getBoolean("endpoint.swagger.enable");

			Router router = Router.router(vertx);

			LOGGER.info("Starting ssv-api on port " + apiPort);

			/**
			 * Enables CORS support in the server if is specified in congig.json.
			 */
			if (enableCORS) {
				HttpServerUtils.initCors(router);
			}

			/**
			 * Defines logs to every HTTP call to the API.
			 */
			HttpServerUtils.initApiLogging(router);
//			router.route().failureHandler(this::exceptionHandler);

			/**
			 * Set authentication method (at this version only is available Basic HTTP)
			 */
			AuthProvider authProvider = new BasicHttpAuthentication(apiUsername, apiPassword);

			router.route()
					.handler(SessionHandler.create(LocalSessionStore.create(vertx)).setAuthProvider(authProvider));

			AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

			/**
			 * Endpoint without security to test service availability. Some cloud providers
			 * requires this implementation.
			 */
			router.get("/test").handler(routingContext -> {
				routingContext.response().setStatusCode(200).end();
			});

			/**
			 * Apply authentication configuration to the rest of endpoints.
			 */
			router.route("/*").handler(routingContext -> {
				basicAuthHandler.handle(routingContext);
			});

			RequestHandlerSelector selector = new RequestHandlerSelector(vertx, authProvider, config);

			if (enableAdmin) {

				router.get("/admin/version").handler(selector::version);

				router.get("/admin/logs").handler(selector::getLogs);

				router.delete("/admin/logs").handler(selector::cleanLogs);

			}

			router.get("/blocking").handler(selector::getBlocking);

			router.get("/blocking-async").handler(selector::getBlockingWithAsyncResponse);

			router.get("/nonblocking").handler(selector::getNonBlocking);

			router.post("/with-query-params").handler(selector::postWithQueryParams);

			router.put("/with-json-body-params").handler(selector::postWithJsonBodyParams);

			String userDir = System.getProperty("user.dir");
			String separator = System.getProperty("file.separator");
			router.post("/with-json-multipart-files")
					.handler(BodyHandler.create().setUploadsDirectory(userDir + separator + "uploads"));
			router.post("/with-json-multipart-files").handler(selector::postWithMultipartFiles);

			PeriodicTaskImplementations.programPeriodicBlockingTask(vertx, 60);

			PeriodicTaskImplementations.programPeriodicNonBlockingTask(vertx, 60);

			if (enableSwagger) {

				// Serve the Swagger UI:
				router.route("/doc/*").handler(routingContext -> {
					HttpServerUtils.authenticatedFunction(authProvider, routingContext, null);
				});

				router.route("/doc/*").handler(StaticHandler.create().setCachingEnabled(false)
						.setWebRoot("webroot/node_modules/swagger-ui-dist"));
			}

			/**
			 * Config the server to accept large HTTP headers.
			 */
			HttpServerOptions httpServerOptions = new HttpServerOptions();
			httpServerOptions.setMaxHeaderSize(1024 * 16);

			/**
			 * Starts the server at the specified port (config.json) or 8080 by defect.
			 */
			vertx.createHttpServer(httpServerOptions).requestHandler(router::accept).exceptionHandler(ex -> {
				LOGGER.error("Http server exception handler.", ex);
			}).listen(config.getInteger("http.port", 8080), result -> {
				if (result.succeeded()) {
					LOGGER.info("Starting ssv-api on port " + config.getInteger("http.port", 8080));
					fut.complete();
				} else {
					fut.fail(result.cause());
					LOGGER.error("ERROR on starting ssv-api", result.cause());
				}
			});

		});

	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		super.stop(stopFuture);
		LOGGER.info("ssv-api stopped!");
	}

}
