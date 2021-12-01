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
    And a <audio-file> file exists
    And the request organization is <organization>
    And the request category is <category>
    And the request customerId is "426346"
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
    Then define and configure entities in folder "/ConfigAndDefine/20210408_173503_FDOQ/entities/"
    #
    #
    Then validate and configure rules in folder "ConfigAndDefine/20210408_173503_FDOQ/ruleEntity"
    #
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/20210408_173503_FDOQ/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/20210408_173503_FDOQ/alerts"
    #
    ##############################################
    
    ###TRAINING FOR ENTITY AND INTENT
    
    Then train entities
    Then submit call category configuration
    Then train call-categories
    Then refresh all caches
    #
    ##############################################
		
		###SENDING OFFLINE AUDIO REQUEST
		
    When the request with file <audio-file> is sent to the audio-connector
    And wait for <audio-file> to get loaded
   #	
   #	##############################################
   #	
   #	###VERIFYING TRANSCRIPT AGAINST GOLD STANDARD
   #	
    Then a transcript is generated for callId
    And the transcript conversation for callId for <turn> has <phrase>
    And the transcript conversation for callId matches the correct version <transcript-file>
   #	##############################################
   #	
   #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
   #	
     And entities for callId exist
    And the entity for callId has "Agent Name" as <agentName>
   #
   #	##############################################
#
#		###VERIFYING SUMMARY AGAINST GOLD STANDARD
#		
    #And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Agent Name" <agentName>
    And a summary for callId has "Claim ID" <Claim ID>
    
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



    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	

	
    #And the entity for "Customer" is <customerName> and for "Agent" is <agentName> for <callId>
    #And the entity for "Room Type" is <roomName> and for "Credit Card" is "XX-XX" for <callId>
    #And the entity for "Check In" is <checkIn> and for "Check Out" is <checkOut> for <callId>
    
    ###DEBUGGING POST REQUESTS
    #And a summary is POSTed for <callId>
    #And a disposition is POSTed for <callId>
    
    
    #And a summary is generated for callid <callId>
    #And the summary for callid "1614574495383qg7xwdgix" has intent <summaryIntent>
    	
    	
   #@addAgent
  #Scenario Outline: Adding an agent to a current organization
   	#Given a "backbone setup request" organization creation exists
    #And we create an organization called <organization> with description as <description>
   	#And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
   	#And we can add <agentName> as an agent to <organization>
   	#Then we delete <agentName> who is an <role> from <organization>
   	#Then we delete a business process called "GpatBusinessProcess" from "ghanshyam1"
   	#And we delete an organization called <organization>
   	
   	#Examples:
    #| organization | description      | colorVR           | category     | agentName    | role    |
    #| "ghanshyam7" | "description"    | "colorSample"     | "gpatbp2"    | "Gpat2"      | "Agent" |    	

	#@checkTranscriptionQuality
	#Scenario: Comparing transcription JSONs
		#Given a "offline-data" request exists
    #And a <audio-file> file exists
    #And the request organization is <organization>
    #And the request category is <category>
    #And the request customerId is "customer"
    #And the request language is <language>
    #And the request agentId is "chinmoy"
    #Then the request callId is <callId>
    #When the request with file <audio-file> is sent to the audio-connector
   	#And wait for <audio-file> seconds
    #Then a transcript is saved for <callId> in file "conversation4.json"
    
    #Examples:
    #| organization | category | language  | callId          |    audio-file              | turn | phrase           | testEntity      | summaryIntent                   | transcript-file                            |
    #| "E2eTest"    | "E2eTest"| "EUU"     | "chinmoyTest9"  | "/audio-files/call18.wav"  |  0   | "my name is"     | "customerName"  | "Claims/Rate Increase/Reversed" | "/transcript-jsons/call18Transcript.json"   |

    
  #@call18
  #Scenario Outline: Test offline load of call18
  	#Given a "offline-data" request exists
    #And the request organization is <organization>
    #And the request category is <category>
    #And the request customerId is "customer"
    #And the request language is <language>
    #And the request agentId is "chinmoy"
    #Then the request callId is <callId>    
  	
  	# Examples:
    #| organization | category | language  | callId          |    audio-file              | turn | phrase           | testEntity      | summaryIntent                   | transcript-file                            |
    #| "E2eTest"    | "E2eTest"| "EUU"     | "chinmoyTest9"  | "/audio-files/call18.wav"  |  0   | "my name is"     | "customerName"  | "Claims/Rate Increase/Reversed" | "/transcript-jsons/call18Transcript.json"   |

