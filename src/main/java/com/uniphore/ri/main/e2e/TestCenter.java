package com.uniphore.ri.main.e2e;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;





public class TestCenter {

	private static TestCenter instance;

	public static TestCenter getInstance() {
		if (instance == null) {
			instance = new TestCenter();
			instance.reset();
		}
		return instance;
	}
	
	public static String accesstoken="l!5ZElx+DJy3#9</gZA_V9q5d2=hD9";

	private LinkedHashMap<String, String> summary;
	private HashMap<String, String> entities;
	private String backendHost;
	private String kecloakHost;
	private String uiHost;
	private String nlpHost;
	private String platformHost;

	private String accessToken;
	private String accessToken2;
	public String keycloakAccessToken;

	private void reset() {

	}
	
	 private String host;
	 

	  public String getHost() {
	    return host;
	  }

	  public void setHost(String host) {
	    this.host = host;
	  }



	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setKeycloakAccessToken(String keycloakAccessToken) {
		this.keycloakAccessToken = keycloakAccessToken;
	}

	public String getKeycloakAccessToken() {
		return keycloakAccessToken;
	}

	public File getFile(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		return file;
	}

	public LinkedHashMap<String, String> getSummary() {
		return summary;
	}

	public String getSummaryEntityVal(String key) {
		return summary.get(key);
	}

	public void setSummary(LinkedHashMap<String, String> summaryMap) {
		this.summary = summaryMap;
	}

	public HashMap<String, String> getEntitiesMap() {
		return entities;
	}

	public String getEntitiesEntityVal(String key) {
		return entities.get(key);
	}

	public void setEntitiesMap(HashMap<String, String> entitiesMap) {
		this.entities = entitiesMap;
	}

	
	public HashMap<String, String> getEntities() {
		return entities;
	}

	public void setEntities(HashMap<String, String> entities) {
		this.entities = entities;
	}

	public static void setInstance(TestCenter instance) {
		TestCenter.instance = instance;
	}

	public String getBackendHost() {
		return backendHost;
	}

	public void setBackendHost(String backendHost) {
		this.backendHost = backendHost;
	}

	public String getKecloakHost() {
		return kecloakHost;
	}

	public void setKecloakHost(String kecloakHost) {
		this.kecloakHost = kecloakHost;
	}

	public String getUiHost() {
		return uiHost;
	}

	public void setUiHost(String uiHost) {
		this.uiHost = uiHost;
	}

	public String getNlpHost() {
		return nlpHost;
	}

	public void setNlpHost(String nlpHost) {
		this.nlpHost = nlpHost;
	}

	public String getPlatformHost() {
		return platformHost;
	}

	public void setPlatformHost(String platformHost) {
		this.platformHost = platformHost;
	}

	

	public String getAccessToken2() {
		return accessToken2;
	}

	public void setAccessToken2(String accessToken2) {
		this.accessToken2 = accessToken2;
	}
	
	

}
