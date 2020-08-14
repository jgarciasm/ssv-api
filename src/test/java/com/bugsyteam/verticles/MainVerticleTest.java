package com.bugsyteam.verticles;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.RestAssured;

import io.vertx.core.Vertx;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainVerticleTest {

	private Vertx vertx;

	private final RequestOptions options = new RequestOptions().setPort(8080).setHost("localhost");

	@Before
	public void setUp(TestContext tc) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(MainVerticle.class.getName(), tc.asyncAssertSuccess());
		System.out.println("Setup Done");
	}

	@After
	public void tearDown(TestContext tc) {
		vertx.close(tc.asyncAssertSuccess());
		System.out.println("Close Done");
	}

	@Test
	public void A_test404(TestContext tc) {
		System.out.println("A Test 1");
		Async async = tc.async();
		System.out.println("A Test 2");
		vertx.createHttpClient().get(options.setURI("/admin/vers"), response -> {
			System.out.println("A Test 3");
			tc.assertEquals(response.statusCode(), 404);
			System.out.println("A Test 4");
			response.bodyHandler(body -> {
				System.out.println("A Test 5");
				tc.assertTrue(body.length() > 0);
				System.out.println("A Test 6");
				async.complete();
				System.out.println("A Test 7");
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("A Test Done");
	}

	@Test
	public void B_test401(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/version"), response -> {
			tc.assertEquals(response.statusCode(), 401);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).end();
		System.out.println("B Test Done");
	}

	@Test
	public void C_testVersion(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/version"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("C Test Done");
	}

	@Test
	public void D_testGetPendingTasks(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/pending_tasks?scanner=economista"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("D Test Done");
	}

	@Test
	public void E_testGetPendingTasksUnknown(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/pending_tasks?scanner=economistaa"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				tc.assertTrue(body.toString().contains("Bad Request"));
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("E Test Done");
	}

	@Test
	public void F_testGetPendingTasksWithoutParameters(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/pending_tasks"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				tc.assertTrue(body.toString().contains("Bad Request"));
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("F Test Done");
	}

	@Test
	public void G_testGetLogs(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/logs?backward_days=0"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("G Test Done");
	}

	@Test
	public void H_testCleanLogs(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().delete(options.setURI("/admin/logs"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("H Test Done");
	}

	@Test
	public void I_testGetLogsUnknown(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/admin/logs?backward_days=1"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				tc.assertTrue(body.toString().contains("No existe"));
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("I Test Done");
	}

	@Test
	public void J_testGetDestinatariosCount(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatarios/count"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("J Test Done");
	}

	@Test
	public void K_testGetDestinatarioByName(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatario?name=MERCADONA%20SA"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("K Test Done");
	}

	@Test
	public void L_testGetDestinatarioByNameUnknown(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatario?name=AAAA"), response -> {
			tc.assertEquals(response.statusCode(), 404);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("L Test Done");
	}

	@Test
	public void M_testGetDestinatarioByNameWithoutParameters(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatario"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				tc.assertTrue(body.toString().contains("Bad Request"));
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("M Test Done");
	}

	@Test
	public void N_testGetDestinatariosWithLimit(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatarios?startindex=50&count=2"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("N Test Done");
	}

	@Test
	public void O_testGetDestinatariosWithLimitWithoutStartIndex(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatarios?count=2"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("O Test Done");
	}

	@Test
	public void P_testGetDestinatariosWithLimitWithoutCount(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatarios?startindex=50"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("P Test Done");
	}

	@Test
	public void Q_testGetDestinatariosWithLimitWithoutParameters(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/destinatarios"), response -> {
			tc.assertEquals(response.statusCode(), 400);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("Q Test Done");
	}

	@Test
	public void R_testGetEmailsCount(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/emails/count"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("R Test Done");
	}

	@Test
	public void S_testScannerStatus(TestContext tc) {
		Async async = tc.async();
		vertx.createHttpClient().get(options.setURI("/scanners/status"), response -> {
			tc.assertEquals(response.statusCode(), 200);
			response.bodyHandler(body -> {
				tc.assertTrue(body.length() > 0);
				async.complete();
			});
		}).putHeader("Authorization", "Basic dXNlcjpwYXNzdw==").end();
		System.out.println("S Test Done");
	}

	@Test
	public void T_testInsertDestAndEmailThenRemove() {

		System.out.println("A0");
		// Insert a Destinatario.
		RestAssured.given().auth().basic("user", "passw").put(
				"/destinatarios?destNombreEmpresa=MERCADO%20JONAD%20MAX&destCIF=23DF&destCantidadEmpleados=12&destVolumenVentas=12000&destActivoBalance=12000&destDireccion=86%20y%2043&destProvincia=ALICANTE&destPoblacion=ALICANTE/ALACANT&destWeb=www.jonadmax.com&destTelefono=53584958&destCNAE=2640&destObligacion=true")
				.then().assertThat().statusCode(200);
		System.out.println("A1");

		// Now get the inserted Destinatario
		final int destId = RestAssured.given().auth().basic("user", "passw")
				.get("/destinatario?name=MERCADO%20JONAD%20MAX").thenReturn().body().jsonPath().getInt("destId");
		System.out.println("A2");

		// Insert a Email.
		RestAssured.given().auth().basic("user", "passw")
				.put("/emails?destEmail=pruebita@gmail.com&emailDepartamento=HAS&destId=" + destId).then().assertThat()
				.statusCode(200);
		System.out.println("A3");

		// Delete Destinatario.
		RestAssured.given().auth().basic("user", "passw").delete("/destinatarios?destId=" + destId).then().assertThat()
				.statusCode(200);
		System.out.println("A4");

		// Check that the resource is not available anymore
		RestAssured.given().auth().basic("user", "passw").get("/destinatario?name=MERCADO%20JONAD%20MAX").then()
				.assertThat().statusCode(404);
		System.out.println("A5");
		// Implement getEmail to test if do not exist
		// get("/destinatario?name=MERCADO%20JONAD%20MAX").then().assertThat().statusCode(400);
		System.out.println("T Test Done");
	}

}
