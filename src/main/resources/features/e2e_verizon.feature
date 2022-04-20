Feature: Test Offline Audio file - verizon

  @verizon @smoke @Regression
  Scenario Outline: To test offline load of verizon call and validate the output
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    #Given a "backbone setup request" organization creation exists
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
    Then delete all entities
    Then delete all dispositions
    #
    Then we add language "E" to org <organization> and category <category>
    #ENTITY
    #
    Then define and configure entities in folder "ConfigAndDefine/verizon/entities/"
    #
    Then validate and configure rules in folder "ConfigAndDefine/verizon/ruleEntity"
    #
    #SUMMARY FORMAT
    #
    Then post summary format
    #
    #INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/verizon/call-categorization"
    #
    #
    Then configure alerts in folder "ConfigAndDefine/verizon/alerts"
    #
    ###########################################
    #
    #TRAINING FOR ENTITY AND INTENT
    #
    Then train entities
    Then train Alerts
    Then submit call category configuration
    Then train call-categories
    Then refresh all caches
    #
    Then configure complex entities "ConfigAndDefine/verizon/complexEntity"
    
    Then validate and configure rules in folder "ConfigAndDefine/verizon/complexEntity/complexRule"
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
    And the transcript conversation for callId matches the correct version <transcript-file>
    #
    #	##############################################
    #
    #	###VERIFYING ENTITIES AGAINST GOLD STANDARD
    #
    And entities for callId exist
    And the entity for callId has "Phone Upgrade" as <phoneupgrade>
    #
    #	##############################################
    #
    #	 ###VERIFYING SUMMARY AGAINST GOLD STANDARD
    #
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Phone Upgrade" <phoneupgrade>
    And a summary for callId has "Social Security Number" <Social Security Number>
    And a summary for callId has "Purchase Option 1" <Purchase Option 1>
    And a summary for callId has "Time to deliver" <Time to deliver>
    And a summary for callId has "Purchase Option 2" <Purchase Option 2>
    And a summary for callId has "Customer Wireless Number" <Customer Wireless Number>
    And a summary for callId has "Purchase Options Rule" <Purchase Options Rule>
    #And a summary for callId has "Phone Opted" <Phone Opted>
    #And a summary for callId has "Claim Amount Rule" <Claim Amount>
    #And a summary for callId has "Rule complex" <Rule complex>
    #
    Then edit "Social Security Number" as "6789"
    And edit "Time to deliver" as "5 to 7 business days"
    And edit "Purchase Options Rule" as "full retail value"
    Then submit the edited summaries
    #
    Then compare if "Social Security Number" has "6789" for callId
    And compare if "Time to deliver" has "5 to 7 business days" for callId
    #
    #############################################
    #
    ##VERIFYING DISPOSITION AGAINST GOLD STANDARD
    #
    And disposition for callId has intent of <intent>
    Then edit and submit disposition intent "24 Months" as "36 Months"
    And we sync <orgAgentName>
    Then compare if disposition has changed intent from "24 Months" to "36 Months"
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
      | organization | category  | orgAgentName | agentEmail                | catalogue file         | role    | language | audio-file                                 | turn | phrase       | intent                                | transcript-file                                                 | description   | colorVR       | phoneupgrade             | Social Security Number | Purchase Option 1   | Purchase Option 2  | Time to deliver         | Customer Wireless Number | Phone Opted              | Purchase Options Rule                 | Rule complex |
      | "APITesting" | "verizon" | "APITesting" | "APITesting@uniphore.com" | "entityCatalogue.json" | "Agent" | "E"      | "audio-files/verizonAudioFile/verizon.wav" |    0 | "my name is" | "Phone Upgrade/Payment/EMI/24 Months" | "src/test/resources/transcript-jsons/verizonTranscriptASR.json" | "description" | "colorSample" | "wireless phone upgrade" | "1234"                 | "full retail value" | "monthly payments" | "7 to 10 business days" | "4085551212"             | "256 gigabyte iphone 12" | "full retail value, monthly payments" | "1237619628" |

	@agents
  Scenario Outline: Add agents and map to a Supervisor
    #Given we create an organization called <organization> with description as <description>
    #And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    #Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    #And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #And we sync <orgAgentName>
    #And map agent <orgAgentName> to supervisor "Super"
    
    Then configure complex entities "ConfigAndDefine/UDS/complexEntity"
    Then validate and configure rules in folder "ConfigAndDefine/UDS/complexEntity/complexRule"

    Examples: 
      | organization | orgAgentName | agentEmail             | description   | colorVR       | category  |
      | "APITesting" | "Agent03"    | "Agent03@uniphore.com" | "description" | "colorSample" | "verizon" |
      