package configPuller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.core.resource.Resource;
import stepDefinition.CommonSteps;
//
//
public class PullerandWriter extends BaseClass{
	CommonSteps cs=new CommonSteps();

	public static JSONArray getEntities() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("entity");
		JSONObject jsonObj = new JSONObject(response.body().asString());
		return jsonObj.getJSONArray("data");
	
	}
//	
	public static JSONObject getDefinitionJSONForEntity(String entity) throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("define/entity/" + entity);
		JSONObject jsonObj = new JSONObject(response.body().asString());
		JSONObject dataObj = jsonObj.getJSONObject("data");
		
		dataObj.remove("id");
		dataObj.put("trained", false);
		dataObj.put("entityType", dataObj.get("entityType").toString().toUpperCase().replace(" ", "_"));
		
		return dataObj;
	}
//	
	public static JSONObject getConfigJSONForEntity(String entity) throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("configure/entity/" + entity);
		System.out.println(response.getBody().asString());
		JSONObject jsonObj = new JSONObject(response.getBody().asString());
		JSONObject dataObj = jsonObj.getJSONObject("data");
		
		return dataObj;
	}
//	
	public static String getIntentCategoryDefinition() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("call-category/download/");
		String csvString = response.getBody().asString();
		Integer indexOfFirstLevel = csvString.lastIndexOf('¿');
		return csvString.substring(indexOfFirstLevel+1,csvString.length());
		 //return csvString;
	}
	
	
	public static JSONObject saveIntentAsJSON() throws IOException, URISyntaxException{
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("call-category/download/json");
		JSONArray jsonArr = new JSONObject(response.getBody().asString()).getJSONArray("data");
		JSONObject levelsCategoryHBeans = new JSONObject();
		levelsCategoryHBeans.put("levels", jsonArr.getJSONArray(0));
		
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("call-category/load/hierarchy");
		JSONObject hierarchyJsonObj = new JSONObject(response.getBody().asString()).getJSONObject("data");

		hierarchyJsonObj.put("hierarchyJsonObj", childrenNodeCleanup(hierarchyJsonObj.getJSONArray("children"),0));
		
		levelsCategoryHBeans.put("categoryHBeanRequest", hierarchyJsonObj.getJSONArray("children"));
		
		return levelsCategoryHBeans;
	}
	
	
	public static JSONArray childrenNodeCleanup(JSONArray childJSONArray, int arrayIndex) {
		while(arrayIndex < childJSONArray.length()) {
			JSONObject currJSONObj = new JSONObject();
			
			try { 
				currJSONObj = childJSONArray.getJSONObject(arrayIndex);
			}
			catch(JSONException e){
				JSONArray tempChildArray = new JSONArray();
				currJSONObj.put("children", tempChildArray);
			}
			
			JSONArray currChildrenJSONArray = currJSONObj.getJSONArray("children");
			if(!(currChildrenJSONArray.isEmpty())) {
				if(currChildrenJSONArray.getJSONObject(arrayIndex).has("id")) {
				childrenNodeCleanup(childJSONArray.getJSONObject(arrayIndex).getJSONArray("children"),0);
				}
			}
			
			childJSONArray.getJSONObject(arrayIndex).remove("id");
			childJSONArray.getJSONObject(arrayIndex).remove("levelOrderNo");
			childJSONArray.getJSONObject(arrayIndex).remove("hitCount");
			childJSONArray.getJSONObject(arrayIndex).remove("configured");
			
			arrayIndex++;
		}
		return childJSONArray;
	}
//		
	
	public static HashSet<String> listOfCategoryPaths() throws IOException, URISyntaxException {
		
		HashSet<String> callCategoryPaths = new HashSet<String>();
		
		String[] intentDefinitions = getIntentCategoryDefinition().split("\n");
		
		for(int i = 1; i < intentDefinitions.length; i++) {
			String intentDefinition = intentDefinitions[i];
			String[] intentLevels = intentDefinition.split(",");
			String currentLevelStr = intentLevels[0];
			
			callCategoryPaths.add(currentLevelStr);
			Integer currentLevel = 1;
			
			while(currentLevel < intentLevels.length) {
				currentLevelStr += "," + intentLevels[currentLevel];
				callCategoryPaths.add(currentLevelStr);
				currentLevel++;
			}
		}
		return callCategoryPaths;
	}
	
//	
	public static JSONObject getConfigJSONForCallCategorization(String callCategoryPath) throws IOException, URISyntaxException {
		
		Integer indexLevel = StringUtils.countMatches(callCategoryPath, ",") + 1;
		String categoryName = callCategoryPath.substring(callCategoryPath.lastIndexOf(",")+1,callCategoryPath.length());
		
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("call-category/" + indexLevel.toString()+"/"+callCategoryPath+"/"+categoryName);
//		ResponseEntity<String> callCategoryConfigResponse = RestRequest.getResponseBack(TestCenter.BACKEND_PORT, "call-category/" + 
//				indexLevel.toString()+ "/" + 
//				callCategoryPath.replace(" ", "%20").replace(",", "%2C") + "/" +
//				categoryName.replace(" ", "%20").replace(",", "%2C") + "/", 
//				TestCenter.getInstance().getRestRequest().getMap(), true);
		
		JSONObject jsonObj = new JSONObject(response.getBody().asString());
		JSONObject dataObj = jsonObj.getJSONObject("data");
		
		//MAIN JSON Object
		JSONObject retJSONObj = new JSONObject();
		
		retJSONObj.put("saved", true);
		retJSONObj.put("trained", false);
		
		//Call Category Bean JSON Array
		JSONArray callCategoryBeanJSONArr = new JSONArray();
		JSONObject callCategoryBeanJSONObj = new JSONObject();
		
		//Bag Of Keywords JSON Array
		JSONArray bagOfKeywordsJSONArray = dataObj.getJSONArray("keyPhraseParam");
		
		callCategoryBeanJSONObj.put("level", indexLevel);
		callCategoryBeanJSONObj.put("name", categoryName);
		
		//Call Category JSON Array
		JSONArray callCategoryPathJSONArray = new JSONArray();
		callCategoryPathJSONArray.put(callCategoryPath);
		callCategoryBeanJSONObj.put("callCategoryPath", callCategoryPathJSONArray);
		
		//KeyPhraseJSON Array
		JSONArray keyPhraseJSONArray = new JSONArray();
		keyPhraseJSONArray.put(dataObj.get("keyPhraseMatchingChannel"));
		callCategoryBeanJSONObj.put("keyPhraseMatchingChannel", keyPhraseJSONArray);
		
		callCategoryBeanJSONObj.put("fuzzyMatch", dataObj.get("fuzzyType"));
		callCategoryBeanJSONArr.put(callCategoryBeanJSONObj);
		callCategoryBeanJSONObj.put("bagOfKeywords", bagOfKeywordsJSONArray);
		
		retJSONObj.put("callCategoryBeans", callCategoryBeanJSONArr);
		
		
		return retJSONObj;
	}
	
////	
	public static JSONArray getAlerts() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("allalert");
		
		JSONObject jsonObj = new JSONObject(response.getBody().asString());
		
		return jsonObj.getJSONArray("data");
	}
////	
	public static JSONObject getAlertJSON(String alertName) throws IOException, URISyntaxException {
		
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		response=request.get("alert/configure/"+alertName);
		
		JSONObject jsonObj = new JSONObject(response.getBody().asString());
		JSONObject dataObj = jsonObj.getJSONObject("data");
		
		dataObj.put("trained", false);
		dataObj.put("draft", false);
		
		dataObj.getJSONArray("alertAttributes").getJSONObject(0).getJSONArray("keyPhrases").getJSONObject(0).put("isNew", true);
		dataObj.getJSONArray("alertAttributes").getJSONObject(0).getJSONArray("keyPhrases").getJSONObject(0).put("categoryId", 0);
		
		return dataObj;
	}
////
	public static void jsonToFile(String pathToWrite, JSONObject jsonObj) {

	      try {
	         FileWriter file = new FileWriter(pathToWrite);
	         file.write(jsonObj.toString(4));
	         file.close();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      
	      System.out.println("JSON file created: "+ jsonObj);
	}
////	
	public static void csvToFile(String pathToWrite, String csvString) {

	      try {
	         FileWriter file = new FileWriter(pathToWrite);
	         file.write(csvString);
	         file.close();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      
	      System.out.println("CSV file created: "+ csvString);
	}
////	
//	public static void csvResourceToFile(String pathToWrite, Resource resource) {
//		try {
//			
//			File file = new File(pathToWrite);
//			OutputStream outStream = new FileOutputStream(file);
//	        
//			outStream.write(resource.getInputStream().readAllBytes());
//			
//			outStream.close();
//
//	      } catch (IOException e) {
//	         // TODO Auto-generated catch block
//	         e.printStackTrace();
//	      }
//	      
//	      System.out.println("CSV file created: ");
//	}
//////	
	public static void saveEntityConfigs(String directoryPath) throws IOException, URISyntaxException{
		JSONArray entities = PullerandWriter.getEntities();
		
		for(int i = 0; i < entities.length(); i++) {
			String entity = entities.getString(i);
			JSONObject jsonObj = getConfigJSONForEntity(entity);
			PullerandWriter.jsonToFile(directoryPath+"/"+entity+"_Configuration.json", jsonObj);
		}
	}
////	
	public static void saveEntityDefinitions(String directoryPath) throws IOException, URISyntaxException{
		JSONArray entities = PullerandWriter.getEntities();
		
		for(int i = 0; i < entities.length(); i++) {
			String entity = entities.getString(i);
			JSONObject jsonObj = getDefinitionJSONForEntity(entity);
			PullerandWriter.jsonToFile(directoryPath+"/"+entity+"_Definition.json", jsonObj);
		}
	}
////	
	public static void saveIntentCategoryDefinition(String directoryPath) throws IOException, URISyntaxException {
		JSONObject jsonObj = PullerandWriter.saveIntentAsJSON();
		PullerandWriter.jsonToFile(directoryPath+"/"+"_Definition.json", jsonObj);
		
	}
////	
	public static void saveSingleIntentCategoryConfig(String directoryPath, String categoryPathName) throws IOException, URISyntaxException {
		JSONObject jsonObj = getConfigJSONForCallCategorization(categoryPathName);
		PullerandWriter.jsonToFile(directoryPath+"/"+categoryPathName+"_Configuration.json", jsonObj);
	}
////	
	public static void saveAlertConfig(String directoryPath) throws IOException, URISyntaxException{
		JSONArray alerts = PullerandWriter.getAlerts();
		
		for(int i = 0; i < alerts.length(); i++) {
			String alert = alerts.getString(i);
			JSONObject jsonObj = getAlertJSON(alert);
			PullerandWriter.jsonToFile(directoryPath+"/"+alert+"_Configuration.json", jsonObj);
		}
	}
//	
	public static void saveIntentCategoryConfigs(String directoryPath, HashSet<String> categorySet) {
		categorySet.forEach(callCategoryPath -> {
			try {
				saveSingleIntentCategoryConfig(directoryPath, callCategoryPath);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});
	}
//	
//	
	public static void saveEntityConfigandDefinitions(String directoryPath) throws IOException, URISyntaxException {
		
		File configDirectory = new File(directoryPath+"/configurations");
		if (!configDirectory.exists()){
		    configDirectory.mkdirs();
		}
		
		File defineDirectory = new File(directoryPath+"/definitions");
		if (!defineDirectory.exists()){
		    defineDirectory.mkdirs();
		}
		
		saveEntityConfigs(directoryPath+"/configurations");
		saveEntityDefinitions(directoryPath+"/definitions");
		
	}
//	
	public static void saveIntentCategoryConfigandDefinitions(String directoryPath) throws IOException, URISyntaxException {
		File configDirectory = new File(directoryPath+"/configurations");
		if (!configDirectory.exists()){
		    configDirectory.mkdirs();
		}
		
		File defineDirectory = new File(directoryPath+"/definitions");
		if (!defineDirectory.exists()){
		    defineDirectory.mkdirs();
		}
		saveIntentCategoryConfigs(directoryPath+"/configurations", listOfCategoryPaths());
		saveIntentCategoryDefinition(directoryPath+"/definitions");
		
	}
//	
	public static void saveAlertConfigurations(String directoryPath) throws IOException, URISyntaxException {
		
		File configDirectory = new File(directoryPath+"/configurations");
		if (!configDirectory.exists()){
		    configDirectory.mkdirs();
		}
		
		saveAlertConfig(directoryPath+"/configurations");
	}
//	
	public static void saveEntitiesandIntent(String directoryPath) throws IOException, URISyntaxException {
		File entityDirectory = new File(directoryPath+"/entities");
		if (!entityDirectory.exists()){
			entityDirectory.mkdirs();
		}
		
		File callCategoryDirectory = new File(directoryPath+"/call-categorization");
		if (!callCategoryDirectory.exists()){
			callCategoryDirectory.mkdirs();
		}
		
		saveEntityConfigandDefinitions(directoryPath+"/entities");
		saveIntentCategoryConfigandDefinitions(directoryPath+"/call-categorization");
	}
//	
	public static void saveAlerts(String directoryPath) throws IOException, URISyntaxException {
		File alertDirectory = new File(directoryPath+"/alerts");
		if (!alertDirectory.exists()){
			alertDirectory.mkdirs();
		}
		
		saveAlertConfigurations(directoryPath+"/alerts");
	}
//	
	public static void saveCallConfigFiles(String directoryPath) throws IOException, URISyntaxException {
		String organization = CommonSteps.orgMap.get("organization");
		
		File orgDirectory = new File(directoryPath+"/"+organization);
		if (!orgDirectory.exists()){
			orgDirectory.mkdirs();
		}
		
		saveEntitiesandIntent(directoryPath+"/"+organization);
	}
//	
	public static void saveCallAlertConfigFiles(String directoryPath) throws IOException, URISyntaxException {
		String organization = CommonSteps.orgMap.get("organization");
		
		File orgDirectory = new File(directoryPath+"/"+organization);
		if (!orgDirectory.exists()){
			orgDirectory.mkdirs();
		}
		
		saveAlerts(directoryPath+"/"+organization);
	}
	
	
}
//
