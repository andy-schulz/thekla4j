package com.teststeps.thekla4j.allure.plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.qameta.allure.ReportStorage;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Link;
import io.qameta.allure.entity.Status;
import io.qameta.allure.entity.TestResult;
import io.qameta.allure.entity.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class Thekla4jAllurePluginTest {

  private Thekla4jAllurePlugin plugin;
  private Configuration configuration;
  private ReportStorage storage;

  @BeforeEach
  void setUp() {
    plugin = new Thekla4jAllurePlugin();
    configuration = mock(Configuration.class);
    storage = mock(ReportStorage.class);
  }

  private static Link requirementLink(String name, String url) {
    return new Link().setName(name).setUrl(url).setType("requirement");
  }

  private static Link issueLink(String name) {
    return new Link().setName(name).setUrl("http://issues/" + name).setType("issue");
  }

  private static LaunchResults launchWith(TestResult... results) {
    LaunchResults launch = mock(LaunchResults.class);
    when(launch.getResults()).thenReturn(new HashSet<>(Arrays.asList(results)));
    return launch;
  }

  private static List<Map<String, String>> requirementsBlock(TestResult result) {
    return result.getExtraBlock(Thekla4jAllurePlugin.REQUIREMENTS_BLOCK_NAME, new ArrayList<>());
  }

  // ===== RequirementLinksAggregator behavior =====

  @Nested
  class RequirementLinksAggregation {

    @Test
    void requirementLinks_areMovedToExtraBlock() {
      TestResult result = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(
            requirementLink("REQ-1", "http://reqs/1"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      List<Map<String, String>> reqs = requirementsBlock(result);
      assertThat(reqs, hasSize(1));
      assertThat(reqs.get(0).get("name"), is("REQ-1"));
      assertThat(reqs.get(0).get("url"), is("http://reqs/1"));
      assertThat(reqs.get(0).get("type"), is("requirement"));
      assertThat(result.getLinks(), empty());
    }

    @Test
    void nonRequirementLinks_arePreserved() {
      TestResult result = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(
            issueLink("BUG-1"),
            requirementLink("REQ-1", "http://reqs/1"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      assertThat(result.getLinks(), hasSize(1));
      assertThat(result.getLinks().get(0).getName(), is("BUG-1"));
      assertThat(result.getLinks().get(0).getType(), is("issue"));
    }

    @Test
    void mixedLinks_onlyRequirementsExtracted() {
      TestResult result = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(
            issueLink("BUG-1"),
            requirementLink("REQ-1", "http://reqs/1"),
            new Link().setName("TMS-1").setUrl("http://tms/1").setType("tms"),
            requirementLink("REQ-2", "http://reqs/2"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      List<Map<String, String>> reqs = requirementsBlock(result);
      assertThat(reqs, hasSize(2));
      assertThat(reqs.stream().map(m -> m.get("name")).collect(Collectors.toList()),
        is(List.of("REQ-1", "REQ-2")));

      assertThat(result.getLinks(), hasSize(2));
      List<String> remainingTypes = result.getLinks()
          .stream()
          .map(Link::getType)
          .collect(Collectors.toList());
      assertThat(remainingTypes, is(List.of("issue", "tms")));
    }

    @Test
    void caseInsensitiveTypeMatching() {
      TestResult result = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(
            new Link().setName("REQ-upper").setUrl("http://r/1").setType("REQUIREMENT"),
            new Link().setName("REQ-mixed").setUrl("http://r/2").setType("Requirement"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      assertThat(requirementsBlock(result), hasSize(2));
      assertThat(result.getLinks(), empty());
    }

    @Test
    void nullLinksInList_areFilteredWithoutError() {
      ArrayList<Link> links = new ArrayList<>();
      links.add(requirementLink("REQ-1", "http://reqs/1"));
      links.add(null);
      links.add(issueLink("BUG-1"));

      TestResult result = new TestResult().setUid("1").setLinks(links);

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      assertThat(requirementsBlock(result), hasSize(1));
      assertThat(result.getLinks(), hasSize(1));
    }

    @Test
    void noRequirementLinks_requirementsBlockIsEmpty() {
      TestResult result = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(issueLink("BUG-1"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      assertThat(requirementsBlock(result), empty());
      assertThat(result.getLinks(), hasSize(1));
    }

    @Test
    void multipleTestResults_acrossLaunches_allProcessed() {
      TestResult result1 = new TestResult()
          .setUid("1")
          .setLinks(new ArrayList<>(List.of(requirementLink("REQ-1", "http://r/1"))));
      TestResult result2 = new TestResult()
          .setUid("2")
          .setLinks(new ArrayList<>(List.of(requirementLink("REQ-2", "http://r/2"))));

      plugin.aggregate(configuration,
        List.of(launchWith(result1), launchWith(result2)), storage);

      assertThat(requirementsBlock(result1), hasSize(1));
      assertThat(requirementsBlock(result1).get(0).get("name"), is("REQ-1"));
      assertThat(requirementsBlock(result2), hasSize(1));
      assertThat(requirementsBlock(result2).get(0).get("name"), is("REQ-2"));
    }
  }

  // ===== WidgetAggregator behavior =====

  @Nested
  class WidgetAggregation {

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> captureWidgetPayload() {
      ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
      verify(storage).addDataJson(eq("widgets/requirements.json"), captor.capture());
      return (List<Map<String, Object>>) captor.getValue();
    }

    @Test
    void widgetPayload_includesTestResultsWithRequirements() {
      TestResult result = new TestResult()
          .setUid("uid-1")
          .setName("my test")
          .setStatus(Status.PASSED)
          .setTime(new Time().setStart(1000L).setStop(2000L))
          .setLinks(new ArrayList<>(List.of(requirementLink("REQ-1", "http://r/1"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      List<Map<String, Object>> payload = captureWidgetPayload();
      assertThat(payload, hasSize(1));
      assertThat(payload.get(0).get("uid"), is("uid-1"));
      assertThat(payload.get(0).get("name"), is("my test"));
      assertThat(payload.get(0).get("status"), is(Status.PASSED));
    }

    @Test
    void widgetPayload_excludesTestResultsWithoutRequirements() {
      TestResult withReqs = new TestResult()
          .setUid("uid-1")
          .setName("with reqs")
          .setLinks(new ArrayList<>(List.of(requirementLink("REQ-1", "http://r/1"))));
      TestResult withoutReqs = new TestResult()
          .setUid("uid-2")
          .setName("no reqs")
          .setLinks(new ArrayList<>(List.of(issueLink("BUG-1"))));

      plugin.aggregate(configuration,
        List.of(launchWith(withReqs, withoutReqs)), storage);

      List<Map<String, Object>> payload = captureWidgetPayload();
      assertThat(payload, hasSize(1));
      assertThat(payload.get(0).get("uid"), is("uid-1"));
    }

    @Test
    void emptyLaunches_produceEmptyWidgetPayload() {
      plugin.aggregate(configuration, Collections.emptyList(), storage);

      List<Map<String, Object>> payload = captureWidgetPayload();
      assertThat(payload, empty());
    }
  }

  // ===== Full pipeline =====

  @Nested
  class FullPipeline {

    @SuppressWarnings("unchecked")
    @Test
    void aggregate_populatesExtraBlockAndWidgetPayload() {
      TestResult result = new TestResult()
          .setUid("uid-1")
          .setName("pipeline test")
          .setStatus(Status.FAILED)
          .setLinks(new ArrayList<>(List.of(
            requirementLink("REQ-1", "http://r/1"),
            issueLink("BUG-1"))));

      plugin.aggregate(configuration, List.of(launchWith(result)), storage);

      List<Map<String, String>> reqs = requirementsBlock(result);
      assertThat(reqs, hasSize(1));
      assertThat(reqs.get(0).get("name"), is("REQ-1"));

      assertThat(result.getLinks(), hasSize(1));
      assertThat(result.getLinks().get(0).getType(), is("issue"));

      ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
      verify(storage).addDataJson(eq("widgets/requirements.json"), captor.capture());
      List<Map<String, Object>> payload = (List<Map<String, Object>>) captor.getValue();
      assertThat(payload, hasSize(1));
      assertThat(payload.get(0).get("uid"), is("uid-1"));
      assertThat(payload.get(0).get("status"), is(Status.FAILED));
    }
  }
}
