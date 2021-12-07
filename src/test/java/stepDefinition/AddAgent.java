package stepDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;

public class AddAgent extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	public static HashMap<String, String>  map;
	
	
	@Given("we create an organization called {string} with description as {string}")
	public void we_create_an_organization_called_with_description_as(String organization, String description)
			throws IOException, URISyntaxException {

		System.out.println("(before) Order 2 function:");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("description", description);
		jsonObj.put("organization", organization);

		loadURL("OCMS_PORT");
		request.log().all().contentType(ContentType.JSON).body(jsonObj.toString()).post("config/organization");

//		System.out.println("Organization: " + response.getBody());
//		Assert.assertEquals("200 OK", response.getStatusLine() );

	}
	
	
	@Given("we create a business process called {string} with colorVR as {string} and description as {string} for {string}")
	public void add_business_process_to_organization(String businessProcess, String colorVR, String description,
			String organization) throws IOException, URISyntaxException {

		loadURL("OCMS_PORT");
		System.out.println("(before) Order 3 function:");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", businessProcess);
		jsonObj.put("colorVR", colorVR);
		jsonObj.put("description", description);

		JSONArray jsonArr = new JSONArray();
		jsonArr.put(jsonObj);
		request.log().all().contentType(ContentType.JSON).body(jsonArr.toString()).post("config/category/" + organization );

//		System.out.println("Business Process: " + businessProcess);
//		Assert.assertEquals("200 OK", response.getStatusLine());

	}
	
	
	@Given("we can add {string} with email {string} as an agent to {string}")
	public void add_agent_to_organization(String agentName, String email, String organization)
			throws IOException, URISyntaxException {

		loadURL("UMS_PORT");
		System.out.println("(before) Order 4 function:");

		HashMap<String, String> map = new HashMap<>();
		map.put("orgName", organization);

		String[] rolesArr = new String[] { "Agent" };

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("displayName", agentName);
		jsonObj.put("externalId", agentName);
		jsonObj.put("userName", agentName);
		jsonObj.put("password", "Uniphore@123");
		jsonObj.put("roles", rolesArr);
		jsonObj.put("email", email);

		request.log().all().body(jsonObj.toString()).post("config/user-mgmt/user/" + organization + "/");

		System.out.println("Agent: " + response.getBody());
		Assert.assertEquals(200 , response.getStatusCode());

	}
	
	
	@Given("get keycloak accessToken with username {string} and password {string} and client id {string} and grant-type {string}")
	public void get_keycloak_access_token(String username, String password, String clientId, String grantType)
			throws IOException, URISyntaxException {
		try {
			loadURL("KEYCLOAK_PORT");
			map=new HashMap<String, String>();
			map.put("grant_type", grantType);
			map.put("client_id", clientId);
			map.put("username", username);
			map.put("password", password);

			response=request.formParams(map).post("auth/realms/master/protocol/openid-connect/token");
		
			jsonPathEvaluator = response.jsonPath();
		
			String access_token = jsonPathEvaluator.get("access_token");
		
			System.out.println("access token : "+access_token);
			
			TestCenter.getInstance().setKeycloakAccessToken(access_token);

	}catch(Exception e) {
		System.out.println(e.getMessage());
	}
	}
	
	
	@Given("we can add keycloak {string} with email {string} as an agent to {string}")
	public void add_keycloak_agent_to_organization(String agentName, String email, String organization)
			throws IOException, URISyntaxException, JSONException {
		loadURL("KEYCLOAK_PORT");
		System.out.println("(before) Order 4 function:");

		map.put("orgName", organization);
		JSONObject attributes = new JSONObject();
		attributes.put("clientId", agentName);
		attributes.put("stationCode", "110");

		JSONArray groups = new JSONArray();
		groups.put("uniphore-agent-group");

		JSONArray realmRoles = new JSONArray();
		realmRoles.put("office_access");
		realmRoles.put("uma_authorization");

		JSONArray cred=new JSONArray();
//		cred.put(")
		JSONObject userAgent = new JSONObject();
		userAgent.put("username", agentName);
		userAgent.put("firstName", agentName);
		userAgent.put("lastName", agentName);
		userAgent.put("email", email);
		userAgent.put("attributes", attributes);
		userAgent.put("groups", groups);
		userAgent.put("realmRoles", realmRoles);
		userAgent.put("enabled", "true");
		
		TestCenter.getInstance().setAccessToken(TestCenter.getInstance().getKeycloakAccessToken());
		
		request.auth().oauth2(TestCenter.getInstance().getKeycloakAccessToken()).header("Content-Type","application/json");
		
		request.body(userAgent.toString()).when();
		
		response=request.log().all().post("auth/admin/realms/demo_realm/users/");
		
		int reponseCode=response.getStatusCode();
		System.out.println("reponseCode "+reponseCode);
		Assert.assertTrue(reponseCode==201 || reponseCode==409);
//		Assert.assertEquals(Matchers.anyOf(Matchers.is(200),Matchers.is(409)),response.getStatusCode());
//		Assert.assertThat(response.getStatusCode(), Matchers.anyOf(Matchers.is(200),Matchers.is(409)));

	}
	
	
	public int getStationCode(String emailDomain) throws IOException, URISyntaxException, JSONException {

		JSONArray users = readUsers(emailDomain);
		System.out.println("users >>>>>>> " + users);

		JSONObject lastUser = users.getJSONObject(users.length() - 1);
		System.out.println("lastUser >>>>>>> " + lastUser);

		Integer lastStationCode = Integer
				.parseInt(lastUser.getJSONObject("attributes").getJSONArray("stationCode").getString(0));

		return lastStationCode += 1;
	}
	
	
	public JSONArray readUsers(String emailDomain) throws IOException, URISyntaxException, JSONException {

		String orginalAccessToken = TestCenter.getInstance().getKeycloakAccessToken();
		
		System.out.println(orginalAccessToken);
		TestCenter.getInstance().setAccessToken(TestCenter.getInstance().getKeycloakAccessToken());
		loadURL("KEYCLOAK_PORT");
		response=request.log().all().accept(ContentType.JSON).auth()
				.oauth2(orginalAccessToken)
				.get("auth/admin/realms/uniphore/users?search=" + emailDomain + "&max=5000");

		TestCenter.getInstance().setAccessToken(orginalAccessToken);

		JSONArray users = new JSONArray(response.asString());

		return users;
	}
	
	
	@Given("we sync {string}")
	public void sync_user(String username) throws IOException, URISyntaxException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("Authorization", "Bearer " + TestCenter.getInstance().getKeycloakAccessToken());
		jsonObj.put("X-Username", username);

		loadURL("KEYCLOAK_PORT");
		HashMap<String, String> map = new HashMap<>();
		map=new HashMap<String, String>();
		map.put("grant_type", "password");
		map.put("client_id", "demo_client_private");
		map.put("username", port.getProperty("username"));
		map.put("password", port.getProperty("password"));
		
		response=request.log().all().formParams(map).post("auth/realms/demo_realm/protocol/openid-connect/token");
		jsonPathEvaluator = response.jsonPath();
		String access_token = jsonPathEvaluator.get("access_token");
		System.out.println(access_token);
		TestCenter.getInstance().setAccessToken(access_token);
		
		loadURL("OCMS_PORT");
		request.auth().oauth2(access_token);
		response=request.post("configuration/sync-user");
		Map<Object, Object> user=response.jsonPath().getMap("user");
		String startDate = String.valueOf(user.get("creation-date"));
		String endDate = String.valueOf(user.get("modification-date"));
		Object id=   user.get("id");
		cs.setUserId(id.toString());
		cs.setStartDate(startDate);
		cs.setEndDate(endDate);
	}

}
