package stepDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;
import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSenderOptions;

import java.util.Map;

public class AddAgent extends BaseClass{
	
	CommonSteps cs=new CommonSteps();
	public static HashMap<String, String>  map;
	public static String agent=null;
	public static JSONArray JSONResponseBody;
	public static Map<Object, Object> user;
	
	@Given("we create an organization called {string} with description as {string}")
	public void we_create_an_organization_called_with_description_as(String organization, String description)
			throws IOException, URISyntaxException {

		System.out.println("(before) Order 2 function:");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("description", description);
		jsonObj.put("organization", organization);

		loadURL("OCMS_PORT");
		request.log().all().contentType(ContentType.JSON).body(jsonObj.toString()).post("config/organization");

//		System.out.println("Organization: " + response.getBody()); Commenting the lines since successful response is not returning any body!
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
		agent=agentName;
		map.put("orgName", organization);
		JSONObject attributes = new JSONObject();
		attributes.put("clientId", agentName);
		attributes.put("stationCode", "110");

		JSONArray groups = new JSONArray();
		groups.put("uniphore-agent-group");

		JSONArray realmRoles = new JSONArray();
		realmRoles.put("office_access");
		realmRoles.put("uma_authorization");

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
		
		response=request.log().all().post("auth/admin/realms/"+port.getProperty("realm")+"/users/");
		
		int reponseCode=response.getStatusCode();
		System.out.println("reponseCode "+reponseCode);
		Assert.assertTrue(reponseCode==201 || reponseCode==409);
				
		HashMap<String, String>  cred=new HashMap<>();
		cred.put("type","password");
		cred.put("value", port.getProperty("password"));
		cred.put("temporary", "false");
		loadURL("KEYCLOAK_PORT");
		request.auth().oauth2(TestCenter.getInstance().getKeycloakAccessToken()).header("Content-Type","application/json");
		request.body(cred);
		response=request.log().all().put("auth/admin/realms/"+port.getProperty("realm")+"/users/"+getId(agent)+"/reset-password");
		Assert.assertEquals(204, response.getStatusCode());
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
				.get("auth/admin/realms/"+port.getProperty("realm")+"/users?search=apitesting@uniphore.com&max=5000");

		TestCenter.getInstance().setAccessToken(orginalAccessToken);

		JSONArray users = new JSONArray(response.asString());

		return users;
	}
	
	public String getId(String agent) {
		loadURL("KEYCLOAK_PORT");
		response=request.log().all().accept(ContentType.JSON).auth()
				.oauth2(TestCenter.getInstance().getKeycloakAccessToken())
				.get("auth/admin/realms/"+port.getProperty("realm")+"/users?search="+agent);
		JSONResponseBody = new  JSONArray(response.body().asString());
		String id=JSONResponseBody.getJSONObject(0).getString("id");
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@Given("map agent {string} to supervisor {string}")
	public void map_agent_to_supervisor(String agent, String Supervisor) throws IOException, URISyntaxException {
		sync_user(Supervisor);
		String supervisor_id= user.get("id").toString();
		Object agentS= user.get("agents");
		Set<Integer> list = new HashSet<Integer>();
		if(agentS!=null) {
		list.addAll((Collection<? extends Integer>) agentS);
		}
		sync_user(agent);
		String id=user.get("id").toString();
		int agent_id=Integer.parseInt(id);
		boolean status=(list!=null?true:false);
		if(status) {list.add(agent_id);}
		else {list=new HashSet<Integer>();
		list.add(agent_id);}
		JSONObject Agent=new JSONObject();Agent.put("agents", list);
		loadURL("OCMS_PORT");
		request.contentType(ContentType.JSON).body(Agent.toString());
		response=request.log().all().put("configuration/sup-agent-map/"+supervisor_id+"/?tenant-id=1");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	@Given("we sync {string}")
	public void sync_user(String username) throws IOException, URISyntaxException {
//		JSONObject jsonObj = new JSONObject();
//
//		jsonObj.put("Authorization", "Bearer " + TestCenter.getInstance().getKeycloakAccessToken());
//		jsonObj.put("X-Username", username);

		loadURL("KEYCLOAK_PORT");
		
		HashMap<String, String> map = new HashMap<>();
		map=new HashMap<String, String>();
		map.put("grant_type", "password");
		map.put("client_id", port.getProperty("client_id"));
		map.put("username", username);
//		map.put("password", ((username.equalsIgnoreCase("Super"))?"Uniphore@123":port.getProperty("password")));
		map.put("password", superPassword(username));
		response=request.log().all().formParams(map).post("auth/realms/"+port.getProperty("realm")+"/protocol/openid-connect/token");
		jsonPathEvaluator = response.jsonPath();
		String access_token = jsonPathEvaluator.get("access_token");
		System.out.println(access_token);
		TestCenter.getInstance().setAccessToken(access_token);
		
		loadURL("OCMS_PORT");
		request.auth().oauth2(access_token);
		response=request.log().all().post("configuration/sync-user");
		user=response.jsonPath().getMap("user");
		String startDate = String.valueOf(user.get("creation-date"));
		String endDate = String.valueOf(user.get("modification-date"));
		Object id=   user.get("id");
		cs.setUserId(id.toString());
		cs.setStartDate(startDate);
		cs.setEndDate(endDate);
	}
	
	public String superPassword(String supervisor) {
		String password=null;
		switch(supervisor) {
		case "Super":
			password= "Uniphore@123";
			break;
		case "default-supervisor":
			password= "Welcome123";
			break;
		 default:
			password=port.getProperty("password");
			break;
		}
		return password;
	}

	
	@Given("we delete category")
	public void delete_agent() {
		loadURL("OCMS_PORT");
		loadQueryParams(CommonSteps.orgMap);
		response=request.delete("config/category");
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	@Given("we delete organization {string}")
	public void delete_org(String org) {
		loadURL("OCMS_PORT");
		response=request.delete("config/organization/"+org);
		Assert.assertEquals(200, response.getStatusCode());
	}
}
