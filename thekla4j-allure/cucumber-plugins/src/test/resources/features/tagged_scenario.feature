Feature: Tagged Feature

  @EPIC=TestEpic @STORY=TestStory @SUITE=TestSuite @SEVERITY=critical @TEST_ID=TC-001 @REQS=REQ-001,REQ-002
  Scenario: A tagged scenario with test id
    Given a passing step

  Scenario: A scenario without test id
    Given a passing step
