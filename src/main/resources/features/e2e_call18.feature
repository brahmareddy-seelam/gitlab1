Feature: Test Offline Audio file - Call18

  @call18 @Regression 
  Scenario Outline: To test offline load of call18 and validate the summary and Feedbackloop
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #And we sync <orgAgentName>
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
    #############################################
    #Then create entity catalog from <catalogue file>
    #
    #And wait for 15 seconds
    #
    ############################################
    #
    ##DEFINITION AND CONFIGURATION` FOR ENTITY AND INTENT
    #
    #ENTITY
    Then we add language "E" to org <organization> and category <category>
    #
    Then delete all entities
    Then delete all dispositions
    #
    Then import ai entities from "ConfigAndDefine/call18/aiEntities/aiEntity.json"
    #
    Then define and configure entities in folder "ConfigAndDefine/call18/entities/"
    #
    #Then validate and configure rules in folder "ConfigAndDefine/call18/ruleEntity"
    #
    #SUMMARY FORMAT
    #
    Then post summary format
    #
    #INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/call18/call-categorization"
    #
    #
    Then configure alerts in folder "ConfigAndDefine/call18/alerts"
    #
    ###########################################
    #
    #TRAINING FOR ENTITY AND INTENT
    Then train entities
    Then train Alerts
    Then submit call category configuration
    Then train call-categories
    Then refresh all caches
    #
    Then configure complex entities "ConfigAndDefine/call18/complexEntity"
    Then validate and configure rules in folder "ConfigAndDefine/call18/complexEntity/complexRule"
    Then train complex rules
    Then post summary format
    #############################################
    #
    #		###SENDING OFFLINE AUDIO REQUEST
    #
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
    #	##############################################
    #
    #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
    #
    And entities for callId exist
    And the entity for callId has "Agent Name" as <agentName>
    #
    #	##############################################
    #
    #	 ###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Agent Name" <agentName>
    And a summary for callId has "Claim ID" <Claim ID>
    And a summary for callId has "Claim Date" <Claim Date>
    And a summary for callId has "Claim DeadLine" <Claim Deadline>
    And a summary for callId has "Claim Amount" <Claim Amount>
    And a summary for callId has "Latest By" <Claim Resolve in>
    And a summary for callId has "Elapsed time" <Elapsed time>
    #And a summary for callId has "Name Asc" <Name Asc>
    #And a summary for callId has "Claim Amount Rule" <Claim Amount>
    #And a summary for callId has "Rule complex" <Rule complex>
    #
    Then edit "Agent Name" as "JohnSmith"
    And edit "Claim Amount" as "4200 dollars"
    Then submit the edited summaries
    #
    Then compare if "Agent Name" has "JohnSmith" for callId
    And compare if "Claim Amount" has "4200 dollars" for callId
    #
    #############################################
    #
    ##VERIFYING DISPOSITION AGAINST GOLD STANDARD
    #
    And disposition for callId has intent of <intent>
    Then edit and submit disposition intent "Query" as "approved"
    And we sync <orgAgentName>
    Then compare if disposition has changed intent from "Query" to "approved"
    #
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"
    #############################################
    #
    ##DELETE ENTITIES
    #
    Then delete all entities
    Then delete all alerts

    #
    ##############################################
    #
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete category
    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization | category | orgAgentName | agentEmail                | catalogue file         | role    | language | audio-file                                | turn | phrase       | intent                  | transcript-file                                                | description   | colorVR       | agentName | customerName | Claim ID   | Claim Date | Claim Deadline | Claim Amount   | Claim Resolve in  | Name Asc    | Claim Amount Rule | Elapsed time | Rule complex |
      | "APITesting" | "call18" | "APITesting" | "APITesting@uniphore.com" | "entityCatalogue.json" | "Agent" | "E"      | "audio-files/call18AudioFiles/call18.wav" |    0 | "my name is" | "insurance/claim/Query" | "src/test/resources/transcript-jsons/call18TranscriptASR.json" | "description" | "colorSample" | "john"    | "stanley"    | "20084798" | "2/13"     | "2/23/"        | "4000 dollars" | "5 business days" | "john, jon" | "4000 dollars"    | "5 weeks"    | "1237619628" |

  @catalog
  Scenario Outline: To create entity-catalog
    Then create entity catalog from <catalogue file>

    Examples: 
      | catalogue file         |
      | "entityCatalogue.json" |

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
      | organization | category | orgAgentName | agentEmail                | catalogue file         | role    | language | audio-file                                | turn | phrase       | intent                  | transcript-file                                             | description   | colorVR       | agentName | customerName | Claim ID   | Claim Date | Claim Deadline | Claim Amount   | Claim Resolve in  |
      | "APITesting" | "call18" | "APITesting1" | "APITesting@uniphore.com" | "entityCatalogue.json" | "Agent" | "E"      | "audio-files/call18AudioFiles/call18.wav" |    0 | "my name is" | "insurance/claim/Query" | "src/test/resources/transcript-jsons/call18Transcript.json" | "description" | "colorSample" | "john"    | "stanley"    | "20084798" | "2/13"     | "2/23/"        | "4000 dollars" | "5 business days" |

  @cti
  Scenario Outline: To add lnaguage to Org
    Then we add language "E" to org <organization> and category <category>

    #Given we add language "E" to Organization and Categories
    Examples: 
      | organization | category | orgAgentName | agentEmail                | catalogue file         | role    | language | audio-file                                | turn | phrase       | intent                  | transcript-file                                             | description   | colorVR       | agentName | customerName | Claim ID   | Claim Date | Claim Deadline | Claim Amount   | Claim Resolve in  |
      | "APITesting" | "UDS"    | "APITesting" | "APITesting@uniphore.com" | "entityCatalogue.json" | "Agent" | "E"      | "audio-files/call18AudioFiles/call18.wav" |    0 | "my name is" | "insurance/claim/Query" | "src/test/resources/transcript-jsons/call18Transcript.json" | "description" | "colorSample" | "john"    | "stanley"    | "20084798" | "2/13"     | "2/23/"        | "4000 dollars" | "5 business days" |

  @alert
  Scenario Outline: To validate alert to supervisot
    Then verify that supervisor has alert <name> with type <type>

    Examples: 
      | name            | type                |
      | "Call Duration" | "Information Alert" |

  @test
  Scenario Outline: Test
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>

    #Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    #And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #And we sync <orgAgentName>
    Examples: 
      | organization | category | orgAgentName | agentEmail                | role    | language | audio-file                | turn | phrase       | intent                  | transcript-file                                | description   | colorVR       | agentName | Repeat Customer | Claim Number | Date     | ETC               |
      | "APITesting" | "UDS"    | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/UDS/UDS.wav" |    0 | "my name is" | "insurance/claim/Query" | "src/test/resources/transcript-jsons/UDS.json" | "description" | "colorSample" | "john"    | "YES"           | "8675319"    | "a week" | "5 business days" |
