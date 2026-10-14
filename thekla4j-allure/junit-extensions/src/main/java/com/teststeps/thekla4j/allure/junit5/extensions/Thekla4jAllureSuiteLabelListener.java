package com.teststeps.thekla4j.allure.junit5.extensions;

import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.TestResult;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drops the suite labels Allure adds on its own whenever {@link ParentSuite}, {@link Suite} or
 * {@link SubSuite} already provide the value.
 *
 * <p>{@link Thekla4jAllureJunit5Extension} can only do that for a test that actually starts: JUnit
 * does not call its callbacks for a test that is {@code @Disabled}, switched off by an
 * {@code ExecutionCondition} or aborted by an assumption in {@code @BeforeAll}/{@code @BeforeEach}.
 * Allure still writes a result for those and adds the test class name as suite label next to the
 * annotation value, which lists the test under two suites in the Suites tree. This listener runs
 * right before a result is written - for every result Allure creates - and keeps only the
 * annotation value.
 *
 * <p>The listener is registered via
 * {@code META-INF/services/io.qameta.allure.listener.TestLifecycleListener}.
 */
public class Thekla4jAllureSuiteLabelListener implements TestLifecycleListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(Thekla4jAllureSuiteLabelListener.class);

  private static final String TEST_CLASS_LABEL = "testClass";

  @Override
  public void beforeTestWrite(final TestResult result) {
    testClassOf(result).ifPresent(testClass -> {
      if (testClass.isAnnotationPresent(ParentSuite.class)) {
        keepOnly(result, "parentSuite", testClass.getAnnotation(ParentSuite.class).value());
      }
      if (testClass.isAnnotationPresent(Suite.class)) {
        keepOnly(result, "suite", testClass.getAnnotation(Suite.class).value());
      }
      if (testClass.isAnnotationPresent(SubSuite.class)) {
        keepOnly(result, "subSuite", testClass.getAnnotation(SubSuite.class).value());
      }
    });
  }

  private void keepOnly(final TestResult result, final String labelName, final String value) {
    result.getLabels()
        .removeIf(label -> labelName.equals(label.getName()) && !value.equals(label.getValue()));
  }

  private Optional<Class<?>> testClassOf(final TestResult result) {
    return result.getLabels()
        .stream()
        .filter(label -> TEST_CLASS_LABEL.equals(label.getName()))
        .map(Label::getValue)
        .filter(Objects::nonNull)
        .findFirst()
        .flatMap(this::loadClass);
  }

  private Optional<Class<?>> loadClass(final String className) {
    try {
      return Optional.of(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
    } catch (ClassNotFoundException | LinkageError e) {
      LOGGER.debug("Could not load test class '{}' to read its suite annotations", className, e);
      return Optional.empty();
    }
  }
}
