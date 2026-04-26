package com.teststeps.thekla4j.allure.plugin;

import io.qameta.allure.Aggregator2;
import io.qameta.allure.CommonJsonAggregator2;
import io.qameta.allure.CompositeAggregator2;
import io.qameta.allure.ReportStorage;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Link;
import io.qameta.allure.entity.TestResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Allure report plugin for thekla4j.
 *
 * <p>Extracts requirement links into a dedicated "Requirements" section in the test result view.
 */
public class Thekla4jAllurePlugin extends CompositeAggregator2 {

  static final String REQUIREMENTS_BLOCK_NAME = "requirements";
  static final String REQUIREMENT_LINK_TYPE = "requirement";

  private static final String WIDGET_JSON_FILE = "requirements.json";

  public Thekla4jAllurePlugin() {
    super(List.of(
      new RequirementLinksAggregator(),
      new RequirementsWidgetAggregator()));
  }

  // ===== Requirement links → extra block =====

  private static final class RequirementLinksAggregator implements Aggregator2 {

    @Override
    public void aggregate(final Configuration configuration, final List<LaunchResults> launchesResults, final ReportStorage storage) {
      launchesResults.stream()
          .flatMap(results -> results.getResults().stream())
          .forEach(this::extractRequirementLinks);
    }

    private void extractRequirementLinks(final TestResult result) {
      final Map<Boolean, List<Link>> partitioned = result.getLinks()
          .stream()
          .filter(Objects::nonNull)
          .collect(Collectors.partitioningBy(
            link -> REQUIREMENT_LINK_TYPE.equalsIgnoreCase(link.getType())));

      final List<Map<String, String>> requirementLinks = partitioned.get(true)
          .stream()
          .map(this::toLinkData)
          .collect(Collectors.toList());

      if (!requirementLinks.isEmpty()) {
        result.addExtraBlock(REQUIREMENTS_BLOCK_NAME, requirementLinks);
        result.setLinks(partitioned.get(false));
      }
    }

    private Map<String, String> toLinkData(final Link link) {
      final Map<String, String> data = new LinkedHashMap<>();
      data.put("name", Objects.toString(link.getName(), ""));
      data.put("url", Objects.toString(link.getUrl(), ""));
      data.put("type", Objects.toString(link.getType(), ""));
      return data;
    }
  }

  // ===== Requirements widget JSON =====

  private static final class RequirementsWidgetAggregator extends CommonJsonAggregator2 {

    RequirementsWidgetAggregator() {
      super("widgets", WIDGET_JSON_FILE);
    }

    @Override
    protected List<Map<String, Object>> getData(final List<LaunchResults> launchesResults) {
      final List<Map<String, Object>> items = new ArrayList<>();

      launchesResults.stream()
          .map(LaunchResults::getResults)
          .flatMap(Collection::stream)
          .forEach(result -> {
            final List<Map<String, String>> requirements =
                result.getExtraBlock(REQUIREMENTS_BLOCK_NAME, new ArrayList<>());

            if (!requirements.isEmpty()) {
              final Map<String, Object> item = new LinkedHashMap<>();
              item.put("uid", result.getUid());
              item.put("name", result.getName());
              item.put("status", result.getStatus());
              item.put("time", result.getTime());
              item.put(REQUIREMENTS_BLOCK_NAME, requirements);
              items.add(item);
            }
          });

      return items;
    }
  }
}
