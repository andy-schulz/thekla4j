package com.teststeps.thekla4j.examples.allure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.AllureActor;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.qameta.allure.Allure;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Demonstrates how {@code @BeforeAll}, {@code @AfterAll}, {@code @BeforeEach}, and
 * {@code @AfterEach} lifecycle methods appear in the Allure report.
 *
 * <p>Includes both passing and failing scenarios to show how Allure visualises fixture failures
 * alongside test body failures.
 *
 * <p>Also verifies that when no {@code @AllureActor} is present the extension does not
 * interfere with standard Allure step tracking ({@link Allure#step(String, io.qameta.allure.util.ThrowableRunnable)}).
 */
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("JUnit Fixtures")
@Epic("AllureExamples")
@Feature("Setup and Teardown")
@Story("IntendedPass")
class SetupTeardownExampleTest {

  /** Actor used by tests that demonstrate actor-based activity logging. */
  @AllureActor
  Actor alice;

  private static int setupCounter = 0;

  // ── lifecycle ───────────────────────────────────────────────────

  @BeforeAll
  static void globalSetup() {
    setupCounter = 0;
  }

  @AfterAll
  static void globalTeardown() {
    // nothing to clean up — demonstrates the TEARDOWN fixture in Allure
  }

  @BeforeEach
  void setUp(TestInfo testInfo) {
    setupCounter++;
    alice = Actor.named("Alice");

    // Intentionally fail the @BeforeEach for one specific test to demonstrate
    // how Allure renders a fixture failure and marks the test as skipped/broken.
    if (testInfo.getDisplayName().startsWith("Failing setup")) {
      throw new IllegalStateException(
                                      "Setup failed intentionally to demonstrate Allure fixture-failure rendering");
    }
  }

  @AfterEach
  void tearDown() {
    // actor is discarded after each test — nothing to clean up explicitly
  }

  // ── activities ──────────────────────────────────────────────────

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

  // ── tests ───────────────────────────────────────────────────────

  @Test
  @DisplayName("Passing test with actor – activity log visible as Allure steps")
  void passingTestWithActor() {
    Either<ActivityError, String> result = alice.attemptsTo(
      new SearchForProduct("Laptop"));

    assertTrue(result.isRight(), "search should succeed");
  }

  @Test
  @DisplayName("Passing test without actor – standard Allure step view")
  void passingTestWithoutActor() {
    // Demonstrates that standard Allure.step() tracking works normally alongside
    // the extension even when no @AllureActor field is present.
    Allure.step("open the application homepage", () -> {
      // simulated UI action
    });

    Allure.step("verify the page title is correct", () -> assertEquals("Home", "Home", "page title should match"));
  }

  @Test
  @DisplayName("Failing test – assertion error in the test body")
  @Story("IntendedFail")
  void failingTestBody() {
    // This assertion is expected to fail to demonstrate how Allure shows
    // assertion errors with expected/actual values in the test body section.
    assertEquals("expected value", "actual value",
      "Intentional failure: expected and actual values do not match");
  }

  @Test
  @DisplayName("Failing setup – @BeforeEach throws before the test runs")
  @Story("IntendedFail")
  void failingSetupTest() {
    // @BeforeEach detects this test by its display name prefix and throws.
    // This line is never reached; Allure marks the test as broken/skipped
    // and shows the fixture failure in the SETUP section.
    assertTrue(true, "test body should not be reached when setup fails");
  }
}
