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
