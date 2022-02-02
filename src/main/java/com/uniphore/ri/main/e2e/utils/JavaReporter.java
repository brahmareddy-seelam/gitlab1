//package com.uniphore.ri.main.e2e.utils;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Properties;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.net.URI;
//import java.util.Optional;
//
////import com.atlassian.jira.rest.client.api.JiraRestClient;
//////import com.atlassian.jira.rest.client.api.domain.Issue;
////import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
////import com.atlassian.util.concurrent.Promise;  
//
//public class JavaReporter {
//
//	int featureTag=0;
//	int scenarioTag=0;
//	int passedSteps =0;
//	int failedSteps=0;
//	int skippedSteps=0;
//	public void report() throws IOException, ParseException {
//		
//	String output = new String(Files.readAllBytes(Paths
//                .get(System.getProperty("user.dir")+"/target/cucumber.json")));
////	JSONObject obj = new JSONObject(output);
//	JSONArray jsonArray = new JSONArray(output);
//	JSONObject temp = jsonArray.getJSONObject(0);
//	JSONArray elements=temp.getJSONArray("elements");
//	
//	JSONArray element=new JSONArray(elements);
//	JSONObject temp_element=element.getJSONObject(0);
//	String testName=temp_element.getString("name");
//	System.out.println(testName);
//	String html="<!DOCTYPE html>\\r\\n\"\r\n"
//			+ "	              + \"<html>\\r\\n\"\r\n"
//			+ "	              + \"\\r\\n\"\r\n"
//			+ "	              + \"<head>\\r\\n\"\r\n"
//			+ "	              + \"	<title>Cucumber Automation Reports - U Assist</title>\\r\\n\"\r\n"
//			+ "	              + \"	<style>\\r\\n\"\r\n"
//			+ "	              + \".center {\\r\\n\"\r\n"
//			+ "	              + \"  text-align: center;\\r\\n\"\r\n"
//			+ "	              + \"  color: blue;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \".headers{\\r\\n\"\r\n"
//			+ "	              + \"text-align: center;\\r\\n\"\r\n"
//			+ "	              + \"  color: blue;\\r\\n\"\r\n"
//			+ "	              + \"  width: 10%;\\r\\n\"\r\n"
//			+ "	              + \" }\\r\\n\"\r\n"
//			+ "	              + \"table, th, td {\\r\\n\"\r\n"
//			+ "	              + \"  border: 1px  solid black;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \"table {\\r\\n\"\r\n"
//			+ "	              + \"  margin-left: auto; \\r\\n\"\r\n"
//			+ "	              + \"  margin-right: auto;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \".pass{\\r\\n\"\r\n"
//			+ "	              + \"bgcolor=\\\"green\\\";\\r\\n\"\r\n"
//			+ "	              + \"text-align: center;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \".fail{\\r\\n\"\r\n"
//			+ "	              + \"bgcolor=\\\"red\\\";\\r\\n\"\r\n"
//			+ "	              + \"text-align: center;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \"div {\\r\\n\"\r\n"
//			+ "	              + \"  margin-top: 20px ;\\r\\n\"\r\n"
//			+ "	              + \"}\\r\\n\"\r\n"
//			+ "	              + \"</style>\\r\\n\"\r\n"
//			+ "	              + \"</head>\\r\\n\"\r\n"
//			+ "	              + \"	<body>\\r\\n\"\r\n"
//			+ "	              + \"		<h2 class=\\\"center\\\">U-Assist Automation Test Report</h2><div>\r\n"
//			+ "		<table>\r\n"
//			+ "		<tr>\r\n"
//			+ "		<th class=\"headers\">Total Features</th><th class=\"headers\"></th>\r\n"
//			+ "		<th class=\"headers\">Total Steps</th><th class=\"headers\"></th>\r\n"
//			+ "		</tr>\r\n"
//			+ "		<tr>\r\n"
//			+ "		<th class=\"headers\">Total Passed</th><th class=\"headers\" bgcolor=\"22FCA1\">10</th>\r\n"
//			+ "		<th class=\"headers\">Total Failed</th><th class=\"headers\" bgcolor=\"F8AB50\">2</th>\r\n"
//			+ "		</tr>\r\n"
//			+ "		<tr>\r\n"
//			+ "		<th class=\"headers\">Total Skipped</th><th class=\"headers\" bgcolor=\"F9EE16\">2</th>\r\n"
//			+ "		</table>\r\n"
//			+ "		</div><div>\r\n"
//					+ "		<table>\r\n"
//					+ "		<tr><th class=\"headers\">Feature</th><th class=\"headers\">"+temp_element.getString("name")+"</th></tr>\r\n"
//					+ "		<tr><th class=\"headers\">Scenarios:</th><th class=\"headers\"></th></tr>\r\n"
//					+ "		<tr><th class=\"headers\">Steps :</th></tr>\r\n"
//					+ "		</table>\r\n"
//					+ "		</div><div>";
//	System.out.println(html);
//	StringBuilder singleString = new StringBuilder();
//	JSONArray elementValues=temp_element.getJSONArray("steps");
//	for(int i=0;i<elementValues.length();i++) {
//	JSONArray steps=new JSONArray(elementValues);
//	JSONObject temp_step=steps.getJSONObject(i);
//	
//	String stepName=temp_step.getString("keyword")+" "+temp_step.getString("name");
//	System.out.println(stepName);
//	
//	
//	JSONObject pilot = temp_step.getJSONObject("result");
//	String status=(String) pilot.get("status");
////	StringBuilder singleString = new StringBuilder();
//	String colour=statusStyle((String) pilot.get("status"));
//	String html1="<tr><th class=\"headers\">"+temp_step.getString("keyword")+" "+temp_step.getString("name")+"</th><th "+colour+">"+status.toUpperCase()+"</th></tr>";
//	System.out.println(singleString.append(html1));
//	if(status.equalsIgnoreCase("passed")){
//		passedSteps++;
//	}
//	else if(status.equalsIgnoreCase("failed")) {
//		failedSteps++;
//	}
//	else if(status.equalsIgnoreCase("skipped")) {
//		skippedSteps++;
//	}
//	System.out.println(status);
//	}
//	System.out.println(html+singleString+"</div>");
//	System.out.println("Passed "+passedSteps+ "Failed "+failedSteps);
//	}
//	
//	public String statusStyle(String status) {
//		String color = null;
//		switch(status) {
//		case "passed":
//			color="style=\"color:green;\"";
//			break;
//		case "failed":
//			color="style=\"color:red;\"";
//			break;
//		case "skipped":
//			color="style=\"color:yellow;\"";
//			break;
//		}
//		
//		return color;
//	}
//	public void sendMail() throws AddressException, MessagingException, IOException {
//		
//		String output = new String(Files.readAllBytes(Paths
//                .get(System.getProperty("user.dir")+"/target/cucumber.json")));
//		JSONArray jsonArray = new JSONArray(output);
//		JSONObject temp = jsonArray.getJSONObject(0);
//		JSONArray elements=temp.getJSONArray("elements");
//		
//		JSONArray element=new JSONArray(elements);
//		JSONObject temp_element=element.getJSONObject(0);
//		JSONArray elementValues=temp_element.getJSONArray("steps");
//		String html="<!DOCTYPE html>\\r\\n\"\r\n"
//				+ "	              + \"<html>\\r\\n\"\r\n"
//				+ "	              + \"\\r\\n\"\r\n"
//				+ "	              + \"<head>\\r\\n\"\r\n"
//				+ "	              + \"	<title>Cucumber Automation Reports - U Assist</title>\\r\\n\"\r\n"
//				+ "	              + \"	<style>\\r\\n\"\r\n"
//				+ "	              + \".center {\\r\\n\"\r\n"
//				+ "	              + \"  text-align: center;\\r\\n\"\r\n"
//				+ "	              + \"  color: blue;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \".headers{\\r\\n\"\r\n"
//				+ "	              + \"text-align: center;\\r\\n\"\r\n"
//				+ "	              + \"  color: blue;\\r\\n\"\r\n"
//				+ "	              + \"  width: 10%;\\r\\n\"\r\n"
//				+ "	              + \" }\\r\\n\"\r\n"
//				+ "	              + \"table, th, td {\\r\\n\"\r\n"
//				+ "	              + \"  border: 1px  solid black;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \"table {\\r\\n\"\r\n"
//				+ "	              + \"  margin-left: auto; \\r\\n\"\r\n"
//				+ "	              + \"  margin-right: auto;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \".pass{\\r\\n\"\r\n"
//				+ "	              + \"bgcolor=\\\"green\\\";\\r\\n\"\r\n"
//				+ "	              + \"text-align: center;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \".fail{\\r\\n\"\r\n"
//				+ "	              + \"bgcolor=\\\"red\\\";\\r\\n\"\r\n"
//				+ "	              + \"text-align: center;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \"div {\\r\\n\"\r\n"
//				+ "	              + \"  margin-top: 20px ;\\r\\n\"\r\n"
//				+ "	              + \"}\\r\\n\"\r\n"
//				+ "	              + \"</style>\\r\\n\"\r\n"
//				+ "	              + \"</head>\\r\\n\"\r\n"
//				+ "	              + \"	<body>\\r\\n\"\r\n"
//				+ "	              + \"		<h2 class=\\\"center\\\">U-Assist Automation Test Report</h2><div>\r\n"
//				+ "		<table>\r\n"
//				+ "		<tr>\r\n"
//				+ "		<th class=\"headers\">Total Features</th><th class=\"headers\"></th>\r\n"
//				+ "		<th class=\"headers\">Total Steps</th><th class=\"headers\"></th>\r\n"
//				+ "		</tr>\r\n"
//				+ "		<tr>\r\n"
//				+ "		<th class=\"headers\">Total Passed</th><th class=\"headers\" bgcolor=\"22FCA1\">10</th>\r\n"
//				+ "		<th class=\"headers\">Total Failed</th><th class=\"headers\" bgcolor=\"F8AB50\">2</th>\r\n"
//				+ "		</tr>\r\n"
//				+ "		<tr>\r\n"
//				+ "		<th class=\"headers\">Total Skipped</th><th class=\"headers\" bgcolor=\"F9EE16\">2</th>\r\n"
//				+ "		</table>\r\n"
//				+ "		</div><div>\r\n"
//						+ "		<table>\r\n"
//						+ "		<tr><th class=\"headers\">Feature</th><th class=\"headers\">"+temp_element.getString("name")+"</th></tr>\r\n"
//						+ "		<tr><th class=\"headers\">Scenarios:</th><th class=\"headers\"></th></tr>\r\n"
//						+ "		<tr><th class=\"headers\">Steps :</th></tr>\r\n"
//						+ "		<tr><th class=\"headers\"></th></tr>\r\n"
//						+ "		</table>\r\n"
//						+ "		</div>";
//						
//						;
////		String testName=temp_element.getString("name"); //Feature Name
////		System.out.println(testName);
//		
//		
//		for(int i=0;i<elementValues.length();i++) {
//		JSONArray steps=new JSONArray(elementValues);
//		JSONObject temp_step=steps.getJSONObject(i);
//		String stepName=temp_step.getString("keyword")+" "+temp_step.getString("name");
//		System.out.println(stepName);
//		
//		
//		JSONObject pilot = temp_step.getJSONObject("result");
//		String status=(String) pilot.get("status");
//		if(status.equalsIgnoreCase("passed")){
//			passedSteps++;
//		}
//		else if(status.equalsIgnoreCase("failed")) {
//			failedSteps++;
//		}
//		else if(status.equalsIgnoreCase("skipped")) {
//			skippedSteps++;
//		}
//		System.out.println(status);
//		}
//
//		System.out.println("Passed "+passedSteps+ "Failed "+failedSteps);
//		
//		String to = "murali@uniphore.com";
//
//		String from = "mmrao92@gmail.com";
//		final String username = "mmrao92@gmail.com";
//		final String password = "munuswamynaidu";
////		String host = "smtp-mail.outlook.com"; 
//		
//		Properties props = new Properties();
//	      props.put("mail.smtp.auth", "true");
//	      props.put("mail.smtp.starttls.enable", "true");
//	      props.put("mail.smtp.host", "smtp.gmail.com");
//	      props.put("mail.smtp.socketFactory.class",    
//                  "javax.net.ssl.SSLSocketFactory");
//	      props.put("mail.smtp.port", "587");
//		
//		
//		
//		Session session = Session.getInstance(props,
//		         new javax.mail.Authenticator() {
//		            protected PasswordAuthentication getPasswordAuthentication() {
//		               return new PasswordAuthentication(username, password);
//		            }
//			});
//		Message message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(from));
//		 message.setRecipients(Message.RecipientType.TO,
//	              InternetAddress.parse(to));
//		 message.setSubject("Testing Subject");
//		 message.setContent(
//	              "<!DOCTYPE html>\r\n"
//	              + "<html>\r\n"
//	              + "\r\n"
//	              + "<head>\r\n"
//	              + "	<title>Cucumber Automation Reports - U Assist</title>\r\n"
//	              + "	<style>\r\n"
//	              + ".center {\r\n"
//	              + "  text-align: center;\r\n"
//	              + "  color: blue;\r\n"
//	              + "}\r\n"
//	              + ".headers{\r\n"
//	              + "text-align: center;\r\n"
//	              + "  color: blue;\r\n"
//	              + "  width: 10%;\r\n"
//	              + " }\r\n"
//	              + "table, th, td {\r\n"
//	              + "  border: 1px  solid black;\r\n"
//	              + "}\r\n"
//	              + "table {\r\n"
//	              + "  margin-left: auto; \r\n"
//	              + "  margin-right: auto;\r\n"
//	              + "}\r\n"
//	              + ".pass{\r\n"
//	              + "bgcolor=\"green\";\r\n"
//	              + "text-align: center;\r\n"
//	              + "}\r\n"
//	              + ".fail{\r\n"
//	              + "bgcolor=\"red\";\r\n"
//	              + "text-align: center;\r\n"
//	              + "}\r\n"
//	              + "div {\r\n"
//	              + "  margin-top: 20px ;\r\n"
//	              + "}\r\n"
//	              + "</style>\r\n"
//	              + "</head>\r\n"
//	              + "	<body>\r\n"
//	              + "		<h2 class=\"center\">U-Assist Automation Test Report</h2>\r\n"
//	              + "		<div>\r\n"
//	              + "		<table>\r\n"
//	              + "		<tr>\r\n"
//	              + "		<th class=\"headers\">Total Features</th><th class=\"headers\"></th>\r\n"
//	              + "		<th class=\"headers\">Total Steps</th><th class=\"headers\"></th>\r\n"
//	              + "		</tr>\r\n"
//	              + "		<tr>\r\n"
//	              + "		<th class=\"headers\">Total Passed</th><th class=\"headers\" bgcolor=\"22FCA1\"></th>\r\n"
//	              + "		<th class=\"headers\">Total Failed</th><th class=\"headers\" bgcolor=\"F8AB50\">2</th>\r\n"
//	              + "		</tr>\r\n"
//	              + "		<tr>\r\n"
//	              + "		<th class=\"headers\">Total Skipped</th><th class=\"headers\" bgcolor=\"F9EE16\">2</th>\r\n"
//	              + "		</table>\r\n"
//	              + "		</div>\r\n"
//	              + "		<div style=�margin-top: 50px 0�>\r\n"
//	              + "		<table>\r\n"
//	              + "		<tr><th class=\"headers\">Feature</th><th class=\"headers\"></th></tr>\r\n"
//	              + "		<tr><th class=\"headers\">Scenarios:</th><th class=\"headers\"></th></tr>\r\n"
//	              + "		<tr><th class=\"headers\">Steps :</th></tr>\r\n"
//	              + "		<tr><th class=\"headers\"></th></tr>\r\n"
//	              + "		</table>\r\n"
//	              + "		</div>\r\n"
//	              + "	</body>\r\n"
//	              + "	\r\n"
//	              + "</html>",
//	             "text/html");
//		 Transport.send(message);
//		 System.out.println("Sent message successfully....");
//	}
//	
//	public static void main(String args[]) throws IOException, ParseException, AddressException, MessagingException {
//		JavaReporter jr= new JavaReporter();
////		jr.report();
//	}
//}
