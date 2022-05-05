Feature: Upload resource

  Scenario: Upload audio file
    Given mp3 file
    When user uploads mp3 file
    And id is returned
    And user can fetch metadata by resource id