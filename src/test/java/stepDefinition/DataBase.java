package stepDefinition;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;

import org.junit.Assert;

public class DataBase extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	
	
	public static MongoClient mongoclient;
	public static MongoDatabase mongodb;
	public static MongoCollection<Document> collection;
	public static MongoCursor<Document> cursor;
	public static String token=null;
	
	@Then("I connect to DB and check if collection {string} has {string} as {string}")
	public void validateMongo(String collections, String key, String value ) {
		try {
		mongoConnect(collections);
		while(cursor.hasNext()) {
			Document doc=cursor.next();
			String data=doc.toJson();
			if(data.contains(key)) {
			JSONObject object = new JSONObject(data); 
			String result=(object.get(key).toString());
			Assert.assertEquals(value, result);
			}
			else {
				Assert.assertEquals(true, data.contains(key));
			}
		}
		
	}catch(Exception e) {
		System.out.println(e);
	}
	}
	
	@Then("I update the gost-call-controller")
	public void update_ghost_call_controller() {
		token=cs.getToken("default-admin");
		loadURL("BACKEND_PORT");
		response=request.log().all().auth().oauth2(token).get("contact-filters");
		JSONObject entityList=new JSONObject(response.getBody().asString()).getJSONObject("data");
		entityList.put("enabled", "true");
		JSONObject feature=entityList.getJSONObject("filters");
		String featureFlags=feature.toString();
		JSONObject newFeature=new JSONObject();
		for (String str : featureFlags.toString().substring(1,featureFlags.length()-1).split(",")){
			Integer indexOfSeparation = str.indexOf(":");
			String entity = str.substring(1, indexOfSeparation-1);
			if(entity.equalsIgnoreCase("maxSilenceDuration")) {
				newFeature.put(entity, 20000);
			}
			else if(entity.equalsIgnoreCase("minContactDuration")) {
				newFeature.put(entity, 10000);
			}
			else {
				newFeature.put(entity, 15000);
			}
			
		}
		entityList.put("filters", newFeature);
		token=cs.getToken("default-admin");
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON).body(entityList.toString()).when();
		response=request.log().all().auth().oauth2(token).headers("X-Username",port.getProperty("X-Username")).put("contact-filters");
		Assert.assertEquals(200,response.getStatusCode());
	}
	
	public void mongoConnect(String collections) {
		try {
			String uri="mongodb://uniphore:password@"+prop.getProperty("Backend")+":27017/uniphore?authSource=uniphore";
			mongoclient=MongoClients.create(uri);
			mongodb= mongoclient.getDatabase("uniphore");
			System.out.println("MongoDB connected");
			collection=mongodb.getCollection(collections);
			Bson filter=Filters.eq("sessionId", CommonSteps.commonMap.get("callId"));
			cursor=collection.find(filter).iterator();
		}catch(Exception e) {
			System.out.println(e);
		}
	}

}
