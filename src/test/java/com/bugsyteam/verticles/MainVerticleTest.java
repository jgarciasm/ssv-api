package com.bugsyteam.verticles;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.multipart.MultipartForm;

@RunWith(VertxUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainVerticleTest {

	private Vertx vertx;

	private WebClient client;

	private final RequestOptions options = new RequestOptions().setPort(8080).setHost("localhost");

	private final WebClientOptions wcoptions = new WebClientOptions().setDefaultPort(8080).setDefaultHost("localhost");

	@Before
	public void setUp(TestContext tc) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(MainVerticle.class.getName(), tc.asyncAssertSuccess());
		client = WebClient.create(vertx, wcoptions);
		System.out.println("Setup Done");
	}

	@After
	public void tearDown(TestContext tc) {
		vertx.close(tc.asyncAssertSuccess());
		System.out.println("Close Done");
	}

	@Test
	public void A_test200(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/test"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				async.complete();
			});
		}).end();
		System.out.println("testA Done");
	}

	@Test
	public void B_test404(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/vers"), response -> {
			tc.assertEquals(response.statusCode(), 404);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("B Test Done");
	}

	@Test
	public void C_test401(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/version"), response -> {
			tc.assertEquals(response.statusCode(), 401);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).end();
		System.out.println("C Test Done");
	}

	@Test
	public void D_testVersion(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/version"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("D Test Done");
	}

	@Test
	public void E_testGetLogs(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/logs?backward_days=0"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("E Test Done");
	}

	@Test
	public void F_testCleanLogs(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().delete(options.setURI("/admin/logs"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("F Test Done");
	}

	@Test
	public void G_testGetLogsUnknown(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/logs?backward_days=1"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				tc.assertTrue(body.toString().contains("No existe"));
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("G Test Done");
	}

	@Test
	public void H_testGetBlocking(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/test/blocking"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("H Test Done");
	}

	@Test
	public void I_testGetBlockingAsync(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/test/blocking-async"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("I Test Done");
	}

	@Test
	public void J_testGetNonBlocking(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/test/nonblocking"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("J Test Done");
	}

	@Test
	public void K_testPostWithQueryParams(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().post(options.setURI("/test/with-query-params?param1=param1"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("K Test Done");
	}

	@Test
	public void L_testSwagger(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/doc/"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("L Test Done");
	}

	@Test
	public void M_testPutWithJsonBodyParams(TestContext tc) {
		Async async = tc.async();

		client.put("/test/with-json-body-params").putHeader("Authorization", "Basic dXNlcjpwYXNzdw==")
				.sendJsonObject(new JsonObject().put("param1", "param1").put("array-param",
						new JsonArray().add("array-p1").add("array-p1").add("array-p1")), ar -> {
							if (ar.succeeded()) {
								HttpResponse<Buffer> response = ar.result();
								tc.assertEquals(response.statusCode(), 200);
								tc.assertTrue(response.body().length() > 0);
								async.complete();
							}
						});

		System.out.println("M Test Done");
	}

	@Test
	public void N_testPostWithJsonMultipartFiles(TestContext tc) {
		Async async = tc.async();
		MultipartForm form = MultipartForm.create().binaryFileUpload("jsonFile", "test.json", "resources/test.json",
				"application/json");

		client.post("/test/with-json-multipart-files").basicAuthentication("user", "passw").sendMultipartForm(form,
				ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						tc.assertEquals(response.statusCode(), 200);
						tc.assertTrue(response.body().length() > 0);
						async.complete();
					} else {
						System.out.println("Something went wrong " + ar.cause().getMessage());
						async.complete();
					}
				});
		System.out.println("N Test Done");
	}

}
