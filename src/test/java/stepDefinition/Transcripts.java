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

	@Then("a transcript is generated for callId")
	public void a_transcript_is_generated_for_callid() throws IOException, URISyntaxException {
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		response=request.get("transcripts");
		Assert.assertEquals(200,response.getStatusCode());
	}
	
	
	@Then("the transcript conversation for callId for {int} has {string}")
	public void the_transcript_conversation_for_callid_has(Integer turnNum, String correctString)
			throws IOException, URISyntaxException, JSONException {
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> jsonToMap=request.get("conversations").as(Map.class);
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
		HashMap<String, String> map = new HashMap<>();
		map.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");loadQueryparams(map);
		

		Map<?, ?> jsonToMap=(request.log().all().get("conversations")).as(Map.class);
		
		System.out.println("jsonToMap "+jsonToMap);

		Map<String, Object> tempMap = CommonSteps
				.jsonStringToMap(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonToMap.get("data")));
		JSONArray responseTranscriptArr = new JSONArray(
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tempMap.get("turns")));
		File jsonFile = new File(jsonFilePath);
		String jsonFileString = new String(Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath())));
		System.out.println("jsonFileString "+jsonFileString);
		JSONObject transcriptJsonFileObj = new JSONObject(jsonFileString);
		JSONObject jsonFileData = (JSONObject) transcriptJsonFileObj.get("data");
		JSONArray jsonFileArr = new JSONArray(jsonFileData.get("turns").toString());
		System.out.println(jsonFileArr.length());
		Assert.assertEquals(responseTranscriptArr.length(), jsonFileArr.length());

		conversationTurns = responseTranscriptArr.length();

		for (int i = 0; i < conversationTurns; i++) {

			JSONObject responseConversationTurn = responseTranscriptArr.getJSONObject(i);
			String responseConversationText = responseConversationTurn.get("text").toString();

			JSONObject jsonFileConversationTurn = jsonFileArr.getJSONObject(i);
			String jsonConversationText = jsonFileConversationTurn.get("text").toString();

			org.apache.commons.text.similarity.LevenshteinDistance LDist = new org.apache.commons.text.similarity.LevenshteinDistance();

			org.apache.commons.text.similarity.JaroWinklerSimilarity JWink = new org.apache.commons.text.similarity.JaroWinklerSimilarity();

			double JWinkSimilarity = JWink.apply(responseConversationText, jsonConversationText) * 100;

			System.out.println(jsonConversationText + "\n" + responseConversationText);
			System.out.println("Levenshtein Distance is: " + LDist.apply(responseConversationText, jsonConversationText));
			System.out.println("JaroWinkler Similarity is: " + JWinkSimilarity + "%\n");

			Assert.assertTrue(JWinkSimilarity >=90.0);

		}
		System.out.println("\nTranscript Matches Gold JSON File\n");
	}
	
	
	@Then("verify transcript turns for supervisor")
	public void verify_transcript_supervisor() throws JsonProcessingException, IOException {
		loadURL("BACKEND_PORT");request.queryParam("sessionId", CommonSteps.commonMap.get("callId"));
		Map<?, ?> jsonToMap=(request.log().all().get("/supervisor/api/transcript")).as(Map.class);
		
		System.out.println("jsonToMap "+jsonToMap);

		Map<String, Object> tempMap = CommonSteps
				.jsonStringToMap(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonToMap.get("data")));
		JSONArray responseTranscriptArr = new JSONArray(
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tempMap.get("data")));
		Assert.assertEquals(responseTranscriptArr.length(),conversationTurns);
	}
}
