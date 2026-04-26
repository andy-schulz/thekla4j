package com.teststeps.thekla4j.allure.cucumber.test.activitylog;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vavr.control.Either;

public class ActivityLogWorldStepDefinitions {

  private final ActivityLogWorld world;

  public ActivityLogWorldStepDefinitions(final ActivityLogWorld world) {
    this.world = world;
  }

  @Action("record successful activity")
  static class RecordSuccessfulActivity extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(final Actor actor) {
      return Either.right(null);
    }
  }

  @Given("actor {string} is on stage")
  public void actorIsOnStage(final String actorName) {
    world.callActorToStageNamed(actorName);
  }

  @When("actor {string} performs a tracked activity")
  public void actorPerformsTrackedActivity(final String actorName) throws ActivityError {
    final Actor actor = world.callActorToStageNamed(actorName);
    final Either<ActivityError, Void> result = actor.attemptsTo(new RecordSuccessfulActivity());
    if (result.isLeft()) {
      throw result.getLeft();
    }
  }

  @Then("the world-backed scenario passes")
  public void theWorldBackedScenarioPasses() {
    // intentionally passes
  }
}
