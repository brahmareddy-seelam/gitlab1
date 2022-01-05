package stepDefinition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;

public class CTI_Language extends BaseClass {

	CommonSteps cs = new CommonSteps();
	AddAgent addAgent=new AddAgent();
	Random random = new Random();
	JSONObject Organization=new JSONObject();

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

	
	
	@Given("we add language {string} to org {string} and category {string}")
	public void addLanguage(String lang, String Org, String cat) {
		boolean status = false;
		loadURL("BACKEND_PORT");
		response = request.get("/cti-language");
		JSONArray langObj = new JSONArray(response.body().asString());
		boolean skillCode=(langObj.toString().contains("99999")?true:false);
		for (int i = 0; i < langObj.length(); i++) {
			JSONObject item = langObj.getJSONObject(i);
			if (item.has("organizationName")) {
				if (item.get("organizationName").toString().equalsIgnoreCase(Org)
						&& item.get("categoryName").toString().equalsIgnoreCase(cat)) {
					item.put("ctiLanguageCode", lang);
					status=true;
				}
			} 
			else {}
		}
		if(status!=true) {
			JSONObject payload = new JSONObject();
			payload.put("ctiLanguageCode", lang);
			payload.put("skillGroupCode", (skillCode?Integer.parseInt(String.format("%04d", random.nextInt(10000))):99999));
			payload.put("isoLanguageCode", "en-us");
			payload.put("categoryName", cat);
			payload.put("organizationName", Org);
			payload.put("engineName", "UNIPHORE");
			langObj.put(payload);
		}
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON);
		request.body(langObj.toString());
		response = request.log().all().put("cti-language");
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
}