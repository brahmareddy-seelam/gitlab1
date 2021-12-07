@E2eOffline
Feature: Test Offline Audio file
  This will test a offline audio wav load end-to-end

  @flightBooking1 @Regression
  Scenario Outline: Test offline load of call18
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #
    ##And we sync <orgAgentName>
    #
    #	###############################################
    #
    #	##SETTING UP CALL VARIABLES
    #
    #Given a "offline-data" request exists
    And a <audio-file> file exists
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
    Then define and configure entities in folder "ConfigAndDefine/flightBooking1/entities/"
    #
    #
    Then validate and configure rules in folder "ConfigAndDefine/flightBooking1/ruleEntity"
    #
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/flightBooking1/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/flightBooking1/alerts"
    #
    ##############################################
    ###TRAINING FOR ENTITY AND INTENT
    Then train entities
    Then train Alerts
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
    #And entities for callId exist
    #And the entity for callId has "Agent Name" as <agentName>
    #
    #	##############################################
    #
    #		###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    #And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Agent Name" <agentName>
    And a summary for callId has "Flight Number" <Flight Number>
    And a summary for callId has "Departure Date " <Departure  Date>
    #And a summary for callId has "Type " <Type>
    #
    Then edit "Agent Name" as "McKenna"
    And edit "Flight Number" as "1836"
    Then submit the edited summaries
    #
    Then compare if "Agent Name" has "McKenna" for callId
    And compare if "Flight Number" has "1836" for callId
    #
    ##############################################
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    And disposition for callId has intent of <intent>
		And we sync <orgAgentName>
    Then edit and submit disposition intent "Flight_Booking" as "Flight_Cancel"
    Then compare if disposition has changed intent from "Flight_Booking" to "Flight_Cancel"
    #
    ##############################################
    #
    ###DELETE ENTITIES
    #
    Then delete all entities
    Then delete all alerts
    ##############################################
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization | category  | orgAgentName | agentEmail                | role    | language | audio-file                                      | turn | phrase                  | intent                                     | transcript-file                                           | description   | colorVR       | agentName | Flight Number | Departure  Date | Type         |
      | "APITesting" | "flight1" | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/flightBooking1/flightBooking1.wav" |    0 | "thank you for calling" | "Flight_Booking/Book/Round Trip/Departure" | "src/test/resources/transcript-jsons/flightBooking1.json" | "description" | "colorSample" | "kenna"   | "2534"        | "8/30/"         | "round trip" |

  
  
  @flightBooking2 @Regression
  Scenario Outline: Test offline load of call18
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #
    ##And we sync <orgAgentName>
    #
    #	###############################################
    #
    #	##SETTING UP CALL VARIABLES
    #
    #Given a "offline-data" request exists
    And a <audio-file> file exists
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
    Then import ai entities from "ConfigAndDefine/flightBooking2/aiEntities/aiEntity.json"
    #
    Then define and configure entities in folder "ConfigAndDefine/flightBooking2/entities/"
    #
    #
    Then validate and configure rules in folder "ConfigAndDefine/flightBooking2/ruleEntity"
    #
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/flightBooking2/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/flightBooking2/alerts"
    #
    ##############################################
    ###TRAINING FOR ENTITY AND INTENT
    Then train entities
    Then train Alerts
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
    #And the entity for callId has "Agent Name" as <agentName>
    #
    #	##############################################
    #
    #		###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Number " <Number>
    And a summary for callId has "Price rule" <Price rule>
    And a summary for callId has "Travel date Rule" <Travel date Rule>
    And a summary for callId has "Location Rule" <Location Rule>
    #
    Then edit "Location Rule" as "London"
    Then submit the edited summaries
    #
    Then compare if "Location Rule" has "London" for callId
    #
    ##############################################
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    And disposition for callId has intent of <intent>
    And we sync <orgAgentName>
    Then edit and submit disposition intent "Flight_Booking" as "Flight_Cancel"
    Then compare if disposition has changed intent from "Flight_Booking" to "Flight_Cancel"
    ##############################################
    #
    ###DELETE ENTITIES
    #
    Then delete all entities
    Then delete all alerts
    ##############################################
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization | category  | orgAgentName | agentEmail                | role    | language | audio-file                                      | turn | phrase                  | intent                                     | transcript-file                                           | description   | colorVR       | agentName | Number       |											 Price rule                                    | Travel date Rule 											| Location Rule |
      | "APITesting" | "flight2" | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/flightBooking2/flightBooking2.wav" |    0 | "thank you for calling" | "Flight_Booking/Book" | "src/test/resources/transcript-jsons/flightBooking2.json" | "description" | "colorSample" | "kenna"   | "9737037772" | "500 dollars, 3400 dollars, 468 dollars, 376 dollars and 15 cents" | "8/0/0, 8/19/ , 0/19/23, 8/17/ , 9/0/0"| "westchester" |

      
      @delete1
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
      | organization | category  | orgAgentName | agentEmail                | role    | language | audio-file                                      | turn | phrase                  | intent                                     | transcript-file                         | description   | colorVR       | agentName | Flight Number | Date    | Type         |
      | "APITesting" | "flight1" | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/flightBooking1/flightBooking1.wav" |    0 | "thank you for calling" | "Flight_Booking/Book/Round Trip/Departure" | "/transcript-jsons/flightBooking1.json" | "description" | "colorSample" | "kenna"   | "2534"        | "8/30/" | "round trip" |
      