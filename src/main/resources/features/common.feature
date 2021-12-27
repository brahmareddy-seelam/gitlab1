Feature: To set up system before any validation

  @env @Common
  Scenario Outline: Setup entity catalog, app-profile and asr-engine
    Then update app-profile from <app-profile>
    Then we update asr-engine from folder <asr-engine>
    Then create entity catalog from <catalogue file>
   	Then refresh all caches

    Examples: 
      | catalogue file         | app-profile        | asr-engine        |
      | "entityCatalogue.json" | "app-profile.json" | "asr-engine.json" |
