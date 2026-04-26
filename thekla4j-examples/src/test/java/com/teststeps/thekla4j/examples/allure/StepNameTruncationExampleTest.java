package com.teststeps.thekla4j.examples.allure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.AllureActor;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Issues;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Reqs;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.TestId;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Demonstrates how step names are built from activity name + description,
 * and how long descriptions are truncated with the full text available as a parameter.
 *
 * <p>Also showcases all annotation features: {@code @Epic}, {@code @Feature}, {@code @Story},
 * {@code @Suite}, {@code @TestId}, {@code @Issues}, and {@code @Reqs}.
 */
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("JUnit Step Name Truncation")
@Epic("AllureExamples")
@Feature("Step Name Truncation")
@Story("IntendedPass")
class StepNameTruncationExampleTest {

  @AllureActor
  Actor tester = Actor.named("Tester");

  @AllureActor
  Actor admin = Actor.named("Admin");

  // ── Activities with short descriptions ─────────────────────────

  @Action("open page '@{url}'")
  static class OpenPage extends BasicInteraction {
    @Called(name = "url")
    private final String url;

    OpenPage(String url) {
      this.url = url;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  @Action("click element '@{selector}'")
  static class ClickElement extends BasicInteraction {
    @Called(name = "selector")
    private final String selector;

    ClickElement(String selector) {
      this.selector = selector;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  @Action("enter text '@{text}' into field '@{field}'")
  static class EnterText extends BasicInteraction {
    @Called(name = "text")
    private final String text;
    @Called(name = "field")
    private final String field;

    EnterText(String field, String text) {
      this.field = field;
      this.text = text;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  // ── Activities with long descriptions (>100 chars when combined) ─

  @Workflow("verify that the user profile page displays the correct personal information including " +
      "first name, last name, email address, phone number, and date of birth for the currently logged in user")
  static class VerifyUserProfileDetails extends SupplierTask<String> {
    @Override
    protected Either<ActivityError, String> performAs(Actor actor) {
      return actor.attemptsTo(
        new OpenPage("/profile"),
        new ClickElement("#personal-info-tab")).map(unused -> "profile verified");
    }
  }

  @Action("validate that the order confirmation email was sent to the customer's registered email address " +
      "and contains the correct order number, item list, shipping address, and estimated delivery date")
  static class ValidateOrderConfirmationEmail extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }
  }

  // ── Task with input that produces long description ─────────────

  @Action("search the product catalog for items matching '@{query}' filtered by category, price range, availability, and customer rating")
  static class SearchProductCatalog extends Task<String, String> {
    @Called(name = "query")
    private String query;

    @Override
    protected Either<ActivityError, String> performAs(Actor actor, String query) {
      this.query = query;
      return Either.right("found 42 results for: " + query);
    }

    @Override
    public String toString() {
      return "SearchProductCatalog";
    }
  }

  @Action("apply filters: category='@{category}', minRating=@{rating}")
  static class ApplyFilters extends Task<String, String> {
    @Called(name = "category")
    private final String category;
    @Called(name = "rating")
    private final int rating;

    ApplyFilters(String category, int rating) {
      this.category = category;
      this.rating = rating;
    }

    @Override
    protected Either<ActivityError, String> performAs(Actor actor, String searchResults) {
      return Either.right("filtered: " + searchResults + " [" + category + ", rating>=" + rating + "]");
    }

    @Override
    public String toString() {
      return "ApplyFilters";
    }
  }

  // ── Tests ──────────────────────────────────────────────────────

  @Test
  @DisplayName("Short descriptions – name and description fit within 100 chars")
  @TestId("TC-TRUNC-001")
  @Reqs({"REQ-STEP-010"})
  void shortDescriptions() {
    Either<ActivityError, Void> result = tester.attemptsTo(
      new OpenPage("/dashboard"),
      new ClickElement("#menu-orders"),
      new EnterText("#search", "laptop"));

    assertTrue(result.isRight());
  }

  @Test
  @DisplayName("Long descriptions – truncated with '...' and full text in parameters")
  @TestId("TC-TRUNC-002")
  @Reqs({"REQ-STEP-011"})
  void longDescriptions() {
    Either<ActivityError, String> result = tester.attemptsTo(
      new VerifyUserProfileDetails());

    assertTrue(result.isRight());

    tester.attemptsTo(new ValidateOrderConfirmationEmail());
  }

  @Test
  @DisplayName("Chained tasks with long descriptions – input/output alongside truncated names")
  @TestId("TC-TRUNC-003")
  @Reqs({"REQ-STEP-012"})
  @Issues({"ALLURE-42"})
  void chainedTasksWithLongDescriptions() {
    Either<ActivityError, String> result = tester.attemptsTo_(
      new SearchProductCatalog(),
      new ApplyFilters("Electronics", 4)).using("wireless headphones");

    assertTrue(result.isRight());
  }

  @Test
  @DisplayName("Multiple actors – admin and tester performing parallel workflows")
  @TestId("TC-TRUNC-004")
  @Reqs({"REQ-STEP-013", "REQ-STEP-014"})
  void multipleActorsWithDescriptions() {
    // Tester performs a search workflow
    Either<ActivityError, String> searchResult = tester.attemptsTo_(
      new SearchProductCatalog(),
      new ApplyFilters("Books", 3)).using("design patterns");

    assertTrue(searchResult.isRight());

    // Admin verifies the profile
    Either<ActivityError, String> profileResult = admin.attemptsTo(
      new VerifyUserProfileDetails());

    assertTrue(profileResult.isRight());

    // Admin also validates email
    admin.attemptsTo(new ValidateOrderConfirmationEmail());
  }
}
