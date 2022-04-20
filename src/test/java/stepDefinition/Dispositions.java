package stepDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.Then;

public class Dispositions extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	Summaries sm=new Summaries();
	public static HashMap<String, String> orgMap=new HashMap<>();
	
	public static String data=null;
	
	public static StringBuilder finalValue=null;
	public static String requestId=null;
	public static LinkedHashMap<String, String> dataMap=new LinkedHashMap<>();
	@Then("disposition for callId has intent of {string}")
	public void disposition_for_callid_has_intent_of(String intent)
			throws IOException, URISyntaxException, JSONException {
		loadURL("DATA_COLLECTOR_PORT");
		request.log().all().header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
		response=request.get("dispositions/"+ CommonSteps.commonMap.get("callId"));
		System.out.println(response.asString());
		requestId=new JSONObject(response.getBody().asString()).getString("requestId").toString();
		String hierarchy = new JSONObject(response.getBody().asString()).getJSONObject("resolution")
				.getString("hierarchy");
		
		data = new JSONObject(response.getBody().asString()).getJSONObject("resolution")
				.getString("data").replaceAll("}]","},]");
		Assert.assertEquals(hierarchy, intent);
	}
	
	
	
	@Then("edit and submit disposition intent {string} as {string}")
	public void edit_disposition(String existing, String edited) throws StringIndexOutOfBoundsException {
		finalValue=new StringBuilder("[{\"requestId\":\""+requestId+"\",\"data\":[");
		String new_data=data.replace(existing, edited);
		boolean status=false;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		  for (String str : new_data.toString().substring(1,data.length()-2).split(",")){
			  if(!str.contains("]")&&!str.matches("^[1-9][0-9]?$|^100$")&&!str.equals("}")){
				  Integer indexOfSeparation = str.indexOf(":");
				  String entity = str.substring(1, indexOfSeparation);
				  status=(str.contains("level")?true:false);
				  if(!entity.contains("rawTurnIds")) {
					  String entityVal=null;
					  if(indexOfSeparation +1<=str.length()-1) {
						   entityVal = str.substring(indexOfSeparation +1 , str.length()-1);
					  }else {
						   entityVal = str.substring(indexOfSeparation , str.length()-1);
					  }
					  dataMap.put(entity.replaceAll("\"", ""), entityVal.trim());
					  String key=entity.replaceAll("\"", "");
					  String value=entityVal.replaceAll("\"", "").trim();
					  finalValue=(status?finalValue.append("{\""+key+"\":\""+value+"\","):finalValue.append("\""+key+"\":\""+value+"\"},"));
				  }
				  else {}
			  } else {}
			}
			finalValue=finalValue.append("]");
			String payload=(finalValue.toString().replace("\"\"\"", "\"\"")).replace("},]", "}]}]");
			System.out.println(payload);
		HashMap<String, String> data=new HashMap<>();
		data.put("sessionId", CommonSteps.commonMap.get("callId"));
		data.put("userId", sm.getUserId());
		loadURL("BACKEND_PORT");
		request.log().all().header("Authorization", "Bearer "+TestCenter.accesstoken).header("Content-Type","application/json")
		.queryParams(data);
		request.body(payload);
		response=request.put("acw/dispositions");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	@Then("compare if disposition has changed intent from {string} to {string}")
	public void intent_change(String edited, String newValue) {
		boolean status = false;
		JSONArray changesArray=new JSONArray();
		JSONArray dispositionData=getEditedDisposition();
		for(int i=0; i<dispositionData.length(); i++){   
			  JSONObject summary = dispositionData.getJSONObject(i);  
			  System.out.println(summary); 
			  changesArray= new JSONArray(summary.get("hierarchyList").toString());
			}
		System.out.println("Result :"+changesArray.toString());
		for(int i=0;i<changesArray.length();i++) {
			boolean value=false;
			Object  editedValue = null;
			JSONObject item = changesArray.getJSONObject(i);
			if (item.has("isEdited"))
	        {
	            value = (boolean) item.get("isEdited");
	            if(!value==false) {
	            	editedValue=item.get("manualEdits");
	            	if(editedValue.equals(newValue))
		            {
		            System.out.println(editedValue);
		            status=true;
		            break;
		            }
	            }
	        }
		}Assert.assertTrue(status);
	}
	
	
	public JSONArray getEditedDisposition() {
		HashMap<String, String> date=new HashMap<>();
		date.put("startDate", cs.getStartDate());
		date.put("endDate", cs.getEndDate());
		date.put("contactId", CommonSteps.commonMap.get("callId"));
		loadURL("BACKEND_PORT");
		request.log().all().header("Authorization", "Bearer "+TestCenter.accesstoken).header("Content-Type","application/json")
		.queryParams(date);
		response=request.get("/category/disposition/compare");
		JSONObject editedSummary = new JSONObject(response.body().asString());
		JSONArray summaryData = editedSummary.getJSONArray("data");
		return summaryData;
	}
}
