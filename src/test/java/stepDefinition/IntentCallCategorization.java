package stepDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.Then;
import io.restassured.RestAssured;

public class IntentCallCategorization extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	
	public static HashMap<String, String> orgMap=new HashMap<>();
	public static String token=null;

	
	@Then("define and configure call categorization with folder {string}")
	public void define_and_configure_call_categorization(String folderPath) throws IOException, URISyntaxException, JSONException{
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");
		
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folderPath).getAbsolutePath()).toString().replace("%20", " ");
		System.out.println(absoluteFolderPath);
		File callCatDefinitionFolder = new File(absoluteFolderPath + "/definitions");
		File callCatConfigFolder = new File(absoluteFolderPath + "/configurations");
		
		for(File defineFile : callCatDefinitionFolder.listFiles()) {
			String configJson = new String(Files.readAllBytes(Paths.get(defineFile.getAbsolutePath())));
			JSONObject defineJson=new JSONObject(configJson);
//			System.out.println("Path is "+Paths.get(defineFile.getAbsolutePath()).toString());
			loadQueryparams(CommonSteps.orgMap);
			request.body(defineJson.toString()).when();
			response=request.auth().oauth2(token).post("call-category/define/json");
			
		    Assert.assertEquals(200,response.getStatusCode());
		}
		
		for(File configFile : callCatConfigFolder.listFiles()) {
			String configJsonString = new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())));
		    JSONObject defineJsonFileObj = new JSONObject(configJsonString); 
		    loadURL("BACKEND_PORT");
		    loadQueryparams(CommonSteps.orgMap);
		    request.body(defineJsonFileObj.toString()).when();
			response=request.auth().oauth2(token).post("call-category/configure");
			Assert.assertEquals(200,response.getStatusCode());
		}
	}
	
}
