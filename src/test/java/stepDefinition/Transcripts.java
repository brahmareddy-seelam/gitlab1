package stepDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.java.en.Then;	

public class Transcripts extends BaseClass{
	
	CommonSteps cs = new CommonSteps();
	ObjectMapper mapper = new ObjectMapper();
	int conversationTurns=0;
	JSONArray responseTranscriptArray;
	public static JSONArray jsonFileArr;
	public static String token=null;
	public static JSONObject jsonFileData;
	
	@Then("a transcript is generated for callId")
	public void a_transcript_is_generated_for_callid() throws IOException, URISyntaxException {
		token=cs.getToken("default-analyst");
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		response=request.auth().oauth2(token).get("transcripts");
		Assert.assertEquals(200,response.getStatusCode());
	}
	
	
	@Then("the transcript conversation for callId for {int} has {string}")
	public void the_transcript_conversation_for_callid_has(Integer turnNum, String correctString)
			throws IOException, URISyntaxException, JSONException {
		token=cs.getToken("default-analyst");
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> jsonToMap=request.auth().oauth2(token).get("conversations").as(Map.class);
		jsonToMap.keySet().forEach(k->System.out.println(k));
		System.out.println(jsonToMap.get("turns"));

		Map<String, Object> tempMap = CommonSteps
				.jsonStringToMap(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonToMap.get("data")));
		JSONArray newArr = new JSONArray(
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tempMap.get("turns")));
		JSONObject jsonObj = new JSONObject(newArr.get(turnNum).toString());
		System.out.println("\nConversation: " + jsonObj.get("text") + "\n");
		Assert.assertTrue(jsonObj.get("text").toString().contains(correctString));

	}

	
	@Then("the transcript conversation for callId matches the correct version {string}")
	public void the_transcript_conversation_for_callid_matches(String jsonFilePath)
			throws IOException, URISyntaxException, JSONException {
		token=cs.getToken("default-analyst");
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		

		Map<?, ?> jsonToMap=(request.log().all().auth().oauth2(token).contentType("application/json; charset=utf-8").get("conversations")).as(Map.class);
		
		System.out.println("jsonToMap "+jsonToMap);

		Map<String, Object> tempMap = CommonSteps
				.jsonStringToMap(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonToMap.get("data")));
		JSONArray responseTranscriptArr = new JSONArray(
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tempMap.get("turns")));
		File jsonFile = new File(jsonFilePath);
		String jsonFileString = new String(Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath())));
		System.out.println("jsonFileString "+jsonFileString);
		JSONObject transcriptJsonFileObj = new JSONObject(jsonFileString);
		jsonFileData = (JSONObject) transcriptJsonFileObj.get("data");
		jsonFileArr = new JSONArray(jsonFileData.get("turns").toString());
		System.out.println(jsonFileArr.length());
//		Assert.assertEquals(responseTranscriptArr.length(), jsonFileArr.length());

		verify_similarity(responseTranscriptArr, jsonFileArr);
	}
	
	
	@Then("verify transcript turns for supervisor")
	public void verify_transcript_supervisor() throws JsonProcessingException, IOException {
		token=cs.getToken(port.getProperty("username"));
		loadURL("SMS_PORT");
		request.queryParam("sessionId", CommonSteps.commonMap.get("callId"));
		Map<?, ?> jsonToMap=(request.log().all().auth().oauth2(token).get("/supervisor/api/transcript")).as(Map.class);
		
		System.out.println("jsonToMap "+jsonToMap);

		Map<String, Object> tempMap = CommonSteps
				.jsonStringToMap(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonToMap.get("data")));
		JSONArray responseTranscriptArr = new JSONArray(
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tempMap.get("data")));
//		Assert.assertEquals(responseTranscriptArr.length(),conversationTurns);
		verify_similarity(responseTranscriptArr, jsonFileArr);
	}
	
	
	public void verify_similarity(JSONArray recieved, JSONArray jsonFileArr) {
		conversationTurns = recieved.length();

		for (int i = 0; i < conversationTurns; i++) {
			String responseConversationText=null;
			JSONObject responseConversationTurn = recieved.getJSONObject(i);
			if(responseConversationTurn.has("text")) {
				responseConversationText = responseConversationTurn.get("text").toString();
			}
			else {
				responseConversationText = responseConversationTurn.get("keyPhrase").toString();
			}
			JSONObject jsonFileConversationTurn = jsonFileArr.getJSONObject(i);
			String jsonConversationText = jsonFileConversationTurn.get("text").toString();

			org.apache.commons.text.similarity.LevenshteinDistance LDist = new org.apache.commons.text.similarity.LevenshteinDistance();

			org.apache.commons.text.similarity.JaroWinklerSimilarity JWink = new org.apache.commons.text.similarity.JaroWinklerSimilarity();

			double JWinkSimilarity = JWink.apply(responseConversationText, jsonConversationText) * 100;

			System.out.println(jsonConversationText + "\n" + responseConversationText);
			System.out.println("Levenshtein Distance is: " + LDist.apply(responseConversationText, jsonConversationText));
			System.out.println("JaroWinkler Similarity is: " + JWinkSimilarity + "%\n");

			Assert.assertTrue(JWinkSimilarity >=95.0);

		}
		System.out.println("\nTranscript Matches Gold JSON File\n");
	
	}
}
