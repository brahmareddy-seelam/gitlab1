package stepDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Summaries extends BaseClass{

	CommonSteps cs=new CommonSteps();
	
	public static HashMap<String, String> orgMap=new HashMap<>();
	
	public static LinkedHashMap<String, String> summariesMap=new LinkedHashMap<>();
	public static String requestId=null;
	public static String sessionId=null;
	public static String startDate=null;
	public static String endDate=null;
	
	@Then("a summary for callId exists") 
	public void a_summary_for_callid_exists_with_intent()throws IOException, URISyntaxException, JSONException {
		loadURL("DATA_COLLECTOR_PORT");
		request.log().all().header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
	    response=request.log().all().get("summaries/" + CommonSteps.commonMap.get("callId") );
	  
	  JSONObject responseJSONObj = new JSONObject(response.body().asString());
	  
	  JSONObject summaryText = new JSONObject(responseJSONObj.getString("text"));
	  
	  String entitiesStr = summaryText.get("bullet").toString();
	  LinkedHashMap<String,String> entitiesMap = new LinkedHashMap<String,String>();
	  
	  for (String str : entitiesStr.toString().substring(1,entitiesStr.length()-1).split(",\"")){
		  Integer indexOfSeparation = str.indexOf(":");
		  String entity = str.substring(0, indexOfSeparation);
		  String entityVal = (indexOfSeparation + 2<str.length()?str.substring(indexOfSeparation + 2, str.length()-1):str.substring(indexOfSeparation + 2, str.length()-0));
		  entitiesMap.put(entity.replaceAll("\"", ""), entityVal.trim());
	  }
	  
	  TestCenter.getInstance().setSummary(entitiesMap);

	}
	
	
	@Then("a summary for callId has intent of {string}")
	public void summary_has_intent_of(String intent) throws JSONException, IOException, URISyntaxException {
		loadURL("DATA_COLLECTOR_PORT");
		request.log().all().header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
		response=request.get("summaries/" + CommonSteps.commonMap.get("callId"));
		JSONObject responseJSONObj = new JSONObject(response.body().asString());
		requestId=responseJSONObj.getString("requestId");
		System.out.println(requestId);
		sessionId=responseJSONObj.getString("sessionId");
		JSONObject summaryText = new JSONObject(responseJSONObj.getString("text"));
		
		System.out.println("\nCall Intent: " + summaryText + "\n");
		Assert.assertEquals(intent,responseJSONObj.getString("intent"));
	}
	
	
	@Then("a summary for callId has {string} {string}")
	public void a_summary_for_callid_has_entity_in_summary(String entityName, String entityValue) throws IOException, URISyntaxException {
		String summaryEntityValue = TestCenter.getInstance().getSummaryEntityVal(entityName);
		System.out.println(entityName + ": " + summaryEntityValue);
		
	    org.apache.commons.text.similarity.LevenshteinDistance LDist = new org.apache.commons.text.similarity.LevenshteinDistance();
	    
	    org.apache.commons.text.similarity.JaroWinklerSimilarity JWink = new org.apache.commons.text.similarity.JaroWinklerSimilarity();
	    
	    double JWinkSimilarity = JWink.apply(entityValue, summaryEntityValue) * 100;
	    Integer LDistanceValue = LDist.apply(entityValue, summaryEntityValue);
	    
	    System.out.println("Expected: " + entityValue + "\n" + "Received: " + summaryEntityValue);
	    System.out.println("Levenshtein Distance is: " + LDistanceValue);
	    System.out.println("JaroWinkler Similarity is: " + JWinkSimilarity + "%\n");
	        
	    Assert.assertTrue(JWinkSimilarity >= 90.0 || LDistanceValue <= 4 || summaryEntityValue.contains(entityValue));
	 
	}

	
	@Then("post summary format")
	  public void post_summary_format() throws IOException, URISyntaxException, JSONException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		Map<?, ?> jsonToMap=request.get("entitylist").as(Map.class);
		JSONObject entityListArray = new JSONObject(jsonToMap);
		JSONArray entityList=entityListArray.optJSONArray("data");
	  String entitiesStr = new String();
	  for (int i = 0; i < entityList.length(); i++) {
	  JSONObject temp = entityList.getJSONObject(i);
	  String entityForSummaryFormat = '"' + temp.get("name").toString() + ": @@" + temp.get("id").toString()+ "@@" + '"' + ',';
	  entitiesStr += entityForSummaryFormat;
	  }

	  System.out.println(entitiesStr);

	  String bulletPointStr = '[' + entitiesStr.substring(0, entitiesStr.length()-1) + ']';

	  String paragraphStr = "{\"header\":{\"label\":\"\",\"body\":\"\"},\"body\":{\"label\":\"\",\"body\":\"\"},\"footer\":{\"label\":\"\",\"body\":\"\"}}";
	  JSONObject summaryFormat = new JSONObject();

	  summaryFormat.put("outputFormat", "bulletPoint");

	  summaryFormat.put("paragraph", paragraphStr);
	  summaryFormat.put("bulletPoint", bulletPointStr);

	  System.out.println("summaryFormat "+summaryFormat);
	  request=RestAssured.given();
	  request.log().all().header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken).queryParams(CommonSteps.commonMap);
	  request.body(summaryFormat.toString());
	  response=request.post("save-summary-format");

	  Assert.assertEquals(200,response.getStatusCode());
	  System.out.println(response.getBody().toString());

	  }

	
//	  @Then("a summary is POSTed for {string}")
//	  public void a_summary_is_posted_for(String callId) throws IOException, URISyntaxException {
//
//	  // Get the transcript
//	  MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//	  map.add("contactId", TestCenter.getInstance().getCallId(callId));
//
//	  String transcriptJSONstr = RestRequest.getResponseBack(TestCenter.BACKEND_PORT, "transcripts", map, true).getBody().toString();
//
//	  ResponseEntity<String> response = RestRequest.postResponseBack(TestCenter.NLP_WRAPPER_PORT, "summary" , RestRequest.transcriptCleanup(transcriptJSONstr));
//
//
//	  System.out.println(response.getBody());
//	  }
//	
	
	
	@Then("edit {string} as {string}")
	public void edit_and_submit_summary(String key, String value) {
		summariesMap=(LinkedHashMap<String, String>) TestCenter.getInstance().getSummary();
		summariesMap.put(key, value);
	}
	
	
	@Then("submit the edited summaries")
	public void submit_edited_summaries() {
		StringBuilder bulletValue = new StringBuilder("[");
	    for (String key : summariesMap.keySet()) {
	    	bulletValue.append("\""+key + ": " + summariesMap.get(key) + "\", ");
	    }
	    bulletValue.delete(bulletValue.length()-2, bulletValue.length()).append("]");
	    bulletValue.toString();
	    HashMap<String, String> params=new HashMap<>();
	    params.put("userId",(String) getUserId());
	    params.put("callId", CommonSteps.commonMap.get("callId"));
	    String summary="[{\"requestId\":\""+requestId+"\",\"data\":{\"format\":\"bulletPoint\",\"bullet\": "+bulletValue+",\"paragraph\":null}}]";
	    loadURL("BACKEND_PORT");loadQueryParams(params);
	    request.contentType(ContentType.JSON).body(summary);
	    response=request.put("acw/summaries");
	    Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	@Then("compare if {string} has {string} for callId")
	public void compare_edited_summary(String key, String editedValue) {
		boolean status = false;
		 JSONArray changesArray=new JSONArray();
		JSONArray summaryData=getEditedSummary(CommonSteps.commonMap.get("callId"));
		for(int i=0; i<summaryData.length(); i++){   
			  JSONObject summary = summaryData.getJSONObject(i);  
			  System.out.println(summary); 
			  changesArray= new JSONArray(summary.get("summary").toString());
			}
		System.out.println(changesArray.toString());
		for(int i=0;i<changesArray.length();i++) {
			Object value = null;
			JSONObject item = changesArray.getJSONObject(i);
			if (item.has("edited"))
	        {
	            value = item.get("edited");
	            String expected=key+": "+editedValue;
	            if(value.equals(expected))
	            {
	            	System.out.println(expected);
	            status=true;
	            }
	        }
		}
		Assert.assertTrue(status);
	}
	
	public String getUserId() {
		loadURL("KEYCLOAK_PORT");
		HashMap<String, String> map = new HashMap<>();
		map=new HashMap<String, String>();
		map.put("grant_type", "password");
		map.put("client_id", port.getProperty("client_id"));
		map.put("username", port.getProperty("username"));
		map.put("password", port.getProperty("password"));
		
		response=request.log().all().formParams(map).post("auth/realms/"+port.getProperty("realm")+"/protocol/openid-connect/token");
		jsonPathEvaluator = response.jsonPath();
		String access_token = jsonPathEvaluator.get("access_token");
		System.out.println(access_token);
		TestCenter.getInstance().setAccessToken(access_token);
		
		loadURL("OCMS_PORT");
		request.auth().oauth2(access_token);
		response=request.post("configuration/sync-user");
		Map<Object, Object> user=response.jsonPath().getMap("user");
		startDate = String.valueOf(user.get("creation-date"));
		endDate=String.valueOf(user.get("modification-date"));
		Object id=   user.get("id");
		return id.toString();
	}
	
	
	public JSONArray getEditedSummary(String callId) {
//		JSONObject dateParams = new JSONObject();
		HashMap<String, String> date=new HashMap<>();
		date.put("startDate", startDate);
		date.put("endDate", endDate);
		date.put("contactId", callId);
		loadURL("BACKEND_PORT");
		request.log().all().header("Authorization", "Bearer "+TestCenter.accesstoken).header("Content-Type","application/json")
		.queryParams(date);
		response=request.get("entities/summaries/compare");
		System.out.println(response.body().asString());
		JSONObject editedSummary = new JSONObject(response.body().asString());
		JSONArray summaryData = editedSummary.getJSONArray("data");
		return summaryData;
	}
}
