# E2eAPIAutomation

Project for api automation using cucumber framework

# Pre - requisite:

Maven - 3.8.3.

Java -  1.8.0_301.

# Steps to setup Cucumber Framework in Local System.

Login to the Sharepoint -https://uniphore.sharepoint.com/:f:/s/QualityEngineering/EkvbaGjWvdBAmrnvH05jrFcBmU7uX9alcOYb7UHZb1WjSw?e=gcO5WV - Connect to preview . ( For Delivery Team)  For Customer, they will have to pick it up Release package 

Download the code from the path.

 

# To Run Cucumber Scenarios:

Open Command Prompt

Traverse to the folder where the project has been placed/downloaded. C:\Users\user\cucumberFramework\e2eAPIAutomation>

Use the following sample format to define the ASR Engine Properties in asr-engine.json file.
[
	{
		"<ENGINE>(lang)": {
			"<protocol1>": [
				"<ip>:<port>"
			],
			"<protocol2>": [
				"<ip>:<port>"
			]
		}
	}
]
------------------------------------------------------------------------------
Sample:
[
{
    "GOOGLE(en-us)":{
                "http":[
                    "localhost:7010"
                        ]
                    }
 },
 {
    "NTE(es-mx)":{
            "http":[
                "18.118.136.28:10103",
                "18.118.136.28:10102",
                "18.118.136.28:10101"
                  ],
             "ws":[
                 "18.118.136.28:10104"
                 ]
              }    
  }
]

Use the following command to setup the environment related properties.

mvn test -Dcucumber.filter.tags="@env" -Dbackend="18.190.161.131" -Dkeycloak="18.190.161.131" -Dcucumber.options="--plugin html:target/cucumber.html"

Use the following command to run the smoke test. 
mvn test -Dcucumber.filter.tags="@smoke" -Dbackend="18.190.161.131" -Dkeycloak="18.190.161.131" -Dcucumber.options="--plugin html:target/cucumber.html"

Report will be generated in target folder as target/cucumber.html

 

# Note: Replace the backend and keycloak values (18.190.161.131) as required for the environment.

# Live call testing needs CTI Language skill to be ‘99999’ and code has been updated in the path src/test/java/stepDefinition/CTI_Language.java.(Line 97) For any new env, all categories will be created with skillcode ‘99999’ which can be changed by removing ‘99999’ and uncommenting ‘String.format("%04d",random.nextInt(10000))’.
