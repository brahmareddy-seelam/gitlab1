package com.uniphore.ri.main.e2e;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;





public class TestCenter {

	private static TestCenter instance;

	public static String UI_PORT = "80";
	public static String AUDIO_CONNECTOR_PORT = "3410";
	public static String APR_PORT = "3410";
	public static String BACKEND_PORT = "3360";
	public static String DATA_COLLECTOR_PORT = "3110";
	public static String NLP_WRAPPER_PORT = "3330";
	public static String NLP_PORT = "3330";
	public static String OCMS_PORT = "3340";
	public static String UMS_PORT = "3350";
	public static String SMS_PORT = "3440";
	public static String KEYCLOAK_PORT = "8080";
	public static String PLATFORM_PORT = "5432";

	public static String KEYCLOAK_REALM = "demo_realm";

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
	private String keycloakAccessToken;

	private void reset() {

	}
	
	 private String host;
	 

	  public String getHost() {
	    return host;
	  }

	  public void setHost(String host) {
	    this.host = host;
	  }


	public HostAndPort getBaseUrl(ServiceType serviceType) {

		switch (serviceType) {
		case BACKEND:
			return new HostAndPort(backendHost, BACKEND_PORT);
		case UI:
			return new HostAndPort(uiHost, UI_PORT);
		case KEYCLOAK:
			return new HostAndPort(kecloakHost, KEYCLOAK_PORT);
		case NLP:
			return new HostAndPort(nlpHost, NLP_PORT);
		case OCMS:
			return new HostAndPort(backendHost, OCMS_PORT);
		case SMS:
			return new HostAndPort(backendHost, SMS_PORT);
		case ACR:
			return new HostAndPort(backendHost, AUDIO_CONNECTOR_PORT);
		case APR:
			return new HostAndPort(backendHost, APR_PORT);
		case NWR:
			return new HostAndPort(nlpHost, NLP_WRAPPER_PORT);
		case UMS:
			return new HostAndPort(platformHost, UMS_PORT);
		case PLATFORM:
			return new HostAndPort(platformHost, AUDIO_CONNECTOR_PORT);
		case DATA_COLLECTOR:
			return new HostAndPort(nlpHost, DATA_COLLECTOR_PORT);
		default:
			return new HostAndPort(backendHost, BACKEND_PORT);
		}
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

	public static String getAUDIO_CONNECTOR_PORT() {
		return AUDIO_CONNECTOR_PORT;
	}

	public static void setAUDIO_CONNECTOR_PORT(String aUDIO_CONNECTOR_PORT) {
		AUDIO_CONNECTOR_PORT = aUDIO_CONNECTOR_PORT;
	}

	public static String getBACKEND_PORT() {
		return BACKEND_PORT;
	}

	public static void setBACKEND_PORT(String bACKEND_PORT) {
		BACKEND_PORT = bACKEND_PORT;
	}

	public static String getDATA_COLLECTOR_PORT() {
		return DATA_COLLECTOR_PORT;
	}

	public static void setDATA_COLLECTOR_PORT(String dATA_COLLECTOR_PORT) {
		DATA_COLLECTOR_PORT = dATA_COLLECTOR_PORT;
	}

	public static String getNLP_WRAPPER_PORT() {
		return NLP_WRAPPER_PORT;
	}

	public static void setNLP_WRAPPER_PORT(String nLP_WRAPPER_PORT) {
		NLP_WRAPPER_PORT = nLP_WRAPPER_PORT;
	}

	public static String getOCMS_PORT() {
		return OCMS_PORT;
	}

	public static void setOCMS_PORT(String oCMS_PORT) {
		OCMS_PORT = oCMS_PORT;
	}

	public static String getUMS_PORT() {
		return UMS_PORT;
	}

	public static void setUMS_PORT(String uMS_PORT) {
		UMS_PORT = uMS_PORT;
	}

	public static String getKEYCLOAK_PORT() {
		return KEYCLOAK_PORT;
	}

	public static void setKEYCLOAK_PORT(String kEYCLOAK_PORT) {
		KEYCLOAK_PORT = kEYCLOAK_PORT;
	}

	public String getKEYCLOAK_REALM() {
		return KEYCLOAK_REALM;
	}

	public void setKEYCLOAK_REALM(String kEYCLOAK_REALM) {
		KEYCLOAK_REALM = kEYCLOAK_REALM;
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

	public static String getUI_PORT() {
		return UI_PORT;
	}

	public static void setUI_PORT(String uI_PORT) {
		UI_PORT = uI_PORT;
	}

	public static String getAPR_PORT() {
		return APR_PORT;
	}

	public static void setAPR_PORT(String aPR_PORT) {
		APR_PORT = aPR_PORT;
	}

	public static String getNLP_PORT() {
		return NLP_PORT;
	}

	public static void setNLP_PORT(String nLP_PORT) {
		NLP_PORT = nLP_PORT;
	}

	public static String getSMS_PORT() {
		return SMS_PORT;
	}

	public static void setSMS_PORT(String sMS_PORT) {
		SMS_PORT = sMS_PORT;
	}

	public static String getPLATFORM_PORT() {
		return PLATFORM_PORT;
	}

	public static void setPLATFORM_PORT(String pLATFORM_PORT) {
		PLATFORM_PORT = pLATFORM_PORT;
	}

	public String getAccessToken2() {
		return accessToken2;
	}

	public void setAccessToken2(String accessToken2) {
		this.accessToken2 = accessToken2;
	}
	
	

}
