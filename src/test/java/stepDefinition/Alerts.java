package stepDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class Alerts extends BaseClass{
 
	CommonSteps cs=new CommonSteps();
	
	public static String token=null;
	
	@Given("configure alerts in folder {string}")
	public void define_and_configure_entities_in_folder(String folderPath) throws IOException, URISyntaxException {
    
	token=cs.getToken("default-analyst");
	String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folderPath).getAbsolutePath())
			.toString();
	
	File entityDefinitionFolder = new File(absoluteFolderPath + "/configurations");
	
	for (File defineFile : entityDefinitionFolder.listFiles()) {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		
		String defineJsonString = new String(Files.readAllBytes(Paths.get(defineFile.getAbsolutePath())));
		
		JSONObject defineJsonFileObj = new JSONObject(defineJsonString);
		
		request.body(defineJsonFileObj.toString()).when();
		response=request.log().all().auth().oauth2(token).post("alert/configure");
		
		Assert.assertTrue((response.getStatusCode())==200||(response.getStatusCode())==409);
		}
/*
	
	for(File configFile : entityConfigFolder.listFiles()) {
		String configJsonString = new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())));
	    JSONObject defineJsonFileObj = new JSONObject(configJsonString); 
	    ResponseEntity<String> configResponse = RestRequest.postResponseBack(TestCenter.BACKEND_PORT, "configure/entity?", TestCenter.getInstance().getRestRequest().getMap(), defineJsonFileObj);
	    Assertions.assertEquals(configResponse.getStatusCodeValue(), 200);
	}
	*/
	}
	
	
	
	@Then("train Alerts")
	public void train_all_alerts() {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.auth().oauth2(token).get("allalertObjects");

		JSONArray alertList=new JSONObject(response.getBody().asString()).getJSONArray("data");
		JSONArray payload=new JSONArray();
		for (int i = 0; i < alertList.length(); i++) {
			JSONObject temp = alertList.getJSONObject(i);
			payload.put(temp.get("name"));
		}

		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		request.body(payload.toString());
		Assert.assertEquals(200, (request.auth().oauth2(token).post("train/alert-selected")).getStatusCode());
	}
	
	
	@Given("verify that supervisor has alert {string} with type {string}")
	public void supervisor_alerts(String alert, String alert_type) {
		token=cs.getToken("default-analyst");
		loadURL("SMS_PORT");
		request.queryParams("sessionId",CommonSteps.commonMap.get("callId"));
		response=request.log().all().auth().oauth2(token).get("/supervisor/api/alert");
		JSONArray langObj = new JSONArray(response.body().asString());
		for (int i = 0; i < langObj.length(); i++) {
			JSONObject item = langObj.getJSONObject(i);
			if (item.has("name")) {
				if (item.get("name").toString().equalsIgnoreCase(alert)
						&& item.get("type").toString().equalsIgnoreCase(alert_type)) {
					System.out.println("Alert sent as expected!");
				}
				}
		}
	}
	
	@Then("delete all alerts")
	public void delete_all_entites() throws IOException, URISyntaxException {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.auth().oauth2(token).get("allalertObjects");
	
		JSONArray alertList=new JSONObject(response.getBody().asString()).getJSONArray("data");
		
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		for(int i = 0; i < alertList.length(); i++) {
			response=request.delete("alert/" +  alertList.getJSONObject(i).get("name"));
			Assert.assertTrue((response.getStatusCode())==200 || (response.getStatusCode())==404);
		}
	}

}
