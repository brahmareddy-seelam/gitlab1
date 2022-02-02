package stepDefinition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.aventstack.extentreports.GherkinKeyword;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.Log;
import com.uniphore.ri.main.e2e.TestCenter;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;

public class CommonSteps extends BaseClass{
	
	public static HashMap<String, String>  commonMap=new HashMap<String, String>();
	public static HashMap<String, String> orgMap=new HashMap<String, String>();
	
	public static String userId;
	public static String startDate;
	public static String endDate;
	
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
	    commonMap.put("language", language);
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
		  Integer duration = (int) (((audioInputStream.getFrameLength()) / audioInputStream.getFormat().getFrameRate())) +10;  
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
		loadURL("AUDIO_CONNECTOR_PORT");loadQueryParams(CommonSteps.commonMap);
		String absoluteFolderPath = Paths.get(TestCenter.getInstance().getFile(filePath).getAbsolutePath()).toString();
		request.multiPart("file",new File(absoluteFolderPath),"audio/wav").when();
		response=request.post("offline/data");
		Assert.assertEquals(200, response.getStatusCode());
	  }
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStringToMap(String string) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(string, Map.class);
	}
	
	@Then("update app-profile")
	public void update_profile() throws IOException {
		loadURL("BACKEND_PORT");
		response=request.log().all().get("app-profile");
		JSONObject entityList=new JSONObject(response.getBody().asString()).getJSONObject("data");
		entityList.put("tenantId", 1);
		JSONObject feature=entityList.getJSONObject("featureFlags");
		String featureFlags=feature.toString();
		JSONObject newFeature=new JSONObject();
		for (String str : featureFlags.toString().substring(1,featureFlags.length()-1).split(",")){
			Integer indexOfSeparation = str.indexOf(":");
			String entity = str.substring(1, indexOfSeparation-1);
			newFeature.put(entity, true);
		}
		entityList.put("featureFlags", newFeature);
		loadURL("BACKEND_PORT");
		request.contentType(ContentType.JSON).body(entityList.toString()).when();
		response=request.log().all().put("app-profile");
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
		int concurrency=5;
		String defineJsonrule = new String(Files.readAllBytes(Paths.get(folder)));
		JSONArray asr_data=new JSONArray(defineJsonrule);
		JSONArray finalASR= new JSONArray();
		
		for (int i = 0; i <asr_data.length(); i++) {
			
	        JSONObject jsonObj = asr_data.getJSONObject(i);
	        String k = jsonObj.keys().next();
	        String value=(jsonObj.getJSONObject(k).toString()).replaceAll("\"\"", "");
	       
	       
	        JSONObject parent=new JSONObject();
	        parent.put("languageCode", k.substring(k.indexOf("(")+1, k.indexOf(")")));
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
		request.log().all().contentType(ContentType.JSON).body(finalASR.toString());
		response=request.put("asr-instance");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	
	public void updateASR(String asr_engine) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		String backup = new String(Files.readAllBytes((Paths.get(System.getProperty("user.dir")+"/asr-engine.json").toAbsolutePath())));
		JSONArray array=new JSONArray(backup);
		JSONObject uniphore_us=array.getJSONObject(1).getJSONObject("UNIPHORE(en-us)");
		JSONArray ws=uniphore_us.getJSONArray("ws");
		ws.remove(0);ws.put(asr_engine); 
		System.out.println(uniphore_us);
		File f=new File(System.getProperty("user.dir")+"/asr-engine.json").getAbsoluteFile();
		@SuppressWarnings("resource")
		FileWriter file= new FileWriter(f);
		file.write(array.toString());
		file.flush();
	}
	
	
	
	
	
	
}
