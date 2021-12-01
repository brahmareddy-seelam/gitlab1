@E2eOffline
Feature: Test Offline Audio file
  This will test a offline audio wav load end-to-end

  @UniphoreDIalogueStems
  Scenario Outline: Test offline load of call18

   	###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
   	   	
   #Given a "backbone setup request" organization creation exists
   #And we create an organization called <organization> with description as <description>
   #And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
   #And we can add <orgAgentName> with email <agentEmail> as an agent to <organization>
   	
   #	Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
   #	And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>   	
   #	
   #	#And we sync <orgAgentName>
   #	
   #	###############################################
   #	
   #	##SETTING UP CALL VARIABLES
   #	
    #Given a "offline-data" request exists
   Given a <audio-file> file exists
    And the request organization is <organization>
    And the request category is <category>
    And the request customerId is "8090909099"
    And the request language is <language>
    And the request agentId is <orgAgentName>
    Then generate the callId
    #
    ##############################################
    #
    #And wait for 15 seconds
    #
    #############################################
    #
    ###DEFINITION AND CONFIGURATION` FOR ENTITY AND INTENT
    #
    ##ENTITY
    #
    #Then define and configure entities in folder "/ConfigAndDefine/call18/entities/"
    #
    ##SUMMARY FORMAT
    #
    #Then post summary format
    #
    ##INTENT
    #
    #Then define and configure call categorization with folder "ConfigAndDefine/call18/call-categorization"

    ##############################################
    
    ###TRAINING FOR ENTITY AND INTENT
    
    #Then train entities
    #Then submit call category configuration
    #Then train call-categories
    #Then refresh all caches
    #
    ##############################################
		
		###SENDING OFFLINE AUDIO REQUEST
		
    #When the request with file <audio-file> is sent to the audio-connector
    #And wait for <audio-file> for 150 seconds
   #	
   #	##############################################
   #	
   #	###VERIFYING TRANSCRIPT AGAINST GOLD STANDARD
   #	
    Then a transcript is generated for <callId>
    #And the transcript conversation for <callId> for <turn> has <phrase>
    And the transcript conversation for <callId> matches the correct version <transcript-file>
   #
   #	##############################################
   #	
   #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
   #	
    #And entities for callid <callId> exist
    And the entity for callid <callId> has <orgAgentName> as <agentName>
   #
   #	##############################################
#
#		###VERIFYING SUMMARY AGAINST GOLD STANDARD
#		
    #And a summary for callid <callId> exists
    #And a summary for callid <callId> has intent of <intent> 
    #And a summary for callid <callId> has "APITesting1008" <agentName>
    #And a summary for callid <callId> has "APITesting1008 Claim Status" <Claim Status>
    #And a summary for callid <callId> has "APITesting1008 Claim ID" <Claim ID>
    
    ##############################################
    
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    
    #And disposition for callid <callId> has intent of <intent> 
    #
    ##############################################
    #
    ###DELETE ENTITIES
    #
    #Then delete all entities
    
    #Then delete the entity "Customer Name"
    #Then delete the entity "Agent Name"
    #Then delete the entity "Room Type"
    #Then delete the entity "Credit Card Exp Date"
    #Then delete the entity "Check-In Date"
    #Then delete the entity "Check-Out Date"
    #Then delete the entity "Customer Email"
    
    ##############################################

    ###DELETE ORGANIZATION AND AGENT

    #Then we delete <orgAgentName> who is an <role> from <organization>
   	#And we delete an organization called <organization>

    ##############################################
  	Examples:

    | organization  | category    	| orgAgentName 		 		 |agentEmail              				|role    |language  | callId    			 					 |    audio-file            								                     | turn | phrase           |intent                  | transcript-file                                | description      | colorVR            | agentName    | customerName |   Claim ID 		|    Claim Status    																			| 
    | "APITests"   	| "APITests"    | "APITesting1008"     |"APITesting1008@uniphore.com"   |"Agent" |"E"       | "1633515436608tgtqqoc02"   | "/audio-files/UniphoreDIalogueStems/UniphoreDIalogueStems.wav"|  0   | "my name is"     |"insurance/claim" 			| "/transcript-jsons/UniphoreDIalogueStems.json" | "description"    | "colorSample"      | "john"       | "stanley"    |   "8675319"    |    "8675319 we can take care of this right away"        | 



    	
    	
    	
    	
    	
    	
    	@delete
    	Scenario Outline: Delete
    	
    	Given a <audio-file> file exists 						
    	And the request organization is <organization>
    	And the request category is <category>
    	And the request customerId is "8090909099"
    	And the request language is <language>
    	And the request agentId is <orgAgentName>
    	Then delete all entities
    	Then delete all alerts
    	
    	  	
    	 Examples:

    	| organization  | category    	| orgAgentName 		 		 |agentEmail              				|catalogue file       |role    |language   |    audio-file            								 | turn | phrase          |intent                  		  | transcript-file                          										| description      | colorVR            | agentName    | customerName |   Claim ID 			|    Claim Date    | Claim Deadline  |Claim Amount   |Claim Resolve in 	 |
   		| "APITesting"    | "call18"        | "APITesting"     |"APITesting@uniphore.com"   |"entityCatalogue.json"|"Agent" |"EUU"     | "audio-files/call18AudioFiles/call18.wav"|  0   | "my name is"     |"insurance/claim/Query" 			| "src/test/resources/transcript-jsons/call18Transcript.json" | "description"    | "colorSample"      | "john"       | "stanley"    |   "20084798"    |    "2/13"        | "2/23/"				 |"4000 dollars" |"5 business days"  |
    	
    	
    	
    	@delete
    	Scenario Outline: Delete
    	
    	Given a <audio-file> file exists 						
    	And the request organization is <organization>
    	And the request category is <category>
    	And the request customerId is "8090909099"
    	And the request language is <language>
    	And the request agentId is <orgAgentName>
    	Then delete all entities
    	Then delete all alerts
    	
    	  	
    	 Examples:

    	| organization 	  | category    	| orgAgentName 		 		 |agentEmail              				|catalogue file        |role    |language  |    audio-file            								 | turn | phrase           |intent                  		  | transcript-file                          										 | description      | colorVR            | agentName    | customerName |   Claim ID 		 |    Claim Date    | Claim Deadline |Claim Amount   |Claim Resolve in 	 |
   		| "APITests"    | "bill"        | "APITesting1008"     |"APITesting1008@uniphore.com"   |"entityCatalogue.json"|"Agent" |"EUU"     | "audio-files/verizonAudioFile/verizon.wav"|  0   | "my name is"     |"insurance/claim/Query" 			| "src/test/resources/transcript-jsons/verizonTranscript.json" | "description"    | "colorSample"      | "john"       | "stanley"    |   "20084798"    |    "2/13"        | "2/23/"				 |"4000 dollars" |"5 business days"  |
    	
