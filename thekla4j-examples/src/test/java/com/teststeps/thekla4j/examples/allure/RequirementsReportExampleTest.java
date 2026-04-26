package com.teststeps.thekla4j.examples.allure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Reqs;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("JUnit Requirements Links")
@Epic("AllureExamples")
@Feature("Requirements Links")
@Story("IntendedPass")
class RequirementsReportExampleTest {

  @Test
  @DisplayName("Passing example with requirement link")
  @Reqs({"REQ-EX-001"})
  void passingExampleWithRequirement() {
    assertEquals(4, 2 + 2);
  }

  @Test
  @DisplayName("Failing example with requirement link")
  @Reqs({"REQ-EX-002"})
  @Story("IntendedFail")
  void failingExampleWithRequirement() {
    assertEquals("expected-banner", "actual-banner");
  }
}
