Feature: Test Offline Audio file - IN

  @IN @smoke @Regression @IN
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
    #	##SETTING UP CALL VINIABLES
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
    ##INFINITION AND CONFIGURATION` FOR ENTITY AND INTENT
    #
    Then delete all entities
    Then delete all dispositions
    Then we add language to org <organization> and category <category>
    #ENTITY
    #
    Then define and configure entities in folder "ConfigAndDefine/IN/entities/"
    Then validate and configure rules in folder "ConfigAndDefine/IN/ruleEntity/"
    #
    #SUMMINY FORMAT
    #
    Then post summary format
    #
    #INTENT
    #
    Then define and configure call categorization with folder "ConfigAndDefine/IN/call-categorization"
    #
    #
    Then configure alerts in folder "ConfigAndDefine/IN/alerts"
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
      | "APITesting" | "IN"     | "APITesting" | "APITesting@uniphore.com" | "Agent" | "E"      | "audio-files/INAudioFile/IN.wav" | "description" | "colorSample" |

  #############################################
  @IN @smoke @Regression
  Scenario Outline: Validate Audio offline processing and compare generated Transcripts with Golden Std.
    Then the request with file <audio-file> is sent to the audio-connector
    And wait for <audio-file> to get loaded
    #
    #	##############################################
    #
    #	###VERIFYING TRANSCRIPT AGAINST GOLD STANDIND
    #
    Then a transcript is generated for callId
    And the transcript conversation for callId for <turn> has <phrase>
    And the transcript conversation for callId matches the correct version <transcript-file>

    Examples: 
      | audio-file                       | turn | phrase                              | transcript-file                               |
      | "audio-files/INAudioFile/IN.wav" |    0 | "welcome to add how can i help you" | "src/test/resources/transcript-jsons/IN.json" |

  @IN @smoke @Regression
  Scenario Outline: Validate Summary.
    And a summary for callId exists
    And a summary for callId has intent of <intent>
    And a summary for callId has "Expiry Date of card" <Expiry Date of card>
    And a summary for callId has "Account Number" <Account Number>
    And a summary for callId has "Payment Status" <Payment Status>

    Examples: 
      | intent                                     | Expiry Date of card | Account Number     | Payment Status                            |
      | "Complaint/Online Payment/Payment Success" | "x/x/xxxx"          | "7612432973072932" | " payment for this account was successful" |

  @IN @smoke @Regression
  Scenario Outline: Validate Feedback loop for Summary and Disposition.
    Then edit "Account Number" as "7612432973072934"
    Then submit the edited summaries
    Then compare if "Account Number" has "7612432973072934" for callId
    And disposition for callId has intent of <intent>
    Then edit and submit disposition intent "Online Payment" as "Offline Payment"
    And we sync <orgAgentName>
    Then compare if disposition has changed intent from "Online Payment" to "Offline Payment"
    

    Examples: 
      | intent                                 |  orgAgentName |
      | "Complaint/Online Payment/Payment Success" | "APITesting" |

  @IN @smoke @Regression
  Scenario Outline: Validate Alerts for Supervisor.
    Then verify that supervisor has alert "Call Duration" with type "Information Alert"
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |

  @IN @smoke @Regression
  Scenario Outline: Validate Transcripts for Supervisor.
    Then verify transcript turns for supervisor
    Then verify that supervisor has alert "Coaching alert" with type "Coaching Alert"

    Examples: 
      | organization |
      | "APITesting" |
