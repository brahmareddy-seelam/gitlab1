Feature: To set up system before any validation

  @envUS @envUK @envAR @envIN @envHE @envJA @envDE @envAU @Common
  Scenario Outline: Setup entity catalog, app-profile and asr-engine
    Then update app-profile
    Then we update asr-engine from folder <asr-engine>
    Then create entity catalog from <catalogue file>
    Then refresh all caches

    Examples: 
      | catalogue file         | asr-engine        |
      | "entityCatalogue.json" | "asr-engine.json" |

  @update
  Scenario: Update the environment with Organization and Category
    Then we update Organization and Category to new env

  @backup
  Scenario: Backup of environment Organization and Category
    Then we create a backup of Organization and category

  @newBackup
  Scenario Outline: Backup of environment Organization and Category
    Given we create a backup of Organization and category
    Then we create a backup of entities for the <organization> and <category> from 2.2 for 2.4
    Then create backup of call Category and alerts for the <organization> and <category>

    Examples: 
      | organization            | category     |
      | "B And E Group Pvt Ltd" | "AE Banking" |

  @newUpdate
  Scenario Outline: Update of environment Organization and Category
    #Then update the intent in new env
    Then update the entites in new env

    Examples: 
      | organization            | category     |
      | "B And E Group Pvt Ltd" | "AE Banking" |

  @complex
  Scenario Outline: Update of environment Organization and Category
    Given the request organization is <organization>
    And the request category is <category>
    Then define and configure entities in folder "ConfigAndDefine/call18/entities/"

    #Then validate and configure rules in folder "ConfigAndDefine/call18/complexEntity/complexRule"
    Examples: 
      | organization | category |
      | "APITesting" | "UDS"    |

  @Time
  Scenario Outline: Check time
    Then I get summary report for <audio-file>

    Examples: 
      | audio-file                       |
      | "audio-files/USAudioFile/US.wav" |

  @DB @Regression
  Scenario Outline: Connect DB
    #Then I connect to DB and check if collection "aggregationMetrics" has ""
    #Then I connect to DB and check if collection "interactions" has "isIgnored" as "true"
    #Then I connect to DB and check if collection "aggregationMetrics" has "isIgnored" as "true"
    Given I update the gost-call-controller
    And a <audio-file> file exists
    And the request organization is <organization>
    And the request category is <category>
    And the request customerId is "8090909099"
    And the request language is <language>
    And the request agentId is <orgAgentName>
    Then generate the callId
    Then the request with file <audio-file> is sent to the audio-connector
    And wait for <audio-file> to get loaded
    Then I connect to DB and check if collection "interactions" has "isIgnored" as "true"

    Examples: 
      | organization | category | orgAgentName | language | audio-file                       |
      | "APITesting" | "US"     | "APITesting" | "E"      | "audio-files/GhostCall/GhostCall.wav" |
