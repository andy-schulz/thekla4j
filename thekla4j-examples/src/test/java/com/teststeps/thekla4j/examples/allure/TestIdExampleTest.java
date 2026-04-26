package com.teststeps.thekla4j.examples.allure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.TestId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Demonstrates the {@link TestId} annotation which appends the test ID to the
 * test name in the Allure report and sets the {@code testCaseId} field.
 *
 * <p>The report will show test names like:
 * <pre>
 * Login with valid credentials (TestId: TC-101)
 * Logout redirects to landing page (TestId: TC-102)
 * </pre>
 */
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("JUnit Test IDs")
@Epic("AllureExamples")
@Feature("Test ID Mapping")
@Story("IntendedPass")
class TestIdExampleTest {

  @Test
  @DisplayName("Login with valid credentials")
  @TestId("TC-101")
  void loginWithValidCredentials() {
    assertEquals("welcome", "welcome");
  }

  @Test
  @DisplayName("Logout redirects to landing page")
  @TestId("TC-102")
  void logoutRedirectsToLandingPage() {
    assertTrue(true);
  }

  @Test
  @DisplayName("Test without TestId")
  void testWithoutTestId() {
    assertTrue(true);
  }
}
