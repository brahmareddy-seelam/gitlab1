package stepDefinition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;


public class Entities extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	public static String token=null;
	public static JSONArray entityListArray ;
	
	
	
	@Then("define and configure entities in folder {string}")
	public void define_and_configure_entities_in_folder(String folderPath) throws IOException, URISyntaxException, JSONException {
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folderPath).getAbsolutePath())
				.toString();
		token=cs.getToken("default-analyst");
		File entityDefinitionFolder = new File(absoluteFolderPath + "/entity-definitions");
		File entityConfigFolder = new File(absoluteFolderPath + "/configurations");
		
		System.out.println("orgMap " +CommonSteps.orgMap);
		//To define Entities
		for (File defineFile : entityDefinitionFolder.listFiles()) {
			loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
			String defineJsonString = new String(Files.readAllBytes(Paths.get(defineFile.getAbsolutePath())));
			JSONObject defineJsonFileObj = new JSONObject(defineJsonString);
			System.out.println("defineJsonFileObj >>>>>> " + defineJsonFileObj);
			request.body(defineJsonFileObj.toString()).when();
			response=request.log().all().auth().oauth2(token).post("define/entity");
			Assert.assertEquals(200,response.getStatusCode());
		}
		//To configure entities
		for (File configFile : entityConfigFolder.listFiles()) {
			loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
			String configJsonString = new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())));
			JSONObject defineJsonFileObj = new JSONObject(configJsonString);
			request.body(defineJsonFileObj.toString()).when();
			response=request.auth().oauth2(token).post("configure/entity");
			if(response.getStatusCode()==409) {
				String name=defineJsonFileObj.getString("name");
				loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
				response=request.auth().oauth2(token).delete("entity"+name);
				}
			Assert.assertEquals(200,response.getStatusCode());
		}
	}
	
	
	
	@Then("entities for callId exist")
	public void a_summary_for_callid_exists_with_intent() throws IOException, URISyntaxException, JSONException {
		HashMap<Integer, String> entityIdToName = new HashMap<Integer, String>();
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.auth().oauth2(token).get("entitylist");
		JSONArray entityList=new JSONObject(response.getBody().asString()).getJSONArray("data");
		for (int i = 0; i < entityList.length(); i++) {
			JSONObject temp = entityList.getJSONObject(i);
			entityIdToName.put(temp.getInt("id"), temp.get("name").toString());
		}
		
		loadURL("DATA_COLLECTOR_PORT");
		request.log().all().auth().oauth2(token).header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
		response=request.get("entities/"+CommonSteps.commonMap.get("callId"));
		JSONArray entitiesJSONArray = new JSONArray(response.getBody().asString());
		System.out.println(entitiesJSONArray);
		HashMap<String, String> entitiesMap = new HashMap<String, String>();
		for (int i = 0; i < entitiesJSONArray.length(); i++) {
			JSONObject temp = entitiesJSONArray.getJSONObject(i);
			{
			entitiesMap.put(entityIdToName.get(temp.getInt("entityId")), temp.getString("text").trim());
			}
		}
		TestCenter.getInstance().setEntitiesMap(entitiesMap);
		System.out.println(entitiesMap.toString());
		Assert.assertNotNull(entitiesMap);

	}
	
		
	@Then("the entity for callId has {string} as {string}")
	public void the_entity_for_has(String entityName, String entityValue)
			throws IOException, URISyntaxException {

		String entitiesEntityValue = TestCenter.getInstance().getEntitiesEntityVal(entityName);
		System.out.println(entityName + ": " + entitiesEntityValue);

		org.apache.commons.text.similarity.LevenshteinDistance LDist = new org.apache.commons.text.similarity.LevenshteinDistance();

		org.apache.commons.text.similarity.JaroWinklerSimilarity JWink = new org.apache.commons.text.similarity.JaroWinklerSimilarity();

		double JWinkSimilarity = JWink.apply(entityValue, entitiesEntityValue) * 100;
		Integer LDistanceValue = LDist.apply(entityValue, entitiesEntityValue);

		System.out.println("Expected: " + entityValue + "\n" + "Received: " + entitiesEntityValue);
		System.out.println("Levenshtein Distance is: " + LDistanceValue);
		System.out.println("JaroWinkler Similarity is: " + JWinkSimilarity + "%\n");

		Assert.assertTrue(
				JWinkSimilarity >= 90.0 || LDistanceValue <= 4 || entitiesEntityValue.contains(entityValue));
	}
	
		
	@Then("delete all entities")
	public void delete_all_entites() throws IOException, URISyntaxException, JSONException {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");
		loadQueryparams(CommonSteps.orgMap);
		response=request.auth().oauth2(token).get("entity");
		JSONObject jsonObj = new JSONObject(response.body().asString());
		JSONArray jsonArr = jsonObj.getJSONArray("data");
		if(jsonArr.length()>0) {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		request.auth().oauth2(token).body(jsonArr.toString());
		Assert.assertTrue((request.post("delete/entities").getStatusCode())==201 || (request.post("delete/entities").getStatusCode())==404);
//		Assert.assertEquals(200, (request.post("delete/entities").getStatusCode()));
		}
	}
	
	@Then("delete all dispositions")
	public void delete_all_dispositions() {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		JSONObject categoryHBenRequest=new JSONObject();
		JSONArray levels=new JSONArray();
		JSONArray categoryBeanRequest=new JSONArray();
		levels.put("Level-1");levels.put("Level-2");levels.put("Level-3");levels.put("Level-4");levels.put("Level-5");
		categoryHBenRequest.put("categoryHBeanRequest", categoryBeanRequest);
		categoryHBenRequest.put("levels", levels);
		request.body(categoryHBenRequest.toString());
		Assert.assertTrue((request.auth().oauth2(token).post("call-category/define/json").getStatusCode())==200);
	}
	
	@Then("validate and configure rules in folder {string}")
	public void validate_rule_entity(String folder) throws IOException {
		token=cs.getToken("default-analyst");
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folder).getAbsolutePath())
				.toString();
		File ruleEntityFolder = new File(absoluteFolderPath);
		
		
		File[] listFile=ruleEntityFolder.listFiles();
		int i=0;
		for (File defineFile : listFile) {
			
			String ruler="Rule"+i;
			HashMap<String, String> rule=new HashMap<>();
			rule.put("ruleName", ruler);
			if (defineFile.isDirectory()) {
			File ruleValidation=new File(defineFile+"/ruleValidate.json");
			String defineJsonString = new String(Files.readAllBytes(Paths.get(ruleValidation.getAbsolutePath())));
			if(ruleValidation.exists()) {
			loadURL("BACKEND_PORT");
			JSONObject defineJsonFileObj = new JSONObject(defineJsonString);
			System.out.println("defineJsonFileObj >>>>>> " + defineJsonFileObj);
			request=RestAssured.given().log().all().auth().oauth2(token).header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken).queryParams(rule);
			request.body(defineJsonFileObj.toString()).when();
			response=request.auth().oauth2(token).post(CommonSteps.commonMap.get("organization")+"/"+CommonSteps.commonMap.get("category")+"/rule/validate");
			Assert.assertEquals(200,response.getStatusCode());
		}
		else {
			System.out.println("File Not Found");
		}
		
		File ruleDefinition = new File(defineFile+"/ruleDefinition.json");
		String defineJson = new String(Files.readAllBytes(Paths.get(ruleDefinition.getAbsolutePath())));
		if(ruleValidation.exists()) {
			loadURL("BACKEND_PORT");
			JSONObject defineJsonFileObj = new JSONObject(defineJson);
			String name=defineJsonFileObj.getString("name");
			ruleList.put(name);
			System.out.println("defineJsonFileObj >>>>>> " + defineJsonFileObj);
			request=RestAssured.given().log().all().auth().oauth2(token).header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken).queryParams(CommonSteps.orgMap);
			request.body(defineJsonFileObj.toString()).when();
			response=request.auth().oauth2(token).post("define/entity");
			Assert.assertEquals(200,response.getStatusCode());
		}
		else {
			System.out.println("File Not Found");
	}
		
		File rules = new File(defineFile+"/rule.json");
		String defineJsonrule = new String(Files.readAllBytes(Paths.get(rules.getAbsolutePath())));
		if(ruleValidation.exists()) {
			loadURL("BACKEND_PORT");
			JSONObject defineJsonFileObj = new JSONObject(defineJsonrule);
			System.out.println("defineJsonFileObj >>>>>> " + defineJsonFileObj);
			request=RestAssured.given().log().all().auth().oauth2(token).header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
			request.body(defineJsonFileObj.toString()).when();
			response=request.auth().oauth2(token).post(CommonSteps.commonMap.get("organization")+"/"+CommonSteps.commonMap.get("category")+"/rule");
			Assert.assertEquals(200,response.getStatusCode());
		}
		else {
			System.out.println("File Not Found");
	}
  }
			i++;
 }
 }
	
	@Then("create entity catalog from {string}")
	public void uploadEntity_Catalogue(String folder) throws IOException {
		token=cs.getToken("default-analyst");
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folder).getAbsolutePath())
				.toString();
		String defineJsonrule = new String(Files.readAllBytes(Paths.get(absoluteFolderPath)));
		JSONObject defineJsonFileObj = new JSONObject(defineJsonrule);
		JSONObject data=defineJsonFileObj.getJSONObject("data");
		JSONObject catalogue=data.getJSONObject("catalog");
		JSONArray items=catalogue.getJSONArray("items");
		for(int i=0;i<items.length();i++) {
			JSONObject finalCatlog=new JSONObject();
			JSONObject value=new JSONObject();
			JSONArray item=new JSONArray();
			JSONObject finalData=new JSONObject();
			finalData.put("id", i+1);
			finalData.put("name", (items.getJSONObject(i)).get("name"));
			finalData.put("type", (items.getJSONObject(i)).get("type"));
			item.put(finalData);
			value.put("items", item);
			value.put("version", "0");
			finalCatlog.put("catalog",value);
//			System.out.println(finalCatlog.toString());
			loadURL("BACKEND_PORT");loadQueryBasic("default-analyst");
			request.log().all().contentType(ContentType.JSON).body(finalCatlog.toString());
			response=request.auth().oauth2(token).post("entity-catalog");
			int reponseCode=response.getStatusCode();
			System.out.println("reponseCode "+reponseCode);
			Assert.assertTrue(reponseCode==200 || reponseCode==409);
		}
	}
	
	
	@Then("import ai entities from {string}")
	public void import_AI_entities(String folder) throws IOException {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(folder).getAbsolutePath())
				.toString();
		System.out.println(absoluteFolderPath);
		String defineJsonrule = new String(Files.readAllBytes(Paths.get(absoluteFolderPath)));
		JSONObject defineJsonFileObj = new JSONObject(defineJsonrule);
		request.body(defineJsonFileObj.toString()).when();
		response=request.log().all().auth().oauth2(token).post("/ai-entity/import");
		Assert.assertEquals(200,response.getStatusCode());
	}
	
	
	@Then("configure complex entities {string}")
	public void complex_entities(String filefolderPath) throws IOException {
		token=cs.getToken("default-analyst");
		String folderPath = Paths.get(TestCenter.getInstance().getFile(filefolderPath).getAbsolutePath())
				.toString();
		
		File entityDefinitionFolder = new File(folderPath + "/entity-definitions");
		File entityConfigFolder = new File(folderPath + "/configurations");
		
		System.out.println("orgMap " +CommonSteps.orgMap);

		for (File defineFile : entityDefinitionFolder.listFiles()) {
			String path=defineFile.toString();
			boolean specialChar=path.contains("\\");
			int index = (specialChar ? path.lastIndexOf("\\") : path.lastIndexOf("/"));
			String fileName = path.substring(index + 1);
			loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
			String defineJsonString = new String(Files.readAllBytes(Paths.get(defineFile.getAbsolutePath())));
			boolean status=(defineJsonString.contains("COMPLEX")?true:false);
			JSONObject defineJsonFileObj = new JSONObject(defineJsonString);
			System.out.println("defineJsonFileObj >>>>>> " + defineJsonFileObj);
			request.body(defineJsonFileObj.toString()).when();
			response=request.log().all().auth().oauth2(token).post("define/entity");
			Assert.assertEquals(200,response.getStatusCode());
			if(status) {
				JSONObject data=new JSONObject(response.getBody().asString());
				JSONObject datas=data.getJSONObject("data"); 
				int id=(int) datas.get("id");
				File name = null;
				String defineJsonString1 =null;
				try {
				name=new File(entityConfigFolder+"/"+fileName);
				System.out.println(name.getAbsolutePath());
				defineJsonString1= new String(Files.readAllBytes(Paths.get(name.getAbsolutePath()))); 
				}catch(Exception e) {
					name=new File(fileName);
					System.out.println(name.getAbsolutePath());
					defineJsonString1= new String(Files.readAllBytes(Paths.get(name.getAbsolutePath())));
				}
				File fileOutput=new File(entityConfigFolder+"/"+fileName);
				JSONObject defineJsonFileObj1 = new JSONObject(defineJsonString1);
				defineJsonFileObj1.put("entityId", id);
				String folders=fileOutput.getAbsolutePath();
				FileWriter file = new FileWriter(folders);
				file.write(defineJsonFileObj1.toString());
				file.flush();file.close();
			}
		}
		
		for (File configFile : entityConfigFolder.listFiles()) {
			loadURL("BACKEND_PORT");
			String configJsonString = new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())));
			JSONObject defineJsonFileObj = new JSONObject(configJsonString);
			request.contentType(ContentType.JSON).body(defineJsonFileObj.toString()).when();
			response=request.log().all().auth().oauth2(token).post("/api-config/"+CommonSteps.orgMap.get("organization")+"/"+CommonSteps.orgMap.get("category"));
			if(response.getStatusCode()==409) {
				String name=defineJsonFileObj.getString("name");
				loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
				response=request.auth().oauth2(token).delete("entity"+name);
				}
			Assert.assertEquals(200,response.getStatusCode());
		}
	}
	
	
	@Then("train complex rules")
	public void train_complex_rules() {
		token=cs.getToken("default-analyst");
		loadURL("BACKEND_PORT"); loadQueryparams(CommonSteps.orgMap);
		request.contentType(ContentType.JSON).body(ruleList.toString());
		response=request.log().all().auth().oauth2(token).post("/train/entity-selected");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
}
