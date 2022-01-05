@E2eOffline
Feature: Test Offline Audio file - UniphoreDIalogueStems
  This will test a offline audio wav load end-to-end

  @UDS @smoke @Regression
  Scenario Outline: Test offline load of UDS and validate transcript with Golden standard
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    And we sync <orgAgentName>
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
    Then we add language "E" to org <organization> and category <category>
    ##ENTITY
    Then delete all entities
    #
    Then import ai entities from "ConfigAndDefine/UDS/aiEntities/aiEntity.json"
    #
    Then define and configure entities in folder "ConfigAndDefine/UDS/entities"
    #
    Then validate and configure rules in folder "ConfigAndDefine/UDS/ruleEntity"
    ##SUMMARY FORMAT
    #
    Then post summary format
    #
    ##INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/UDS/call-categorization"
    #
    Then configure alerts in folder "ConfigAndDefine/UDS/alerts"
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
    #
    #
    #	##############################################
    #
    #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
    #
    And entities for callId exist
    And the entity for callId has "Person" as <agentName>
    #
    #	##############################################
    #
    #		###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Person" <agentName>
    And a summary for callId has "Repeat Customer" <Repeat Customer>
    And a summary for callId has "Date" <Date>
    And a summary for callId has "Duration" <ETC>
    And a summary for callId has "Claim Number" <Claim Number>
    #
    Then edit "Person" as "Johnson"
    And edit "Duration" as "7 business days"
    Then submit the edited summaries
    #
    Then compare if "Person" has "Johnson" for callId
    And compare if "Duration" has "7 business days" for callId
    ##############################################
    ###VERIFYING DISPOSITION AGAINST GOLD STANDARD
    And disposition for callId has intent of <intent>
    Then edit and submit disposition intent "Approval" as "Pending"
    And we sync <orgAgentName>
    Then compare if disposition has changed intent from "Approval" to "Pending"
    #
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"
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
      | organization | category | orgAgentName | agentEmail                | role    | language | audio-file                                                     | turn | phrase       | intent                     | transcript-file                                | description   | colorVR       | agentName | Repeat Customer | Claim Number  | Date     | ETC               |
      | "APITesting"   | "UDS"    | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/UDS/UDS.wav" |    0 | "my name is" | "insurance/claim/Approval" | "src/test/resources/transcript-jsons/UDS.json" | "description" | "colorSample" | "john"    | "YES"           | "8675319" | "a week" | "5 business days" |

  
  