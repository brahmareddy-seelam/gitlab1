package stepDefinition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;

public class CTI_Language extends BaseClass{
	
	CommonSteps cs = new CommonSteps();
	
	@Given("we add language {string} to Organization and Categories")
	public void addLanguage(String lang) {
		
		Random random = new Random();
	    
		
		loadURL("OCMS_PORT");
		response=request.get("config/organization");
		JSONObject jsonObj = new JSONObject(response.body().asString());
		JSONArray jsonArr = jsonObj.getJSONArray("data");
		 JSONArray payloadArray = new JSONArray();
		for(int i=0;i<jsonArr.length();i++) {
			loadURL("OCMS_PORT");
			String Org=jsonArr.get(i).toString();
			response=request.get("config/category/"+jsonArr.get(i).toString());
			JSONObject catObj = new JSONObject(response.body().asString());
			Iterator<String> key=catObj.keys();
			try {
				 String status=(catObj.getJSONArray("data").isEmpty()?"Yes":"No" ) ;
				 JSONArray catArr = catObj.getJSONArray("data");
				
					for(int j=0;j<catArr.length();j++) {
						JSONObject temp = catArr.getJSONObject(j);
						{
							String name=temp.getString("name").trim();
							JSONObject payload = new JSONObject();
							payload.put("ctiLanguageCode", lang);
							payload.put("skillGroupCode", String.format("%04d", random.nextInt(10000)));
							payload.put("isoLanguageCode", "en-us");
							payload.put("categoryName", name);
							payload.put("orgnizationName", Org);
							payload.put("engineName","UNIPHORE");
							
							
							payloadArray.put(payload);
//							
						}
					}
			}catch(Exception e) {
				System.out.println("No Categories found for "+Org);
			}
			
			
			}
		loadURL("BACKEND_PORT");request.contentType(ContentType.JSON);
		request.body(payloadArray.toString());
		response=request.log().all().put("cti-language");
		Assert.assertEquals(200 , response.getStatusCode());
		}
	}


