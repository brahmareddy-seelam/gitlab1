@E2eOffline
Feature: Test Offline Audio file
  This will test a offline audio wav load end-to-end

  @call18
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
    Then the request callId is <callId>
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
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/customerName-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/customerName-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/agentName-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/agentName-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/roomType-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/roomType-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/creditCard-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/creditCard-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/checkInDate-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/checkInDate-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/checkOutDate-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/checkOutDate-eC.json"
    #Then define an entity with entityDefine json "/scenarios/call18/entities/entity-definitions/customerEmail-eD.json" and entityConfig json "/scenarios/call18/entities/entity-configurations/customerEmail-eC.json"
    #
    ##SUMMARY FORMAT
    #
    #Then post summary format
    #
    ##INTENT
    #
    #Then define and configure call categorization with folder "ConfigAndDefine/call18/call-categorization"
    #
    #Then define an intent call categorization with "/intent-call-categorization-definition/hotelbooking-call-categorization-definition.csv"
    #Then configure call category with json "/intent-call-categorization-config/HotelBooking-Level1-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Deluxe-Level2-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Normal-Level2-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Deluxe-Booked-Level3-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Deluxe-NotBooked-Level3-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Normal-Booked-Level3-C.json"
    #Then configure call category with json "/intent-call-categorization-config/Normal-NotBooked-Level3-C.json"
    #
    ##############################################
    
    ###TRAINING FOR ENTITY AND INTENT
    
    #Then train entities
    #Then submit call category configuration
    #Then train call-categories
    Then refresh all caches
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
    #Then a transcript is generated for <callId>
    #And the transcript conversation for <callId> for <turn> has <phrase>
    #And the transcript conversation for <callId> matches the correct version <transcript-file>
   #
   #	##############################################
   #	
   #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
   #	
    #And entities for callid <callId> exist
    #And the entity for callid <callId> has <orgAgentName> as <agentName>
   #
   #	##############################################
#
#		###VERIFYING SUMMARY AGAINST GOLD STANDARD
#		
    And a summary for callid <callId> exists
    #And a summary for callid <callId> has intent of <intent> 
    #And a summary for callid <callId> has "Customer Name" <customerName> 
    #And a summary for callid <callId> has "Agent Name" <agentName>
    #And a summary for callid <callId> has "APITesting1008 Claim ID" <Claim ID>
    #And a summary for callid <callId> has "APITesting1008 Claim date" <Claim Date>
    #
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

    | organization  | category    	| orgAgentName 		 		 |agentEmail              				|role    |language  | callId    			 					 |    audio-file            								 | turn | phrase           |intent                  		  | transcript-file                           | description      | colorVR            | agentName    | customerName |   Claim ID 			|    Claim Date    | customerEmail 								 				|
    | "APITests"   	| "APITests"    | "APITesting1008"     |"APITesting1008@uniphore.com"   |"Agent" |"E"       | "1633515436608tgtqqob88"   | "/audio-files/call18AudioFiles/call18.wav"|  0   | "my name is"     |"insurance/claim/Query" 			| "/transcript-jsons/call18Transcript.json" | "description"    | "colorSample"      | "john"       | "stanley"    |   "20084798"    |    "2/13"        | "sarah dot parker and gmail dot com"  |



    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	

	
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

