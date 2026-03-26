package com.teststeps.thekla4j.allure.cucumber.test.stepdefs;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CommonStepDefinitions {

  @Given("a passing step")
  public void aPassingStep() {
    // intentionally passes
  }

  @When("another passing step")
  public void anotherPassingStep() {
    // intentionally passes
  }

  @Then("the scenario passes")
  public void theScenarioPasses() {
    // intentionally passes
  }

  @Given("a step that throws an assertion error")
  public void aStepThrowsAssertionError() {
    throw new AssertionError("Expected condition not met");
  }

  @Given("a step that throws an activity error")
  public void aStepThrowsActivityError() throws ActivityError {
    throw ActivityError.of("Activity failed during test step");
  }

  @Given("a step with parameter {word}")
  public void aStepWithParameter(final String value) {
    // intentionally passes for any parameter value
  }
}
