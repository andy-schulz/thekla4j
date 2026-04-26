package com.teststeps.thekla4j.allure.junit5.extensions;

import static io.qameta.allure.junitplatform.AllureJunitPlatform.ALLURE_PARAMETER;
import static io.qameta.allure.junitplatform.AllureJunitPlatform.ALLURE_PARAMETER_EXCLUDED_KEY;
import static io.qameta.allure.junitplatform.AllureJunitPlatform.ALLURE_PARAMETER_MODE_KEY;
import static io.qameta.allure.junitplatform.AllureJunitPlatform.ALLURE_PARAMETER_VALUE_KEY;
import static io.qameta.allure.junitplatform.AllureJunitPlatform.ALLURE_REPORT_ENTRY_BLANK_PREFIX;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
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
import com.teststeps.thekla4j.allure.shared.ActivityLogReporter;
import com.teststeps.thekla4j.allure.shared.RequirementLinkResolver;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Param;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.util.ObjectUtils;
import io.qameta.allure.util.ResultsUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combined thekla4j Allure JUnit 5 extension.
 *
 * <p>This single extension replaces the two previously separate extensions
 * ({@code ActivityErrorAsFailedExtension} and {@code AllureJunit5}) and provides all of their
 * functionality in one place:
 *
 * <ul>
 * <li><strong>ActivityError → FAILED</strong> – catches {@link ActivityError} thrown during a
 * test method and explicitly sets the Allure test status to {@link Status#FAILED} (red)
 * instead of the default BROKEN (yellow). The error is re-thrown wrapped in a
 * {@link java.lang.AssertionError} so that {@code AllureJunitPlatform} also records it as
 * FAILED.
 * <li><strong>Hierarchy labels</strong> – processes {@link Epic}, {@link Feature}, {@link Story},
 * {@link Suite}, {@link SubSuite}, and {@link ParentSuite} annotations on test classes and
 * methods, adding the corresponding Allure labels to the test result.
 * <li><strong>Issue/Requirement links</strong> – processes {@link Issues} and {@link Reqs}
 * annotations on test classes and methods, adding links to the test result.
 * <li><strong>Fixture lifecycle</strong> – wraps {@code @BeforeAll}, {@code @AfterAll},
 * {@code @BeforeEach}, and {@code @AfterEach} methods as named Allure fixtures with
 * pass/fail status.
 * <li><strong>Parameterized test parameters</strong> – publishes test-template parameters to
 * Allure via report entries.
 * <li><strong>Activity log steps</strong> – after test execution, scans for fields annotated with
 * {@link AllureActor} and maps each Actor's {@link ActivityLogNode} tree into native Allure
 * steps with status, timing, parameters, and attachments.
 * </ul>
 *
 * <h2>Registration</h2>
 *
 * <h3>Option 1 – per test class</h3>
 * <pre>{@code
 * @ExtendWith(AllureThekla4jExtension.class)
 * public class MyTest { ... }
 * }</pre>
 *
 * <h3>Option 2 – global auto-detection</h3>
 * <p>The extension is pre-registered via
 * {@code META-INF/services/org.junit.jupiter.api.extension.Extension}. Enable it project-wide by
 * adding the following to {@code src/test/resources/junit-platform.properties}:
 * <pre>
 * junit.jupiter.extensions.autodetection.enabled=true
 * </pre>
 */
@SuppressWarnings("MultipleStringLiterals")
public class Thekla4jAllureJunit5Extension implements InvocationInterceptor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(Thekla4jAllureJunit5Extension.class);

  private final AllureLifecycle lifecycle;

  /**
   * Creates a new {@code Thekla4jAllureJunit5Extension} using the default Allure lifecycle.
   */
  public Thekla4jAllureJunit5Extension() {
    this(Allure.getLifecycle());
  }

  /**
   * Creates a new {@code Thekla4jAllureJunit5Extension} using the given Allure lifecycle.
   *
   * @param lifecycle the {@link AllureLifecycle} to use for reporting
   */
  public Thekla4jAllureJunit5Extension(final AllureLifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }

  // ===== BeforeTestExecutionCallback – label processing =====

  @Override
  public void beforeTestExecution(final ExtensionContext context) {
    lifecycle.getCurrentTestCase().ifPresent(uuid -> {
      lifecycle.updateTestCase(uuid, testResult -> {
        context.getTestClass().ifPresent(testClass -> {
          processParentSuite(testClass, testResult);
          processSuite(testClass, testResult);
          processSubSuite(testClass, testResult);
          processEpic(testClass, testResult);
          processFeature(testClass, testResult);
          processStory(testClass, testResult);
        });

        context.getTestMethod().ifPresent(testMethod -> {
          processEpic(testMethod, testResult);
          processFeature(testMethod, testResult);
          processStory(testMethod, testResult);
        });
        processIssues(context, testResult);
        processRequirements(context, testResult);
        processTestId(context, testResult);
      });
    });
  }

  private void processTestId(final ExtensionContext context, final io.qameta.allure.model.TestResult testResult) {
    final Optional<Method> maybeMethod = context.getTestMethod();
    if (maybeMethod.isPresent() && maybeMethod.get().isAnnotationPresent(TestId.class)) {
      applyTestId(maybeMethod.get().getAnnotation(TestId.class).value(), testResult);
      return;
    }

    context.getTestClass()
        .filter(testClass -> testClass.isAnnotationPresent(TestId.class))
        .ifPresent(testClass -> applyTestId(testClass.getAnnotation(TestId.class).value(), testResult));
  }

  private void applyTestId(final String testId, final io.qameta.allure.model.TestResult testResult) {
    final String id = testId.trim();
    if (!id.isEmpty()) {
      testResult.setName(testResult.getName() + " (TestId: " + id + ")");
      testResult.setTestCaseId(id);
    }
  }

  // ===== AfterTestExecutionCallback – activity log step mapping =====

  @Override
  public void afterTestExecution(final ExtensionContext context) {
    lifecycle.getCurrentTestCase().ifPresent(uuid -> {
      final Object testInstance = context.getTestInstance().orElse(null);
      if (testInstance == null) {
        return;
      }

      final List<Actor> actors = findAllureActors(testInstance);
      ActivityLogReporter.reportActorLogs(
        lifecycle,
        uuid,
        actors,
        context.getExecutionException().isPresent());
    });
  }

  /**
   * Scans the test instance's class hierarchy for fields annotated with {@link AllureActor}
   * and returns the Actor values.
   */
  private List<Actor> findAllureActors(final Object testInstance) {
    final List<Actor> result = new ArrayList<>();
    Class<?> clazz = testInstance.getClass();
    while (clazz != null && clazz != Object.class) {
      for (final Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(AllureActor.class) && Actor.class.isAssignableFrom(field.getType())) {
          try {
            if (!field.canAccess(testInstance) && !field.trySetAccessible()) {
              LOGGER.warn(
                "Could not access @AllureActor field '{}' on test class '{}'",
                field.getName(),
                clazz.getName());
              continue;
            }
            final Actor actor = (Actor) field.get(testInstance);
            if (actor != null) {
              result.add(actor);
            }
          } catch (IllegalAccessException | SecurityException e) {
            LOGGER.warn(
              "Could not access @AllureActor field '{}' on test class '{}'",
              field.getName(),
              clazz.getName(),
              e);
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
    return result;
  }

  private void processIssues(final ExtensionContext context, final io.qameta.allure.model.TestResult testResult) {
    final Optional<Method> maybeMethod = context.getTestMethod();
    if (maybeMethod.isPresent() && maybeMethod.get().isAnnotationPresent(Issues.class)) {
      addIssueLinks(maybeMethod.get().getAnnotation(Issues.class).value(), testResult);
      return;
    }

    context.getTestClass()
        .filter(testClass -> testClass.isAnnotationPresent(Issues.class))
        .ifPresent(testClass -> addIssueLinks(testClass.getAnnotation(Issues.class).value(), testResult));
  }

  private void addIssueLinks(final String[] issueIds, final io.qameta.allure.model.TestResult testResult) {
    Stream.of(issueIds)
        .map(String::trim)
        .filter(issueId -> !issueId.isEmpty())
        .forEach(issueId -> testResult.getLinks().add(ResultsUtils.createIssueLink(issueId)));
  }

  private void processRequirements(final ExtensionContext context, final io.qameta.allure.model.TestResult testResult) {
    final Optional<Method> maybeMethod = context.getTestMethod();
    if (maybeMethod.isPresent() && maybeMethod.get().isAnnotationPresent(Reqs.class)) {
      addRequirementLinks(maybeMethod.get().getAnnotation(Reqs.class).value(), testResult);
      return;
    }

    context.getTestClass()
        .filter(testClass -> testClass.isAnnotationPresent(Reqs.class))
        .ifPresent(testClass -> addRequirementLinks(testClass.getAnnotation(Reqs.class).value(), testResult));
  }

  private void addRequirementLinks(final String[] requirementIds, final io.qameta.allure.model.TestResult testResult) {
    Stream.of(requirementIds)
        .map(String::trim)
        .filter(requirementId -> !requirementId.isEmpty())
        .forEach(requirementId -> testResult.getLinks()
            .add(RequirementLinkResolver.createRequirementLink(requirementId)));
  }

  private void processParentSuite(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(ParentSuite.class)) {
      final ParentSuite parentSuite = testClass.getAnnotation(ParentSuite.class);
      testResult.getLabels().removeIf(label -> "parentSuite".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("parentSuite").setValue(parentSuite.value()));
    }
  }

  private void processSuite(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(Suite.class)) {
      final Suite suite = testClass.getAnnotation(Suite.class);
      testResult.getLabels().removeIf(label -> "suite".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("suite").setValue(suite.value()));
    }
  }

  private void processSubSuite(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(SubSuite.class)) {
      final SubSuite subSuite = testClass.getAnnotation(SubSuite.class);
      testResult.getLabels().removeIf(label -> "subSuite".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("subSuite").setValue(subSuite.value()));
    }
  }

  private void processEpic(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(Epic.class)) {
      final Epic epic = testClass.getAnnotation(Epic.class);
      testResult.getLabels().add(new Label().setName("epic").setValue(epic.value()));
    }
  }

  private void processEpic(final Method testMethod, final io.qameta.allure.model.TestResult testResult) {
    if (testMethod.isAnnotationPresent(Epic.class)) {
      final Epic epic = testMethod.getAnnotation(Epic.class);
      testResult.getLabels().removeIf(label -> "epic".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("epic").setValue(epic.value()));
    }
  }

  private void processFeature(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(Feature.class)) {
      final Feature feature = testClass.getAnnotation(Feature.class);
      testResult.getLabels().add(new Label().setName("feature").setValue(feature.value()));
    }
  }

  private void processFeature(final Method testMethod, final io.qameta.allure.model.TestResult testResult) {
    if (testMethod.isAnnotationPresent(Feature.class)) {
      final Feature feature = testMethod.getAnnotation(Feature.class);
      testResult.getLabels().removeIf(label -> "feature".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("feature").setValue(feature.value()));
    }
  }

  private void processStory(final Class<?> testClass, final io.qameta.allure.model.TestResult testResult) {
    if (testClass.isAnnotationPresent(Story.class)) {
      final Story story = testClass.getAnnotation(Story.class);
      testResult.getLabels().add(new Label().setName("story").setValue(story.value()));
    }
  }

  private void processStory(final Method testMethod, final io.qameta.allure.model.TestResult testResult) {
    if (testMethod.isAnnotationPresent(Story.class)) {
      final Story story = testMethod.getAnnotation(Story.class);
      testResult.getLabels().removeIf(label -> "story".equals(label.getName()));
      testResult.getLabels().add(new Label().setName("story").setValue(story.value()));
    }
  }

  // ===== InvocationInterceptor – test method with ActivityError handling =====

  @Override
  public void interceptTestMethod(
                                  final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
    try {
      invocation.proceed();
    } catch (ActivityError e) {
      lifecycle.updateTestCase(result -> result
          .setStatus(Status.FAILED)
          .setStatusDetails(new StatusDetails()
              .setMessage(e.getMessage())
              .setTrace(stackTraceOf(e))));
      throw new AssertionError(e.getMessage(), e);
    }
  }

  // ===== InvocationInterceptor – test template (parameterized tests) =====

  @Override
  public void interceptTestTemplateMethod(
                                          final Invocation<Void> invocation, final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) throws Throwable {
    sendParameterEvent(invocationContext, extensionContext);
    invocation.proceed();
  }

  private void sendParameterEvent(
                                  final ReflectiveInvocationContext<Method> invocationContext, final ExtensionContext extensionContext) {
    final Parameter[] parameters = invocationContext.getExecutable().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      final Parameter parameter = parameters[i];

      final Class<?> parameterType = parameter.getType();
      if (parameterType.getCanonicalName().startsWith("org.junit.jupiter.api")) {
        continue;
      }
      final Object value = invocationContext.getArguments().get(i);
      final Map<String, String> map = new HashMap<>();
      map.put(ALLURE_PARAMETER, parameter.getName());
      map.put(ALLURE_PARAMETER_VALUE_KEY, ObjectUtils.toString(value));

      Stream.of(parameter.getAnnotationsByType(Param.class))
          .findFirst()
          .ifPresent(param -> {
            Stream.of(param.value(), param.name())
                .map(String::trim)
                .filter(name -> name.length() > 0)
                .findFirst()
                .ifPresent(name -> map.put(ALLURE_PARAMETER, name));

            map.put(ALLURE_PARAMETER_MODE_KEY, param.mode().name());
            map.put(ALLURE_PARAMETER_EXCLUDED_KEY, Boolean.toString(param.excluded()));
          });

      extensionContext.publishReportEntry(wrap(map));
    }
  }

  /**
   * Wraps a report-entry data map so that blank/null values are prefixed with
   * {@link io.qameta.allure.junitplatform.AllureJunitPlatform#ALLURE_REPORT_ENTRY_BLANK_PREFIX}.
   *
   * @param data the raw event data map
   * @return a new map with blank values properly prefixed
   */
  @SuppressWarnings("PMD.InefficientEmptyStringCheck")
  public Map<String, String> wrap(final Map<String, String> data) {
    final Map<String, String> res = new HashMap<>();
    data.forEach((key, value) -> {
      if (Objects.isNull(value) || value.trim().isEmpty()) {
        res.put(key, ALLURE_REPORT_ENTRY_BLANK_PREFIX + value);
      } else {
        res.put(key, value);
      }
    });
    return res;
  }

  // ===== Helpers =====

  private static String stackTraceOf(final Throwable t) {
    StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }
}
