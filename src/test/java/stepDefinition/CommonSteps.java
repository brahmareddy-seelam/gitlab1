package stepDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;

import com.aventstack.extentreports.GherkinKeyword;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.Log;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CommonSteps extends BaseClass{
	
	public static HashMap<String, String>  commonMap=new HashMap<String, String>();
	public static HashMap<String, String> orgMap=new HashMap<String, String>();
	
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
}
