package com.teststeps.thekla4j.allure.junit5.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.ParentSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.SubSuite;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * End-to-end tests that verify {@link Thekla4jAllureJunit5Extension} labels are written correctly to
 * JSON result files on disk.
 */
class AllureThekla4jExtensionFileOutputIntegrationTest {

  /** Fixture with all six label annotations at class level. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Epic("FileEpic")
  @Feature("FileFeature")
  @Story("FileStory")
  @Suite("FileSuite")
  @SubSuite("FileSubSuite")
  @ParentSuite("FileParentSuite")
  static class FileOutputFixture {
    @Test
    void passingTest() {
    }
  }

  /** Fixture: method-level annotations override class-level in file output. */
  @ExtendWith(Thekla4jAllureJunit5Extension.class)
  @Epic("ClassEpic")
  @Feature("ClassFeature")
  @Story("ClassStory")
  static class FileOutputMethodOverrideFixture {
    @Epic("MethodEpic")
    @Feature("MethodFeature")
    @Story("MethodStory")
    @Test
    void annotatedTest() {
    }
  }

  // ===== Helpers =====

  private JsonNode runFixtureAndReadResult(final Class<?> fixtureClass, final Path tempDir) throws IOException {
    AllureLifecycle testLifecycle = new AllureLifecycle(new FileSystemResultsWriter(tempDir));
    AllureLifecycle original = Allure.getLifecycle();
    Allure.setLifecycle(testLifecycle);
    try {
      LauncherDiscoveryRequest request =
          LauncherDiscoveryRequestBuilder.request()
              .selectors(selectClass(fixtureClass))
              .build();
      LauncherFactory.create().execute(request);
    } finally {
      Allure.setLifecycle(original);
    }

    List<Path> resultFiles =
        Files.list(tempDir)
            .filter(p -> p.getFileName().toString().endsWith("-result.json"))
            .collect(Collectors.toList());

    assertThat("expected exactly one result JSON file", resultFiles, hasSize(1));
    return new ObjectMapper().readTree(resultFiles.get(0).toFile());
  }

  private String labelValue(final JsonNode root, final String labelName) {
    for (JsonNode label : root.path("labels")) {
      if (labelName.equals(label.path("name").asText())) {
        return label.path("value").asText();
      }
    }
    return null;
  }

  private List<String> labelValues(final JsonNode root, final String labelName) {
    List<String> values = new ArrayList<>();
    for (JsonNode label : root.path("labels")) {
      if (labelName.equals(label.path("name").asText())) {
        values.add(label.path("value").asText());
      }
    }
    return values;
  }

  // ===== Tests: class-level annotations written to file =====

  @Test
  void fileOutput_epic_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    assertThat(labelValue(result, "epic"), is("FileEpic"));
  }

  @Test
  void fileOutput_feature_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    assertThat(labelValue(result, "feature"), is("FileFeature"));
  }

  @Test
  void fileOutput_story_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    assertThat(labelValue(result, "story"), is("FileStory"));
  }

  @Test
  void fileOutput_suite_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    List<String> suiteValues = labelValues(result, "suite");
    assertThat(suiteValues, hasSize(1));
    assertThat(suiteValues.get(0), is("FileSuite"));
  }

  @Test
  void fileOutput_subSuite_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    List<String> subSuiteValues = labelValues(result, "subSuite");
    assertThat(subSuiteValues, hasSize(1));
    assertThat(subSuiteValues.get(0), is("FileSubSuite"));
  }

  @Test
  void fileOutput_parentSuite_isWrittenToResultJson(@TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputFixture.class, tempDir);

    List<String> parentSuiteValues = labelValues(result, "parentSuite");
    assertThat(parentSuiteValues, hasSize(1));
    assertThat(parentSuiteValues.get(0), is("FileParentSuite"));
  }

  @Test
  void fileOutput_methodAnnotations_overrideClassLevel_inResultJson(
                                                                    @TempDir final Path tempDir) throws IOException {
    JsonNode result = runFixtureAndReadResult(FileOutputMethodOverrideFixture.class, tempDir);

    List<String> epicValues = labelValues(result, "epic");
    assertThat(epicValues, hasSize(1));
    assertThat(epicValues.get(0), is("MethodEpic"));

    List<String> featureValues = labelValues(result, "feature");
    assertThat(featureValues, hasSize(1));
    assertThat(featureValues.get(0), is("MethodFeature"));

    List<String> storyValues = labelValues(result, "story");
    assertThat(storyValues, hasSize(1));
    assertThat(storyValues.get(0), is("MethodStory"));
  }
}
