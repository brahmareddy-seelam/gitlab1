package stepDefinition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import configPuller.PullerandWriter;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class CTI_Language extends BaseClass {

	CommonSteps cs = new CommonSteps();
	IntentCallCategorization ic=new IntentCallCategorization();
	AddAgent addAgent=new AddAgent();
	Entities entity=new Entities();
	public static String token=null;
	Random random = new Random();
	public static HashMap<String,String> language;
	
	JSONObject Organization=new JSONObject();
	JSONArray data=new JSONArray();
	@Given("we add language {string} to Organization and Categories")
	public void addLanguage(String lang) {

		loadURL("OCMS_PORT");
		response = request.get("config/organization");
		JSONObject jsonObj = new JSONObject(response.body().asString());
		JSONArray jsonArr = jsonObj.getJSONArray("data");
		JSONArray payloadArray = new JSONArray();
		for (int i = 0; i < jsonArr.length(); i++) {
			loadURL("OCMS_PORT");
			String Org = jsonArr.get(i).toString();
			response = request.get("config/category/" + jsonArr.get(i).toString());
			JSONObject catObj = new JSONObject(response.body().asString());
//			Iterator<String> key = catObj.keys();
			try {
//				String status = (catObj.getJSONArray("data").isEmpty() ? "Yes" : "No");
				JSONArray catArr = catObj.getJSONArray("data");

				for (int j = 0; j < catArr.length(); j++) {
					JSONObject temp = catArr.getJSONObject(j);
					{
						String name = temp.getString("name").trim();
						JSONObject payload = new JSONObject();
						payload.put("ctiLanguageCode", lang);
						payload.put("skillGroupCode", 99999/* String.format("%04d", random.nextInt(10000)) */);
						payload.put("isoLanguageCode", "en-us");
						payload.put("categoryName", name);
						payload.put("orgnizationName", Org);
						payload.put("engineName", "UNIPHORE");
						payloadArray.put(payload);
					}
				}
			} catch (Exception e) {
				System.out.println("No Categories found for " + Org);
			}
		}
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON);
		request.body(payloadArray.toString());
		response = request.log().all().put("cti-language");
		Assert.assertEquals(200, response.getStatusCode());
	}

	
	
	@Given("we add language to org {string} and category {string}")
	public void addLanguage(String Org, String cat) {
		token=cs.getToken("default-admin");
		boolean status = false;
		loadURL("BACKEND_PORT");
		response = request.auth().oauth2(token).get("/cti-language");
		CTI_Language.ctiLang();
		JSONArray langObj = new JSONArray(response.body().asString());
		boolean skillCode=(langObj.toString().contains("99999")?true:false);
		for (int i = 0; i < langObj.length(); i++) {
			JSONObject item = langObj.getJSONObject(i);
			if (item.has("organizationName")) {
				if (item.get("organizationName").toString().equalsIgnoreCase(Org)
						&& item.get("categoryName").toString().equalsIgnoreCase(cat)) {
					item.put("ctiLanguageCode", language.get("cti"));
					status=true;
				}
			} 
			else {}
		}
		if(status!=true) {
			JSONObject payload = new JSONObject();
			payload.put("ctiLanguageCode", language.get("cti"));
			payload.put("skillGroupCode", (skillCode?Integer.parseInt(String.format("%04d", random.nextInt(10000))):99999));
			payload.put("isoLanguageCode", language.get("iso"));
			payload.put("categoryName", cat);
			payload.put("organizationName", Org);
			payload.put("engineName", "UNIPHORE");
			langObj.put(payload);
		}
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON);
		request.body(langObj.toString());
		response = request.log().all().auth().oauth2(token).headers("X-Username",port.getProperty("X-Username")).put("cti-language");
		System.out.println(response.asString());
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	@Given("we create a backup of Organization and category")
	public void backupOrgCat() throws IOException {
		loadURL("OCMS_PORT");
		response = request.get("config/organization");
		JSONObject jsonObj = new JSONObject(response.body().asString());
		JSONArray jsonArr = jsonObj.getJSONArray("data");
		organization(jsonArr);
		System.out.println(Organization.toString());
		File f=new File("src/test/resources/backup.json"); String folder=f.getAbsolutePath();
		System.out.println(folder);
		@SuppressWarnings("resource")
		FileWriter file= new FileWriter(folder);
		file.write(Organization.toString());
		file.flush();
	}
	
	
	@Given("we update Organization and Category to new env")
	public void updateOrgCat() throws IOException, URISyntaxException {
		String file="src/test/resources/backup.json";
		File entityDefinitionFolder = new File(file);
		String backup = new String(Files.readAllBytes(Paths.get(entityDefinitionFolder.getAbsolutePath())));
		JSONObject backupJSON = new JSONObject(backup);
		Iterator<String> keys= backupJSON.keys();
		while(keys.hasNext()) {
			String name=keys.next();
			addAgent.we_create_an_organization_called_with_description_as(name,"Description" );
			boolean status = (backupJSON.get(name).toString() != "" ? true : false);
			if(status==true) {
				JSONArray categories=backupJSON.getJSONArray(name);
				for(int i=0;i<categories.length();i++) {
					Object category=categories.get(i);
					addAgent.add_business_process_to_organization(category.toString(), "colorSample", "Description", name);
				}
			}
		}
	}
	
	
	
	public JSONObject organization(JSONArray jsonArr) {
		for (int i = 0; i < jsonArr.length(); i++) {
			loadURL("OCMS_PORT");
			String org=jsonArr.get(i).toString();
			response = request.get("config/category/" + jsonArr.get(i).toString());
			JSONObject catObj = new JSONObject(response.body().asString());
			boolean status = (catObj.get("data").toString() != "" ? true : false);
			if(status==true) {
			JSONArray catArr = catObj.getJSONArray("data");
			JSONArray cat=new JSONArray();
			for (int j = 0; j < catArr.length(); j++) {
				JSONObject temp = catArr.getJSONObject(j);
				{
					String name = temp.getString("name").trim();
					cat.put(name);
				}
			  }
			Organization.put(org, cat);
			}
			else {Organization.put(org,"");}
		}
		return Organization;
	}
	
	
	@Given("we create a backup of entities for the {string} and {string} from 2.2 for 2.4")
	public void create_backUP(String org, String cat) throws IOException {
		HashMap<String, String> map=new HashMap<>();
		String file1="src/test/resources/backup.json";
		File entityDefinitionFolder = new File(file1);
		String backup = new String(Files.readAllBytes(Paths.get(entityDefinitionFolder.getAbsolutePath())));
		JSONObject backupJSON = new JSONObject(backup);
		Iterator<String> keys= backupJSON.keys();
		while(keys.hasNext()) {
			String name=keys.next();
			map.put("organization", name);
			boolean status = (backupJSON.get(name).toString() != "" ? true : false);
			if(status==true) {
				JSONArray categories=backupJSON.getJSONArray(name);
				for(int i=0;i<categories.length();i++) {
					Object category=categories.get(i);
					map.put("category", category.toString());
					loadURL("BACKEND_PORT");loadQueryparams(map);
					response=request.get("entity");
					JSONObject object=new JSONObject(response.getBody().asString());
					if(object.get("data").toString() != "" )
					{
					 JSONArray jsonArr = new JSONObject(response.body().asString()).getJSONArray("data");
					loadURL("BACKEND_PORT");loadQueryparams(map);
					request.body(jsonArr.toString());
					if(jsonArr.length()>0) {
					JSONObject jsonObject = new JSONObject(request.post("export/entities").body().asString());
					JSONArray export=new JSONObject(jsonObject.getString("data")).getJSONArray("export");
					entities(export);
					File directory=new File("src/test/resources/backUp/"+name);
					directory.mkdir();
					File categoryDir=new File("src/test/resources/backUp/"+name+"/"+category.toString());categoryDir.mkdir();
					File f=new File("src/test/resources/backUp/"+name+"/"+category.toString()+"/"+category.toString()+"_backupEntities.json"); String folder=f.getAbsolutePath();
					System.out.println(folder);
					@SuppressWarnings("resource")
					FileWriter file= new FileWriter(folder);
					file.write(data.toString());
					file.flush();
					}
				}
			}
		  }
		}
	}
	
	
	public static HashMap<String,String> ctiLang() {
		language=new HashMap<>();
		String lang= prop.getProperty("Tag").replaceAll("[^A-Z]", "");
		switch(lang) {
		case "US":
			language.put("cti","EUU");
			language.put("iso","en-us");
			break;
		case "UK":
			language.put("cti","EUB");
			language.put("iso","en-gb");
			break;
		case "PH":
			language.put("cti","EUP");
			language.put("iso","fil-en");
			break;
		case "IN":
			language.put("cti","EUI");
			language.put("iso","en-in");
			break;
		case "DE":
			language.put("cti","EUD");
			language.put("iso","de");
			break;
		case "HE":
			language.put("cti","UHE");
			language.put("iso","hi-en-in");
			break;
		case "SE":
			language.put("cti","EUS");
			language.put("iso","es-us");
			break;
		case "SM":
			language.put("cti","EUM");
			language.put("iso","es-mx");
			break;
		case "JA":
			language.put("cti","JUE");
			language.put("iso","ja");
			break;
		case "AR":
			language.put("cti","AUE");
			language.put("iso","ar");
			break;
		case "AU":
			language.put("cti","AUE");
			language.put("iso","en-au");
			break;
		}
		return language;
		
	}
	
	
	public void entities(JSONArray export) {
		for(int i=0;i<export.length();i++) {
			JSONObject JSONResponseBody = export.getJSONObject(i);
				JSONObject entity=(JSONObject) JSONResponseBody.get("defineEntity");
				entity.remove("displayText");
				entity.remove("entityTextIfTrue");
				entity.remove("defaultText");
				entity.put("trained", false);
				String entityType=(String) entity.get("entityType");
				entity.put("sourceType", (entityType!="RULE"?"NLP":"RULE"));
				entity.put("catalogReference", catalogReference(entityType));
				JSONResponseBody.put("defineEntity", entity);
				if(!JSONResponseBody.isNull("configureEntity")) {
				JSONObject config=(JSONObject) JSONResponseBody.get("configureEntity");
				JSONObject outputParams=(JSONObject) config.get("outputParams");
				String name=config.getString("name");
				JSONArray inputParams=new JSONArray(config.getJSONArray("inputParams"));
				if(inputParams.length()>0) {
					JSONObject configureEntity=(JSONObject) inputParams.get(0);
					configureEntity.remove("order");
					JSONObject input=new JSONObject();
					JSONArray inputParam=new JSONArray();
					inputParam.put(configureEntity);
					input.put("inputParams",inputParam);
					input.put("outputParams",outputParams);input.put("name", name);
					JSONResponseBody.put("configureEntity", input);
				data.put(JSONResponseBody);
				}
			}
		}
	}
	
	
	public String catalogReference(String entityType) {
		String entity = null;
		switch(entityType) {
			case "STRING":
				entity= "String";
				break;
			case "RULE":
				entity= "null";
				break;	
			case "CARDINAL":
				entity= "NUMBER_DEFAULT";
				break;
		}
		return entity;
	}
	
	
	@Given("create backup of call Category and alerts for the {string} and {string}")
	public void backUp_Category_and_alerts(String org, String cat) throws IOException, URISyntaxException {
		String file1="src/test/resources/backup.json";
		File entityDefinitionFolder = new File(file1);
		String backup = new String(Files.readAllBytes(Paths.get(entityDefinitionFolder.getAbsolutePath())));
		JSONObject backupJSON = new JSONObject(backup);
		Iterator<String> keys= backupJSON.keys();
		while(keys.hasNext()) {
			String name=keys.next();
			CommonSteps.orgMap.put("organization", name);
			boolean status = (backupJSON.get(name).toString() != "" ? true : false);
			if(status==true) {
				JSONArray categories=backupJSON.getJSONArray(name);
				for(int i=0;i<categories.length();i++) {
					Object category=categories.get(i);
					CommonSteps.orgMap.put("category", category.toString());
		PullerandWriter.saveCallConfigFiles(System.getProperty("user.dir")+"/src/test/resources/backUp/"+name+"/"+category);
	    PullerandWriter.saveCallAlertConfigFiles(System.getProperty("user.dir")+"/src/test/resources/backUp/"+name+"/"+category);
				}
			}
		}
	}
		
	@Given("update the intent in new env")
	public void update_intent() throws IOException, JSONException, URISyntaxException {
	String filePath="src/test/resources/backup.json";
	File entityDefinitionFolder = new File(filePath);
	String backup = new String(Files.readAllBytes(Paths.get(entityDefinitionFolder.getAbsolutePath())));
	JSONObject backupJSON = new JSONObject(backup);
	Iterator<String> keys= backupJSON.keys();
	while(keys.hasNext()) {
		String name=keys.next().replace("%20", " "); 
		boolean status = (backupJSON.get(name).toString() != "" ? true : false);
		if(status==true) {
			JSONArray categories=backupJSON.getJSONArray(name);
			for(int i=0;i<categories.length();i++) {
				Object category=categories.get(i);
				String path="backUp"+"/"+name+"/"+category.toString().replace("%20", " ");
		ic.define_and_configure_call_categorization(path+"/call-categorization");
		}
		}
	}
 }
	
	
	@Given("update the entites in new env")
	public void update_entites() throws IOException, JSONException, URISyntaxException {
		String filePath="src/test/resources/backup.json";
		File entityDefinitionFolder = new File(filePath);
		String backup = new String(Files.readAllBytes(Paths.get(entityDefinitionFolder.getAbsolutePath())));
		JSONObject backupJSON = new JSONObject(backup);
		Iterator<String> keys= backupJSON.keys();
		while(keys.hasNext()) {
			String name=keys.next().replace("%20", " "); 
			CommonSteps.orgMap.put("organization", name);
			boolean status = (backupJSON.get(name).toString() != "" ? true : false);
			if(status==true) {
				JSONArray categories=backupJSON.getJSONArray(name);
				for(int i=0;i<categories.length();i++) {
					Object category=categories.get(i);
					CommonSteps.orgMap.put("category", category.toString());
					String path="src/test/resources/backUp"+"/"+name+"/"+category.toString().replace("%20", " ")+"/"+category.toString().replace("%20", " ")+"_backupEntities.json";;
					File entityFolder = new File(path);
					if(Files.exists(Paths.get(entityFolder.getAbsolutePath()))) {
					String backupEntity = new String(Files.readAllBytes(Paths.get(entityFolder.getAbsolutePath())));
					loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
					request.body(backupEntity.toString());
					response=request.post("/import/entites");
					Assert.assertEquals(200, response.getStatusCode());
				}
			  }
			}
		}	
	}
}