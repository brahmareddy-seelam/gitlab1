@E2eOffline
Feature: Test Offline Audio file - FDOQ
  This will test a offline audio wav load end-to-end

  @FDOQ @Regression 
  Scenario Outline: Test offline load of call18
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #
    And we sync <orgAgentName>
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
    Then we add language "E" to org <organization> and category <category>
    #
    Then delete all entities
    #
    Then define and configure entities in folder "ConfigAndDefine/FDOQ/entities/"
    #
    #
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/FDOQ/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/FDOQ/alerts"
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
    #And the transcript conversation for callId matches the correct version <transcript-file>
    #	##############################################
    #
    #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
    #
    And entities for callId exist
    And the entity for callId has "Member Id" as <Member Id>
    #
    #	##############################################
    #
    #		###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Member Id" <Member Id>
    And a summary for callId has "Customer Name" <Customer Name>
    And a summary for callId has "Date of Birth" <DOB>
    #
    Then edit "Customer Name" as "John"
    Then edit "Member Id" as "16"
    Then submit the edited summaries
    #
    Then compare if "Customer Name" has "John" for callId
    And compare if "Member Id" has "16" for callId
    ##############################################
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    And disposition for callId has intent of <intent>
    And we sync <orgAgentName>
    Then edit and submit disposition intent "active" as "inactive"
    Then compare if disposition has changed intent from "active" to "inactive"
    #
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"
    #
    ##############################################
    #
    ###DELETE ENTITIES
    #
    Then delete all entities
    Then delete all alerts
    
    ##############################################
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete category

    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization | category | orgAgentName | agentEmail                | role    | language | callId                   | audio-file                                    | turn | phrase                     | intent                   | transcript-file                                 | description   | colorVR       | Member Id | Customer Name | DOB         |
      | "APITests"   | "FDOQ"   | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "1633515436608tgtqqoc02" | "audio-files/FDOQ/FDOQ.wav" |    0 | "how can i help you today" | "insurance/cover/active" | "src/test/resources/transcript-jsons/FDOQ.json" | "description" | "colorSample" | "12"      | "john"        | "1/10/1980" | 
