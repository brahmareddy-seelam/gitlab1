Feature: Test Offline Audio file - US

  @US @smoke @Regression
  Scenario Outline: Validate Entity,Disposition and Alert Creation and Training.
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    #Given a "backbone setup request" organization creation exists
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    #And map agent <orgAgentName> to supervisor "default-supervisor"
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
    Then we add language to org <organization> and category <category>
    #ENTITY
    #
    Then define and configure entities in folder "ConfigAndDefine/US/entities/"
    #
    Then validate and configure rules in folder "ConfigAndDefine/US/ruleEntity"
    #
    #SUMMARY FORMAT
    #
    Then post summary format
    #
    #INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/US/call-categorization"
    #
    #
    Then configure alerts in folder "ConfigAndDefine/US/alerts"
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
    Then configure complex entities "ConfigAndDefine/US/complexEntity"
    Then validate and configure rules in folder "ConfigAndDefine/US/complexEntity/complexRule"
    Then train complex rules
    Then post summary format

    ##############################################
    #
    ###DELETE ORGANIZATION AND AGENT
    #Then we delete category
    #Then we delete <orgAgentName> who is an <role> from <organization>
    #And we delete an organization called <organization>
    ##############################################
    Examples: 
      | organization | category | orgAgentName | agentEmail                | catalogue file         | role    | language | audio-file                       | description   | colorVR       |
      | "APITesting" | "US"     | "APITesting" | "APITesting@uniphore.com" | "entityCatalogue.json" | "Agent" | "E"      | "audio-files/USAudioFile/US.wav" | "description" | "colorSample" |

  @US @smoke @Regression
  Scenario Outline: Validate Audio offline processing and compare generated Transcripts with Golden Std.
    Then the request with file <audio-file> is sent to the audio-connector
    And wait for <audio-file> to get loaded
    #
    #	##############################################
    #
    #	###VERIFYING TRANSCRIPT AGAINST GOLD STANDARD
    #
    Then a transcript is generated for callId
    And the transcript conversation for callId for <turn> has <phrase>
    And the transcript conversation for callId matches the correct version <transcript-file>

    Examples: 
      | audio-file                       | turn | phrase       | intent                                | transcript-file                                            |
      | "audio-files/USAudioFile/US.wav" |    0 | "my name is" | "Phone Upgrade/Payment/EMI/24 Months" | "src/test/resources/transcript-jsons/USTranscriptASR.json" |

  @US @smoke @Regression
  Scenario Outline: Validate Summary.
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Phone Upgrade" <phoneupgrade>
    And a summary for callId has "Social Security Number" <Social Security Number>
    And a summary for callId has "Purchase Option 1" <Purchase Option 1>
    And a summary for callId has "Time to deliver" <Time to deliver>
    And a summary for callId has "Purchase Option 2" <Purchase Option 2>
    And a summary for callId has "Customer Wireless Number" <Customer Wireless Number>
    And a summary for callId has "Purchase Options Rule" <Purchase Options Rule>
    And a summary for callId has "Phone Opted" <Phone Opted>
    #And a summary for callId has "Claim Amount Rule" <Claim Amount>
    And a summary for callId has "Rule complex" <Rule complex>

    Examples: 
      | intent                                | transcript-file                                            | description   | colorVR       | phoneupgrade             | Social Security Number | Purchase Option 1   | Purchase Option 2  | Time to deliver         | Customer Wireless Number | Phone Opted              | Purchase Options Rule                 | Rule complex |
      | "Phone Upgrade/Payment/EMI/24 Months" | "src/test/resources/transcript-jsons/USTranscriptASR.json" | "description" | "colorSample" | "wireless phone upgrade" | "1234"                 | "full retail value" | "monthly payments" | "7 to 10 business days" | "4085551212"             | "256 gigabyte iphone 12" | "full retail value, monthly payments" | "1237619628" |

  @US @smoke @Regression
  Scenario Outline: Validate Feedback loop for Summary and Disposition.
    Then edit "Social Security Number" as "6789"
    And edit "Time to deliver" as "5 to 7 business days"
    And edit "Purchase Options Rule" as "full retail value"
    Then submit the edited summaries
    Then compare if "Social Security Number" has "6789" for callId
    And compare if "Time to deliver" has "5 to 7 business days" for callId
    And disposition for callId has intent of <intent>
    Then edit and submit disposition intent "24 Months" as "36 Months"
    And we sync <orgAgentName>
    Then compare if disposition has changed intent from "24 Months" to "36 Months"

    Examples: 
      | orgAgentName | intent                                |
      | "APITesting" | "Phone Upgrade/Payment/EMI/24 Months" |

  @US @smoke @Regression
  Scenario Outline: Validate Alerts for Supervisor.
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |

  @US @smoke @Regression
  Scenario Outline: Validate Transcripts for Supervisor.
    Then verify transcript turns for supervisor
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |
