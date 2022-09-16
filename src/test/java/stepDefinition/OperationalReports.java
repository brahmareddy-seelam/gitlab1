package stepDefinition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Assert;

import com.opencsv.exceptions.CsvException;
import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;

public class OperationalReports extends BaseClass {

	CommonSteps cs = new CommonSteps();
	Summaries sum=new Summaries();

	public static HashMap<String, String> commonMap = new HashMap<String, String>();
	public static HashMap<String, String> orgMap = new HashMap<String, String>();
	public static HashMap<String, String> map = new HashMap<>();
	public static String userId;
	public static String startDate;
	public static String endDate;
	public static String token = null;
	public static List<String> value;
	public static Integer duration = 0;
	public static Map<String, Map<String, String>> oldValue;
	public static Map<String, Map<String, String>> newValue;
	public static Map<String, Map<String, String>> reportData;
	public static String startTime = null;
	public static String endtime = null;
	public static String data = null;
	public static String[] keyArr;
	public static String oldCallId;
	public static String newCallId;
	public static String keyForTest;

	public static String Total_agents_who_received_at_least_1_call = "Total agents who received at least 1 call";
	public static String Total_contacts_received_at_logger = "Total contacts received at logger";
	public static String Total_ghost_contacts = "Total ghost contacts";
	public static String Contacts_with_transcripts = "Contacts with transcripts";
	public static String Total_contacts_with_on_demand_requests = "Total contacts with on demand requests";
	public static String Total_On_Demand_Requests = "Total On Demand Requests";
	public static String Total_contacts_with_at_least_1_NLPAI_entity_extracted = "Total contacts with at least 1 NLP/AI entity extracted";
	public static String Average_ACW_time = "Average ACW time (secs)";
	public static String Count = "Count";
	public static String Total_contacts_with_summaries_generated = "Total contacts with summaries generated";
	public static String Total_summaries_generated = "Total summaries generated";
	public static String Total_default_Summaries = "Total default Summaries";
	public static String Total_meaningful_summaries = "Total meaningful summaries";
	public static String Total_contacts_with_summary_failed = "Total contacts with summary failed";
	public static String Average_accuracy = "Average accuracy (%)";
	public static String Average_summary_latency = "Average summary latency (secs)";
	public static String End_of_call = "End of call (ACW)";
	public static String On_demand_summary = "On demand summary";

	@Then("I get time")
	public String getTime(int timeInSeconds) throws IOException {
		String time;
		Instant instant = Instant.now();
		time = instant.minus(timeInSeconds, ChronoUnit.SECONDS).toString();
		time = time.replaceAll("[a-zA-Z]", " ");
		System.out.println(time.split("\\.")[0]);
		return (time.split("\\.")[0]);

	}

	@Then("I get summary report for {string} and {string}")
	public void getSummaryReport(String filepath, String file)
			throws IOException, CsvException, URISyntaxException, InterruptedException, UnsupportedAudioFileException {
		startTime = getTime(0);
		cs.the_request_with_is_sent_to_the_audio_connector(filepath);
		cs.add_wait_for_call(filepath);
		cs.the_request_with_is_sent_to_the_audio_connector(file);
		cs.add_wait_for_call(file);
		endtime = getTime(0);
		reportData = null;
		reportData = downloadReport("Summary");
//		the_request_with_is_sent_to_the_audio_connector(filepath);
//		add_wait_for_call(filepath);
//		Endtime=getTime(0);
//		newValue = null;
//		newValue = downloadReport(startTime,Endtime);
		compareListforSummary("topOfFunnel");
//		compareListforSummary("Features");
	}

	
	@Then("I get contact details report for {string} and {string}")
	public void getContactDetailsReport(String filepath, String file)
			throws IOException, CsvException, InterruptedException, URISyntaxException, UnsupportedAudioFileException {
		startTime = getTime(0);
		oldCallId=CommonSteps.commonMap.get("callId");
		cs.the_request_with_is_sent_to_the_audio_connector(filepath);
		cs.add_wait_for_call(filepath);
//		sum.edit_and_submit_summary("Time to deliver","5 to 7 business days");
//		sum.submit_edited_summaries();
		cs.the_request_call_id_is();
		newCallId=CommonSteps.commonMap.get("callId");
		cs.the_request_with_is_sent_to_the_audio_connector(file);
		cs.add_wait_for_call(file);
		endtime = getTime(0);
		reportData = null;
		reportData = downloadReport("ContactDetails");
		compareContactList();
//		
	}
	
	@Then("I get contact event report for {string} and {string}")
	public void getContactEventsReport(String filepath, String file)
			throws IOException, CsvException, InterruptedException, URISyntaxException, UnsupportedAudioFileException {
		startTime = getTime(0);
		oldCallId=CommonSteps.commonMap.get("callId");
		cs.the_request_with_is_sent_to_the_audio_connector(filepath);
		cs.add_wait_for_call(filepath);
//		sum.edit_and_submit_summary("Time to deliver","5 to 7 business days");
//		sum.submit_edited_summaries();
//		cs.the_request_call_id_is();
//		newCallId=CommonSteps.commonMap.get("callId");
//		cs.the_request_with_is_sent_to_the_audio_connector(file);
//		cs.add_wait_for_call(file);
		endtime = getTime(0);
		reportData = null;
		reportData = downloadReport("ContactEvents");
		compareContactEvent();
//		
	}
	
	@Then("I get summary details report for {string} and {string}")
	public void getSummaryDetailsReport(String filepath, String file)
			throws IOException, CsvException, InterruptedException, URISyntaxException, UnsupportedAudioFileException {
		startTime = getTime(0);
		oldCallId=CommonSteps.commonMap.get("callId");
//		Assert.assertEquals(200,(request.auth().oauth2(token).get("refresh/asr-intances")).getStatusCode());
		cs.the_request_with_is_sent_to_the_audio_connector(filepath);
		cs.add_wait_for_call(filepath);
//		sum.edit_and_submit_summary("Time to deliver","5 to 7 business days");
//		sum.submit_edited_summaries();
//		cs.the_request_call_id_is();
//		newCallId=CommonSteps.commonMap.get("callId");
		cs.the_request_with_is_sent_to_the_audio_connector(file);
		cs.add_wait_for_call(file);
		endtime = getTime(0);
		reportData = null;
		reportData = downloadReport("SummaryDetails");
		compareSummaryEvent();
//		
	}
	
	@Then("I get summary trend report for {string} and {string}")
	public void getSummaryTrendReport(String filepath, String file)
			throws IOException, CsvException, InterruptedException, URISyntaxException, UnsupportedAudioFileException {
		startTime = getTime(0);
		oldCallId=CommonSteps.commonMap.get("callId");
//		Assert.assertEquals(200,(request.auth().oauth2(token).get("refresh/asr-intances")).getStatusCode());
		cs.the_request_with_is_sent_to_the_audio_connector(filepath);
		cs.add_wait_for_call(filepath);
//		sum.edit_and_submit_summary("Time to deliver","5 to 7 business days");
//		sum.submit_edited_summaries();
//		cs.the_request_call_id_is();
//		newCallId=CommonSteps.commonMap.get("callId");
		cs.the_request_with_is_sent_to_the_audio_connector(file);
		cs.add_wait_for_call(file);
		endtime = getTime(0);
		reportData = null;
		reportData = downloadReport("SummaryTrends");
		compareSummaryTrend();
//		
	}

	
	@SuppressWarnings("deprecation")
	public Map<String, Map<String, String>> downloadReport(String type)
			throws IOException, CsvException, InterruptedException {
		switch (type) {
		case "Summary":
			download(type);
			break;
		case "ContactDetails":
			downloadContact(type);
			break;
		case "ContactEvents":
			downloadContact(type);
			break;
		case "SummaryDetails":
			downloadContact(type);
			break;
		case "SummaryTrends":
			download(type);
			break;
		}
		if (data.contains("Report is not available for given filters")) {
			Thread.sleep(120000);
			downloadReport(type);
		}
		System.out.println(data);
		if(data.contains("[")) {
			String regex = "(?<=\\[[^\\]\\[]{0,10000}),(?=[^\\]\\[]*])";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(data);
			data=matcher.replaceAll("-");
		}
		data = StringUtils.replaceAll(data, "\r", "");
		String dataArray[] = data.split("\n");
		String keys = dataArray[0];
		String keys2 = dataArray[1];
		Map<String, Map<String, String>> outerMap = new HashMap<>();
		List<String> keysFromFile = new ArrayList<>();
		keyArr = keys.split(",");
		keysFromFile.addAll(Arrays.asList(keyArr));
		if (keys.length() < keys2.length()) {

			keysFromFile.add("Count");
		}

		for (int k = 1; k < dataArray.length; k++) {
			Map<String, String> map = new HashMap<>();
			List<String> row = new ArrayList<>();
			String[] rowArr = dataArray[k].split(",");
			row.addAll(Arrays.asList(rowArr));
			if (!row.isEmpty()) {
				keyForTest = row.get(0);
				boolean present = Arrays.asList(rowArr).contains(keyForTest);
				if (present) {
					for (int l = 1; l < row.size(); l++) {
						if (row.size() > keysFromFile.size() && type!="SummaryDetails") {
							keysFromFile = new ArrayList<>();
							keysFromFile.addAll(Arrays.asList(rowArr));
						}
						map.put(keysFromFile.get(l).trim(), row.get(l).trim());
					}
					outerMap.put(keyForTest, map);
				}
			}
		}
		return outerMap;
	}

	
	public void download(String type) throws IOException {
		token = cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");
		map.put("startDate", startTime);
		map.put("endDate", endtime);
		if(!type.equalsIgnoreCase("Summary")) {
			map.put("typeOfAggregation", "Daily");
			map.put("typeOfSummary", "ACW");
		}
		String uri=type.equalsIgnoreCase("Summary")?"/report/csv/aggregation/summary":"/report/csv/aggregation/summary/trends";
		byte[] dowloadedFile = request.log().all().auth().oauth2(token).contentType("application/zip").params(map)
				.get(uri).then().extract().asByteArray();
		System.out.println("Download File Size : " + dowloadedFile.length);
		String name=type.equalsIgnoreCase("Summary")?"summary.csv":"summaryTrends.csv";
		FileOutputStream os = new FileOutputStream(
				new File(System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\"+name));
		os.write(dowloadedFile);
		os.close();
		File file = new File(
				System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\"+name);
		byte[] bytes = FileUtils.readFileToByteArray(file);
		data = new String(bytes);
		
	}

	
	@SuppressWarnings("resource")
	public void downloadContact(String type) throws IOException {
		token = cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");
		map.put("startDate", startTime);
		map.put("endDate", endtime);
		byte[] dowloadedFile = null;
		switch(type) {
		case "ContactDetails":
			 dowloadedFile = request.log().all().auth().oauth2(token).contentType("application/zip").params(map)
			.get("report/csv/contact-details").then().extract().asByteArray();
			 break;
		case "ContactEvents":
			HashMap<String, String> sessionId=new HashMap<>();
			sessionId.put("sessionIds",CommonSteps.commonMap.get("callId"));
			dowloadedFile = request.log().all().auth().oauth2(token).contentType("application/zip").params(sessionId)
			.get("report/csv/contact-events").then().extract().asByteArray();
			 break;
		case "SummaryDetails":
			dowloadedFile = request.log().all().auth().oauth2(token).contentType("application/zip").params(map)
			.get("report/csv/summary-details").then().extract().asByteArray();
			 break;
		}
		System.out.println("Download File Size : " + dowloadedFile.length);
		String zipname = System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\results.zip";
		FileOutputStream fs = new FileOutputStream(new File(zipname));
		fs.write(dowloadedFile);
		fs.close();
		FileInputStream fis = new FileInputStream(zipname);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;
		File file = null;
		while ((entry = zis.getNextEntry()) != null) {
			System.out.println("Unzipping: " + entry.getName());
			int size;
			byte[] buffer = new byte[2048];
			FileOutputStream fos = null;
			
			switch(type) {
			case "ContactDetails":
				 fos= new FileOutputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\contactDetails.csv");
				 break;
			case "ContactEvents":
				fos= new FileOutputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\contactEvents.csv");
					break;
			case "SummaryDetails":
				fos= new FileOutputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\summaryDetails.csv");
				break;
			}
			BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
			while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, size);
			}
			bos.flush();
			bos.close();
		}
		switch(type) {
		case "ContactDetails":
			file = new File(
					System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\contactDetails.csv");
			 break;
		case "ContactEvents":
			file = new File(
					System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\contactEvents.csv");
			break;
		case "SummaryDetails":
			file = new File(
					System.getProperty("user.dir") + "\\src\\test\\resources\\Operational Reports\\summaryDetails.csv");
			break;
		}
		
		byte[] bytes = FileUtils.readFileToByteArray(file);
		data = new String(bytes);
	}

	
	public static void extract(ZipInputStream zip, File target) throws IOException {
		try {
			ZipEntry entry;
			int BUFFER_SIZE = 4096;
			while ((entry = zip.getNextEntry()) != null) {
				File file = new File(target, entry.getName());

				if (!file.toPath().normalize().startsWith(target.toPath())) {
					throw new IOException("Bad zip entry");
				}

				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}

				byte[] buffer = new byte[BUFFER_SIZE];
				file.getParentFile().mkdirs();
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				int count;

				while ((count = zip.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();
			}
		} finally {
			zip.close();
		}
	}

	public void compareListforSummary(String summary) {
		switch (summary) {
		case "topOfFunnel":
			compareTopofFunnel();
			break;
		case "Features":
			getCompareFeaturesSummary();
			break;
		}
//		
	}

	public void compareContactList() {
		getDifferenceSummary(oldCallId, "language", "en-us");
		getDifferenceSummary(oldCallId, "ASR Engine", "UNIPHORE");
		getDifferenceSummary(oldCallId, "Ghost Contact", "NO");
		getDifferenceSummary(oldCallId, "Transcript Generation", "YES");
		getDifferenceSummary(oldCallId, "Total NLP entities", "15");
		getDifferenceSummary(oldCallId, "Total AI entities", "3");
		getDifferenceSummary(oldCallId, "Total Rule entities", "2");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Summary", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Disposition", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Summary Edited", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Edited", "YES");
		
		getDifferenceSummary(newCallId, "language", "en-us");
		getDifferenceSummary(newCallId, "ASR Engine", "UNIPHORE");
		getDifferenceSummary(newCallId, "Is ACW Meaningful Summary", "NO");
		getDifferenceSummary(newCallId, "Is ACW Request Success", "YES");
		getDifferenceSummary(newCallId, "Is ACW Meaningful Disposition", "NO");
		getDifferenceSummary(newCallId, "Is ACW Summary Edited", "NO");
		getDifferenceSummary(newCallId, "Is ACW Disposition Request Success", "YES");
		getDifferenceSummary(newCallId, "Is ACW Disposition Edited", "NO");
		
	}
	
	public void compareContactEvent() {
		getDifferenceSummary(oldCallId, "language", "en-us");
		getDifferenceSummary(oldCallId, "ASR Engine", "UNIPHORE");
		getDifferenceSummary(oldCallId, "Ghost Contact", "NO");
		getDifferenceSummary(oldCallId, "Transcript Generation", "YES");
		getDifferenceSummary(oldCallId, "Total NLP entities", "15");
		getDifferenceSummary(oldCallId, "Total AI entities", "3");
		getDifferenceSummary(oldCallId, "Total Rule entities", "2");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Summary", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Disposition", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Summary Edited", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Edited", "YES");
	}
	
	public void compareSummaryEvent() {
		getDifferenceSummary(oldCallId, "Total NLP entities", 7);
		getDifferenceSummary(oldCallId, "Total AI entities", 1);
		getDifferenceSummary(oldCallId, "Total Rule entities", 1);
	}
	
	public void compareSummaryTrend() {
		getDifferenceSummary(keyForTest, "Total contacts received at logger", 2);
		getDifferenceSummary(keyForTest, "Total contacts with meaningful summaries", 2);
	}
	
	public void compareSummaryDetails() {
		
		
		getDifferenceSummary(oldCallId, "Customer Option:", "Customer Option:");
		getDifferenceSummary(oldCallId, "ASR Engine", "UNIPHORE");
		getDifferenceSummary(oldCallId, "Ghost Contact", "NO");
		getDifferenceSummary(oldCallId, "Transcript Generation", "YES");
		getDifferenceSummary(oldCallId, "Total NLP entities", "15");
		getDifferenceSummary(oldCallId, "Total AI entities", "3");
		getDifferenceSummary(oldCallId, "Total Rule entities", "2");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Summary", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Meaningful Disposition", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Summary Edited", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Request Success", "YES");
		getDifferenceSummary(oldCallId, "Is ACW Disposition Edited", "YES");
	}
	
	
	public void compareTopofFunnel() {
		getDifferenceSummary(Total_agents_who_received_at_least_1_call, Count, 1);
		getDifferenceSummary(Total_contacts_received_at_logger, Count, 1);
		getDifferenceSummary(Total_ghost_contacts, Count, 1);
		getDifferenceSummary(Contacts_with_transcripts, Count, 1);
		getDifferenceSummary(Total_contacts_with_on_demand_requests, Count, 0);
		getDifferenceSummary(Total_On_Demand_Requests, Count, 0);
		getDifferenceSummary(Total_contacts_with_at_least_1_NLPAI_entity_extracted, Count, 1);

	}

	public void getCompareFeaturesSummary() {
		getDifferenceSummary(Total_contacts_with_summaries_generated, End_of_call, 1);
		getDifferenceSummary(Total_summaries_generated, End_of_call, 1);
		getDifferenceSummary(Total_default_Summaries, End_of_call, 0);
		getDifferenceSummary(Total_meaningful_summaries, End_of_call, 1);
		getDifferenceSummary(Total_contacts_with_summary_failed, End_of_call, 0);
	}

	public void getDifferenceSummary(String parameter, String value, int diff) {

		Assert.assertEquals(diff, (Integer.parseInt(reportData.get(parameter).get(value))));

	}
	
	public void getDifferenceSummary(String parameter, String value, String diff) {
		String key=new String(parameter);
		System.out.println(reportData.get(key).get(value));
		Assert.assertEquals(diff, (reportData.get(parameter).get(value)));

	}

	@Then("I update the gost-call-controller")
	public void update_ghost_call_controller() {
		token = cs.getToken("default-admin");
		loadURL("BACKEND_PORT");
		response = request.log().all().auth().oauth2(token).get("contact-filters");
		JSONObject entityList = new JSONObject(response.getBody().asString()).getJSONObject("data");
		entityList.put("enabled", "true");
		JSONObject feature = entityList.getJSONObject("filters");
		String featureFlags = feature.toString();
		JSONObject newFeature = new JSONObject();
		for (String str : featureFlags.toString().substring(1, featureFlags.length() - 1).split(",")) {
			Integer indexOfSeparation = str.indexOf(":");
			String entity = str.substring(1, indexOfSeparation - 1);
			if (entity.equalsIgnoreCase("maxSilenceDuration")) {
				newFeature.put(entity, 20000);
			} else if (entity.equalsIgnoreCase("minContactDuration")) {
				newFeature.put(entity, 10000);
			} else {
				newFeature.put(entity, 15000);
			}

		}
		entityList.put("filters", newFeature);
		token = cs.getToken("default-admin");
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON).body(entityList.toString()).when();
		response = request.log().all().auth().oauth2(token).headers("X-Username", port.getProperty("X-Username"))
				.put("contact-filters");
		Assert.assertEquals(200, response.getStatusCode());
	}

}
