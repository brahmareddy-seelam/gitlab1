@E2eOffline
Feature: Test Offline Audio file
  This will test a offline audio wav load end-to-end

  @call19 @hotel-booking 
  Scenario Outline: Test offline load of call19
    #Given a "backbone setup request" organization creation exists
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #	And we sync <orgAgentName>
    #
    ###############################################
    #
    ##SETTING UP CALL VARIABLES
    #
    #Given a "offline-data" request exists
    And a <audio-file> file exists
    And the request organization is <organization>
    And the request category is <category>
    And the request customerId is "customer"
    And the request language is <language>
    And the request agentId is <orgAgentName>
    Then generate the callId
    #
    ##############################################
    #And wait for 15 seconds
    ##############################################
    ###DEFINITION AND CONFIGURATION` FOR ENTITY AND INTENT
    ##ENTITY
    Then define and configure entities in folder "/scenarios/call19/entities"
    #
    Then validate and configure rules in folder "ConfigAndDefine/call19/ruleEntity"
    #
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "scenarios/call19/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/call19/alerts"
    #
    ##############################################
    #
    ###TRAINING FOR ENTITY AND INTENT
    Then train entities
    Then submit call category configuration
    Then train call-categories
    Then refresh all caches
    #
    ##############################################
    ###SENDING OFFLINE AUDIO REQUEST
    #
    When the request with file <audio-file> is sent to the audio-connector
    #
    ##############################################
    ###VERIFYING TRANSCRIPT AGAINST GOLD STANDARD
    #
    Then a transcript is generated for callId
    And the transcript conversation for callId for <turn> has <phrase>
    And the transcript conversation for callId matches the correct version <transcript-file>
    #
    ##############################################
    ###VERIFYING ENTITIES AGAINST GOLD STANDARD
    #
    And wait for 15 seconds
    And entities for callId exist
    And the entity for callId has "Agent Name" as <agentName>
    #
    ##############################################
    ###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Customer Name" <customerName>
    And a summary for callId has "Agent Name" <agentName>
    And a summary for callId has "Room Type" <roomType>
    And a summary for callId has "Credit Card Exp Date" <creditCardExpDate>
    And a summary for callId has "Check-In Date" <checkinDate>
    And a summary for callId has "Check-Out Date" <checkoutDate>
    And a summary for callId has "Customer Email" <customerEmail>
    #
    ##############################################
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    #
    And disposition for callId has intent of <intent>
    #
    ##############################################
    ###DELETE ENTITIES
    #
    #Then delete all entities
		#
    ##############################################
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization  | category | orgAgentName     | agentEmail               | role    | language   |  audio-file                | turn | phrase       | intent                        | transcript-file                           | description   | colorVR       | agentName | customerName | roomType                | creditCardExpDate | checkinDate | checkoutDate | customerEmail                        |
      | "APITests"    | "call19" | "APITesting" | "APITesting@uni.com" | "Agent" | "EUU"      |  "/audio-files/call19.wav" |    0 | "my name is" | "Hotel Booking/Deluxe/Booked" | "/transcript-jsons/call19Transcript.json" | "description" | "colorSample" | "michael" | "sarah"      | "sea view deluxe sweet" | "x/xx"            | "26"        | "27"         | "sarah dot parker and gmail dot com" |
