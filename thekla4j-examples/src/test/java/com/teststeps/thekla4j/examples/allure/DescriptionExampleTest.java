package com.teststeps.thekla4j.examples.allure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Description;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Demonstrates the {@link Description} annotation which adds a multi-line description
 * to the Allure test result.
 *
 * <p>Each element in {@code @Description} becomes one line in the Allure report.
 * A method-level annotation overrides a class-level one.
 */
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@ParentSuite("thekla4jExamples")
@Suite("AllureLabeling")
@SubSuite("Test Descriptions")
@Epic("AllureExamples")
@Feature("Test Description")
@Story("IntendedPass")
@Description({"This description is defined at class level.", "It will appear on every test method that does not define its own @Description."
})
class DescriptionExampleTest {

  @Test
  @DisplayName("Test inheriting the class-level description")
  void testWithClassLevelDescription() {
    assertTrue(true);
  }

  @Test
  @DisplayName("Test with its own method-level description")
  @Description({"This test verifies the happy-path scenario.", "Given a valid input, the system should return a successful result.", "Edge cases such as null input and empty strings are covered in separate tests."
  })
  void testWithMethodLevelDescription() {
    assertTrue(true);
  }

  @Test
  @DisplayName("Test without any description")
  void testWithoutDescription() {
    assertTrue(true);
  }
}
