package com.teststeps.thekla4j.allure.junit5.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import com.teststeps.thekla4j.commons.error.ActivityError;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.test.AllureResultsWriterStub;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

class AllureThekla4jExtensionTest {

  private AllureResultsWriterStub stub;
  private AllureLifecycle lifecycle;
  private String uuid;

  @BeforeEach
  void setUp() {
    stub = new AllureResultsWriterStub();
    lifecycle = new AllureLifecycle(stub);
    uuid = UUID.randomUUID().toString();
    lifecycle.scheduleTestCase(new TestResult().setUuid(uuid).setStatus(Status.PASSED));
    lifecycle.startTestCase(uuid);
  }

  private List<TestResult> stopAndWrite() {
    lifecycle.stopTestCase(uuid);
    lifecycle.writeTestCase(uuid);
    return stub.getTestResults();
  }

  private String labelValue(final TestResult result, final String name) {
    return result.getLabels()
        .stream()
        .filter(l -> name.equals(l.getName()))
        .map(Label::getValue)
        .findFirst()
        .orElse(null);
  }

  private List<Label> labelsNamed(final TestResult result, final String name) {
    return result.getLabels()
        .stream()
        .filter(l -> name.equals(l.getName()))
        .collect(Collectors.toList());
  }

  private ExtensionContext contextForClass(final Class<?> testClass) {
    ExtensionContext ctx = mock(ExtensionContext.class);
    org.mockito.Mockito.when(ctx.getTestClass()).thenReturn(Optional.of(testClass));
    org.mockito.Mockito.when(ctx.getTestMethod()).thenReturn(Optional.empty());
    return ctx;
  }

  private ExtensionContext contextForClassAndMethod(final Class<?> testClass, final String methodName) throws NoSuchMethodException {
    ExtensionContext ctx = mock(ExtensionContext.class);
    org.mockito.Mockito.when(ctx.getTestClass()).thenReturn(Optional.of(testClass));
    org.mockito.Mockito.when(ctx.getTestMethod())
        .thenReturn(Optional.of(testClass.getDeclaredMethod(methodName)));
    return ctx;
  }

  // ===== Annotation fixture classes =====

  @Epic("ClassEpic")
  static class WithEpic {
  }

  @Feature("ClassFeature")
  static class WithFeature {
  }

  @Story("ClassStory")
  static class WithStory {
  }

  @Suite("ClassSuite")
  static class WithSuite {
  }

  @SubSuite("ClassSubSuite")
  static class WithSubSuite {
  }

  @ParentSuite("ClassParentSuite")
  static class WithParentSuite {
  }

  @Epic("ClassEpic")
  @Feature("ClassFeature")
  @Story("ClassStory")
  static class WithClassAnnotations {
    @Epic("MethodEpic")
    void methodWithEpic() {
    }

    @Feature("MethodFeature")
    void methodWithFeature() {
    }

    @Story("MethodStory")
    void methodWithStory() {
    }
  }

  static class WithNoAnnotations {
  }

  // ===== Label processing tests =====

  @Nested
  class LabelProcessing {

    @Test
    void classLevel_epic_addsEpicLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithEpic.class));

      assertThat(labelValue(stopAndWrite().get(0), "epic"), is("ClassEpic"));
    }

    @Test
    void classLevel_feature_addsFeatureLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithFeature.class));

      assertThat(labelValue(stopAndWrite().get(0), "feature"), is("ClassFeature"));
    }

    @Test
    void classLevel_story_addsStoryLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithStory.class));

      assertThat(labelValue(stopAndWrite().get(0), "story"), is("ClassStory"));
    }

    @Test
    void classLevel_suite_replacesSuiteLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithSuite.class));

      assertThat(labelValue(stopAndWrite().get(0), "suite"), is("ClassSuite"));
    }

    @Test
    void classLevel_subSuite_replacesSubSuiteLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithSubSuite.class));

      assertThat(labelValue(stopAndWrite().get(0), "subSuite"), is("ClassSubSuite"));
    }

    @Test
    void classLevel_parentSuite_replacesParentSuiteLabel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithParentSuite.class));

      assertThat(labelValue(stopAndWrite().get(0), "parentSuite"), is("ClassParentSuite"));
    }

    @Test
    void methodLevel_epic_overridesClassLevel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClassAndMethod(WithClassAnnotations.class, "methodWithEpic"));

      List<Label> epicLabels = labelsNamed(stopAndWrite().get(0), "epic");
      assertThat(epicLabels, hasSize(1));
      assertThat(epicLabels.get(0).getValue(), is("MethodEpic"));
    }

    @Test
    void methodLevel_feature_overridesClassLevel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClassAndMethod(WithClassAnnotations.class, "methodWithFeature"));

      List<Label> featureLabels = labelsNamed(stopAndWrite().get(0), "feature");
      assertThat(featureLabels, hasSize(1));
      assertThat(featureLabels.get(0).getValue(), is("MethodFeature"));
    }

    @Test
    void methodLevel_story_overridesClassLevel() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClassAndMethod(WithClassAnnotations.class, "methodWithStory"));

      List<Label> storyLabels = labelsNamed(stopAndWrite().get(0), "story");
      assertThat(storyLabels, hasSize(1));
      assertThat(storyLabels.get(0).getValue(), is("MethodStory"));
    }

    @Test
    void noAnnotations_noTagLabelsAdded() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      ext.beforeTestExecution(contextForClass(WithNoAnnotations.class));

      TestResult result = stopAndWrite().get(0);
      assertThat(labelsNamed(result, "epic"), empty());
      assertThat(labelsNamed(result, "feature"), empty());
      assertThat(labelsNamed(result, "story"), empty());
      assertThat(labelValue(result, "suite"), nullValue());
      assertThat(labelValue(result, "subSuite"), nullValue());
      assertThat(labelValue(result, "parentSuite"), nullValue());
    }

    @Test
    void noActiveTestCase_beforeTestExecution_doesNotThrow() throws Exception {
      stub = new AllureResultsWriterStub();
      lifecycle = new AllureLifecycle(stub);
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);

      ext.beforeTestExecution(contextForClass(WithEpic.class));

      assertThat(stub.getTestResults(), empty());
    }

    @Test
    void methodLevel_unannotatedMethod_classLabelsArePreserved() throws Exception {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Method unannotated = WithClassAnnotations.class.getDeclaredMethod("methodWithStory");

      ExtensionContext ctx = mock(ExtensionContext.class);
      org.mockito.Mockito.when(ctx.getTestClass()).thenReturn(Optional.of(WithEpic.class));
      org.mockito.Mockito.when(ctx.getTestMethod()).thenReturn(Optional.of(unannotated));

      ext.beforeTestExecution(ctx);

      assertThat(labelValue(stopAndWrite().get(0), "epic"), is("ClassEpic"));
    }
  }

  // ===== ActivityError handling tests =====

  @Nested
  class ActivityErrorHandling {

    @Test
    void activityError_setsAllureStatusToFailed() throws Throwable {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Invocation<Void> invocation = mock(Invocation.class);
      doThrow(ActivityError.of("assertion failed")).when(invocation).proceed();

      assertThrows(AssertionError.class, () -> ext.interceptTestMethod(invocation, null, null));

      assertThat(stopAndWrite().get(0).getStatus(), is(Status.FAILED));
    }

    @Test
    void activityError_statusDetailsContainErrorMessage() throws Throwable {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Invocation<Void> invocation = mock(Invocation.class);
      doThrow(ActivityError.of("expected <foo> but was <bar>")).when(invocation).proceed();

      assertThrows(AssertionError.class, () -> ext.interceptTestMethod(invocation, null, null));

      assertThat(stopAndWrite().get(0).getStatusDetails().getMessage(),
        equalTo("expected <foo> but was <bar>"));
    }

    @Test
    void activityError_statusDetailsContainStackTrace() throws Throwable {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Invocation<Void> invocation = mock(Invocation.class);
      doThrow(ActivityError.of("some error")).when(invocation).proceed();

      assertThrows(AssertionError.class, () -> ext.interceptTestMethod(invocation, null, null));

      assertThat(stopAndWrite().get(0).getStatusDetails().getTrace(), notNullValue());
    }

    @Test
    void activityError_isRethrownAsAssertionErrorWithOriginalCause() throws Throwable {
      stub = new AllureResultsWriterStub();
      lifecycle = new AllureLifecycle(stub);
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);

      ActivityError cause = ActivityError.of("must be rethrown");
      Invocation<Void> invocation = mock(Invocation.class);
      doThrow(cause).when(invocation).proceed();

      AssertionError thrown =
          assertThrows(AssertionError.class, () -> ext.interceptTestMethod(invocation, null, null));

      assertThat(thrown.getMessage(), equalTo("must be rethrown"));
      assertThat(thrown.getCause(), instanceOf(ActivityError.class));
    }

    @Test
    void otherException_propagatesWithoutAllureUpdate() throws Throwable {
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Invocation<Void> invocation = mock(Invocation.class);
      doThrow(new RuntimeException("unexpected crash")).when(invocation).proceed();

      assertThrows(RuntimeException.class, () -> ext.interceptTestMethod(invocation, null, null));

      assertThat(stopAndWrite().get(0).getStatus(), is(Status.PASSED));
    }

    @Test
    void noException_invocationProceedIsCalled() throws Throwable {
      stub = new AllureResultsWriterStub();
      lifecycle = new AllureLifecycle(stub);
      Thekla4jAllureJunit5Extension ext = new Thekla4jAllureJunit5Extension(lifecycle);
      Invocation<Void> invocation = mock(Invocation.class);

      assertDoesNotThrow(() -> ext.interceptTestMethod(invocation, null, null));

      verify(invocation).proceed();
    }
  }
}
