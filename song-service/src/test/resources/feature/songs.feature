Feature: Songs API
  As API user I want to be able to perform CRD action on songs

  Scenario: Create new song
    Given a song to create
    When user posts song
    Then song is saved
    And song id returned

  Scenario: Get song
    Given saved song
    When user gets song by id
    Then song is returned

  Scenario: Delete songs
    Given saved songs
    When user deletes songs
    Then deleted songs ids are returned