Feature: Delete all configurations

  @deleteAll 
  Scenario Outline: Delete
    And the request organization is <organization>
    And the request category is <category>
    Then delete all entities
    Then delete all alerts

    Examples: 
      | organization | category  |  
      | "APITesting" | "call18"  | 
      | "APITesting" | "call19"  |
      | "APITesting" | "verizon" | 
      
