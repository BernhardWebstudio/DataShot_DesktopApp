package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.jobs.NahimaExportJob;
import java.io.IOException;
import java.util.Map;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

public class TestNahimaExportEvent extends TestCase {

	private static class StubNahimaManager extends NahimaManager {
		String lastUrl;
		String lastBody;
		Map<String, String> lastHeaders;
		String nextResponse = "{\"event\":{\"_id\":42,\"type\":\"DATASHOT_EXPORT\"}}";

		StubNahimaManager() throws IOException, InterruptedException {
			super("https://nahima.example.org", "user", "pass", false, false);
		}

		@Override
		protected void startSessionAndRetrieveToken() {
			// no-op for unit test
		}

		@Override
		protected void login() {
			// no-op for unit test
		}

		@Override
		protected String postRequest(String url, String data, Map<String, String> headers) {
			this.lastUrl = url;
			this.lastBody = data;
			this.lastHeaders = headers;
			return this.nextResponse;
		}
	}

	@Test
	public void testSendEventPostsSymmetricPayload() throws Exception {
		StubNahimaManager manager = new StubNahimaManager();
		JSONObject info = new JSONObject();
		info.put("total_specimens", 10);
		info.put("newly_exported", 6);
		info.put("updated", 2);
		info.put("skipped", 1);
		info.put("failed", 1);

		JSONObject response = manager.sendEvent(NahimaExportJob.EXPORT_EVENT_TYPE, info);

		assertNotNull(manager.lastUrl);
		assertTrue(manager.lastUrl.startsWith("https://nahima.example.org/api/v1/event?token="));
		assertNotNull(manager.lastHeaders);
		assertEquals("application/json", manager.lastHeaders.get("Content-Type"));

		JSONObject sent = new JSONObject(manager.lastBody);
		assertTrue(sent.has("event"));
		JSONObject event = sent.getJSONObject("event");
		assertEquals("DATASHOT_EXPORT", event.getString("type"));
		assertEquals(10, event.getJSONObject("info").getInt("total_specimens"));
		assertEquals(6, event.getJSONObject("info").getInt("newly_exported"));
		assertEquals(2, event.getJSONObject("info").getInt("updated"));
		assertEquals(1, event.getJSONObject("info").getInt("skipped"));
		assertEquals(1, event.getJSONObject("info").getInt("failed"));

		assertEquals(42, response.getJSONObject("event").getInt("_id"));
	}

	@Test
	public void testSendEventRejectsBlankType() throws Exception {
		StubNahimaManager manager = new StubNahimaManager();
		try {
			manager.sendEvent("  ", new JSONObject());
			fail("Expected IllegalArgumentException for blank eventType");
		} catch (IllegalArgumentException expected) {
			// expected
		}
	}

	@Test
	public void testSendEventThrowsOnErrorResponse() throws Exception {
		StubNahimaManager manager = new StubNahimaManager();
		manager.nextResponse = "{\"code\":\"error.permission\",\"description\":\"Not allowed\"}";
		try {
			manager.sendEvent(NahimaExportJob.EXPORT_EVENT_TYPE, new JSONObject());
			fail("Expected RuntimeException on error response");
		} catch (RuntimeException expected) {
			assertTrue(expected.getMessage().contains("error.permission"));
		}
	}

	@Test
	public void testSendEventHandlesEmptyResponse() throws Exception {
		StubNahimaManager manager = new StubNahimaManager();
		manager.nextResponse = "";
		JSONObject response = manager.sendEvent(NahimaExportJob.EXPORT_EVENT_TYPE, new JSONObject());
		assertNotNull(response);
		assertEquals(0, response.length());
	}

	@Test
	public void testBuildExportEventInfoContainsStatistics() {
		NahimaExportJob job = new NahimaExportJob();
		job.setOneSpecimenToExportBarcode("ETHZ-ENT0001234");
		JSONObject info = job.buildExportEventInfo();

		assertEquals("DataShot", info.getString("source"));
		assertEquals("unknown", info.getString("status"));
		assertEquals(0, info.getInt("total_specimens"));
		assertEquals(0, info.getInt("processed"));
		assertEquals(0, info.getInt("newly_exported"));
		assertEquals(0, info.getInt("updated"));
		assertEquals(0, info.getInt("skipped"));
		assertEquals(0, info.getInt("failed"));
		assertEquals("ETHZ-ENT0001234", info.getString("barcode"));
		assertTrue(info.has("finished_at"));
		assertTrue(info.has("duration_ms"));
	}
}
