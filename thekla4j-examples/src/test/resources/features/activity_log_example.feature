@EPIC=AllureExamples @FEATURE=ActivityLogSteps @SUITE=AllureLabeling @SUB_SUITE=CucumberActivityLog @PARENT_SUITE=thekla4jExamples
Feature: Activity Log in Cucumber

  Demonstrates how the thekla4j activity log appears in the Allure report
  when using the Cucumber plugin with Thekla4jWorld.

  @STORY=IntendedPass @SEVERITY=normal @REQS=REQ-CUC-001
  Scenario: Single actor performs a search workflow
    Given Alice is on stage
    When Alice searches for "Laptop"
    Then the search result should contain "Laptop"

  @STORY=IntendedPass @SEVERITY=normal @REQS=REQ-CUC-002,REQ-CUC-003
  Scenario: Multiple actors interact with the system
    Given Alice is on stage
    And Bob is on stage
    When Alice searches for "Monitor"
    And Bob searches for "Keyboard"
    Then the search result should contain "Monitor"

  @STORY=IntendedPass @SEVERITY=critical @REQS=REQ-CUC-004 @TEST_ID=TC-CUC-001
  Scenario: Chained tasks with input flowing between steps
    Given Alice is on stage
    When Alice looks up the price for "Headphones" with a 25% discount
    Then the discounted price is calculated

  @STORY=IntendedFail @SEVERITY=minor @REQS=REQ-CUC-005
  Scenario: Failing activity shows error in activity log
    Given Alice is on stage
    When Alice searches for "Monitor"
    And Alice validates the search result banner which should fail
    Then the search result should contain "Monitor"
