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

	@Given("configure alerts in folder {string}")
	public void define_and_configure_entities_in_folder(String folderPath) throws IOException, URISyntaxException {
    
	String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folderPath).getAbsolutePath())
			.toString();
	
	File entityDefinitionFolder = new File(absoluteFolderPath + "/configurations");
	
	for (File defineFile : entityDefinitionFolder.listFiles()) {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		
		String defineJsonString = new String(Files.readAllBytes(Paths.get(defineFile.getAbsolutePath())));
		
		JSONObject defineJsonFileObj = new JSONObject(defineJsonString);
		
		request.body(defineJsonFileObj.toString()).when();
		response=request.log().all().post("alert/configure");
		
		Assert.assertEquals(200,response.getStatusCode());
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
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("allalertObjects");

		JSONArray alertList=new JSONObject(response.getBody().asString()).getJSONArray("data");
		JSONArray payload=new JSONArray();
		for (int i = 0; i < alertList.length(); i++) {
			JSONObject temp = alertList.getJSONObject(i);
			payload.put(temp.get("name"));
		}

		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		request.body(payload.toString());
		Assert.assertEquals(200, (request.post("train/alert-selected")).getStatusCode());
	}
	
	
	
	@Then("delete all alerts")
	public void delete_all_entites() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("allalertObjects");
	
		JSONArray alertList=new JSONObject(response.getBody().asString()).getJSONArray("data");
		
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		for(int i = 0; i < alertList.length(); i++) {
			Assert.assertEquals(200,(request.delete("alert/" +  alertList.getJSONObject(i).get("name")).getStatusCode()));
		}
	}

}
