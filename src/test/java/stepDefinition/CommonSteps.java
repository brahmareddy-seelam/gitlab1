package stepDefinition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.Log;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.lingala.zip4j.ZipFile;

public class CommonSteps extends BaseClass{
	

	
	public static HashMap<String, String> commonMap = new HashMap<String, String>();
	public static HashMap<String, String> orgMap = new HashMap<String, String>();
	public static HashMap<String, String> map = new HashMap<>();
	public static String userId;
	public static String startDate;
	public static String endDate;
	public static String token = null;
	public static List<String> value;
	public static Integer duration = 0;
	public static Map<String, Map<String, String>> oldValue;
	public static Map<String, Map<String, String>> newValue;
	public static Map<String, Map<String, String>> reportData;
	public static String startTime = null;
	public static String endtime = null;
	public static String data = null;
	public static String[] keyArr;

	public static String Total_agents_who_received_at_least_1_call = "Total agents who received at least 1 call";
	public static String Total_contacts_received_at_logger = "Total contacts received at logger";
	public static String Total_ghost_contacts = "Total ghost contacts";
	public static String Contacts_with_transcripts = "Contacts with transcripts";
	public static String Total_contacts_with_on_demand_requests = "Total contacts with on demand requests";
	public static String Total_On_Demand_Requests = "Total On Demand Requests";
	public static String Total_contacts_with_at_least_1_NLPAI_entity_extracted = "Total contacts with at least 1 NLP/AI entity extracted";
	public static String Average_ACW_time = "Average ACW time (secs)";
	public static String Count = "Count";
	public static String Total_contacts_with_summaries_generated = "Total contacts with summaries generated";
	public static String Total_summaries_generated = "Total summaries generated";
	public static String Total_default_Summaries = "Total default Summaries";
	public static String Total_meaningful_summaries = "Total meaningful summaries";
	public static String Total_contacts_with_summary_failed = "Total contacts with summary failed";
	public static String Average_accuracy = "Average accuracy (%)";
	public static String Average_summary_latency = "Average summary latency (secs)";
	public static String End_of_call = "End of call (ACW)";
	public static String On_demand_summary = "On demand summary";
	
	
	@And("a {string} file exists")
	  public void the_call19_wav_file_exists(String audioFile) throws IOException, ClassNotFoundException {
//		scenarioDef.createNode(new GherkinKeyword("Then"), "a {string} file exists");
	    File wavFile = TestCenter.getInstance().getFile(audioFile);
	    assert (wavFile.exists() && !wavFile.isDirectory() && audioFile.endsWith(".wav")):"audio file " + audioFile + " does not exist or is not a wav file";
	    Log.info("----------------------Audio File exists-----------------------------");
	  }

	@And("the request organization is {string}")
	  public void the_request_organization_is(String organization) {
	    commonMap.put("organization", organization);
	    orgMap.put("organization", organization);
//	    Log.info("-----------------Organization added to Dataprovider-----------------");
	  }
	
	@And("the request category is {string}")
	  public void the_request_category_is(String category) {
	    commonMap.put("category", category);
	    orgMap.put("category", category);
//	    Log.info("-----------------Category added to Dataprovider-----------------");
	  }

	 @And("the request customerId is {string}")
	  public void the_request_customer_id_is(String customerId) {
	    commonMap.put("customerId", customerId);
	  }

	 @And("the request language is {string}")
	  public void the_request_language_is(String language) {
		CTI_Language.ctiLang();
	    commonMap.put("language", CTI_Language.language.get("cti"));
	  }

	 @And("the request agentId is {string}")
	  public void the_request_agent_id_is(String agentId) {
	    commonMap.put("agentId", agentId);
	  }

	 @And("generate the callId")
	  public void the_request_call_id_is() {
		 long time=System.currentTimeMillis();
	    commonMap.put("callId", Long.toString(time).concat(RandomStringUtils.randomAlphanumeric(9).toLowerCase()).toString());
	    System.out.println(commonMap.toString());
	  }
	
	
	 public String getStartDate() {
		 return startDate;
	 }
	 
	 
	 public void setUserId(String string) {
		 CommonSteps.userId=string;
	 }
	 
	 
	 public void setStartDate(String startDate) {
		 CommonSteps.startDate=startDate;
	 }
	 
	 public String getEndDate() {
		 return endDate;
	 }
	 
	 public void setEndDate(String endDate) {
		 CommonSteps.endDate=endDate;
	 }
	 
	 
	 public String getUserid() {
		 return userId;
	 }
	 
	
	public String getMap(String key) {
		return commonMap.get(key);
	}

	public HashMap<String, String> getCommonMap() {
		return commonMap;
	}
	
	public void setMap(HashMap<String, String> commonMap) {
		CommonSteps.commonMap = commonMap;
	}
	
	@Then("wait for {string} to get loaded")
	  public void add_wait_for_call(String audioFile) throws InterruptedException, UnsupportedAudioFileException, IOException {
		  File wavFile = TestCenter.getInstance().getFile(audioFile);
		  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
		  duration = (int) (((audioInputStream.getFrameLength()) / audioInputStream.getFormat().getFrameRate())) +10;  
		  System.out.println("Total duration "+duration+" seconds");
		  TimeUnit.SECONDS.sleep(duration);
	  }
	
	
	@Then("wait for {int} seconds")
	  public void wait_for_seconds(Integer seconds) throws InterruptedException, UnsupportedAudioFileException, IOException {
		  TimeUnit.SECONDS.sleep(seconds);
	  }
	
	
	@When("the request with file {string} is sent to the audio-connector")
	  public void the_request_with_is_sent_to_the_audio_connector(String filePath)
	      throws IOException, URISyntaxException {
		token=getToken(port.getProperty("username"));
		loadURL("AUDIO_CONNECTOR_PORT");loadQueryParams(CommonSteps.commonMap);
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(filePath).getAbsolutePath()).toString();
		request.multiPart("file",new File(absoluteFolderPath),"audio/wav").when();
		response=request.auth().oauth2(token).post("offline/data");
		Assert.assertEquals(200, response.getStatusCode());
	  }
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStringToMap(String string) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(string, Map.class);
	}
	
	
	
	public String getToken(String user) {
		String access_token=null;
		loadURL("KEYCLOAK_PORT");
		map=new HashMap<String, String>();
		map.put("grant_type", "password");
		map.put("client_id", "service-client");
		map.put("username", user);
		map.put("password", user.equalsIgnoreCase("APITesting")?"Welcome@123":"Welcome123");
		map.put("client_secret", "58e85b73-aaaa-41a9-b3c3-5e1d81d01a33");

		response=request.log().all().formParams(map).post("auth/realms/uniphore/protocol/openid-connect/token");
	
		jsonPathEvaluator = response.jsonPath();
	
		access_token = jsonPathEvaluator.get("access_token");
	
		System.out.println("access token : "+access_token);
		
//		TestCenter.getInstance().setKeycloakAccessToken(access_token);

		return access_token;
		
	}
	
	@Then("update app-profile")
	public void update_profile() throws IOException {
		token=getToken("default-admin");
		loadURL("BACKEND_PORT");
		response=request.log().all().auth().oauth2(token).get("app-profile");
		JSONObject entityList=new JSONObject(response.getBody().asString()).getJSONObject("data");
		entityList.put("tenantId", 1);
		JSONObject feature=entityList.getJSONObject("featureFlags");
		String featureFlags=feature.toString();
		JSONObject newFeature=new JSONObject();
		for (String str : featureFlags.toString().substring(1,featureFlags.length()-1).split(",")){
			Integer indexOfSeparation = str.indexOf(":");
			String entity = str.substring(1, indexOfSeparation-1);
			newFeature.put(entity, (entity.equalsIgnoreCase("inCall")?false:true));
		}
		entityList.put("featureFlags", newFeature);
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON).body(entityList.toString()).when();
		response=request.log().all().auth().oauth2(token).put("admin/app-profile");
		Assert.assertEquals(201,response.getStatusCode());
	}
	
	@Then("we update asr-engine from folder {string}")
	public void update_asr(String folder) throws JSONException, IOException {
		String asr_engine=System.getProperty("asrengine");
		if(asr_engine!="0") {
			this.updateASR(asr_engine);
			this.update_asr_engine(folder);
		}else{
			update_asr_engine(folder);
		}
	}
	
	public void update_asr_engine(String folder) throws IOException {
		token=getToken("default-admin");
		int concurrency=5;
		String defineJsonrule = new String(Files.readAllBytes(Paths.get(folder)));
		JSONArray asr_data=new JSONArray(defineJsonrule);
		JSONArray finalASR= new JSONArray();
		
		for (int i = 0; i <asr_data.length(); i++) {
			
	        JSONObject jsonObj = asr_data.getJSONObject(i);
	        String k = jsonObj.keys().next();
	        String value=(jsonObj.getJSONObject(k).toString()).replaceAll("\"\"", "");
	       
	       
	        JSONObject parent=new JSONObject();
//	        String language=prop.getProperty("Tag").replace("@env", "");
	        CTI_Language.ctiLang();
	        if(jsonObj.has("GOOGLE(en-us)")) {
		        parent.put("languageCode", "en-us");
		        }
	        else {
	        parent.put("languageCode", CTI_Language.language.get("iso"));
	        }
	        parent.put("engineName", k.substring(0, k.indexOf('(')));
	        JSONArray endpoint=new JSONArray();
	        
	        for (String s : value.toString().substring(0,value.length()).split("]")) {
	        	JSONObject child=new JSONObject();
	        if(!s.contains("}")) {
	        Integer indexOfSeparation = value.indexOf(":");
	        String entity = s.substring(1, indexOfSeparation);
	        String values=s.substring(s.indexOf("[")+1, s.length());
	        String new_value=values.replaceAll("\"", "").trim();
	        if(new_value.contains(",")) {
	        	 for (String str : new_value.toString().substring(0,new_value.length()).split(",")) {
	        		 JSONObject child_value=new JSONObject();
	        		 child_value.put("url", str);
	        		 child_value.put("protocol", entity.replaceAll("\"", "").trim());
	        		 child_value.put("concurrency", concurrency);
	     	       endpoint.put(child_value);
	     	      
	        		 System.out.println(str);
	        	 }
	        }
	        else {
	        child.put("url", new_value);
	        child.put("protocol", entity.replaceAll("[^a-zA-Z0-9]", " ").trim());
	        child.put("concurrency", concurrency);
	        }
	        }
	        else {
	        }
	        if(!child.isEmpty()) {
	        endpoint.put(child);
	        }
	    }
	        parent.put("endpoints", endpoint);
	        finalASR.put(parent);
		}
		concurrency++;
		System.out.println(finalASR.toString());
		loadURL("BACKEND_PORT");
		loadQueryBasic("default-admin");
		request.log().all().contentType(ContentType.JSON).body(finalASR.toString());
		response=request.auth().oauth2(token).put("asr-instance");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	
	public void updateASR(String asr_engine) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		String backup = new String(Files.readAllBytes((Paths.get(System.getProperty("user.dir")+"/asr-engine.json").toAbsolutePath())));
		JSONArray array=new JSONArray(backup);
		JSONObject uniphore_us=array.getJSONObject(1).getJSONObject("UNIPHORE(en-us)");
		JSONArray ws=uniphore_us.getJSONArray("wss");
		ws.remove(0);ws.put(asr_engine); 
		System.out.println(uniphore_us);
		File f=new File(System.getProperty("user.dir")+"/asr-engine.json").getAbsoluteFile();
		@SuppressWarnings("resource")
		FileWriter file= new FileWriter(f);
		file.write(array.toString());
		file.flush();
	}
	
	
	
	}
	
	

