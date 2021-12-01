package com.uniphore.ri.main.e2e;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.path.json.JsonPath;
import com.uniphore.*;
public class BaseClass {
	
	public static RequestSpecification request=null;
	public static Response response;
	public static JsonPath jsonPathEvaluator=null;
	
	public static Properties prop=new Properties();
	public static Properties port=new Properties();
	
	public BaseClass() {
	FileInputStream fis = null;
	FileInputStream portfis = null;
	try {
		fis = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/properties/env.properties");
		portfis=new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/properties/port.properties");
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	try {
		prop.load(fis);
	/*	fis.close();
		FileOutputStream out = new FileOutputStream(System.getProperty("user.dir")+"/src/test/resources/properties/env.properties");
		prop.setProperty("Backend", System.getProperty("backend"));
		prop.setProperty("Keycloak", System.getProperty("keycloak"));
		prop.store(out, null);
		out.close(); */
		
		port.load(portfis);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}
	
	
	public static RequestSpecification loadURL(String env) {
		try {
			request=null;
		switch(env) {
		case "UI_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("UI_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("UI_PORT"));
			break;
		case "AUDIO_CONNECTOR_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("AUDIO_CONNECTOR_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("AUDIO_CONNECTOR_PORT"));
			break;
		case "APR_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("APR_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("APR_PORT"));
			break;
		case "BACKEND_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("BACKEND_PORT");
			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("BACKEND_PORT"));
			break;
		case "DATA_COLLECTOR_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("DATA_COLLECTOR_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("DATA_COLLECTOR_PORT"));
			break;
		case "NLP_WRAPPER_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("NLP_WRAPPER_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("NLP_WRAPPER_PORT"));
			break;
		case "NLP_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("NLP_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("NLP_PORT"));
			break;
		case "OCMS_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("OCMS_PORT");
			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("OCMS_PORT"));
			break;
		case "UMS_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("UMS_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("UMS_PORT"));
			break;
		case "SMS_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("SMS_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("SMS_PORT"));
			break;
		case "KEYCLOAK_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Keycloak")+":"+port.getProperty("KEYCLOAK_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("KEYCLOAK_PORT"));
			break;
		case "PLATFORM_PORT":
			RestAssured.baseURI="http://"+prop.getProperty("Backend")+":"+port.getProperty("PLATFORM_PORT");
//			Log.info("Setting URL to http:"+prop.getProperty("Backend")+":"+port.getProperty("PLATFORM_PORT"));
			break;
		}
		RestAssured.urlEncodingEnabled = true;
		request=RestAssured.given();
	}catch(Exception e){
		System.out.println("Exception "+e.getMessage());
	}
		return request;
	}
	
	public static RequestSpecification loadQueryparams(HashMap<String, String> map) {
		request.log().all().header("Authorization", "Bearer "+TestCenter.accesstoken).header("Content-Type","application/json")
		.queryParams(map);
		return request;
	}
	
	public static RequestSpecification loadQueryParams(HashMap<String, String> map) {
		request.log().all().header("Authorization", "Bearer "+TestCenter.accesstoken)
		.queryParams(map);
		return request;
	}
	
}
