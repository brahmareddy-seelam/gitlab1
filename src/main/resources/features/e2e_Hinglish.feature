Feature: Test Offline Audio file - DE

  @DE @smoke @Regression
  Scenario Outline: Validate Entity,Disposition and Alert Creation and Training.
    ###CREATION OF ORGANIZATION, CATEGORY, AND AGENT
    #Given a "backbone setup request" organization creation exists
    Given we create an organization called <organization> with description as <description>
    And we create a business process called <category> with colorVR as <colorVR> and description as <description> for <organization>
    Given get keycloak accessToken with username "admin" and password "Welcome@123" and client id "admin-cli" and grant-type "password"
    And we can add keycloak <orgAgentName> with email <agentEmail> as an agent to <organization>
    And map agent <orgAgentName> to supervisor "default-supervisor"
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
    #
    ##DEFINITION AND CONFIGURATION` FOR ENTITY AND INTENT
    #
    Then delete all entities
    Then delete all dispositions
    Then we add language to org <organization> and category <category>
    #ENTITY
    #
    Then define and configure entities in folder "ConfigAndDefine/DE/entities/"
    #Then validate and configure rules in folder "ConfigAndDefine/DE/ruleEntity"
    #
    #SUMMARY FORMAT
    #
    Then post summary format
    #
    #INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/DE/call-categorization"
    #
    #
    Then configure alerts in folder "ConfigAndDefine/DE/alerts"
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
    #Then configure complex entities "ConfigAndDefine/JA/complexEntity"
    #Then validate and configure rules in folder "ConfigAndDefine/JA/complexEntity/complexRule"
    #Then train complex rules
    #Then post summary format
    Examples: 
      | organization | category | orgAgentName | agentEmail                | role    | language | audio-file                       | description   | colorVR       |
      | "APITesting" | "DE1"     | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/DEAudioFile/DE.wav" | "description" | "colorSample" |

  #############################################
  @DE @smoke @Regression
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
      | audio-file                       | turn | phrase                         | transcript-file                               |
      | "audio-files/DEAudioFile/DE.wav" |    0 | "hallo schönen guten tag hier" | "src/test/resources/transcript-jsons/DE.json" |

  @DE @smoke @Regression
  Scenario Outline: Validate Summary.
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Email" <Email>
    And a summary for callId has "Date of birth" <Date of birth>
    And a summary for callId has "Customer name" <Customer name>
    And a summary for callId has "Telephone number" <Telephone number>
    And a summary for callId has "Address of customer" <Address of customer>

    Examples: 
      | intent                                                            | Email                                   | Date of birth | Customer name  | Address of customer | Telephone number |
      | "Mobiltelefon und Wlan/Problem mit der Internetverbindung/Lösung" | "sophie punkt braun at g m x punkt net" | "04/13/1993"  | "sophie braun" | "hauptstrasse"      | "01796752431"    |

  @DE @smoke @Regression
  Scenario Outline: Validate Feedback loop for Summary and Disposition.
    Then edit "Email" as "sophie braun at g m x punkt net"
    Then submit the edited summaries
    Then compare if "Email" has "sophie braun at g m x punkt net" for callId
    And disposition for callId has intent of <intent>

    Examples: 
      | intent                                                            |
      | "Mobiltelefon und Wlan/Problem mit der Internetverbindung/Lösung" |

  @DE @smoke @Regression
  Scenario Outline: Validate Alerts for Supervisor.
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |

  @DE @smoke @Regression
  Scenario Outline: Validate Transcripts for Supervisor.
    Then verify transcript turns for supervisor
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |
