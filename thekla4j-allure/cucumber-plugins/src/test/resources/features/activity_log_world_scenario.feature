Feature: Activity log from world-backed step definitions

  Scenario: Activity log survives early world clearing before case finished
    Given actor "Alice" is on stage
    When actor "Alice" performs a tracked activity
    Then the world-backed scenario passes
