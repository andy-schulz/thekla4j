/*
 * Copyright 2016-2024 Qameta Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teststeps.thekla4j.allure.cucumber.plugins;

import static io.qameta.allure.util.ResultsUtils.createFeatureLabel;
import static io.qameta.allure.util.ResultsUtils.createFrameworkLabel;
import static io.qameta.allure.util.ResultsUtils.createHostLabel;
import static io.qameta.allure.util.ResultsUtils.createLabel;
import static io.qameta.allure.util.ResultsUtils.createLanguageLabel;
import static io.qameta.allure.util.ResultsUtils.createStoryLabel;
import static io.qameta.allure.util.ResultsUtils.createSuiteLabel;
import static io.qameta.allure.util.ResultsUtils.createTestClassLabel;
import static io.qameta.allure.util.ResultsUtils.createThreadLabel;

import io.cucumber.messages.types.Feature;
import io.cucumber.plugin.event.TestCase;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Link;
import io.qameta.allure.util.ResultsUtils;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scenario labels and links builder.
 */
@SuppressWarnings({"CyclomaticComplexity", "PMD.CyclomaticComplexity", "PMD.NcssCount", "MultipleStringLiterals"})
class LabelBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(LabelBuilder.class);
  private static final String COMPOSITE_TAG_DELIMITER = "=";

  private static final String SEVERITY = "@SEVERITY";
  private static final String ISSUE_LINK = "@ISSUE";
  private static final String ISSUES_LINK = "@ISSUES";
  private static final String TMS_LINK = "@TMSLINK";
  private static final String PLAIN_LINK = "@LINK";
  private static final String OWNER = "@OWNER";
  private static final String EPIC = "@EPIC";
  private static final String STORY = "@STORY";
  private static final String TEST_ID = "@TEST_ID";

  private static final String PARENT_SUITE = "@PARENT_SUITE";
  private static final String SUITE = "@SUITE";
  private static final String SUB_SUITE = "@SUB_SUITE";

  private boolean isSuiteSet = false;
  private boolean isStorySet = false;

  private final List<Label> scenarioLabels = new ArrayList<>();
  private final List<Link> scenarioLinks = new ArrayList<>();
  private String testId = "";

  LabelBuilder(final Feature feature, final TestCase scenario, final Deque<String> tags) {
    final TagParser tagParser = new TagParser(feature, scenario);

    while (tags.peek() != null) {
      final String tag = tags.remove();

      if (tag.contains(COMPOSITE_TAG_DELIMITER)) {

        final String[] tagParts = tag.split(COMPOSITE_TAG_DELIMITER, 2);
        if (tagParts.length < 2 || Objects.isNull(tagParts[1]) || tagParts[1].isEmpty()) {
          // skip empty tags, e.g. '@tmsLink=', to avoid formatter errors
          continue;
        }

        final String tagKey = tagParts[0].toUpperCase();
        final String tagValue = tagParts[1];

        // Handle composite named links
        if (tagKey.startsWith(PLAIN_LINK + ".")) {
          tryHandleNamedLink(tag);
          continue;
        }

        switch (tagKey) {
          case SEVERITY:
            getScenarioLabels().add(ResultsUtils.createSeverityLabel(tagValue.toLowerCase()));
            break;
          case TMS_LINK:
            getScenarioLinks().add(ResultsUtils.createTmsLink(tagValue));
            break;
          case ISSUE_LINK:
            getScenarioLinks().add(ResultsUtils.createIssueLink(tagValue));
            break;
          case ISSUES_LINK:
            Arrays.stream(tagValue.split(","))
                .flatMap(issue -> Arrays.stream(issue.split(";")))
                .forEach(issue -> getScenarioLinks().add(ResultsUtils.createIssueLink(issue)));
            break;
          case PLAIN_LINK:
            getScenarioLinks().add(ResultsUtils.createLink(null, tagValue, tagValue, null));
            break;
          case OWNER:
            getScenarioLabels().add(ResultsUtils.createOwnerLabel(tagValue));
            break;
          // BDD EPIC -> Feature -> Story
          // the feature is always the Feature name from the file
          // The story can then be set on Scenario level
          case EPIC:
            getScenarioLabels().add(ResultsUtils.createEpicLabel(tagValue));
            break;
          case STORY:
            getScenarioLabels().add(ResultsUtils.createStoryLabel(tagValue));
            isStorySet = true;
            break;


          case PARENT_SUITE:
            getScenarioLabels().add(resolveParentSuiteLabel(tagValue));
            break;
          case SUITE:
            getScenarioLabels().add(ResultsUtils.createSuiteLabel(tagValue));
            isSuiteSet = true;
            break;
          case SUB_SUITE:
            getScenarioLabels().add(ResultsUtils.createSubSuiteLabel(tagValue));
            break;

          case TEST_ID:
            testId = tagValue;
            break;
          default:
            resolveParentSuiteOverride().ifPresent(getScenarioLabels()::add);

            LOGGER.warn("Composite tag {} is not supported. adding it as RAW", tagKey);
            getScenarioLabels().add(getTagLabel(tag));
            break;
        }
      } else if (tagParser.isPureSeverityTag(tag)) {
        getScenarioLabels().add(ResultsUtils.createSeverityLabel(tag.substring(1)));
      } else if (!tagParser.isResultTag(tag)) {
        getScenarioLabels().add(getTagLabel(tag));
      }
    }

    final String featureName = feature.getName();
    final URI uri = scenario.getUri();

    getScenarioLabels().addAll(ResultsUtils.getProvidedLabels());
    getScenarioLabels().addAll(Arrays.asList(
      createHostLabel(),
      createThreadLabel(),

      // feature is always the Feature name from the file
      createFeatureLabel(featureName),

      createTestClassLabel(scenario.getName()),
      createFrameworkLabel("cucumber7jvm"),
      createLanguageLabel("java"),
      createLabel("gherkin_uri", uri.toString())));


    // Default Suite is the feature name
    if (!isSuiteSet) {
      getScenarioLabels().add(createSuiteLabel(featureName));
    }

    // Default Story is the scenario name
    if (!isStorySet) {
      getScenarioLabels().add(createStoryLabel(scenario.getName()));
    }

    featurePackage(uri.toString(), featureName)
        .map(ResultsUtils::createPackageLabel)
        .ifPresent(getScenarioLabels()::add);
  }

  public List<Label> getScenarioLabels() {
    return scenarioLabels;
  }

  public List<Link> getScenarioLinks() {
    return scenarioLinks;
  }

  public String getTestId() {
    return testId;
  }

  private Label getTagLabel(final String tag) {
    return ResultsUtils.createTagLabel(tag.substring(1));
  }

  /**
   * Handle composite named links.
   *
   * @param tagString Full tag name and value
   */
  private void tryHandleNamedLink(final String tagString) {
    final String namedLinkPatternString = PLAIN_LINK + "\\.(\\w+-?)+=(\\w+(-|_)?)+";
    final Pattern namedLinkPattern = Pattern.compile(namedLinkPatternString, Pattern.CASE_INSENSITIVE);

    if (namedLinkPattern.matcher(tagString).matches()) {
      final String type = tagString.split(COMPOSITE_TAG_DELIMITER)[0].split("[.]")[1];
      final String name = tagString.split(COMPOSITE_TAG_DELIMITER)[1];
      getScenarioLinks().add(ResultsUtils.createLink(null, name, null, type));
    } else {
      LOGGER.warn("Composite named tag {} does not match regex {}. Skipping", tagString,
        namedLinkPatternString);
    }
  }

  private Optional<String> featurePackage(final String uriString, final String featureName) {
    final Optional<URI> maybeUri = safeUri(uriString);
    if (!maybeUri.isPresent()) {
      return Optional.empty();
    }
    URI uri = maybeUri.get();

    if (!uri.isOpaque()) {
      final URI work = new File("").toURI();
      uri = work.relativize(uri);
    }
    final String schemeSpecificPart = uri.normalize().getSchemeSpecificPart();
    final Stream<String> folders = Stream.of(schemeSpecificPart.replaceAll("\\.", "_").split("[/\\\\]"));
    final Stream<String> name = Stream.of(featureName);
    return Optional.of(Stream.concat(folders, name)
        .filter(Objects::nonNull)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.joining(".")));
  }

  private static Label resolveParentSuiteLabel(final String tagValue) {
    final String envValue = System.getenv("PARENT_SUITE");
    return ResultsUtils.createParentSuiteLabel(envValue != null ? envValue : tagValue);
  }

  private static Optional<Label> resolveParentSuiteOverride() {
    final String envValue = System.getenv("PARENT_SUITE");
    return envValue != null ? Optional.of(ResultsUtils.createParentSuiteLabel(envValue)) : Optional.empty();
  }

  private static Optional<URI> safeUri(final String uri) {
    try {
      return Optional.of(URI.create(uri));
    } catch (Exception e) {
      LOGGER.debug("could not parse feature uri {}", uri, e);
    }
    return Optional.empty();
  }
}
