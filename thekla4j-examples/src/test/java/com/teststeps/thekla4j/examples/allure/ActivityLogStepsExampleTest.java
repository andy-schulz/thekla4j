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
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Reqs;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
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
 * Demonstrates the {@code @AllureActor} annotation which maps the Actor's
 * activity log tree into native Allure steps.
 *
 * <p>Run the Allure report to see nested steps like:
 * <pre>
 * Alice
 * └── SearchForProduct ── input: "Laptop"
 * └── EnterSearchTerm ── input: "Laptop"
 * └── ClickSearchButton
 * </pre>
 */
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("JUnit Activity Log")
@Epic("AllureExamples")
@Feature("Activity Log Steps")
@Story("IntendedPass")
class ActivityLogStepsExampleTest {

  @AllureActor
  Actor alice = Actor.named("Alice");

  // ── simple activities used by the test ──────────────────────────

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

  @Action("validate search result for '@{expected}'")
  static class ValidateSearchResult extends BasicInteraction {
    @Called(name = "expected")
    private final String expected;

    ValidateSearchResult(String expected) {
      this.expected = expected;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.left(ActivityError.of("Element '#result-banner' not found on page after 5s timeout"));
    }
  }

  @Workflow("search and validate product '@{product}'")
  static class SearchAndValidateProduct extends SupplierTask<String> {
    @Called(name = "product")
    private final String product;

    SearchAndValidateProduct(String product) {
      this.product = product;
    }

    @Override
    protected Either<ActivityError, String> performAs(Actor actor) {
      return actor.attemptsTo(
        new EnterSearchTerm(product),
        new ClickSearchButton(),
        new ValidateSearchResult(product)).map(unused -> "validated: " + product);
    }
  }

  // ── tasks with JSON input/output ────────────────────────────────

  @Action("submit order request")
  static class SubmitOrderRequest extends Task<String, String> {
    @Override
    protected Either<ActivityError, String> performAs(Actor actor, String orderJson) {
      // simulate processing the order and returning a response
      String response = "{\n" + "  \"orderId\": \"ORD-2026-78432\",\n" + "  \"status\": \"CONFIRMED\",\n" + "  \"customer\": {\n" +
          "    \"id\": \"CUST-1001\",\n" + "    \"name\": \"Alice Wonderland\",\n" + "    \"email\": \"alice@example.com\",\n" +
          "    \"tier\": \"GOLD\"\n" + "  },\n" + "  \"items\": [\n" + "    {\n" + "      \"sku\": \"SKU-LAPTOP-001\",\n" +
          "      \"name\": \"ThinkPad X1 Carbon\",\n" + "      \"quantity\": 1,\n" + "      \"unitPrice\": 1299.99,\n" +
          "      \"discount\": 129.99\n" + "    },\n" + "    {\n" + "      \"sku\": \"SKU-MOUSE-042\",\n" +
          "      \"name\": \"Wireless Ergonomic Mouse\",\n" + "      \"quantity\": 2,\n" + "      \"unitPrice\": 49.99,\n" +
          "      \"discount\": 0.00\n" + "    }\n" + "  ],\n" + "  \"shipping\": {\n" + "    \"method\": \"EXPRESS\",\n" +
          "    \"address\": \"42 Rabbit Hole Lane, Wonderland, WL 12345\",\n" + "    \"estimatedDelivery\": \"2026-04-20\"\n" + "  },\n" +
          "  \"payment\": {\n" + "    \"method\": \"CREDIT_CARD\",\n" + "    \"last4\": \"4242\",\n" + "    \"total\": 1269.98,\n" +
          "    \"currency\": \"USD\"\n" + "  }\n" + "}";
      return Either.right(response);
    }

    @Override
    public String toString() {
      return "SubmitOrderRequest";
    }
  }

  @Action("validate order confirmation")
  static class ValidateOrderConfirmation extends Task<String, String> {
    @Override
    protected Either<ActivityError, String> performAs(Actor actor, String responseJson) {
      if (responseJson.contains("\"status\": \"CONFIRMED\"")) {
        return Either.right(responseJson);
      }
      return Either.left(ActivityError.of("Order was not confirmed"));
    }

    @Override
    public String toString() {
      return "ValidateOrderConfirmation";
    }
  }

  // ── tasks with input → output chaining ──────────────────────────

  @Action("fetch product price for '@{productName}'")
  static class FetchProductPrice extends Task<String, Double> {
    @Called(name = "productName")
    private String productName;

    @Override
    protected Either<ActivityError, Double> performAs(Actor actor, String productName) {
      this.productName = productName;
      // simulate a price lookup
      double price = productName.length() * 9.99;
      return Either.right(price);
    }

    @Override
    public String toString() {
      return "FetchProductPrice";
    }
  }

  @Action("apply discount of @{percent}% to price")
  static class ApplyDiscount extends Task<Double, Double> {
    @Called(name = "percent")
    private final int percent;

    ApplyDiscount(int percent) {
      this.percent = percent;
    }

    @Override
    protected Either<ActivityError, Double> performAs(Actor actor, Double price) {
      double discounted = price * (1.0 - percent / 100.0);
      return Either.right(Math.round(discounted * 100.0) / 100.0);
    }

    @Override
    public String toString() {
      return "ApplyDiscount(" + percent + "%)";
    }
  }

  @Action("format price as currency string")
  static class FormatPrice extends Task<Double, String> {
    @Override
    protected Either<ActivityError, String> performAs(Actor actor, Double price) {
      return Either.right(String.format("$%.2f", price));
    }

    @Override
    public String toString() {
      return "FormatPrice";
    }
  }

  // ── tests ───────────────────────────────────────────────────────

  @Test
  @DisplayName("Single actor – activity log mapped as Allure steps")
  @Reqs({"REQ-STEP-001"})
  void singleActorSteps() {
    Either<ActivityError, String> result = alice.attemptsTo(
      new SearchForProduct("Laptop"));

    assertTrue(result.isRight(), "search should succeed");
  }

  @Test
  @DisplayName("Multiple activities – each mapped as separate step tree")
  @Reqs({"REQ-STEP-002", "REQ-STEP-003"})
  void multipleActivities() {
    Either<ActivityError, String> r1 = alice.attemptsTo(
      new SearchForProduct("Phone"));
    Either<ActivityError, String> r2 = alice.attemptsTo(
      new SearchForProduct("Tablet"));

    assertTrue(r1.isRight() && r2.isRight(), "both searches should succeed");
  }

  @Test
  @DisplayName("Failing activity – error visible in Allure step tree")
  @Reqs({"REQ-STEP-004"})
  @Story("IntendedFail")
  void failingActivityWithStackTrace() {
    // First search succeeds and shows passing steps in the tree
    alice.attemptsTo(new SearchForProduct("Monitor"));

    // Second search fails at validation — shows a failing step in the tree
    Either<ActivityError, String> result = alice.attemptsTo(
      new SearchAndValidateProduct("Monitor"));

    // Assert the result — produces an AssertionError showing expected vs actual
    assertEquals("validated: Monitor", result.getOrElse(""),
      "Expected product validation to succeed, but the result banner was not found");
  }

  @Test
  @DisplayName("Chained tasks with input/output – data flows between steps")
  @Reqs({"REQ-STEP-005"})
  void chainedTasksWithInputOutput() {
    // Chain: product name → price → discounted price → formatted string
    // Each step's input/output should be visible as parameters in the Allure report
    Either<ActivityError, String> result = alice.attemptsTo_(
      new FetchProductPrice(),
      new ApplyDiscount(20),
      new FormatPrice()).using("Laptop");

    assertTrue(result.isRight(), "price calculation should succeed");
  }

  @Test
  @DisplayName("Multiple chained operations – different discount rates")
  @Reqs({"REQ-STEP-006"})
  void multipleChainedOperations() {
    // First chain: full price
    Either<ActivityError, String> fullPrice = alice.attemptsTo_(
      new FetchProductPrice(),
      new FormatPrice()).using("Keyboard");

    assertTrue(fullPrice.isRight());

    // Second chain: with 50% discount
    Either<ActivityError, String> discountedPrice = alice.attemptsTo_(
      new FetchProductPrice(),
      new ApplyDiscount(50),
      new FormatPrice()).using("Keyboard");

    assertTrue(discountedPrice.isRight());
  }

  @Test
  @DisplayName("JSON input/output – large payloads visible in Allure step parameters")
  @Reqs({"REQ-STEP-007"})
  void jsonInputOutput() {
    String orderRequest = "{\n" + "  \"customer\": {\n" + "    \"id\": \"CUST-1001\",\n" + "    \"name\": \"Alice Wonderland\",\n" +
        "    \"email\": \"alice@example.com\",\n" + "    \"loyaltyTier\": \"GOLD\",\n" + "    \"memberSince\": \"2020-03-15\"\n" + "  },\n" +
        "  \"items\": [\n" + "    {\n" + "      \"sku\": \"SKU-LAPTOP-001\",\n" + "      \"name\": \"ThinkPad X1 Carbon\",\n" +
        "      \"quantity\": 1,\n" + "      \"unitPrice\": 1299.99\n" + "    },\n" + "    {\n" + "      \"sku\": \"SKU-MOUSE-042\",\n" +
        "      \"name\": \"Wireless Ergonomic Mouse\",\n" + "      \"quantity\": 2,\n" + "      \"unitPrice\": 49.99\n" + "    }\n" + "  ],\n" +
        "  \"shipping\": {\n" + "    \"method\": \"EXPRESS\",\n" + "    \"address\": \"42 Rabbit Hole Lane, Wonderland, WL 12345\"\n" + "  },\n" +
        "  \"payment\": {\n" + "    \"method\": \"CREDIT_CARD\",\n" + "    \"last4\": \"4242\"\n" + "  }\n" + "}";

    // Chain: order JSON → submit → response JSON → validate → confirmed JSON
    Either<ActivityError, String> result = alice.attemptsTo_(
      new SubmitOrderRequest(),
      new ValidateOrderConfirmation()).using(orderRequest);

    assertTrue(result.isRight(), "order should be confirmed");
  }
}
