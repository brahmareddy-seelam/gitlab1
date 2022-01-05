Feature: To set up system before any validation

  @env @Common
  Scenario Outline: Setup entity catalog, app-profile and asr-engine
    Then update app-profile
    Then we update asr-engine from folder <asr-engine>
    Then create entity catalog from <catalogue file>
   	Then refresh all caches

    Examples: 
      | catalogue file         |  asr-engine        |
      | "entityCatalogue.json" |  "asr-engine.json" |

      
   @update
   Scenario: Update the environment with Organization and Category
   	Then we update Organization and Category to new env
   	
   
   @backup
   Scenario: Backup of environment Organization and Category
   	Then we create a backup of Organization and category
   	