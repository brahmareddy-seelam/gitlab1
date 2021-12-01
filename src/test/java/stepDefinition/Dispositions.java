package stepDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.Then;

public class Dispositions extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	
	public static HashMap<String, String> orgMap=new HashMap<>();
	
	public static String data=null;
	public static LinkedHashMap<String, String> dataMap=new LinkedHashMap<>();
	@Then("disposition for callId has intent of {string}")
	public void disposition_for_callid_has_intent_of(String intent)
			throws IOException, URISyntaxException, JSONException {
		loadURL("DATA_COLLECTOR_PORT");
		request.log().all().header("Content-Type","application/json").header("Authorization", "Bearer "+TestCenter.accesstoken);
		response=request.get("dispositions/"+CommonSteps.commonMap.get("callId") );
		String hierarchy = new JSONObject(response.getBody().asString()).getJSONObject("resolution")
				.getString("hierarchy");
		
		String data = new JSONObject(response.getBody().asString()).getJSONObject("resolution")
				.getString("data");
		Assert.assertEquals(hierarchy, intent);
	}
}
