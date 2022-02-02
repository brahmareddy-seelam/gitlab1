package stepDefinition;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;

import io.cucumber.java.en.Then;

public class CacheAndTrain extends BaseClass{
	
    CommonSteps cs=new CommonSteps();
	
	public static HashMap<String, String> orgMap=new HashMap<>();
	
	public CacheAndTrain() {
		loadURL("BACKEND_PORT");
	}

	
	@Then("train entities")
	public void train_entities() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT"); loadQueryparams(CommonSteps.orgMap);
		validate("train/entity");
	}
	
	
	@Then ("submit call category configuration")
	public void submit_call_category_config() throws IOException, URISyntaxException{
		loadURL("BACKEND_PORT"); loadQueryparams(CommonSteps.orgMap);
		validate("submit/call-category");
	}
	
	@Then ("train call-categories")
	public void train_call_categories() throws IOException, URISyntaxException{
		loadURL("BACKEND_PORT");loadQueryparams(CommonSteps.orgMap);
		validate("train/call-category");
	}
	
	
	@Then("refresh all caches")
	public void refresh_all_caches() throws IOException, URISyntaxException {
		loadURL("BACKEND_PORT");
		Assert.assertEquals(200,(request.get("refresh/alerts/cache")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/disposition-config/cache")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/entity-config/cache")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/rules/cache")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/summary-format/cache")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/asr-intances")).getStatusCode());
		Assert.assertEquals(200,(request.get("refresh/cti-langauages")).getStatusCode());
		System.out.println("--------------------Caches refreshed successfully!---------------");
		
	}
	
	
	public void validate(String url) {
		int StatusCode=0;
		StatusCode=request.post(url).getStatusCode();
		for(int i=0;i<3;i++) {
		if(StatusCode!=200) {
			System.out.println("Number "+i);
			StatusCode=request.post(url).getStatusCode();
			System.out.println("API call failed! Trying for "+i+" time");
		}
		}
	}
	
}
