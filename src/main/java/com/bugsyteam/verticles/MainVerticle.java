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
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = Logger.getLogger(MainVerticle.class);

	@Override
	public void start(Future fut) {

		// Get config parameters
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

			AuthProvider authProvider = new BasicHttpAuthentication(apiUsername, apiPassword);

			// Configurations to accept CORS
			if (enableCORS) {
				HttpServerUtils.initCors(router);
			}

			//
			HttpServerUtils.initApiLogging(router);
			// router.route().failureHandler(this::exceptionHandler);
			//

			// Set Authentication
			router.route()
					.handler(SessionHandler.create(LocalSessionStore.create(vertx)).setAuthProvider(authProvider));

			AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

			// Endpoint without security to test service availability
			// Some cloud providers requires this implementations
			router.get("/test").handler(routingContext -> {
				routingContext.response().setStatusCode(200).end();
			});

			router.route("/*").handler(routingContext -> {
				basicAuthHandler.handle(routingContext);
			});

			RequestHandlerSelector selector = new RequestHandlerSelector(vertx, authProvider, config);

			// Endpoints:

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

			router.post("/with-json-multipart-files").handler(selector::postWithMultipartFiles);

			// Cambiar a la implementacion de logs de Glenda

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

			// To accept larger HTTP Headers
			HttpServerOptions httpServerOptions = new HttpServerOptions();
			httpServerOptions.setMaxHeaderSize(1024 * 16);

			vertx.createHttpServer(httpServerOptions).requestHandler(router::accept).exceptionHandler(ex -> {
				LOGGER.error("Http server exception handler.", ex);
			}).listen(config.getInteger("http.port", 8080), result -> {
				if (result.succeeded()) {
					LOGGER.info("Starting API_Destinatarios on port " + config.getInteger("http.port", 8080));
					fut.complete();
				} else {
					fut.fail(result.cause());
					LOGGER.error("ERROR on starting API_Destinatarios", result.cause());
				}
			});

		});

	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		// must call super.stop() or call stopFuture.complete()
		super.stop(stopFuture);
		LOGGER.info("API_Destinatarios stopped!");
	}

}
