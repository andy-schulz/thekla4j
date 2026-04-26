package com.teststeps.thekla4j.examples.cucumber.stepdefs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.cucumber.Thekla4jWorld;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vavr.control.Either;

public class ActivityLogStepDefinitions {

  private final Thekla4jWorld world;
  private Either<ActivityError, String> lastResult;

  public ActivityLogStepDefinitions(Thekla4jWorld world) {
    this.world = world;
  }

  // ── Activities ────────────────────────────────────────────────

  @Action("enter search term '@{term}'")
  static class EnterSearchTerm extends BasicInteraction {
    @Called(name = "term")
    private final String term;

    EnterSearchTerm(String term) {
      this.term = term;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  @Action("click search button")
  static class ClickSearchButton extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  @Workflow("search for product '@{product}'")
  static class SearchForProduct extends SupplierTask<String> {
    @Called(name = "product")
    private final String product;

    SearchForProduct(String product) {
      this.product = product;
    }

    @Override
    protected Either<ActivityError, String> performAs(Actor actor) {
      return actor.attemptsTo(
        new EnterSearchTerm(product),
        new ClickSearchButton()).map(unused -> "found: " + product);
    }
  }

  @Action("validate search result banner")
  static class ValidateSearchResultBanner extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.left(
        ActivityError.of("Element '#result-banner' not found on page after 5s timeout"));
    }
  }

  @Action("fetch product price for '@{productName}'")
  static class FetchProductPrice extends Task<String, Double> {
    @Called(name = "productName")
    private String productName;

    @Override
    protected Either<ActivityError, Double> performAs(Actor actor, String productName) {
      this.productName = productName;
      return Either.right(productName.length() * 9.99);
    }

    @Override
    public String toString() {
      return "FetchProductPrice";
    }
  }

  @Action("apply discount of @{percent}% to price")
  static class ApplyDiscount extends Task<Double, String> {
    @Called(name = "percent")
    private final int percent;

    ApplyDiscount(int percent) {
      this.percent = percent;
    }

    @Override
    protected Either<ActivityError, String> performAs(Actor actor, Double price) {
      double discounted = price * (1.0 - percent / 100.0);
      return Either.right(String.format("$%.2f", Math.round(discounted * 100.0) / 100.0));
    }

    @Override
    public String toString() {
      return "ApplyDiscount(" + percent + "%)";
    }
  }

  // ── Step definitions ──────────────────────────────────────────

  @Given("{word} is on stage")
  public void actorIsOnStage(String actorName) {
    world.callActorToStageNamed(actorName);
  }

  @When("{word} searches for {string}")
  public void actorSearchesFor(String actorName, String product) {
    Actor actor = world.callActorToStageNamed(actorName);
    lastResult = actor.attemptsTo(new SearchForProduct(product));
  }

  @When("{word} validates the search result banner which should fail")
  public void actorValidatesSearchResultWhichFails(String actorName) throws ActivityError {
    Actor actor = world.callActorToStageNamed(actorName);
    Either<ActivityError, Void> result = actor.attemptsTo(new ValidateSearchResultBanner());
    if (result.isLeft()) {
      throw result.getLeft();
    }
  }

  @When("{word} looks up the price for {string} with a {int}% discount")
  public void actorLooksUpPrice(String actorName, String product, int discount) {
    Actor actor = world.callActorToStageNamed(actorName);
    lastResult = actor.attemptsTo_(
      new FetchProductPrice(),
      new ApplyDiscount(discount)).using(product);
  }

  @Then("the search result should contain {string}")
  public void searchResultContains(String expected) {
    assertTrue(lastResult != null && lastResult.isRight(),
      "search should have succeeded");
  }

  @Then("the discounted price is calculated")
  public void discountedPriceIsCalculated() {
    assertTrue(lastResult != null && lastResult.isRight(),
      "price calculation should have succeeded");
  }
}
