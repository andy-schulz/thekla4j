Feature: Tagged Outline Feature

  @TEST_ID=OUTLINE-1
  Scenario Outline: A parameterized scenario with <value>
    Given a step with parameter <value>

    Examples:
      | value |
      | foo   |
      | bar   |
